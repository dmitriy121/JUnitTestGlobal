package com.macaroni.test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.util.HashMap;
import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertEquals;


public class TestGlobalGet {

    private static final String GET_BASE_URI = "http://httpbin.org/get";
    private static final String GET_CONTENTTYPE_EXPECTED = "application/json";
    private static final String GET_URL_EXPECTED = "http://httpbin.org/get";
    private static final String GET_ORIGIN_EXPECTED = "193.254.217.206"; // your IP
    private static final String GET_BODY_EXPECTED_FOR_2_METHOD = "{\n" +
                                                                    "  \"args\": {}, \n" +
                                                                    "  \"headers\": {\n" +
                                                                    "    \"Accept\": \"*/*\", \n" +
                                                                    "    \"Accept-Encoding\": \"gzip,deflate\", \n" +
                                                                    "    \"Content-Length\": \"0\", \n" +
                                                                    "    \"Host\": \"httpbin.org\", \n" +
                                                                    "    \"User-Agent\": \"Apache-HttpClient/4.5.1 (Java/1.8.0_77)\"\n" +
                                                                    "  }, \n" +
                                                                    "  \"origin\": \"" + GET_ORIGIN_EXPECTED + "\", \n" +
                                                                    "  \"url\": \"http://httpbin.org/get\"\n" +
                                                                    "}\n";

    private static final String GET_BODY_EXPECTED_FOR_1_METHOD ="{\n" +
                                                                "  \"args\": {}, \n" +
                                                                "  \"headers\": {\n" +
                                                                "    \"Accept\": \"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\", \n" +
                                                                "    \"Host\": \"httpbin.org\", \n" +
                                                                "    \"User-Agent\": \"Jersey/2.22.2 (HttpUrlConnection 1.8.0_77)\"\n" +
                                                                "  }, \n" +
                                                                "  \"origin\": \"193.254.217.206\", \n" +
                                                                "  \"url\": \"http://httpbin.org/get\"\n" +
                                                                "}\n";

    private static HashMap<String, String> JSON_PATH_EXPECTED;
    private Client client = ClientBuilder.newClient();

    @Before

    public void createJsonExpected() {
    JSON_PATH_EXPECTED = new HashMap<String, String>();

    }

    @Test
    public void testGetData1Method() {

        WebTarget target = client.target(GET_BASE_URI);
                String responseData = target.request().get(String.class);

        assertEquals(GET_BODY_EXPECTED_FOR_1_METHOD,responseData);
    }


    @Test
    public void testGetData2Method() {

        Response res = get(GET_BASE_URI);

        JsonPath jp = new JsonPath(get(GET_BASE_URI).asString());

        assertEquals(GET_BODY_EXPECTED_FOR_2_METHOD,res.getBody().asString());
        assertEquals(200, res.getStatusCode());
        assertEquals(GET_CONTENTTYPE_EXPECTED, res.getContentType());
        assertEquals(GET_ORIGIN_EXPECTED, jp.get("origin"));
        assertEquals(JSON_PATH_EXPECTED, jp.get("args"));
        assertEquals(GET_URL_EXPECTED, jp.get("url"));
    }

    @After
    public void clearJsonExpected() {
        JSON_PATH_EXPECTED.clear();
    }
}
