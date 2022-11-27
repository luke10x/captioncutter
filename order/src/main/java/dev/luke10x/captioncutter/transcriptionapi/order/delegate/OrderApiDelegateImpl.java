package dev.luke10x.captioncutter.transcriptionapi.order.delegate;

import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import dev.luke10x.captioncutter.transcriptionapi.order.service.OrderUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.util.UUID.*;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@Component
public class OrderApiDelegateImpl implements OrderApiDelegate
{
    @Autowired
    OrderUploadService orderUploadService;

    public Mono<ResponseEntity<Order>> orderPost(ServerWebExchange exchange) {

        var headers = exchange.getRequest().getHeaders();
        int length = (int) headers.getContentLength();

        if (length < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Content-Length header must be set"
            );
        }
        String fileKey = randomUUID() + ".wav";

        return orderUploadService.createOrder(
                exchange.getRequest().getBody(),
                length,
                fileKey
        )
                .map(o -> status(HttpStatus.CREATED).body(o));
    }
}
