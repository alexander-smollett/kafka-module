package org.brutforcer.kafka.deserialize;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.dto.UserRegistryEventBody;
import org.brutforcer.kafka.events.KafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "kafka", name = "enable", havingValue = "true", matchIfMissing = true)
public class EventDeserializer implements Deserializer<KafkaEvent> {

    private final ObjectMapper mapper;

    @Autowired
    public EventDeserializer() {
        this.mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();;
    }

    @Override
    public KafkaEvent deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.info("Null received at deserializing");
                return null;
            }
            log.debug("Deserializing kafka event...");
            var json = mapper.readTree(data);

            checkFields(json);

            var type = KafkaEvent.Type.valueOf(json.get("type").asText());
            EventBody body = switch (type) {
                case USER_REGISTRY -> mapper.readValue(json.get("body").toPrettyString(), KafkaEvent.Type.USER_REGISTRY.getClazz());
                case USER_EDIT -> mapper.readValue(json.get("body").toPrettyString(), KafkaEvent.Type.USER_EDIT.getClazz());
                case USER_CONFIRM -> mapper.readValue(json.get("body").toPrettyString(), KafkaEvent.Type.USER_CONFIRM.getClazz());
                case USER_DELETE -> mapper.readValue(json.get("body").toPrettyString(), KafkaEvent.Type.USER_DELETE.getClazz());
            };

            return new KafkaEvent(type, body);
        } catch (Exception e) {
            log.error("Deserialize error KefkaEvent -> ",e);
            throw new SerializationException("Unknown error when deserializing byte[] to KafkaEvent");
        }
    }

    private void checkFields(JsonNode json) {
        if (!json.has("type")){
            log.error("Event not have type field");
            throw new SerializationException("Error when deserializing byte[] to KafkaEvent: event not have type field");
        }
        if (!json.has("body")){
            log.error("Event not have body");
            throw new SerializationException("Error when deserializing byte[] to KafkaEvent: event not have body");
        }
    }
}
