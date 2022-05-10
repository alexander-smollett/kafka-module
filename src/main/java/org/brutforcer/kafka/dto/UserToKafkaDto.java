package org.brutforcer.kafka.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserToKafkaDto(
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
