package com.macaroni.test;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import static com.jayway.restassured.RestAssured.given;
import javax.ws.rs.core.Cookie;

public class TestGlobalCookies {


    private static final String BASE_URI_COOKIES = "http://httpbin.org/cookies";
    private static final String SET_BASE_URI_COOKIES = "http://httpbin.org/cookies/set?name=value";
    private static final String DELETE_BASE_URI_COOKIES = "http://httpbin.org/cookies/delete?name";
    private static final String COOKIES_EXPECTED_1_METHOD = "{\n" +
                                                            "  \"cookies\": {\n" +
                                                            "    \"k1\": \"v1\"\n" +
                                                            "  }\n" +
                                                            "}\n";
    private static final String COOKIES_EXPECTED_2_METHOD="{\n" +
                                                            "  \"cookies\": {\n" +
                                                            "    \"$Version\": \"1\", \n" +
                                                            "    \"session\": \"150\"\n" +
                                                            "  }\n" +
                                                            "}\n";
    private static final String SET_COOKIES_EXPECTED = "{\n" +
                                                        "  \"cookies\": {\n" +
                                                        "    \"key\": \"value\"\n" +
                                                        "  }\n" +
                                                        "}\n";

    private static HashMap<String, String> JSON_PATH_EXPECTED = new HashMap<String, String>();
    private Client client = ClientBuilder.newClient();

    @Before
    public void createJsonPathExpectedCookies() {

    JSON_PATH_EXPECTED.put("key1","value1");
    }

    @Test
    public void testCookies1Method(){

        given().
                expect().
                statusCode(200).
                get(BASE_URI_COOKIES);

        Assert.assertEquals(COOKIES_EXPECTED_1_METHOD,
                given().cookies("k1","v1").
                get(BASE_URI_COOKIES).asString());
    }

    @Test
    public void testCookies2Method(){

        WebTarget target = client.target(BASE_URI_COOKIES);

        Cookie cookie = new Cookie("session", "150");

        String responseData = target.request().cookie(cookie).get(String.class);
        Assert.assertEquals(COOKIES_EXPECTED_2_METHOD,responseData);
    }

    @Test
    public void testSetCookies(){

        given().
            cookie("key", "value").
            expect().
            statusCode(200).
        when().
            get(SET_BASE_URI_COOKIES);

        Assert.assertEquals(JSON_PATH_EXPECTED,
                given().cookie("key1", "value1").
                        get(BASE_URI_COOKIES).jsonPath().get("cookies"));

        Assert.assertEquals(SET_COOKIES_EXPECTED,
                given().cookie("key", "value").
                get(BASE_URI_COOKIES).asString());
    }

    @Test
    public void testDeleteCookies(){

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://httpbin.org/cookies/delete?session");

        Cookie cookie = new Cookie("session", "150");

        target.request().cookie(cookie).delete(Cookie.class);
    }

    @After
    public void clearJsonExpectedCookies() {
        JSON_PATH_EXPECTED.clear();
    }
}
