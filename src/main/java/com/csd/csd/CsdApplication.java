package com.csd.csd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication
@RestController
public class CsdApplication {
	public static void main(String[] args) {
		SpringApplication.run(CsdApplication.class, args);
	}
	@GetMapping(value = "/index")
	public String index(){
		return "index";
	}

	@Autowired
	private ListingRepository listingRepository;

	@PostMapping("/add")
	public String addListing(@RequestParam String name, @RequestParam String des) {
		Listing listing = new Listing();
		listing.setName(name);
		listing.setDes(des);
		listingRepository.save(listing);
		return "Added new listing to repo!";
	}

	@GetMapping("/list")
	public Iterable<Listing> getListings() {
		return listingRepository.findAll();
	}

	@GetMapping("/find/{id}")
	public Listing findListingById(@PathVariable Integer id) {
		return listingRepository.findListingById(id);
	}


}
