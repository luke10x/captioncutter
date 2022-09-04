package dev.luke10x.captioncutter.transcriptionapi.order.aws.v2;

import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

@Component
public class AwsV2AsyncUploader {

    @Autowired
    S3AsyncClient s3AsyncClient;

    public Mono<Order> putToStorage(String bucketName, String fileKey, Flux<ByteBuffer> byteBufferMono, Integer length) {
        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        CompletableFuture<PutObjectResponse> future = s3AsyncClient
                .putObject(PutObjectRequest.builder()
                                .bucket(bucketName)
                                .contentLength(Long.valueOf(length))
                                .key(fileKey.toString())
                                .contentType(mediaType.toString())
//                                .metadata(metadata)
                                .build(),
                        AsyncRequestBody.fromPublisher(byteBufferMono.next()))
                .thenApply(v -> {
                    System.out.println("🍄Grybas ");
                    return v;
                });
        System.out.println("🚨Red light ");


        return Mono.fromFuture(future).map(putResponse -> {
            var responseAsStr = putResponse.toString();
            Order o = new Order();
            o.setOrderId(fileKey);
            o.setStatus("Response was: " + responseAsStr);

           return o;
        });

    }
}
