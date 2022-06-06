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

@Slf4j
@Service
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "reactive", matchIfMissing = false)
public class ReactiveKafkaEventSender implements ReactiveEventSender {

    private final NewTopic topic;
    private final EventCreator eventCreator;
    private final KafkaSender<Long, KafkaEvent> sender;

    @Autowired
    public ReactiveKafkaEventSender(@Qualifier("eventTopic") NewTopic topic,
                                    EventCreator eventCreator, KafkaSender<Long, KafkaEvent> sender) {
        this.topic = topic;
        this.eventCreator = eventCreator;
        this.sender = sender;
    }

    @PostConstruct
    void init(){
        log.info("Kafka module set mode: reactive");
    }

    @Override
    public Mono<Void> sendEvent(KafkaEvent event) {
        return sender.createOutbound()
                .send(Mono.just(new ProducerRecord<Long, KafkaEvent>(topic.name(), event)))
                .then()
                .doOnSuccess(ignore -> log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", event.type(), event.body()));
    }

    @Override
    public <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body) {
        return sender.createOutbound()
                .send(Mono.just(new ProducerRecord<Long, KafkaEvent>(topic.name(), eventCreator.event(type, body))))
                .then()
                .doOnSuccess(ignore -> log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", type, body));
    }
}
