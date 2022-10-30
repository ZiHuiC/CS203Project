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
        
        if (!listings.findByName("test listing 1").isEmpty())
            listings.deleteByName("test listing 1");
        if (!listings.findByName("testlisting2").isEmpty())
            listings.deleteByName("testlisting2");
        if (!listings.findByName("test listing 3").isEmpty())
            listings.deleteByName("test listing 3");
        if (!tags.findTagByValue("test").isEmpty())
            tags.deleteByValue("test");
        if (!tags.findTagByValue("test2").isEmpty())
            tags.deleteByValue("test2");
        if (!users.findByUsername("test@lendahand.com").isEmpty())
            users.deleteByUsername("test@lendahand.com");
        if (!users.findByUsername("post test").isEmpty())
            users.deleteByUsername("post test");
        if (!users.findByUsername("post test3").isEmpty())
            users.deleteByUsername("post test3");
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

    private void createTestTag2() {
		if (tags.findTagByValue("test2").isEmpty()){
			Tag tag = new Tag("test2");
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

    private Listing addListing3() {
        addAdmin();
        createTestTag2();
        if (listings.findByName("test listing 3").isEmpty()){
            Listing listing = new Listing(
                "test listing 3",  "des", 
                "ad-hoc",  "location"
            );
            listing.setLister(users.findByUsername("admin@lendahand.com").get());
			listing.setTag(tags.findTagByValue("test2").get());
			return listings.save(listing);
        }
        return listings.findByName("test listing 3").get();
    }

    private Long findListingIdByName(String name) {
        return listings.findByName(name).get().getId();
    }

    private Long findUserIdByUsername(String username) {
        return users.findByUsername(username).get().getId();
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
        addListing1();
        addTestLendahand();
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

    @Test
	public void addListing_Success() throws Exception {
        addAdmin();
        createTestTag();
        URI uri = new URI(baseUrl + port + "/listingpage/newlisting?userId=" +
            findUserIdByUsername("admin@lendahand.com") + "&tagName=test");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "testlisting2");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "ad-hoc");
        requestParams.put("location", "east");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .post(uri).
        then().
            statusCode(201).
			body("id", equalTo(findListingIdByName("testlisting2").intValue()), 
            "name", equalTo("testlisting2"),
            "des", equalTo("pass"),
            "commitment", equalTo("ad-hoc"),
            "location", equalTo("east"));
    }

    @Test
	public void addListing_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage/newlisting?userId=1&tagName=test");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test2");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "ad-hoc");
        requestParams.put("location", "east");
    
        given()
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .post(uri).
        then().
            statusCode(401);
    }

    @Test
	public void addListing_UserNotFound_Fail() throws Exception {
        addAdmin();
        URI uri = new URI(baseUrl + port + "/listingpage/newlisting?userId=99999999999&tagName=test");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test2");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "ad-hoc");
        requestParams.put("location", "east");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .post(uri).
        then().
            statusCode(404);
    }

    @Test
	public void addListing_TagNotFound_Fail() throws Exception {
        addAdmin();
        URI uri = new URI(baseUrl + port + "/listingpage/newlisting?userId=" +
            findUserIdByUsername("admin@lendahand.com") + "&tagName=nothere");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test2");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "ad-hoc");
        requestParams.put("location", "east");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .post(uri).
        then().
            statusCode(404);
    }

    @Test
	public void editListingById_Success() throws Exception {
        addListing1();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage/edit/" 
            + findListingIdByName("test listing 1"));
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test listing 1");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "3days");
        requestParams.put("location", "West");
        requestParams.put("tag", "test2");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .put(uri).
        then().
            statusCode(200).
			body("id", equalTo(findListingIdByName("test listing 1").intValue()), 
            "name", equalTo("test listing 1"),
            "des", equalTo("pass"),
            "commitment", equalTo("3days"),
            "location", equalTo("West"));
    }

    @Test
	public void editListing_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage/edit/1" );
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test listing 1");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "3days");
        requestParams.put("location", "West");
        requestParams.put("tag", "test2");
    
        given()
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .put(uri).
        then().
            statusCode(401);
    }

    @Test
	public void editListing_UserNotFound_Fail() throws Exception {
        addAdmin();
        URI uri = new URI(baseUrl + port + "/listingpage/edit/999999999999");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test listing 1");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "3days");
        requestParams.put("location", "West");
        requestParams.put("tag", "test2");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .put(uri).
        then().
            statusCode(404);
    }

    @Test
	public void editListing_TagNotFound_Fail() throws Exception {
        addListing1();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage/edit/" 
            + findListingIdByName("test listing 1"));
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "test listing 1");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "3days");
        requestParams.put("location", "West");
        requestParams.put("tag", "tagisnothere");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .put(uri).
        then().
            statusCode(404);
    }

    @Test
	public void editListing_NotAuthorized_Fail() throws Exception {
        addAdmin();
        addTestLendahand();
        createTestTag();
        URI uri = new URI(baseUrl + port + "/listingpage/newlisting?userId=" +
            findUserIdByUsername("admin@lendahand.com") + "&tagName=test");
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "testlisting2");
        requestParams.put("des", "pass");
        requestParams.put("commitment", "ad-hoc");
        requestParams.put("location", "east");
    
        given().auth().basic("test@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .post(uri).
        then().
            statusCode(201).
			body("id", equalTo(findListingIdByName("testlisting2").intValue()), 
            "name", equalTo("testlisting2"),
            "des", equalTo("pass"),
            "commitment", equalTo("ad-hoc"),
            "location", equalTo("east"));
    }

    //must run without other listings
    @Test
	public void findListingByTitle_NoParam_ReturnsSize2() throws Exception {
        addListing1();
        addListing3();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(2));
    }

    @Test
	public void findListingByTitle_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/listingpage");
    
        given()
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(401);
    }

    //must run without other listings
    @Test
	public void findListingByTitle_InName_ReturnsSize2() throws Exception {
        addListing1();
        addListing3();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage?inName=test");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(2));
    }

    //must run without other listings
    @Test
	public void findListingByTitle_InNameNotThere_ReturnSize0() throws Exception {
        addListing1();
        addListing3();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage?inName=asdhauygsub");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(0));
    }
    //must run without other listings
    @Test
    public void findListingByTitle_FilterDTO_ReturnSize2() throws Exception {
        addListing1();
        addListing3();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage");
        JSONObject requestParams = new JSONObject();
        requestParams.put("commitment", "commitment");
        requestParams.put("tag", "test");
        requestParams.put("username", "admin@lendahand.com");

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(1));
    }

    @Test
	public void findListingByTitle_InNameFilterDTO_ReturnSize0() throws Exception {
        addListing1();
        addListing3();
        createTestTag2();
        URI uri = new URI(baseUrl + port + "/listingpage?inName=asdhauygsub");
        JSONObject requestParams = new JSONObject();
        requestParams.put("commitment", "commitment");
        requestParams.put("tag", "test");
        requestParams.put("username", "admin@lendahand.com");
    
        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .body(requestParams.toJSONString())
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(0));
    }
}
