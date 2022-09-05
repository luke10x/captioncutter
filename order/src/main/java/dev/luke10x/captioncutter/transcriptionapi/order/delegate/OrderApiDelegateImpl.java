package dev.luke10x.captioncutter.transcriptionapi.order.delegate;

import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import dev.luke10x.captioncutter.transcriptionapi.order.service.OrderUploadService;
import dev.luke10x.captioncutter.transcriptionapi.shared.Greeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class OrderApiDelegateImpl implements OrderApiDelegate
{
    @Autowired
    OrderUploadService orderUploadService;

    @Value("${application.bucket.name}")
    private String bucketName;

    public Mono<ResponseEntity<Order>> orderPost(ServerWebExchange exchange) {


        var g = new Greeter();
        g.sayHi();

        var headers = exchange.getRequest().getHeaders();
        int length = (int) headers.getContentLength();

        if (length < 0) {
//            throw new UploadFailedException(
//                    HttpStatus.BAD_REQUEST.value(),
//                    Optional.of("required header missing: Content-Length")
//            );
        }
        String fileKey = UUID.randomUUID() + ".wav";

        return orderUploadService.createOrder(
                exchange.getRequest().getBody(),
                length,
                bucketName,
                fileKey
        )
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o));
    }
}
