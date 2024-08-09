package com.example.demo.kafka;

import com.example.demo.entity.Users;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {


    @Value("${topic}")
    String yemektopic;




    @Bean
    public NewTopic topic(){
        return new NewTopic(yemektopic, 3, (short) 1);
        //3 partititon ve tek sunucu oldupu için 1 replicaset
    }




    @Autowired
    public KafkaTemplate<String,Object> jsonKafkaTemplate;
    @Autowired
    public  KafkaTemplate<String, String> stringKafkaTemplate;

    @PostMapping("/mesaj/{message}")
    private void sendMessage(@PathVariable  String message) {
       stringKafkaTemplate.send(yemektopic, message);

    }

    @PostMapping("/nesne-yolla")
    private void sendMessage(@RequestBody Users users) {
        Users newuser =new Users();
        newuser.setUsername(users.getUsername());
        newuser.setPassword(users.getPassword());
        newuser.setEmail(users.getEmail());
        newuser.setRole(users.getRole());
        jsonKafkaTemplate.send(yemektopic, newuser);

    }



}
