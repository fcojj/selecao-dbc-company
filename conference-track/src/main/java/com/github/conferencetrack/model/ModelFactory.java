
package com.github.conferencetrack.model;

import com.github.conferencetrack.bean.Talk;
import com.github.conferencetrack.dao.InputReader;
import com.google.common.annotations.VisibleForTesting;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jonas Rodrigues on 18/08/2016.
 */
public class ModelFactory {

    private static final Logger LOGGER = Logger.getLogger(ModelFactory.class.getName());
    public static final int TITLE_INDEX = 0;
    public static final int MINUTES_INDEX = 1;
    private TalkModel talkModel;
    private Duration sessionMorning;
    private Duration sessionAfternoon;
    private InputReader inputReader;
    List<Talk> temp;

    public ModelFactory() {
        this.temp = new ArrayList<Talk>();
        this.talkModel = new TalkModel();
        this.sessionMorning = Duration.ofMinutes(180L);
        this.sessionAfternoon = Duration.ofMinutes(240L);
    }

    public TalkModel getTalkModel() {
        return talkModel;
    }

    public void showTalksScheduling(){
        ListIterator<Talk> iterator = getTalkModel().getTalks().listIterator();
        LocalTime localTime = LocalTime.of(9, 0);
        System.out.print("TRACK 1:");
        while (iterator.hasNext()) {
            Talk talk = iterator.next();
            System.out.print("\n" + localTime + " ");
            System.out.print(talk.getTitle() + " ");

            if (talk.getDuration() != null) {//not is lunch and not is networking
                System.out.print(talk.getDuration().toMinutes() + "min");
                if(iterator.next().getTitle().equals("Lunch")){// verify if next Talk is Lunch
                    localTime = LocalTime.of(12,0);//adjustment time launch
                }else {
                    localTime = localTime.plusMinutes(talk.getDuration().toMinutes());
                }
                iterator.previous();
            } else {// new track
                if(talk.getTitle().equals("Networking Event")) {// verify if current Talk is Networking
                    localTime = LocalTime.of(9, 0);//adjustment time launch
                    if(iterator.hasNext()) {//adjustment TRACK
                        System.out.print("\n\n" + iterator.next().getTrack() + ":");
                        iterator.previous();
                    }
                }else if(talk.getTitle().equals("Lunch")){// verify if current Talk is Lunch
                    localTime = LocalTime.of(13, 0);//adjustment time next Talk to 13 horus
                }
            }
        }
    }

    public void manufactureModel(){
        inputReader = new InputReader();

        for(List<String> data : inputReader.fileProcess()){
            processDataFile(data);
        }

        processScheduling();
    }

    private void processDataFile(List<String> data) {
        try {
            buildSubmittedTalks(data);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            LOGGER.log(Level.INFO, "Input talk is not in the correct format", e);
        }
    }

    private void processScheduling() {
        Duration sessionMorningCount = Duration.ofMinutes(0L);
        Duration sessionAfternoonCount = Duration.ofMinutes(0L);
        int countTrack = 1;

        scheduling(sessionMorningCount, sessionAfternoonCount, countTrack);
        talkModel.setTalks(temp);
    }

    private void scheduling(Duration sessionMorningCount, Duration sessionAfternoonCount, Integer countTrack) {
        ListIterator<Talk> iterator = talkModel.getTalks().listIterator();

        schedulingSession(sessionMorning, sessionMorningCount, countTrack, iterator, "SESSION 1");

        temp.add(createLunch(countTrack));//add lunch
        iterator = talkModel.getTalks().listIterator();

        schedulingSession(sessionAfternoon, sessionAfternoonCount, countTrack, iterator, "SESSION 2");
        schedulingNetworking(sessionAfternoonCount, countTrack);

        countTrack++;

        if (isFinishScheduling()) {
            sessionMorningCount = sessionMorningCount.ofMinutes(0);
            sessionAfternoonCount = sessionAfternoonCount.ofMinutes(0);
            scheduling(sessionMorningCount, sessionAfternoonCount, countTrack);
        } else {
            return;
        }
    }

    private boolean isFinishScheduling() {
        return talkModel.getTalks().size() != 0;
    }

    private void schedulingNetworking(Duration sessionAfternoonCount, Integer countTrack) {
        if (!sessionAfternoonCount.minusMinutes(Duration.ofMinutes(180L).toMinutes()).isNegative()
                || !sessionAfternoonCount.minusMinutes(Duration.ofMinutes(240L).toMinutes()).isZero()) {
            Talk talk = new Talk("Networking Event", "TRACK " + countTrack);

            temp.add(talk);
        }
    }

    private void schedulingSession(Duration session, Duration sessionCount, Integer countTrack, ListIterator<Talk> iterator, String nameSession) {
        while (iterator.hasNext()) {
            Talk talk = iterator.next();

            if (!session.minusMinutes(talk.getDuration().plus(sessionCount).toMinutes()).isNegative()) {
                talk.setSession(nameSession);
                talk.setTrack("TRACK " + countTrack);
                temp.add(talk);
                sessionCount = sessionCount.plusMinutes(talk.getDuration().toMinutes());
                iterator.remove();
            }
        }
    }

    private Talk createLunch(Integer countTrack) {
        Talk lunch = new Talk();

        lunch.setSession("SESSION 1");
        lunch.setTrack("TRACK " + countTrack);
        lunch.setTitle("Lunch");

        return lunch;
    }

    private void buildSubmittedTalks(List<String> data) {
        Talk talk = extractTalk(data);
        talkModel.getTalks().add(talk);
    }

    private Talk extractTalk(List<String> dataModel) {
        String title = dataModel.get(TITLE_INDEX);
        String minutes = dataModel.get(MINUTES_INDEX);
        Duration talkDuration = Duration.ofMinutes(Long.valueOf(minutes));
        Talk talk = new Talk(title, talkDuration);

        return talk;
    }
}
