package org.brutforcer.kafka.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventCreator {

    private final ObjectMapper mapper;

    @Autowired
    public EventCreator(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public<T> KafkaEvent event(KafkaEvent.Type type, T body) {
        if (body.getClass().equals(type.getClazz())){
            try {
                return new KafkaEvent(type, mapper.writeValueAsBytes(body));
            } catch (JsonProcessingException e) {
                log.error("ERROR: ",e);
                return null;
            }
        }
        log.error("Error in creating event -> event type not match body class. Expected class: {}, received: {}", type.getClazz(), body.getClass());
        throw new IllegalArgumentException("Event type not match body class");
    }
}
