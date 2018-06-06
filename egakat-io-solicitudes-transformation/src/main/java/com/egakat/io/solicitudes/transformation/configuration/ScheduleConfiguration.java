package com.egakat.io.solicitudes.transformation.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfiguration {

	private static final int THREADS_COUNT = 10;

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
	    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
	    threadPoolTaskScheduler.setPoolSize(THREADS_COUNT);
	    return threadPoolTaskScheduler;
	}
}
