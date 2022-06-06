package org.brutforcer.kafka.events;

import org.brutforcer.kafka.dto.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EventCreatorTest {

    private final EventCreator eventCreator = new EventCreator();

    @Test
    void successfullyRegistryEvent() {
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
        var event = eventCreator.event(KafkaEvent.Type.USER_REGISTRY, body);
        assertEquals(KafkaEvent.Type.USER_REGISTRY, event.type());
        assertEquals(body, event.body());
    }

    @Test
    void successfullyConfirmEvent() {
        EventBody body = new UserConfirmEventBody(
                "10",
                "username"
        );
        var event = eventCreator.event(KafkaEvent.Type.USER_CONFIRM, body);
        assertEquals(KafkaEvent.Type.USER_CONFIRM, event.type());
        assertEquals(body, event.body());
    }

    @Test
    void successfullyEditEvent() {
        EventBody body = new UserEditEventBody(
                "USERNAME",
                "NAME",
                "LAST NAME",
                "OTHER NAME",
                "MALE",
                LocalDate.now(),
                "ADDRESS"
        );
        var event = eventCreator.event(KafkaEvent.Type.USER_EDIT, body);
        assertEquals(KafkaEvent.Type.USER_EDIT, event.type());
        assertEquals(body, event.body());
    }

    @Test
    void successfullyDeleteEvent() {
        EventBody body = new UserDeleteEventBody(
                "10",
                "username"
        );
        var event = eventCreator.event(KafkaEvent.Type.USER_DELETE, body);
        assertEquals(KafkaEvent.Type.USER_DELETE, event.type());
        assertEquals(body, event.body());
    }

    @Test
    void failedRegistryEvent() {
        EventBody body = new UserDeleteEventBody(
                "10",
                "username"
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->eventCreator.event(KafkaEvent.Type.USER_REGISTRY, body),
                "Creating event from incorrect body type"
        );
    }

    @Test
    void failedConfirmEvent() {
        EventBody body = new UserDeleteEventBody(
                "10",
                "username"
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->eventCreator.event(KafkaEvent.Type.USER_CONFIRM, body),
                "Creating event from incorrect body type"
        );
    }

    @Test
    void failedEditEvent() {
        EventBody body = new UserDeleteEventBody(
                "10",
                "username"
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->eventCreator.event(KafkaEvent.Type.USER_EDIT, body),
                "Creating event from incorrect body type"
        );
    }

    @Test
    void failedDeleteEvent() {
        EventBody body = new UserConfirmEventBody(
                "10",
                "username"
        );
        assertThrows(
                IllegalArgumentException.class,
                ()->eventCreator.event(KafkaEvent.Type.USER_DELETE, body),
                "Creating event from incorrect body type"
        );
    }
}