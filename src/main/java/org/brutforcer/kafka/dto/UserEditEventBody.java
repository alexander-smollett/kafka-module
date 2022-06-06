package org.brutforcer.kafka.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserEditEventBody(
        String username,
        String firstName,
        String lastName,
        String otherName,
        String sex,
        LocalDate birthDate,
        String address
) implements EventBody {
}
