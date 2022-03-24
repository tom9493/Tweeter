package edu.byu.cs.tweeter.server.Interface;

import java.io.InputStream;

public interface ImageDAOInterface {
    public abstract void uploadImage(String keyName, InputStream stream);
    public abstract String getImageURL(String keyName);
}
