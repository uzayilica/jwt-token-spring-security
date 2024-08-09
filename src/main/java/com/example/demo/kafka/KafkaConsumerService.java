package com.example.demo.kafka;

import com.example.demo.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {


    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);
    @Value("${topic}")
    String yemektopic;



    @KafkaListener(topics ="yemektopic" ,groupId = "yemek-group-1")
    public void consumeStringMessage(String message){
        log.info("Kafka İle Mesaj Alındı " + message);

    }

    @KafkaListener(topics ="yemektopic" ,groupId = "yemek-group-1")
    public void consumeJsonMessage(Users user){
        log.info("Kafka İle Mesaj Alındı " + user);

    }


}
