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
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "kafka", name = "mode", havingValue = "default", matchIfMissing = true)
public class KafkaConsumerConfig {

    @Value("${kafka.host}")
    private String host;
    @Value("${kafka.group.id}")
    private String groupId;

    private final Deserializer eventDeserializer;

    @Autowired
    public KafkaConsumerConfig(EventDeserializer eventDeserializer) {
        this.eventDeserializer = eventDeserializer;
    }

    @Bean
    public ConsumerFactory<Long, KafkaEvent> consumerFactory() {
        var factory = new DefaultKafkaConsumerFactory<Long, KafkaEvent>(consumerConfigs());
        factory.setValueDeserializer(eventDeserializer);
        return factory;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "org.brutforcer.kafka.events.KafkaEvent");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return props;
    }

    @Bean
    public KafkaListenerContainerFactory<?> singleFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, KafkaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<?> kafkaListenerContainerFactory() {
        return new ConcurrentKafkaListenerContainerFactory<>();
    }
}
