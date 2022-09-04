package dev.luke10x.captioncutter.transcriptionapi.order.service;

import dev.luke10x.captioncutter.transcriptionapi.order.openapi.model.Order;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderUploadService {
    Mono<Order> createOrder(
            Flux<DataBuffer> content, long length, String bucketName, String fileName
    );
}
