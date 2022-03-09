package edu.byu.cs.tweeter.model.net.response;

import java.util.Objects;

public class FollowingCountResponse extends Response {

    private int count;

    public FollowingCountResponse(String message) {
        super(false, message);
    }

    public FollowingCountResponse(int count) {
        super(true);
        this.count = count;
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

        FollowingCountResponse that = (FollowingCountResponse) param;

        return (Objects.equals(count, that.count) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}