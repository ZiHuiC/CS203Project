package com.csd;

import java.util.ArrayList;
import java.util.Arrays;

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
		ListingRepository listings = ctx.getBean(ListingRepository.class);
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

		if (listings.findByName("Clean ECP").isEmpty()){
			Listing listing = new Listing("Clean ECP", "Clean up ECP", "ad-hoc", "West");
			listing.setLister(users.findByUsername("user@gmail.com").get());
			listing.setTag(tags.findTagByValue("Coastal").get());
			System.out.println("[Add listings]: " + listings.save(listing).getName());
		}
		else
			System.out.println("Listing already added");
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
