package org.brutforcer.kafka.events;

import lombok.extern.slf4j.Slf4j;
import org.brutforcer.kafka.dto.EventBody;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "kafka", name = "enable", havingValue = "true", matchIfMissing = true)
public class EventCreator {

    public <T extends EventBody> KafkaEvent event(KafkaEvent.Type type, T body) {
        if (body.getClass().equals(type.getClazz())) {
            return new KafkaEvent(type, body);
        }
        log.error("Error in creating event -> event type not match body class. Expected class: {}, received: {}", type.getClazz(), body.getClass());
        throw new IllegalArgumentException("Event type not match body class");
    }
}
