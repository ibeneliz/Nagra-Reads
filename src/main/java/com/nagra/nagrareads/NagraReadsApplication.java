package com.nagra.nagrareads;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NagraReadsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NagraReadsApplication.class, args);
    }
}
