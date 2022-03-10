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
    public String datetime;
    public List<String> urls;
    public List<String> mentions;

    public Status() {
    }

    public Status(String post, User user, String datetime, List<String> urls, List<String> mentions) {
        this.post = post;
        this.user = user;
        this.datetime = datetime;
        this.urls = urls;
        this.mentions = mentions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return datetime;
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
                Objects.equals(datetime, status.datetime) &&
                Objects.equals(mentions, status.mentions) &&
                Objects.equals(urls, status.urls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user, datetime, mentions, urls);
    }

    @Override
    public String toString() {
        return "Status{" +
                "post='" + post + '\'' +
                ", user=" + user +
                ", datetime=" + datetime +
                ", mentions=" + mentions +
                ", urls=" + urls +
                '}';
    }

}
