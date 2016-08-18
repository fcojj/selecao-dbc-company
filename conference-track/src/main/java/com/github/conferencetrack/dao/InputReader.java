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

    public String getUrl(String fileName) {
        File f1 = new File(fileName);
        String fileUrl = f1.getAbsolutePath();

        return fileUrl.toString();
    }

    public List<List<String>> fileProcess() {
        List<List<String>> lines = new ArrayList<List<String>>();
        try {
            Files.readAllLines(Paths.get(getUrl(FILE_NAME))).forEach(line -> {
                List<String> output = new ArrayList<String>();
                Matcher match = Pattern.compile(REGEX_TITLE_AND_DURATION_TALK).matcher(line.replace("lightning", "5min"));
                while (match.find()) {
                    output.add(match.group());
                }
                lines.add(output);
            });


        } catch (IOException e2) {
            LOGGER.log(Level.INFO, "File lot not available", e2);
        }finally {
            return lines;
        }
    }
}
