package org.brutforcer.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.EventCreator;
import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "default")
public class KafkaEventSender implements EventSender{

    private final NewTopic topic;
    private final EventCreator eventCreator;
    private final KafkaTemplate<Long, KafkaEvent> kafkaTemplate;

    @Autowired
    public KafkaEventSender(@Qualifier("eventTopic") NewTopic topic,
                            EventCreator eventCreator,
                            KafkaTemplate<Long, KafkaEvent> kafkaTemplate) {
        this.topic = topic;
        this.eventCreator = eventCreator;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body) {
        log.debug("IN sendEvent -> send event to kafka with type: {} and body: {}", type, body);
        KafkaEvent event = eventCreator.event(type, body);
        var send = kafkaTemplate.send(topic.name(), event);
        log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", type, body);
        return send;
    }

    @Override
    public ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event) {
        log.debug("IN sendEvent -> send event to kafka with type: {} and body: {}", event.type(), event.body());
        var send = kafkaTemplate.send(topic.name(), event);
        log.info("IN sendEvent -> event with type: {} successfully send to kafka. Body: {}", event.type(), event.body());
        return send;
    }
}
