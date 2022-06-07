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
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "off", matchIfMissing = true)
public class DummyReactiveKafkaEventSender implements ReactiveEventSender {

    @PostConstruct
    void init(){
        log.info("Kafka module set mode: off");
    }

    @Override
    public Mono<Void> sendEvent(KafkaEvent event) {
        log.warn("Kafka module has mode \"OFF\". Event NOT SENDING in kafka. In you need really sending events, change {kafka.mode} property to default or reactive");
        return Mono.empty();
    }

    @Override
    public <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body) {
        log.warn("Kafka module has mode \"OFF\". Event NOT SENDING in kafka. In you need really sending events, change {kafka.mode} property to default or reactive");
        return Mono.empty();
    }
}
