package com.example.demo1.Controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
class DemoController {
    @GetMapping("/api")
    public ResponseEntity<String> requestMethodName() {
        var random = new Random();
        if (random.nextInt(0, 10) > 3) {
            return ResponseEntity.ok("Hello, World!");
        }
        
        throw new RuntimeException("Something went wrong");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.internalServerError().body(exception.getLocalizedMessage());
    }
}