package edu.byu.cs.tweeter.client.model.net;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;

import java.util.List;

public class TweeterRequestException extends TweeterRemoteException {

    public TweeterRequestException(String message, String remoteExceptionType, List<String> remoteStakeTrace) {
        super(message, remoteExceptionType, remoteStakeTrace);
    }
}