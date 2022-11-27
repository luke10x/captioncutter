package dev.luke10x.captioncutter.transcriptionapi.order.adapter.aws;

import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import dev.luke10x.captioncutter.transcriptionapi.order.service.OrderUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

import static org.springframework.core.io.buffer.DataBufferUtils.join;
import static software.amazon.awssdk.core.async.AsyncRequestBody.*;

@Primary
@Component
@Slf4j
public class AsyncOrderUploadService implements OrderUploadService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    S3AsyncClient s3AsyncClient;

    @Override
    public Mono<Order> createOrder(Flux<DataBuffer> content, long length, String fileName) {
        Mono<ByteBuffer> byteBuffers = join(content)
                .map(DataBuffer::asByteBuffer);
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        CompletableFuture<CreateBucketResponse> createBucketFuture = s3AsyncClient.createBucket(
                CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());

        CompletableFuture<PutObjectResponse> putObjectFuture = s3AsyncClient
                .putObject(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .contentLength(length)
                                .key(fileName)
                                .contentType(mediaType.toString())
                                .build(),
                        fromPublisher(byteBuffers));

        CompletableFuture<PutObjectResponse> future = createBucketFuture
                .thenCombine(putObjectFuture, (cbr, por) -> por);

        return Mono.fromFuture(future).map(putResponse -> {
            String responseAsStr = putResponse.toString();
            log.info("🍄v2.1");

            return (new Order())
                    .orderId(fileName)
                    .status("Response was: " + responseAsStr);
        });
    }
}
