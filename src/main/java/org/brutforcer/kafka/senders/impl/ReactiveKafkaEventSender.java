package org.brutforcer.kafka.senders.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.EventCreator;
import org.brutforcer.kafka.events.KafkaEvent;
import org.brutforcer.kafka.senders.ReactiveEventSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "reactive", matchIfMissing = false)
public class ReactiveKafkaEventSender implements ReactiveEventSender {

    private final NewTopic topic;
    private final EventCreator eventCreator;
    private final KafkaSender<Long, KafkaEvent> sender;
    private static final String CORRELATION_HEADER = "tmx-correlation-id";

    @Autowired
    public ReactiveKafkaEventSender(@Qualifier("eventTopic") NewTopic topic,
                                    EventCreator eventCreator, KafkaSender<Long, KafkaEvent> sender) {
        this.topic = topic;
        this.eventCreator = eventCreator;
        this.sender = sender;
    }

    @PostConstruct
    void init() {
        log.info("Kafka module set mode: reactive");
    }

    @Override
    public <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body) {
        return sendEvent(type, body, null);
    }

    @Override
    public <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body, UUID correlationId) {
        return sendEvent(eventCreator.event(type, body), null);
    }

    @Override
    public Mono<Void> sendEvent(KafkaEvent event) {
        return sendEvent(event, null);
    }

    @Override
    public Mono<Void> sendEvent(KafkaEvent event, UUID correlationId) {
        return sender.createOutbound()
                .send(Mono.just(record(event, correlationId)))
                .then()
                .doOnSuccess(ignore -> log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", event.type(), event.body()));
    }

    private ProducerRecord<Long, KafkaEvent> record(KafkaEvent event, UUID correlationId) {
        ProducerRecord<Long, KafkaEvent> record = new ProducerRecord<>(topic.name(), event);
        if (correlationId != null) {
            record.headers().add(CORRELATION_HEADER, correlationId.toString().getBytes(StandardCharsets.UTF_8));
        }
        return record;
    }
}
