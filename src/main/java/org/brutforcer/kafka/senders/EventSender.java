package org.brutforcer.kafka.senders;

import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

public interface EventSender {

    <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body);
    <T extends EventBody> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body, UUID correlationId);
    ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event);
    ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent event, UUID correlationId);
}
