package com.example.demo1.Controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class DemoController {

    @GetMapping("/api")
    public ResponseEntity<String> requestGetMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 3) {
            return ResponseEntity.ok("Hello, World!");
        } else {
            throw new RuntimeException("Internal server error");
        }
    }

    @PostMapping("/api")
    public ResponseEntity<String> requestPostMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 3) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post request successful!");
        } else {
            throw new RuntimeException("Internal server error");
        }
    }

    @PutMapping("/api")
    public ResponseEntity<String> requestPutMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 3) {
            return ResponseEntity.status(HttpStatus.OK).body("Put request successful!");
        } else {
            throw new RuntimeException("Internal server error");
        }
    }

    @DeleteMapping("/api")
    public ResponseEntity<String> requestDeleteMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 3) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete request successful!");
        } else {
            throw new RuntimeException("Internal server error");
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getLocalizedMessage());
    }
}

