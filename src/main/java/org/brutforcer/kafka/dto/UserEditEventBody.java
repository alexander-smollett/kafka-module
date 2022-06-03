package org.brutforcer.kafka.dto;

import java.time.LocalDate;

public record UserEditEventBody(
        String firstName,
        String lastName,
        String otherName,
        String sex,
        LocalDate birthDate,
        String address
) implements EventBody {
}
