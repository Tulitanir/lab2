package org.example;

public class App {
    public static void main(String[] args) throws Exception {
        String intervalEnv = System.getenv("INTERVAL");
        String sleepEnv = System.getenv("SLEEP");
        String repeatsEnv = System.getenv("REPEATS");

        long interval;
        if (intervalEnv != null) {
            interval = Long.parseLong(intervalEnv);
        } else {
            interval = 1000;
        }

        long sleep;
        if (sleepEnv != null) {
            sleep = Long.parseLong(sleepEnv);
        } else {
            sleep = 300;
        }

        long repeats;
        if (repeatsEnv != null) {
            repeats = Long.parseLong(repeatsEnv);
        } else {
            repeats = 100;
        }

        var circuitBreaker = new CircuitBreaker(interval);
        for (int i = 0; i < repeats; i++) {
            try {
                System.out.println("Response with code 200: " + circuitBreaker.request("http://server:8080/api"));
            }
            catch (InternalServerErrorException e) {
                System.out.println(e.getLocalizedMessage());
            }
            Thread.sleep(sleep);
        }
    }
}
