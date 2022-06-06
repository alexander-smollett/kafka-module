package org.brutforcer.kafka.senders;

import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import reactor.core.publisher.Mono;

public interface ReactiveEventSender {

    Mono<Void> sendEvent(KafkaEvent event);
    <T extends EventBody> Mono<Void> sendEvent(KafkaEvent.Type type, T body);
}
