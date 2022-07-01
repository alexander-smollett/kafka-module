package org.brutforcer.kafka.senders;

import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveEventSender {

    <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body);
    <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body, UUID correlationId);
    Mono<Void> sendEvent(KafkaEvent event);
    Mono<Void> sendEvent(KafkaEvent event, UUID correlationId);
}
