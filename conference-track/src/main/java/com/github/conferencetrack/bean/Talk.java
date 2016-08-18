package com.github.conferencetrack.bean;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Created by Fco Jonas Rodrigues on 17/08/2016.
 */
public class Talk{

    private String title;
    private Duration duration;
    private String track;
    private String session;

    public Talk(String title, Duration duration) {
        this.title = title;
        this.duration = duration;
        this.track = "";
        this.session = "";
    }

    public Talk(String title, String track) {
        this.title = title;
        this.track = track;
    }

    public Talk() {

    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talk talk = (Talk) o;
        return Objects.equals(title, talk.title) &&
                Objects.equals(duration, talk.duration) &&
                Objects.equals(track, talk.track) &&
                Objects.equals(session, talk.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, duration, track, session);
    }

    @Override
    public String toString() {
        return "Talk{" +
                "title='" + title + '\'' +
                ", duration=" + duration +
                ", track='" + track + '\'' +
                ", session='" + session + '\'' +
                '}';
    }
}
