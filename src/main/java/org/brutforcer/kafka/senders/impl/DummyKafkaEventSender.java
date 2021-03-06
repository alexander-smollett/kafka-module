package org.brutforcer.kafka.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import org.brutforcer.kafka.senders.EventSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "off", matchIfMissing = false)
public class DummyKafkaEventSender implements EventSender {

    @PostConstruct
    void init(){
        log.info("Kafka module set mode: off");
    }

    @Override
    public <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body) {
        return sendEvent(type, body, null);
    }

    @Override
    public <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body, UUID correlationId) {
        log.warn("Kafka module has mode \"OFF\". Event NOT SENDING in kafka. In you need really sending events, change {kafka.mode} property to default or reactive\nEvent type: {}, correlation ID: {}, event body: {}", type, correlationId, body);
        return null;
    }

    @Override
    public ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event) {
        return sendEvent(event, null);
    }

    @Override
    public ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event, UUID correlationId) {
        log.warn("Kafka module has mode \"OFF\". Event NOT SENDING in kafka. In you need really sending events, change {kafka.mode} property to default or reactive\nEvent type: {}, correlation ID: {}, event body: {}", event.type(), correlationId, event.body());
        return null;
    }
}
