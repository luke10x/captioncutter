package dev.luke10x.captioncutter.transcriptionapi.order.adapter.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import dev.luke10x.captioncutter.transcriptionapi.order.service.OrderUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.InputStream;

//@Primary
@Component
@Slf4j
public class SimpleOrderUploadService implements OrderUploadService {
    @Autowired
    AmazonS3 amazonS3;

    @Override
    public Mono<Order> createOrder(Flux<DataBuffer> content, long length, String bucketName, String fileName) {
        return DataBufferUtils.join(content)
                .map(DataBuffer::asInputStream)
                .map(inputStream -> {
                    log.info("Now we will write to s3 this IS");
                    return putToStorage(bucketName, fileName, inputStream, (int) length);
                });
    }

    private Order putToStorage(String bucketName, String fileKey, InputStream inputStream, Integer length) {
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
