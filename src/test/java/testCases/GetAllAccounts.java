package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;

import java.util.concurrent.TimeUnit;

public class GetAllAccounts extends ConfigReader {
	
	String baseURI;
	String allAccountEndPoint;
	String authFilePath;
	String headerContentType;
	long responseTime;
	String bearerToken;
	
	public GetAllAccounts() {
		baseURI= getProperty("base_uri");
		allAccountEndPoint=getProperty("get_all_account_end_point");
		headerContentType=getProperty("header_content_type");
	}
	
	   @Test
	    public void getAllAccounts() {
	        
	        /*
	         * given: all input details -> (baseURI, Headers, Authorization, PayLoad/Body, QueryParameters)
	         * when: submit api requests -> HttpMethod(Endpoint/Resource)
	         * then: validate response -> (status code, Headers, responseTime, Payload/Body)
	         */
	        
	        Response response =
	            
	            given()
	                .baseUri(baseURI)
	                .header("Content-Type", headerContentType)
	                .auth().preemptive().basic("demo@codefios.com", "abc123")
	                .log().all()
	            .when()
	                .get(allAccountEndPoint)
	            .then()
	            	.log().all()
	                .extract().response();
	    
	        int statusCode = response.getStatusCode();
	        System.out.println("Status code: " + statusCode);
	        Assert.assertEquals(statusCode, 200, "Status codes are NOT matching!");
	    
	        responseTime = response.timeIn(TimeUnit.MILLISECONDS);
	        System.out.println("Response Time: " + responseTime);
	        Assert.assertTrue(responseTime < 3000, "Response time is greater than expected!");
	    
	        String responseheaderContentType = response.getHeader("Content-Type");
	        System.out.println("Header Content Type: " + responseheaderContentType);
	        Assert.assertEquals(responseheaderContentType, headerContentType, "Content-Type header does not match!");
	    
	        String responseBody = response.getBody().asString();
	        System.out.println("Response Body: " + responseBody);
	        
	        JsonPath jpath= new JsonPath(responseBody);
			String firstAccountId= jpath.getString("records[0].account_id");
			System.out.println("First Account Id:" + firstAccountId);
			
			if (firstAccountId !=null) {
				System.out.println("First Account ID is NOT null.");
			}else {
				System.out.println("First Account ID is NULL!");
			}

	    }
	}
