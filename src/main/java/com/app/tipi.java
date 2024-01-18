package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class tipi {
	private static final Logger loggger = LoggerFactory.getLogger(tipi.class);
	public static void main(String[] args) {
		SpringApplication.run(tipi.class, args);
		loggger.info("Application started");
	}


	
}
