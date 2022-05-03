package org.brutforcer.kafka.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserToKafkaDto(
        String username,
        String password,
        String firstName,
        String lastName,
        String otherName,
        LocalDate birthDate,
        String sex,
        String address
) implements EventBody {
}
