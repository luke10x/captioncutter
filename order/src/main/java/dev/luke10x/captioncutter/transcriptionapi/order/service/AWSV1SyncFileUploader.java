package dev.luke10x.captioncutter.transcriptionapi.order.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
public class AWSV1SyncFileUploader {
    @Autowired
    AmazonS3 amazonS3;

    public Order putToStorage(String bucketName, String fileKey, InputStream inputStream, Integer length) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(length);
            log.info("Creates bucket");
            amazonS3.createBucket(bucketName);
            log.info("Created bucket");

            PutObjectResult putResult = amazonS3.putObject(
                    new PutObjectRequest(bucketName, fileKey, inputStream, metadata)
            );
            log.info("Object was put in a bucket");

            var lastModified = putResult.getMetadata().getLastModified().toString();
            Order o = new Order();
            o.setOrderId(fileKey);
            o.setStatus("Last modified at: " + lastModified);

            return o;
        } catch (AmazonServiceException serviceException) {
            log.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            log.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }
}
