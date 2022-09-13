package com.csd.csd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CsdApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsdApplication.class, args);
	}

}
