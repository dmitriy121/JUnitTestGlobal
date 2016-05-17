package com.macaroni.test;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.given;

public class TestGlobalAuthentication {

    private static final String BASE_URI_AUTHENTICATION = "http://httpbin.org/basic-auth/user/passwd";

    @Test
    public void basicAuthentication() {
        given().auth().basic("user", "passwd").
                expect().
                statusCode(200).
                when().
                get(BASE_URI_AUTHENTICATION);
    }

    @Test
    public void wrongAuthentication() throws Exception {
        given().auth().basic("123", "abCD1").
                expect().
                statusCode(401).
                when().
                get(BASE_URI_AUTHENTICATION);
    }

    @Test
    public void basicPreemptiveAuthentication() throws Exception {
        given().auth().preemptive().basic("user", "passwd").
                expect().
                statusCode(200).
                when().get(BASE_URI_AUTHENTICATION);
    }

}
