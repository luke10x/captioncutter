package dev.luke10x.captioncutter.speech2text.consumer;

import dev.luke10x.captioncutter.speech2text.caption.TranscriberDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Controller;

//import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_QUEUE;

@Slf4j
@Controller
public class SimpleMessageConsumer {
    private static final String ORDER_QUEUE = "dummy-queue";

    @Autowired
    TranscriberDemo transcriber;
//    @Override
    @SqsListener(value = ORDER_QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void consume(@NotificationMessage String event) {
        if (event != null) {
            log.info("Received order event for consumer 10: " + event);
            try {
                transcriber.go();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}