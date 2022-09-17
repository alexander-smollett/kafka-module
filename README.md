# kafka-module
Содержит сендера для отправки событий в очередь events Kafka, а также настройки для слушателей этой очереди

## Реализованные [события](/src/main/java/org/brutforcer/kafka/events/KafkaEvent.java)
1) Регистрация пользователя (_USER_REGISTRY_).
   - Событие создается в [сервисе управления пользователями](https://github.com/alexander-smollett/user-service) после успешной регистрации пользователя.
   - Событие потребляется в [сервере аутентификации и авторизации](https://github.com/alexander-smollett/authorization-server) 
2) Подтверждение регистрации пользователя (_USER_CONFIRM_).
    - Событие создается в [сервере аутентификации и авторизации](https://github.com/alexander-smollett/authorization-server) после успешного подтверждения регистрации пользователя.
    - Событие потребляется в [сервисе управления пользователями](https://github.com/alexander-smollett/user-service)
3) Регистрация пользователя (_USER_EDIT_).
    - Событие создается в [сервисе управления пользователями](https://github.com/alexander-smollett/user-service) после изменения пользователя.
    - Событие потребляется в [сервере аутентификации и авторизации](https://github.com/alexander-smollett/authorization-server)
4) Регистрация пользователя (_USER_DELETE_).
    - Событие создается в [сервисе управления пользователями](https://github.com/alexander-smollett/user-service) после удаления пользователя.
    - Событие потребляется в [сервере аутентификации и авторизации](https://github.com/alexander-smollett/authorization-server)

# Конфигурационные свойства
    kafka.enable = true             //включение отключение модуля
    kafka.mode = default            //переключение режима конфигурации: default/reactive
    kafka.host = localhost:9092     //адрес хоста kafka
    kafka.group.id = groupName      //название группы потребителей (consumers)
    kafka.topic.name = topicName    //наименование прослушиваемого топика в кафке


