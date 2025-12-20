package com.github.calhanwynters.refproductmngr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Explicitly scan the root package to pick up businesscore and businessinfra
@SpringBootApplication(scanBasePackages = "com.github.calhanwynters.refproductmngr")
public class RefproductmngrApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefproductmngrApplication.class, args);
	}
}