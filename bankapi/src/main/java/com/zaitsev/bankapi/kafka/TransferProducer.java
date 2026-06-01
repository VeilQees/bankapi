package com.zaitsev.bankapi.kafka;

import com.zaitsev.common.dto.TransferEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferProducer {

    private final KafkaTemplate<String, TransferEvent> kafkaTemplate;

    public void sendTransferEvent(TransferEvent event){

        kafkaTemplate.send(
                "transfer-topic",
                event
        );
    }
}