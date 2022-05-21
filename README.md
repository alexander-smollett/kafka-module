# kafka-module
Содержит сендера для отправки событий в очередь events Kafka, а также настройки для слушателей этой очереди

# Конфигурационные свойства
    kafka.enable = true             //включение отключение модуля
    kafka.mode = default            //переключение режима конфигурации: default/reactive
    kafka.host = localhost:9092     //адрес хоста kafka
    kafka.group.id = groupName      //название группы потребителей (consumers)
    kafka.topic.name = topicName    //наименование прослушиваемого топика в кафке


