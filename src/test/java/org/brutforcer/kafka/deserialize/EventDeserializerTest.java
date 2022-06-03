package org.brutforcer.kafka.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.dto.UserRegistryEventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

class EventDeserializerTest {

    private ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private EventDeserializer deserializer = new EventDeserializer();

    @Test
    void deserialize() throws JsonProcessingException {
        EventBody body = new UserRegistryEventBody(
                "128",
                "user",
                "123",
                "NAME",
                "LAST NAME",
                "OTHER NAME",
                "email@mail.ru",
                LocalDate.now(),
                "MALE",
                "TUT",
                Collections.emptyList()
        );
        var event = new KafkaEvent(KafkaEvent.Type.USER_REGISTRY, body);
        var eventBytes = mapper.writeValueAsBytes(event);
        var deserializedEvent = deserializer.deserialize("X", eventBytes);
        System.out.println(deserializedEvent);
    }
}