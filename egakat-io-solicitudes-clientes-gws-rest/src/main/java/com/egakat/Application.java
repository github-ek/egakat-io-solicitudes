package com.egakat;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class Application {

	public static void main(String[] args) {
		// @formatter:off
		new SpringApplicationBuilder(Application.class)
		.build()
		.run(args);
		// @formatter:on
	}
}
