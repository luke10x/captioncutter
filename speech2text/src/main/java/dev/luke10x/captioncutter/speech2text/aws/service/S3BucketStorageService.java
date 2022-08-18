package dev.luke10x.captioncutter.speech2text.aws.service;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class S3BucketStorageService {

    private Logger logger = LoggerFactory.getLogger(S3BucketStorageService.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucketName;

    /**
     * TODO should not be in this MS!
     * Upload file into AWS S3
     *
     * @param keyName
     * @param file
     * @return String
     */
    public String uploadFile(String keyName, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            System.out.println("🇺🇦 creates bucket");
            amazonS3.createBucket(bucketName);
            System.out.println("🇺🇦 createD bucket");

            amazonS3.putObject(
                    new PutObjectRequest(bucketName, keyName, file.getInputStream(), metadata)
            );
            System.out.println("🇺🇦 createD OBJECT ina bucket");

            return "File uploaded: " + keyName;
        } catch (IOException ioe) {
            logger.error("IOException: " + ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
        return "File not uploaded: " + keyName;
    }

    /**
     * Downloads file using amazon S3 client from S3 bucket
     *
     * @param keyName
     * @return ByteArrayOutputStream
     */
    public InputStream downloadFile(String keyName) {
        try {
            S3Object s3object = amazonS3.getObject(new GetObjectRequest(bucketName, keyName));
            return s3object.getObjectContent();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            int len;
//            byte[] buffer = new byte[4096];
//            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
//                outputStream.write(buffer, 0, len);
//            }
//
//            return outputStream;
//        } catch (IOException ioException) {
//            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException Message:    " + serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }
}
