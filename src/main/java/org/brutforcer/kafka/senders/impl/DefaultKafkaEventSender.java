package org.brutforcer.kafka.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.EventCreator;
import org.brutforcer.kafka.events.KafkaEvent;
import org.brutforcer.kafka.senders.EventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "default", matchIfMissing = false)
public class DefaultKafkaEventSender implements EventSender {

    private final NewTopic topic;
    private final EventCreator eventCreator;
    private final KafkaTemplate<Long, KafkaEvent> kafkaTemplate;
    private static final String CORRELATION_HEADER = "tmx-correlation-id";

    @Autowired
    public DefaultKafkaEventSender(@Qualifier("eventTopic") NewTopic topic,
                                   EventCreator eventCreator,
                                   KafkaTemplate<Long, KafkaEvent> kafkaTemplate) {
        this.topic = topic;
        this.eventCreator = eventCreator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    void init(){
        log.info("Kafka module set mode: default");
    }

    @Override
    public <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body) {
        return sendEvent(type, body, null);
    }

    @Override
    public <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body, UUID correlationId) {
        return sendEvent(eventCreator.event(type, body), null);
    }

    @Override
    public ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event) {
        return sendEvent(event, null);
    }

    @Override
    public ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event, UUID correlationId) {
        log.debug("IN sendEvent -> send event to kafka with type: {} and body: {}", event.type(), event.body());
        ProducerRecord<Long, KafkaEvent> record = new ProducerRecord<>(topic.name(), event);
        if (correlationId != null) {
            record.headers().add(CORRELATION_HEADER, correlationId.toString().getBytes(StandardCharsets.UTF_8));
        }
        var send = kafkaTemplate.send(record);
        log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", event.type(), event.body());
        return send;
    }
}
