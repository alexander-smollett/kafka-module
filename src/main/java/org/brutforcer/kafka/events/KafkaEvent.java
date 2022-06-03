package org.brutforcer.kafka.events;

import org.brutforcer.kafka.dto.*;

public record KafkaEvent(
        Type type,
        EventBody body
){

    public enum Type {
        USER_REGISTRY(UserRegistryEventBody.class),
        USER_EDIT(UserEditEventBody.class),
        USER_CONFIRM(UserConfirmEventBody.class),
        USER_DELETE(UserDeleteEventBody.class)
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
