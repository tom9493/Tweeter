package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class FollowersCountResponse extends Response {

    private int count;

    FollowersCountResponse(boolean success, String message) {
        super(success, message);
    }

    FollowersCountResponse(boolean success) {
        super(success);
    }

    public int getCount() { return count; }

    @Override
    public boolean equals(Object param) {
        if (this == param) {
            return true;
        }

        if (param == null || getClass() != param.getClass()) {
            return false;
        }

        FollowersCountResponse that = (FollowersCountResponse) param;

        return (Objects.equals(count, that.count) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}