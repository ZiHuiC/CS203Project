package com.csd;

import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagRepository;
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
		TagRepository tags = ctx.getBean(TagRepository.class);
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

		createTag(tags, "Coastal");
		createTag(tags, "Marine");
		createTag(tags, "Jungle");
		createTag(tags, "Clean Energy");
		createTag(tags, "Agriculture");
		createTag(tags, "Recycling and Waste");
		createTag(tags, "Others");
	}

	private static void createTag(TagRepository tags, String name) {
		if (tags.findTagByValue(name).isEmpty()){
			Tag tag = new Tag(name);
			System.out.println("[Add tag]: " + tags.save(tag).getValue());
		}	
		else
			System.out.println(name + " already added");
	}
}
