package org.brutforcer.kafka.events;

import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.dto.UserToKafkaDto;

public record KafkaEvent(
        Type type,
        byte[] body
){

    public enum Type {
        REGISTRY_USER(UserToKafkaDto.class),
        EDIT_USER(UserToKafkaDto.class)
//        CREATE_PERSON(clazz),
//        EDIT_PERSON(clazz),
//        FOUND_FAMILY_RELATIONSHIP(clazz),
//        ACCEPT_FAMILY_RELATIONSHIP(clazz),
//        REJECT_FAMILY_RELATIONSHIP(clazz)
        ;

        private final Class<? extends EventBody> clazz;

        Type(Class<? extends EventBody> clazz) {
            this.clazz = clazz;
        }

        public Class<? extends EventBody> getClazz() {
            return clazz;
        }
    }
}
