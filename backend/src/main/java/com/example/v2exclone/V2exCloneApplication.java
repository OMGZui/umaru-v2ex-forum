package com.example.v2exclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.v2exclone.entity")
@EnableJpaRepositories("com.example.v2exclone.repository")
public class V2exCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(V2exCloneApplication.class, args);
    }

}
