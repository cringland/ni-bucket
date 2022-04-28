package com.cringland.ni.repository;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.extern.java.Log;

@Component
@Log
public class ImageStorage {

    private static final String BUCKET_NAME = "ni-bucket-image";
    private final AmazonS3 s3;


    @Autowired
    public ImageStorage(AmazonS3 s3) {
        if (s3.doesBucketExistV2(BUCKET_NAME)) {
            log.info("\"" + BUCKET_NAME + "\" bucket already exists. Not attempting to create.");
        } else {
            log.info("Creating \"" + BUCKET_NAME + "\" bucket");
            s3.createBucket(BUCKET_NAME);
        }
        this.s3 = s3;
    }

    public String storeImage(String prefix, MultipartFile file) throws IOException {
        var id = prefix + "/" + UUID.randomUUID().toString();

        var metadata = new ObjectMetadata();
        metadata.addUserMetadata("Content-Length", String.valueOf(file.getSize()));
        if (file.getContentType() != null)
            metadata.addUserMetadata("Content-Type", file.getContentType());

        s3.putObject(BUCKET_NAME, id, file.getInputStream(), metadata);
        log.info("Stored image in S3 with id " + id);
        return id;
    }
}
