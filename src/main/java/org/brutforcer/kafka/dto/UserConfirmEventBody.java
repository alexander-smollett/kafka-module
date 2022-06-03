package org.brutforcer.kafka.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;

@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UserConfirmEventBody(
        String id,
        String username
) implements EventBody {}
