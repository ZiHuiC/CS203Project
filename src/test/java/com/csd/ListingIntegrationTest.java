package com.csd;
import java.net.URI;

import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import com.csd.listing.tag.Tag;
import com.csd.listing.tag.TagRepository;
import com.csd.user.User;
import com.csd.user.UserRepository;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ListingIntegrationTest {
    @LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";
    
    @Autowired
    private UserRepository users;

    @Autowired
    private ListingRepository listings;

    @Autowired
    private TagRepository tags;

    @Autowired
	private BCryptPasswordEncoder encoder;

    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

    @AfterEach
    void tearDown(){
        //remove added entities in database
        
        // if (!listings.findByName("test listing 1").isEmpty())
        //     listings.deleteByName("test listing 1");
        // if (!tags.findTagByValue("test").isEmpty())
        //     tags.deleteByValue("test");
        // if (!users.findByUsername("test@lendahand.com").isEmpty())
        //     users.deleteByUsername("test@lendahand.com");
        // if (!users.findByUsername("post test").isEmpty())
        //     users.deleteByUsername("post test");
        // if (!users.findByUsername("post test3").isEmpty())
        //     users.deleteByUsername("post test3");
    }

    private User addTestLendahand() {
        User user = new User(
            "test@lendahand.com",
            encoder.encode("password"),
            "firstname",
            "lastname",
            "62353535",
            "ROLE_USER");
        if (users.findByUsername("test@lendahand.com").isEmpty())
            return users.save(user);
        return users.findByUsername("test@lendahand.com").get();
    }

    private void addAdmin() {
        if (users.findByUsername("admin@lendahand.com").isEmpty())
            users.save(new User(
                    "admin@lendahand.com",
                    encoder.encode("password"),
                    "firstname",
                    "lastname",
                    "62353535",
                    "ROLE_ADMIN"
            ));
    }

    private void createTestTag() {
		if (tags.findTagByValue("test").isEmpty()){
			Tag tag = new Tag("test");
            tags.save(tag);
		}
    }

    private Listing addListing1() {
        addAdmin();
        createTestTag();
        if (listings.findByName("test listing 1").isEmpty()){
            Listing listing = new Listing(
                "test listing 1",  "des", 
                "commitment",  "location"
            );
            listing.setLister(users.findByUsername("admin@lendahand.com").get());
			listing.setTag(tags.findTagByValue("test").get());
			return listings.save(listing);
        }
        return listings.findByName("test listing 1").get();
        
    }

    private Long findListingIdByName(String name) {
        return listings.findByName(name).get().getId();
    }

    @Test
    public void findListingById_Success() throws Exception{
        Listing listing = addListing1();
        URI uri = new URI(baseUrl + port + "/listingpage/" + 
                findListingIdByName("test listing 1"));

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("id", equalTo(listing.getId().intValue()), 
            "name", equalTo(listing.getName()),
            "des", equalTo(listing.getDes()),
            "noOfParticipants", equalTo(listing.getNoOfParticipants()),
            "commitment", equalTo(listing.getCommitment()),
            "location", equalTo(listing.getLocation()),
            "photo", equalTo(listing.getPhoto())
            );
    }

    @Test
	public void findListingById_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage/1");

        given().get(uri).
        then().
            statusCode(401);
    }

    @Test
	public void findListingById_NotFound_Fail() throws Exception {
        addAdmin();
        URI uri = new URI(baseUrl + port + "/listingpage/999999999");

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(404);
    }

    @Test
	public void deleteListing_Success() throws Exception {
        addListing1();
        URI uri = new URI(baseUrl + port + "/listingpage/removal/" + 
            findListingIdByName("test listing 1"));
        
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .delete(uri).
        then().
            statusCode(200);
    }

    @Test
	public void deleteListing_ListingDontExist_Fail() throws Exception {
        addListing1();
        URI uri = new URI(baseUrl + port + "/listingpage/removal/9999999999");
        
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .delete(uri).
        then().
            statusCode(404);
    }

    @Test
	public void deleteListing_NotAuthorized_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage/removal/" + 
            findListingIdByName("test listing 1"));
        
        given().auth().basic("test@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .delete(uri).
        then().
            statusCode(403);
    }

    @Test
	public void deleteListing_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage/removal/1");
        
        given()
            .accept("*/*").contentType("application/json")
            .delete(uri).
        then().
            statusCode(401);
    }
}
