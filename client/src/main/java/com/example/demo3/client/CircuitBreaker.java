package com.example.demo3.client;

import java.util.Date;

class CircuitBreaker {

    enum State {
        OPEN,
        HALF_OPEN,
        CLOSED,
    }

    private State state;
    private Date lastFailTime;
    private final long interval;
    private final int failureThreshold, successThreshold;

    private int successCount, failureCount;

    public CircuitBreaker(long interval, int failureThreshold, int succesThreshold) {
        this.interval = interval;
        this.state = State.CLOSED;
        this.failureThreshold = failureThreshold;
        this.successThreshold = succesThreshold;
    }

    public synchronized void requestSuccess() {
        if (state.equals(State.HALF_OPEN)) {
            successCount++;
            if (successCount >= successThreshold) {
                close();
            }
        }
    }

    public synchronized void requestFailure() {
        if (state.equals(State.CLOSED)) {
            failureCount++;

            if (failureCount >= failureThreshold) {
                open();
            }
        } else if (state.equals(State.HALF_OPEN)) {
            open();
        }
    }

    public synchronized boolean checkState() {
        if (state.equals(State.OPEN)) {
            long now = System.currentTimeMillis();
            if (now - lastFailTime.getTime() >= interval) {
                halfOpen();
            } else {
                return false;
            }
        }
        return  true;
    }

    private void close() {
        state = State.CLOSED;
        lastFailTime = null;
        failureCount = 0;
        successCount = 0;
    }

    private void open() {
        state = State.OPEN;
        lastFailTime = new Date(System.currentTimeMillis());
        successCount = 0;
    }

    private void halfOpen() {
        state = State.HALF_OPEN;
    }

    // public synchronized String request(String address) throws URISyntaxException, IOException {
    //     if (state.equals(State.OPEN)) {
    //         if (lastFailTime != null && (System.currentTimeMillis() - lastFailTime.getTime() > interval)) {
    //             state = State.HALF_OPEN;
    //             return tryRequest(address);
    //         }

    //         throw new InternalServerErrorException("Circuit Breaker is open");
    //     }

    //     return tryRequest(address);
    // }

    // private String tryRequest(String address) throws InternalServerErrorException, URISyntaxException, IOException {
    //     URI uri = new URI(address);
    //     URL url = uri.toURL();
    //     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    //     connection.setRequestMethod("GET");

    //     if (connection.getResponseCode() == 500) {
    //         state = State.OPEN;
    //         lastFailTime = new Date(System.currentTimeMillis());
    //         connection.disconnect();
    //         throw new InternalServerErrorException("Response with code 500");
    //     }

    //     BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    //     String line;
    //     StringBuilder response = new StringBuilder();

    //     while ((line = reader.readLine()) != null) {
    //         response.append(line);
    //     }
    //     reader.close();

    //     state = State.CLOSED;
    //     lastFailTime = null;
    //     connection.disconnect();
    //     return response.toString();
    // }
}