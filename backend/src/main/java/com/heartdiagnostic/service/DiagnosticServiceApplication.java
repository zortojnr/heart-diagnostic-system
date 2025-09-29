package com.heartdiagnostic.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*")
public class DiagnosticServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiagnosticServiceApplication.class, args);
    }
}
