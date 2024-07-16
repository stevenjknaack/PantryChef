package com.pantrychef.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The starting point of the application
 */
@SpringBootApplication()
public class BackendApplication {

	/**
	 * Runs the application
	 * @param args Any additional arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
