package com.macaroni.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class TestGlobalHTMLFormPost {

    private static final String BASE_HTML = "http://httpbin.org/forms/post";
    private static final String BASE_HTML_POST = "http://httpbin.org/post";
    private static final String FORM_EXPECTED_1_METHOD ="{\n" +
                                                            "  \"args\": {}, \n" +
                                                            "  \"data\": \"\", \n" +
                                                            "  \"files\": {}, \n" +
                                                            "  \"form\": {\n" +
                                                            "    \"comments\": \"Please deliver as soon as possible\", \n" +
                                                            "    \"custemail\": \"xxx_XXXXX@gmail.com\", \n" +
                                                            "    \"custname\": \"dmitriy\", \n" +
                                                            "    \"custtel\": \"067-000-00-00\", \n" +
                                                            "    \"delivery\": \"17:00\"\n" +
                                                            "  }, \n" +
                                                            "  \"headers\": {\n" +
                                                            "    \"Accept\": \"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\", \n" +
                                                            "    \"Content-Length\": \"131\", \n" +
                                                            "    \"Content-Type\": \"application/x-www-form-urlencoded\", \n" +
                                                            "    \"Host\": \"httpbin.org\", \n" +
                                                            "    \"User-Agent\": \"Jersey/2.22.2 (HttpUrlConnection 1.8.0_77)\"\n" +
                                                            "  }, \n" +
                                                            "  \"json\": null, \n" +
                                                            "  \"origin\": \"193.254.217.206\", \n" +
                                                            "  \"url\": \"http://httpbin.org/post\"\n" +
                                                            "}\n";

    private static final String FORM_EXPECTED_2_METHOD = "{delivery=17:00, " +
                                                            "custtel=067-000-00-00, " +
                                                            "comments=Please deliver as soon as possible, " +
                                                            "custemail=xxx_XXXXX@gmail.com, " +
                                                            "custname=dmitriy}";



    private WebDriver driver = new FirefoxDriver();
    private Client client = ClientBuilder.newClient();


    private void formHtmlPost(){
        WebElement element = driver.findElement(By.xpath("//input[@name='custname']"));

        element.sendKeys("dmitriy");
        driver.findElement(By.xpath("//input[@name='custtel']")).sendKeys("067-000-00-00");
        driver.findElement(By.xpath("//input[@name='custemail']")).sendKeys("xxx_XXXXX@gmail.com");
        driver.findElement(By.xpath("/html/body/form/fieldset[1]/p[1]/label/input")).click();
        driver.findElement(By.xpath("/html/body/form/fieldset[2]/p[1]/label/input")).click();
        driver.findElement(By.xpath("/html/body/form/fieldset[2]/p[2]/label/input")).click();
        driver.findElement(By.xpath("/html/body/form/fieldset[2]/p[3]/label/input")).click();
        driver.findElement(By.xpath("/html/body/form/fieldset[2]/p[4]/label/input")).click();
        driver.findElement(By.xpath("/html/body/form/p[4]/label/input")).sendKeys("17:00");
        driver.findElement(By.xpath("/html/body/form/p[5]/label/textarea")).sendKeys("Please deliver as soon as possible");
        driver.findElement((By.xpath("//button[text()='Submit order']"))).click();
    }

    @Before
    public void getDriver(){
        driver.get(BASE_HTML);
    }

    @Test
    public void HTMLFormPostExpected1Method() {

        WebTarget target = client.target("http://httpbin.org/post");

        MultivaluedMap<String, String> postForm = new
                MultivaluedHashMap<String, String>();

        postForm.add("comments","Please deliver as soon as possible");
        postForm.add("custemail","xxx_XXXXX@gmail.com");
        postForm.add("custname","dmitriy");
        postForm.add("custtel","067-000-00-00");
        postForm.add("delivery","17:00");

        String responseData = target.request().post(Entity.form(postForm),String.class);

        assertEquals(FORM_EXPECTED_1_METHOD,responseData);
    }

    @Test
    public void HTMLFormPostExpected2Method() {


        assertEquals(FORM_EXPECTED_2_METHOD,

                    given().formParam("comments","Please deliver as soon as possible").
                             formParam("custemail","xxx_XXXXX@gmail.com").
                             formParam("custname","dmitriy").
                             formParam("custtel","067-000-00-00").
                             formParam("delivery","17:00").
                    post(BASE_HTML_POST).
                        thenReturn().getBody().jsonPath().get("form").toString());
    }

    @Test
    public void HTMLFormPost() {

        formHtmlPost();
    }

    @After
    public void closeDriver(){
        driver.close();
    }
}
