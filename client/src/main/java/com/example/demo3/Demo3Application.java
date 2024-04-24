package com.example.demo3;

import com.example.demo3.client.HttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;

//@SpringBootApplication
public class Demo3Application {

	public static void main(String[] args) throws InterruptedException {
//		SpringApplication.run(Demo3Application.class, args);
		var random = new Random();
		var url = "http://server:8080/api";
		var httpClient = new HttpClient(3000, 3, 3);
		for (int i = 0; i < 200; i++) {
			int option = random.nextInt(2);
			try {
				switch (option) {
					case 0:
						System.out.println(httpClient.post(url));
					case 1:
						System.out.println(httpClient.put(url));
					default:
						System.out.println(httpClient.get(url));
				}
			}
			catch (RuntimeException e) {
				System.out.println(e.getLocalizedMessage());;
			}
			Thread.sleep(1000);
		}
	}

}
