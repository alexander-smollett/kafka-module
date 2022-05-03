package org.brutforcer.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.brutforcer.kafka.events.EventCreator;
import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
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
    public <T> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body) {
        KafkaEvent event = eventCreator.event(type, body);
        return kafkaTemplate.send(topic.name(), event);
    }
}
