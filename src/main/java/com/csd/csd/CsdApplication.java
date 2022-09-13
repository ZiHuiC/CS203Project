package com.csd.csd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
public class CsdApplication {
	public static void main(String[] args) {
		SpringApplication.run(CsdApplication.class, args);
	}
	@GetMapping(value = "/some/path/test")
	public String index(){
		return "index";
	}


}
