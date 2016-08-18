package com.github.conferencetrack.dao;

import com.github.conferencetrack.bean.Talk;
import com.github.conferencetrack.model.ModelFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fco Jonas Rodrigues on 17/08/2016.
 */
public class InputReader {

    public static final String REGEX_TITLE_AND_DURATION_TALK = "[0-9]+|[A-Za-z':,.?!*\"@+()\\-\\s]+";
    private static String FILE_NAME = "input.txt";
    private static final Logger LOGGER = Logger.getLogger(InputReader.class.getName());
    private final ModelFactory modelFactory;


    public InputReader(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    private String getUrl() {
        File f1 = new File(FILE_NAME);
        String fileUrl = f1.getAbsolutePath();

        return fileUrl.toString();
    }

    public void fileProcess() {
        try {
            Files.readAllLines(Paths.get(getUrl())).forEach(line -> {
                List<String> output = new ArrayList<String>();
                Matcher match = Pattern.compile(REGEX_TITLE_AND_DURATION_TALK).matcher(line.replace("lightning", "5min"));
                while (match.find()) {
                    output.add(match.group());
                }
                modelFactory.manufactureModel(output);
            });

            modelFactory.processSchedule();
            ListIterator<Talk> iterator = modelFactory.getTalkModel().getTalks().listIterator();
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
        } catch (IOException e2) {
            LOGGER.log(Level.INFO, "File lot not available", e2);
        }
    }

    public static void main(String[] args) {
        new InputReader(new ModelFactory()).fileProcess();
    }


}
