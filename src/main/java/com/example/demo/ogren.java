package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ogren {
    private static final Logger log = LoggerFactory.getLogger(ogren.class);
    //tamamen öğrenmek için test paketidir.



    //*PostConstruct Alıştırması DİREK PROGRAM ÇALIŞINCA EKRANA YAZAR
    @PostConstruct
    public void init() {
        System.out.println("JAVA'DA RUN'A TIKLADIĞIN AN ÇALIŞACAKTIR");
    }



    //*CommandLineRunner Alıştırması DİREK PROGRAM ÇALIŞINCA EKRANA YAZAR
    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
            System.out.println("JAVA'DA RUN'A TIKLADIĞIN AN ÇALIŞACAKTIR");
        };
    }


    //*Logger Kullanımı
    @Bean
    @PostConstruct
    public void loggerOrnek() {
        log.info("info");
        log.warn("warning");
        log.error("error");
    }



}
