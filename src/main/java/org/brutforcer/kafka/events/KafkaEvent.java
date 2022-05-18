package org.brutforcer.kafka.events;

import org.brutforcer.kafka.dto.EventBody;
import org.brutforcer.kafka.dto.UserRegistryEventBody;

public record KafkaEvent(
        Type type,
        EventBody body
){

    public enum Type {
        REGISTRY_USER(UserRegistryEventBody.class),
//        EDIT_USER(clazz)
//        CONFIRM_USER(clazz)
//        DELETE_USER(clazz)
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
