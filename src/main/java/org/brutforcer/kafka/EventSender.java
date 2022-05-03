package org.brutforcer.kafka;

import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public interface EventSender {

    <T> ListenableFuture<SendResult<Long, KafkaEvent>> sendEvent(KafkaEvent.Type type, T body);
}
