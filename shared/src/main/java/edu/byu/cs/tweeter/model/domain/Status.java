package edu.byu.cs.tweeter.model.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a status (or tweet) posted by a user.
 */
public class Status implements Serializable {

    public String post;
    public User user;
    public long timeStamp;
    public List<String> urls;
    public List<String> mentions;

    public Status() {}

    public Status(String post, User user, long timeStamp, List<String> urls, List<String> mentions) {
        this.post = post;
        this.user = user;
        this.timeStamp = timeStamp;
        this.urls = urls;
        this.mentions = mentions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTimeStamp(long timeStamp) { this.timeStamp = timeStamp; }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPost() {
        return post;
    }

    public List<String> getUrls() {
        return urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(post, status.post) &&
                Objects.equals(user, status.user) &&
                Objects.equals(timeStamp, status.timeStamp) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user, timeStamp, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", userAlias=" + user +
                ", timeStamp=" + timeStamp +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }

}
