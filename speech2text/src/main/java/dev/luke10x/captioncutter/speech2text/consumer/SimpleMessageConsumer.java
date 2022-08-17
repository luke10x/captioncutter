package dev.luke10x.captioncutter.speech2text.consumer;

//import com.authga.springcloudaws.model.Event;
import dev.luke10x.captioncutter.speech2text.caption.TranscriberDemo;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

//import static com.authga.springcloudaws.config.AWSConfigConstants.ORDER_QUEUE;

@Slf4j
@Controller
public class SimpleMessageConsumer {
    private static final String ORDER_QUEUE = "dummy-queue";

//    @Override
    @SqsListener(value = ORDER_QUEUE, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void consume(@NotificationMessage String event) {
        if (event != null) {
            log.info("Received order event for consumer 10: " + event);
            try {
                TranscriberDemo.go();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}