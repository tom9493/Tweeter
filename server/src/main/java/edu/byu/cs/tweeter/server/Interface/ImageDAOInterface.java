package edu.byu.cs.tweeter.server.Interface;

import java.io.InputStream;
import java.util.Base64;

public interface ImageDAOInterface {
    public abstract void uploadImage(String keyName, InputStream stream);
    public abstract String getImageURL(String keyName);
}
