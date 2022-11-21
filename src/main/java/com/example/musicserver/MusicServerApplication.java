package com.example.musicserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//only need BCrypt class, no security framework
@SpringBootApplication(exclude =
        {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class} )
public class MusicServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MusicServerApplication.class, args);
    }
}
