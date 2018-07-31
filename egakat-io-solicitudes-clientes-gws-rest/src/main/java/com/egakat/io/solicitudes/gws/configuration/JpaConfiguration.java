package com.egakat.io.solicitudes.gws.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.val;

@Configuration
@EnableTransactionManagement
public class JpaConfiguration extends HikariConfig {

	static final String DATASOURCE_PROPERTIES_PREFIX = "spring.datasource";

	@Bean
	@ConfigurationProperties(prefix = DATASOURCE_PROPERTIES_PREFIX)
	public DataSource dataSource() {
		val result = DataSourceBuilder.create().type(HikariDataSource.class).build();
		return result;
	}
}