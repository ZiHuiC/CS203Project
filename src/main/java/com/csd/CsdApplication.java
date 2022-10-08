package com.csd;

import com.csd.user.User;
import com.csd.user.UserRepository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CsdApplication {
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CsdApplication.class, args);
		// JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        if (users.findByUsername("admin@lendahand.com").isEmpty())
			System.out.println("[Add user]: " +
					users.save(new User(
							"admin@lendahand.com",
							encoder.encode("password"),
							"firstname",
							"lastname",
							"62353535",
							"ROLE_ADMIN"
					)).getUsername());
		else
			System.out.println("Admin already added");

		if (users.findByUsername("user@gmail.com").isEmpty())
			System.out.println("[Add user]: " +
					users.save(new User(
							"user@gmail.com",
							encoder.encode("password"),
							"firstname",
							"lastname",
							"62353535",
							"ROLE_USER"
					)).getUsername());
		else
			System.out.println("User already added");
	}
}
