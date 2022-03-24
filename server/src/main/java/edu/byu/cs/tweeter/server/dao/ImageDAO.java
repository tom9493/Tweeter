package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import edu.byu.cs.tweeter.server.Interface.ImageDAOInterface;

import java.io.InputStream;

public class ImageDAO implements ImageDAOInterface {
    AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-west-1").build();
    String bucketName = "cs340s3";

    @Override
    public void uploadImage(String userAlias, InputStream imageURL) {
        String fileObjKeyName = userAlias + "_photo.png";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/png");
        PutObjectRequest request = new PutObjectRequest(bucketName, fileObjKeyName, imageURL, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
    }

    @Override
    public String getImageURL(String keyName) { return s3.getUrl(bucketName, keyName).toString(); }
}
