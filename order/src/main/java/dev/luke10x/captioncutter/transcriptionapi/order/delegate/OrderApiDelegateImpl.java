package dev.luke10x.captioncutter.transcriptionapi.order.delegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.api.OrderApiDelegate;
import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Component
public class OrderApiDelegateImpl implements OrderApiDelegate {
    public Mono<ResponseEntity<Order>> orderPost(ServerWebExchange exchange) {

//        exchange.getRequest().getBody()
        Order o = new Order();
        o.setOrderId(UUID.randomUUID().toString());
        o.setStatus("BAKED");
        ResponseEntity<Order> response = ResponseEntity.accepted().body(o);
        Mono<ResponseEntity<Order>> mono = Mono.delay(Duration.ofSeconds(5))
                .thenReturn(response);

        return mono;
    }
}
