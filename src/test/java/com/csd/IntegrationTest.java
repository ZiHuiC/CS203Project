package com.csd;

import java.net.URI;

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

// Using REST Assured https://rest-assured.io/ is another way to write integration tests
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTest {

	@LocalServerPort
	private int port;

	private final String baseUrl = "http://localhost:";
    
    @Autowired
    private UserRepository users;

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
        users.deleteByUsername("test@lendahand.com");
    }

    @Test
    public void getUsers_Success() throws Exception{
        URI uri = new URI(baseUrl + port + "/profiles");

        given().auth().basic("admin@lendahand.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(200);
    }

    @Test
	public void getUsers_NotAuthenticated_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/profiles");

        given().get(uri).
        then().
            statusCode(401);
    }

    @Test
	public void getUsers_NotAuthorized_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/profiles");

        given().auth().basic("user@gmail.com", "password")
            .accept("*/*").contentType("application/json")
            .get(uri).
        then().
            statusCode(403);
    }
    
    @Test
	public void getUser_ValidUserId_Success() throws Exception {
		URI uri = new URI(baseUrl + port + "/user?username=test@lendahand.com");
        User user = new User(
        "test@lendahand.com",
        encoder.encode("password"),
        "firstname",
        "lastname",
        "62353535",
        "ROLE_USER");
        if (users.findByUsername("test@lendahand.com").isEmpty())
            users.save(user);
        Long id = (users.findByUsername("test@lendahand.com")).get().getId();

		given().get(uri).
		then().
			statusCode(200).
			body("id", equalTo(id.intValue()), 
            "contactNo", equalTo(user.getContactNo()),
            "firstname", equalTo(user.getFirstname()),
            "lastname", equalTo(user.getLastname()));
	}

    @Test
	public void getUser_InvalidUserId_Fail() throws Exception {
        URI uri = new URI(baseUrl + port + "/user?username=dontExist");

        given().get(uri).
        then().
            statusCode(404);
    }


}
