package com.main.writeRoom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WriteRoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(WriteRoomApplication.class, args);
	}

}
