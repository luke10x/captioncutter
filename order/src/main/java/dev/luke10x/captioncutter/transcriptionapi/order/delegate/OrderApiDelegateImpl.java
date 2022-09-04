package dev.luke10x.captioncutter.transcriptionapi.order.delegate;

import dev.luke10x.captioncutter.transcriptionapi.order.aws.v2.AwsV2AsyncUploader;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import dev.luke10x.captioncutter.transcriptionapi.order.aws.v1.AWSV1SyncFileUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.UUID;

@Slf4j
@Component
public class OrderApiDelegateImpl implements OrderApiDelegate
{
    @Autowired
    AWSV1SyncFileUploader syncUploader;

    @Autowired
    AwsV2AsyncUploader asyncUploader;

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

//        return DataBufferUtils.join(exchange.getRequest().getBody())
//                .map(DataBuffer::asInputStream)
//                .map(inputStream -> {
//                    log.info("Now we will write to s3 this IS");
//                    return syncUploader.putToStorage(bucketName, fileKey, inputStream, length);
//                })
//                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o));


        Flux<ByteBuffer> byteBufferMono =  exchange.getRequest().getBody()
                .map(DataBuffer::asByteBuffer);

        return asyncUploader.putToStorage(bucketName, fileKey, byteBufferMono, length)
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o));
    }
}
