package ISOFT.APITesting.RestAssuredModules;

import static com.jayway.restassured.RestAssured.given;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.EncoderConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;

public class HTTPOperations {
	
	
	public static Response HTTPRequest(String URL, String Type, Map<String, String> headersMap, String body){
		Response res = null;
		if(Type.equals("SOAP")){
			res = given().config(RestAssured.config()
					.encoderConfig(EncoderConfig.encoderConfig()
							.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
				    .relaxedHTTPSValidation().headers(headersMap)
				    .body(body)
				    .when()
				    .post(URL);	
		}else if(Type.equals("GET")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.when()
						.get(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.when()
						.get(URL);
			}			
		}else if(Type.equals("DELETE")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.delete(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.delete(URL);
			}			
		}else if(Type.equals("POST")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.post(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.post(URL);
			}
		}else if(Type.equals("PATCH")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.patch(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
								.relaxedHTTPSValidation()
						.body(body)
						.when()
						.patch(URL);
			}
		}else if(Type.equals("PUT")){
			if(headersMap.size()>0){
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
						.relaxedHTTPSValidation().headers(headersMap)
						.body(body)
						.when()
						.put(URL);
			}else{
				res = given().config(RestAssured.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.appendDefaultContentCharsetToContentTypeIfUndefined(false)))
						.relaxedHTTPSValidation()
						.body(body)
						.when()
						.put(URL);
			}
		}

	//System.out.println(	res.getBody().asString());
//		try {
//			temp = new String (temp.getBytes() , "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println(temp);
		return res;
	}
}
