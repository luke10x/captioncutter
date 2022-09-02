package dev.luke10x.captioncutter.transcriptionapi.order.delegate;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
//import software.amazon.awssdk.core.SdkResponse;
//import software.amazon.awssdk.core.async.AsyncRequestBody;
//import software.amazon.awssdk.services.s3.S3AsyncClient;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Component
public class OrderApiDelegateImpl implements OrderApiDelegate
{
//    @Autowired
//    S3AsyncClient amazonS3Async;

    @Autowired
    AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String bucketName;

    public Mono<ResponseEntity<Order>> orderPost(ServerWebExchange exchange) {

        var headers = exchange.getRequest().getHeaders();
        int length = (int) headers.getContentLength();

        if (length < 0) {
//            throw new UploadFailedException(
//                    HttpStatus.BAD_REQUEST.value(),
//                    Optional.of("required header missing: Content-Length")
//            );
        }
        String fileKey = UUID.randomUUID() + ".wav";

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .map(DataBuffer::asInputStream)
                .map(inputStream -> {
                    log.info("Now we will write to s3 this IS");
                    return putToStorage(bucketName, fileKey, inputStream, length);
                }).map(putResult -> {
                    var lastModified = putResult.getMetadata().getLastModified().toString();
                    Order o = new Order();
                    o.setOrderId(fileKey);
                    o.setStatus("Last modified at: " + lastModified);

                    return ResponseEntity.status(HttpStatus.CREATED).body(o);
                });

        // All this bellow is for asynch client
//        CompletableFuture<PutObjectResponse> future = amazonS3Async
//                .putObject(PutObjectRequest.builder()
//                                .bucket(bucketName)
//                                .contentLength(length)
//                        .metadata(metadata)
//                                .key(UUID.randomUUID() + ".wav")
//                                .contentType(String.valueOf(MediaType.APPLICATION_OCTET_STREAM))
//                                .build(),
//                        AsyncRequestBody.fromPublisher(monoByteBuffer)
//                )
//                .thenApply(e -> {
//                    System.out.println("UPL COMPL");
//                    return e;
//                })
//                .exceptionally(e -> {
//                    System.out.println("BEDA");
//                    try {
//                        throw e;
//                    } catch (Throwable ex) {
//                        throw new RuntimeException(ex);
//                    }
//                });
    }

    PutObjectResult putToStorage(String bucketName, String fileKey, InputStream inputStream, Integer length) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(length);
            log.info("Creates bucket");
            amazonS3.createBucket(bucketName);
            log.info("Created bucket");

            PutObjectResult result = amazonS3.putObject(
                    new PutObjectRequest(bucketName, fileKey, inputStream, metadata)
            );
            log.info("Object was put in a bucket");
            return result;
        } catch (AmazonServiceException serviceException) {
            log.info("AmazonServiceException: "+ serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            log.info("AmazonClientException Message: " + clientException.getMessage());
            throw clientException;
        }
    }
}
