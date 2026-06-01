package com.zaitsev.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = "transfer-events",
            groupId = "notification-group"
    )
    public void listen(String message) {

        log.info("NEW EVENT: {}", message);
    }
}