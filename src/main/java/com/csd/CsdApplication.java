package com.csd;

import com.csd.user.User;
import com.csd.user.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CsdApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CsdApplication.class, args);
		// JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
//        System.out.println("[Add user]: " +
//				users.save(new User("admin", "goodpassword", "12345678", "email@gmail.email", "ROLE_ADMIN", "cheko", "pek")).getUsername());
//		System.out.println("[Add user]: " +
//				users.save(new User("tommy", "goodpassword", "12345678", "email@gmail.email", "ROLE_USER", "cheko", "pek")).getUsername());
	}


}
