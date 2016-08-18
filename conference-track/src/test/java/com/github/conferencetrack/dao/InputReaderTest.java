package com.github.conferencetrack.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

/**
 * Test to {@link com.github.conferencetrack.dao.InputReader}
 * Created by Jonas Rodrigues on 18/08/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class InputReaderTest {

    @InjectMocks
    @Spy
    InputReader inputReader;

    @Test
    public void shouldProcessFileInputTalk(){
        //given
        given(inputReader.getUrl(anyString())).willReturn(getUrl("input_test.txt"));

        //when
        List<List<String>> result = inputReader.fileProcess();

        //then
        assertEquals(19, result.size());
    }

    private String getUrl(String fileName) {
        File f1 = new File(fileName);
        String fileUrl = f1.getAbsolutePath();

        return fileUrl.toString();
    }

}
