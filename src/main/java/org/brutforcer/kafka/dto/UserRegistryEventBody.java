package org.brutforcer.kafka.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import org.brutforcer.kafka.deserialize.EventDeserializer;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserRegistryEventBody(
        String id,
        String username,
        String password,
        String firstName,
        String lastName,
        String otherName,
        LocalDate birthDate,
        String sex,
        String address,
        List<String>roleNames
) implements EventBody {
}
