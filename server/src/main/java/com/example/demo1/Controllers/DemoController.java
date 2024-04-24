package com.example.demo1.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.example.demo1.InternalServerErrorException;

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
    private final Random random = new Random();

    @GetMapping("/api")
    public ResponseEntity<String> requestGetMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 2) {
            return ResponseEntity.ok("Get request successful!");
        } else {
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @PostMapping("/api")
    public ResponseEntity<String> requestPostMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 2) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Post request successful!");
        } else {
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @PutMapping("/api")
    public ResponseEntity<String> requestPutMethod() {
        var random = new Random();
        if (random.nextInt(0, 10) > 2) {
            return ResponseEntity.status(HttpStatus.OK).body("Put request successful!");
        } else {
            throw new InternalServerErrorException("Internal server error");
        }
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<String> handle(InternalServerErrorException exception) {
        int randomNumber = random.nextInt(2);
        HttpStatus httpStatus;
        switch (randomNumber) {
            case 0:
                httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
            case 1:
                httpStatus = HttpStatus.BAD_GATEWAY;
            default: 
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(httpStatus).body(httpStatus.getReasonPhrase());
    }
}

