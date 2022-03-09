package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.Status;

import java.util.List;
import java.util.Objects;

public class StoryResponse extends PagedResponse {

    private List<Status> story;

    StoryResponse(String message) {
        super(message, false);
    }

    StoryResponse(List<Status> story, boolean hasMorePages) {
        super(hasMorePages);
        this.story = story;
    }

    public List<Status> getStory() {
        return story;
    }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        StoryResponse that = (StoryResponse) param;

        return (Objects.equals(story, that.story) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(story);
    }
}
