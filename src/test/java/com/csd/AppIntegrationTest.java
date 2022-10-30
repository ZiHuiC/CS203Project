package com.csd;
import java.net.URI;
import java.util.List;

import com.csd.application.Application;
import com.csd.application.ApplicationRepository;
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
public class AppIntegrationTest {
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
    private ApplicationRepository applications;

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
        List<Application> listOfApp = applications.findApplicationByMessage("testapp1");
        if (listOfApp.size() != 0){
            for (Application app: listOfApp){
                applications.deleteById(app.getId());
            }
        }
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

    private Application addApplication1() {
        addListing1();
        if (applications.findApplicationByMessage("testapp1").isEmpty()){
            Application app = new Application("testapp1");
            app.setApplicant(users.findByUsername("admin@lendahand.com").get());
            app.setListing(listings.findByName("test listing 1").get());
            return applications.save(app);
        }
        return applications.findApplicationByMessage("testapp1").get(0);
    }
    

    private Long findListingIdByName(String name) {
        return listings.findByName(name).get().getId();
    }

    private Long findUserIdByUsername(String username) {
        return users.findByUsername(username).get().getId();
    }

    private Long findFirstAppIdByMessage(String msg) {
        return applications.findApplicationByMessage(msg).get(0).getId();
    }

    @Test
    public void findApplicationById_Success() throws Exception{
        Application app = addApplication1(); 
        URI uri = new URI(baseUrl + port + "/application/" + 
                findFirstAppIdByMessage("testapp1"));

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("id", equalTo(app.getId().intValue()), 
            "message", equalTo(app.getMessage()));
    }

    @Test
    public void findApplicationById_NotAuthenticated_Error401() throws Exception{
        URI uri = new URI(baseUrl + port + "/application/1");

        given()
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(401);
    }

    @Test
    public void findApplicationById_NotFound_Error404() throws Exception{
        addAdmin();
        URI uri = new URI(baseUrl + port + "/application/9999999999999");

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(404);
    }

    @Test
    public void getApplications_NonEmptyList_Success() throws Exception{
        addApplication1(); 
        URI uri = new URI(baseUrl + port + "/user/applications?userId=" + 
                findUserIdByUsername("admin@lendahand.com"));

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(1));
    }

    @Test
    public void getApplications_EmptyList_Success() throws Exception{
        addAdmin();; 
        URI uri = new URI(baseUrl + port + "/user/applications?userId=" + 
                findUserIdByUsername("admin@lendahand.com"));

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200).
			body("size()", equalTo(0));
    }

    @Test
    public void getApplications_NotAuthenticated_Error401() throws Exception{
        URI uri = new URI(baseUrl + port + "/user/applications?userId=1");

        given()
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(401);
    }

    @Test
    public void getApplications_UserNotFound_Error404() throws Exception{
        addAdmin();
        URI uri = new URI(baseUrl + port + "/user/applications?userId=9999999999999999");

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(404);
    }
}
