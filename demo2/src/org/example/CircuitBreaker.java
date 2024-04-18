package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;

public class CircuitBreaker {
    private State state;
    private Date lastFailTime;
    private final long interval; 

    public CircuitBreaker(long interval) {
        this.interval = interval;
        this.state = State.CLOSED;
    }

    public String request(String address) throws URISyntaxException, IOException {
        if (state.equals(State.OPEN)) {
            if (lastFailTime != null && (System.currentTimeMillis() - lastFailTime.getTime() > interval)) {
                state = State.HALF_OPEN;
                return tryRequest(address);
            }

            throw new InternalServerErrorException("Circuit Breaker is open");
        }

        return tryRequest(address);
    }

    private String tryRequest(String address) throws InternalServerErrorException, URISyntaxException, IOException {
        URI uri = new URI(address);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 500) {
            state = State.OPEN;
            lastFailTime = new Date(System.currentTimeMillis());
            connection.disconnect();
            throw new InternalServerErrorException("Response with code 500");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        state = State.CLOSED;
        lastFailTime = null;
        connection.disconnect();
        return response.toString();
    }
}

enum State {
    OPEN,
    HALF_OPEN,
    CLOSED,
}