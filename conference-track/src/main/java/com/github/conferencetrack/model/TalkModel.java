package com.github.conferencetrack.model;

import com.github.conferencetrack.bean.Talk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Jonas Rodrigues on 17/08/2016.
 */
public class TalkModel {

    private List<Talk> talks;

    public List<Talk> getTalks() {
        if (talks == null) {
            talks = new ArrayList<Talk>();
        }
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalkModel talkModel = (TalkModel) o;
        return Objects.equals(talks, talkModel.talks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talks);
    }

    @Override
    public String toString() {
        return "TalkModel{" +
                "talks=" + talks +
                '}';
    }
}
