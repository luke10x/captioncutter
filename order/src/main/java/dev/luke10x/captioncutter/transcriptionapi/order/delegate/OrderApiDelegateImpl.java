package dev.luke10x.captioncutter.transcriptionapi.order.delegate;
import dev.luke10x.captioncutter.transcriptionapi.order.aws.exception.UploadFailedException;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderApiDelegateImpl
        implements OrderApiDelegate
{

    @Autowired
    S3AsyncClient amazonS3Async;

    @Value("${application.bucket.name}")
    private String bucketName;

//        uploadFile(@RequestParam("fileName") String fileName,
//                @RequestParam("file") MultipartFile file) {

    public Mono<ResponseEntity<Order>> orderPost(ServerWebExchange exchange) {

//        exchange.getRequest().getBody()
//        Order o = new Order();
//        o.setOrderId(UUID.randomUUID().toString());
//        o.setStatus("BAKED2");
//
////
//        ResponseEntity<Order> response = ResponseEntity.accepted().body(o);
//        Mono<ResponseEntity<Order>> mono = Mono.delay(Duration.ofSeconds(5))
//                .thenReturn(response);
//        return mono;


        Flux<java.nio.ByteBuffer> byteBuffers = exchange.getRequest().getBody()
                .map(DataBuffer::asByteBuffer);
        return uploadToS3(exchange.getRequest().getHeaders(),
                byteBuffers);
    }

    private Mono<ResponseEntity<Order>> uploadToS3(@RequestHeader HttpHeaders headers,
                                                                    @RequestBody Flux<ByteBuffer> body) {
        long length = headers.getContentLength();
        if (length < 0) {
            throw new UploadFailedException(HttpStatus.BAD_REQUEST.value(), Optional.of("required header missing: Content-Length"));
        }

        String fileKey = UUID.randomUUID().toString();
        Map<String, String> metadata = new HashMap<String, String>();
        MediaType mediaType = headers.getContentType();

        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        CompletableFuture<PutObjectResponse> future = amazonS3Async
                .putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .contentLength(length)
                        .key(UUID.randomUUID().toString() + ".wav")
                        .contentType(mediaType.toString())
                        .metadata(metadata)
                        .build(), AsyncRequestBody.fromPublisher(body));

        return Mono.fromFuture(future)
                .map((response) -> {
                    checkResult(response);

                    Order o = new Order();
                    o.setOrderId(fileKey);
                    o.setStatus("SLICED2");
//var o = "HIIH";
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(o);
                });
    }

    /**
     * check result from an API call.
     * @param result Result from an API call
     */
    private static void checkResult(SdkResponse result) {
        if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful()) {
            throw new UploadFailedException(result);
        }
    }
}
