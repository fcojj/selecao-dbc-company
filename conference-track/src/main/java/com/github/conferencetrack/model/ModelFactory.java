
package com.github.conferencetrack.model;

import com.github.conferencetrack.bean.Talk;

import java.time.Duration;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Jonas Rodrigues on 18/08/2016.
 */
public class ModelFactory {

    public static final int TITLE_INDEX = 0;
    public static final int MINUTES_INDEX = 1;
    private TalkModel talkModel;
    private SortedSet<Duration> durationsTalks;
    private static final Logger LOGGER = Logger.getLogger(ModelFactory.class.getName());
    private Duration sessionMorning;
    private Duration sessionAfternoon;
    private Duration trackDuration;
    List<Talk> temp;

    public TalkModel getTalkModel() {
        return talkModel;
    }

    public void manufactureModel(List<String> data) {
        try {
            buildSubmittedTalks(data);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            LOGGER.log(Level.INFO, "Input talk is not in the correct format", e);
        }
    }

    public ModelFactory() {
        this.temp = new ArrayList<Talk>();
        this.talkModel = new TalkModel();
        this.durationsTalks = new TreeSet<Duration>();
        this.sessionMorning = Duration.ofMinutes(180L);
        this.sessionAfternoon = Duration.ofMinutes(240L);
        this.trackDuration = Duration.ofMinutes(420L);
    }

    public void processSchedule() {
        Duration sessionMorningCount = Duration.ofMinutes(0L);
        Duration sessionAfternoonCount = Duration.ofMinutes(0L);
        int countTrack = 1;

        scheduling(sessionMorningCount, sessionAfternoonCount, countTrack);
        talkModel.setTalks(temp);
    }

    private void scheduling(Duration sessionMorningCount, Duration sessionAfternoonCount, Integer countTrack) {
        ListIterator<Talk> iterator = talkModel.getTalks().listIterator();

        schendulingSession(sessionMorning, sessionMorningCount, countTrack, iterator, "SESSION 1");

        temp.add(createLunch(countTrack));//add lunch
        iterator = talkModel.getTalks().listIterator();

        schendulingSession(sessionAfternoon, sessionAfternoonCount, countTrack, iterator, "SESSION 2");
        schendulingNetworking(sessionAfternoonCount, countTrack);

        countTrack++;

        if (isFinishSchenduling()) {
            sessionMorningCount = sessionMorningCount.ofMinutes(0);
            sessionAfternoonCount = sessionAfternoonCount.ofMinutes(0);
            scheduling(sessionMorningCount, sessionAfternoonCount, countTrack);
        } else {
            return;
        }
    }

    private boolean isFinishSchenduling() {
        return talkModel.getTalks().size() != 0;
    }

    private void schendulingNetworking(Duration sessionAfternoonCount, Integer countTrack) {
        if (!sessionAfternoonCount.minusMinutes(Duration.ofMinutes(180L).toMinutes()).isNegative()
                || !sessionAfternoonCount.minusMinutes(Duration.ofMinutes(240L).toMinutes()).isZero()) {
            Talk talk = new Talk("Networking Event", "TRACK " + countTrack);

            temp.add(talk);
        }
    }

    private void schendulingSession(Duration session, Duration sessionCount, Integer countTrack, ListIterator<Talk> iterator, String nameSession) {
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
