package org.brutforcer.kafka.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.brutforcer.kafka.deserialize.EventDeserializer;
import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "reactive")
public class ReactorKafkaReceiverConfig {

    @Value("${kafka.host}")
    private String host;
    @Value("${kafka.group.id}")
    private String groupId;
    @Value("${kafka.topic.name}")
    private String topicName;

    @Bean
    public KafkaReceiver<Long, KafkaEvent> kafkaReceiver() {
        return new DefaultKafkaReceiver<Long, KafkaEvent>(
                ConsumerFactory.INSTANCE,
                ReceiverOptions.<Long, KafkaEvent>create(consumerConfigs())
                        .subscription(Collections.singleton(topicName))
        );
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "org.brutforcer.kafka.events.KafkaEvent");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.brutforcer.kafka.deserialize.EventDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return props;
    }
}
