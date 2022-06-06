package org.brutforcer.kafka.deserialize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.brutforcer.kafka.dto.*;
import org.brutforcer.kafka.events.KafkaEvent;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EventDeserializerTest {

    private final ObjectMapper mapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();
    private final EventDeserializer deserializer = new EventDeserializer();

    @Test
    void userRegistryDeserialize() throws JsonProcessingException {
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
        assertEquals(KafkaEvent.Type.USER_REGISTRY, deserializedEvent.type());
        assertTrue(deserializedEvent.body() instanceof UserRegistryEventBody);
        UserRegistryEventBody deserializedBody = (UserRegistryEventBody) deserializedEvent.body();
        assertEquals(body, deserializedBody);
    }

    @Test
    void userEditDeserialize() throws JsonProcessingException {
        EventBody body = new UserEditEventBody(
                "USERNAME",
                "NAME",
                "LAST NAME",
                "OTHER NAME",
                "MALE",
                LocalDate.now(),
                "ADDRESS"
        );
        var event = new KafkaEvent(KafkaEvent.Type.USER_EDIT, body);
        var eventBytes = mapper.writeValueAsBytes(event);
        var deserializedEvent = deserializer.deserialize("X", eventBytes);
        assertEquals(KafkaEvent.Type.USER_EDIT, deserializedEvent.type());
        assertTrue(deserializedEvent.body() instanceof UserEditEventBody);
        UserEditEventBody deserializedBody = (UserEditEventBody) deserializedEvent.body();
        assertEquals(body, deserializedBody);
    }

    @Test
    void userDeleteDeserialize() throws JsonProcessingException {
        EventBody body = new UserDeleteEventBody(
                "10",
                "username"
        );
        var event = new KafkaEvent(KafkaEvent.Type.USER_DELETE, body);
        var eventBytes = mapper.writeValueAsBytes(event);
        var deserializedEvent = deserializer.deserialize("X", eventBytes);
        assertEquals(KafkaEvent.Type.USER_DELETE, deserializedEvent.type());
        assertTrue(deserializedEvent.body() instanceof UserDeleteEventBody);
        UserDeleteEventBody deserializedBody = (UserDeleteEventBody) deserializedEvent.body();
        assertEquals(body, deserializedBody);
    }

    @Test
    void userConfirmDeserialize() throws JsonProcessingException {
        EventBody body = new UserConfirmEventBody(
                "10",
                "username"
        );
        var event = new KafkaEvent(KafkaEvent.Type.USER_CONFIRM, body);
        var eventBytes = mapper.writeValueAsBytes(event);
        var deserializedEvent = deserializer.deserialize("X", eventBytes);
        assertEquals(KafkaEvent.Type.USER_CONFIRM, deserializedEvent.type());
        assertTrue(deserializedEvent.body() instanceof UserConfirmEventBody);
        UserConfirmEventBody deserializedBody = (UserConfirmEventBody) deserializedEvent.body();
        assertEquals(body, deserializedBody);
    }
}