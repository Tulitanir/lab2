package com.example.demo3.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class HttpClient {
    private final CircuitBreaker circuitBreaker;
    private final RestTemplate restTemplate;

    public HttpClient(long cbInterval, int cbFailThreshold, int cbSuccessThreshold) {
        circuitBreaker = new CircuitBreaker(cbInterval, cbFailThreshold, cbSuccessThreshold);
        restTemplate = new RestTemplate();
    }

    public String get(String url) throws RuntimeException {
        return request(url, HttpMethod.GET);
    }

    public String put(String url) throws RuntimeException {
        return request(url, HttpMethod.PUT);
    }

    public String post(String url) throws RuntimeException {
        return request(url, HttpMethod.POST);
    }


    private String request(String url, HttpMethod httpMethod) throws RuntimeException {
        if (!circuitBreaker.checkState()) {
            throw new RuntimeException("Circuit breaker is open");
        }
        RequestEntity<Void> request = new RequestEntity<>(httpMethod, URI.create(url));
        try {
            var response = restTemplate.exchange(request, String.class);
            circuitBreaker.requestSuccess();
            return response.getBody();
        } catch (HttpServerErrorException e) {
            circuitBreaker.requestFailure();
            return e.getLocalizedMessage();
        }
    }
}
