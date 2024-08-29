package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import util.ConfigReader;
import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class Authentication extends ConfigReader {
	String baseURI;
	String authEndPoint;
	String authFilePath;
	String headerContentType;
	long responseTime;
	String bearerToken;

	public Authentication() {
		baseURI = getProperty("base_uri");
		authEndPoint = getProperty("auth_end_point");
		authFilePath = "src\\main//java//data//authPayload.json";
		headerContentType = getProperty("header_content_type");
	}

	public boolean validateResponseTime() {
		boolean withinRange = false;
		if (responseTime <= 3000) {
			withinRange = true;
			System.out.println("Response time is within range.");
		} else
			System.out.println("Response time is out of range.");
		return withinRange;
	}

//	@Test
	public String generateBearerToken() {
		/*
		 * given: all input details -> (baseURI, Headers, Authorization, PayLoad/Body,
		 * QueryParameters) when: submit api requests -> HttpMethod(Endpoint/Resource)
		 * then: validate response -> (status code, Headers, responseTime, Payload/Body)
		 */
		Response response =

				given().baseUri(baseURI).header("Content-Type", headerContentType).body(new File(authFilePath))
						.relaxedHTTPSValidation().log().all().when().post(authEndPoint).then().log().all()
						.statusCode(201).extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("Status code: " + statusCode);
		Assert.assertEquals(statusCode, 201, "Status codes are NOT matching!");

		responseTime = response.timeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time:" + responseTime);
		Assert.assertEquals(validateResponseTime(), true);

		String responseheaderContentType = response.getHeader("Content-Type");
		System.out.println("Header Content Type:" + responseheaderContentType);
		Assert.assertEquals(responseheaderContentType, headerContentType);

		String responseBody =response.getBody().asString();
		System.out.println("Response Body:" + responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		bearerToken =jp.get("access_token");
		System.out.println("Bearer_Token:" + bearerToken);
		
		return bearerToken;
		

	}

}