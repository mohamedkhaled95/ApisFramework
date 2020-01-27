package ISOFT.APITesting.TCs;

import java.util.HashMap;
import java.util.Map;

//import atu.alm.wrapper.exceptions.ALMServiceException;
import ISOFT.APITesting.Utilities.*;
import org.testng.annotations.*;
import org.testng.Reporter;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import ISOFT.APITesting.RestAssuredModules.HTTPOperations;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;


public class CorrelationTCs_ValuesValidation  {
	
	@SuppressWarnings("unused")
	private static ITestResult context = null;
	private Response [] previousJsonResponse = new Response [variablesProvider.maxRequests];
	private int Step = -1;
	@BeforeMethod

	public void SetUp(ITestResult ctx) {
		context = ctx;
	}

	@DataProvider()

	public static Object[][] ProvideData(ITestContext context) throws Exception{
		String FileType = context.getCurrentXmlTest().getAllParameters().get("FileFormat");

		if(FileType != null && FileType.equalsIgnoreCase("XML")){
			String XmlFile = context.getCurrentXmlTest().getAllParameters().get("XmlFile");
			Object[][] testObjArray = XmlUtilities.xmlreader(XmlFile);

			return testObjArray;

		}


		else{
			String ExcelFile = context.getCurrentXmlTest().getAllParameters().get("ExcelFile");
			String ExcelSheet = context.getCurrentXmlTest().getAllParameters().get("ExcelSheet");
			Object[][] testObjArray = ExcelUtilities.get_data(ExcelFile, ExcelSheet, variablesProvider.dataProviderWithValuesCols);
			return (testObjArray);
		}
	}


	
	@org.testng.annotations.Test(dataProvider="ProvideData")

	public void test_steps(String URL, String reguestMethod, String headerKeys,
			String headerValues, String postBody, String StatusCode,
			String JsonKeys, String values, String DataTypes){

		if(reguestMethod.equals("SOAP")){
			RestAssured.defaultParser = Parser.XML;
		}else {
			RestAssured.defaultParser = Parser.JSON;
		}

		Step++;
		//for header data
		String [] headerKeysList = headerKeys.split("\\r?\\n");
		String [] headerValuesList = headerValues.split("\\r?\\n");
		Assert.assertEquals(headerKeysList.length, headerValuesList.length);
		Map <String, String> headersMap = new HashMap<String, String>();

		for(int i=0 ; i<headerKeysList.length ; i++){
			if(headerKeysList[i].equals("") &&  headerValuesList[i].equals("")) break;
			else if (headerKeysList[i].equals("vf-target-environment")) {

				headerValuesList[i]="aws-"+ TestParameters.env+"-es";

			}
			headerKeysList[i] = ParsingTestData.getAllString(headerKeysList[i], previousJsonResponse);
			headerValuesList[i] = ParsingTestData.getAllString(headerValuesList[i], previousJsonResponse);
			headersMap.put(headerKeysList[i], headerValuesList[i]);

		}
		URL = ParsingTestData.getAllString(URL, previousJsonResponse);
		reguestMethod = ParsingTestData.getAllString(reguestMethod, previousJsonResponse);
		postBody = ParsingTestData.getAllString(postBody, previousJsonResponse);

		Response res = HTTPOperations.HTTPRequest(URL, reguestMethod, headersMap, postBody);
		if (res == null){
			Assert.assertEquals(variablesProvider.availableRequestTypes, reguestMethod, "[Step " + Integer.toString(Step+1) + "]: Invalid Request type check test data please");
		}
		previousJsonResponse[Step] = res;

		//Prepare expected cols
		int StCode = Integer.parseInt(ParsingTestData.getAllString(StatusCode, previousJsonResponse));
		String [] JsonKeysList = JsonKeys.split("\\r?\\n");
		String [] JsonValuesList = values.split("\\r?\\n");
		String [] DataTypesList = DataTypes.split("\\r?\\n");
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")){
		  		break;
			}else{
				JsonKeysList[i] = ParsingTestData.getAllString(JsonKeysList[i], previousJsonResponse);
				JsonValuesList[i] = ParsingTestData.getAllString(JsonValuesList[i], previousJsonResponse);
				DataTypesList[i] = ParsingTestData.getAllString(DataTypesList[i], previousJsonResponse);
			}
		}
		String Params = DocumentationUtilities.getTestDataWithValues(URL, reguestMethod, headerKeysList,
				headerValuesList, postBody, StCode, JsonKeysList, JsonValuesList, DataTypesList);
		Params = "[Step " + Integer.toString(Step+1) + "]\n"+ Params +
				"The returned  response:\n\n"+
				"Headers:\n" + res.headers().toString()+ "\n\n" +
				"Cookies:\n" + res.cookies().toString()+ "\n\n" +
				"Body:\n" + res.getBody().prettyPrint() + "\n";

		Reporter.log("Status Code :"+res.getStatusCode()+"</br> Response Body:</br>"+res.getBody().prettyPrint()+"</br>Response Headers:</br>" + res.headers().toString());
		Assert.assertEquals((int)res.getStatusCode(),
				StCode, Params);
		/* Validation section:
		 * this section to check:
		 * 1) Key Data type
		 * 2) Key is exists
		 * 3) Key with specific value
		*/
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")) break;

			if(JsonValuesList[i].equals(Keywords.NO_VALIDATION)){
				if(DataTypesList[i].equals(Keywords.NO_VALIDATION)){
					//check it is null only
					if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
						String[] ps = (JsonKeysList[i].split(":"));
						Assert.assertNull(res.getHeader(ps[1]),
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");
					}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
						String[] ps = (JsonKeysList[i].split(":"));
						Assert.assertNull(res.getCookie(ps[1]),
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");
					}else{
						Assert.assertNull(res.path(JsonKeysList[i]),
								Params + (JsonKeysList[i]) + ": is NOT NULL" + "\n");
					}
				}else{
					if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
						String[] ps = (JsonKeysList[i].split(":"));
						//check it is not null
						Assert.assertNotNull(res.getHeader(ps[1]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
						String[] ps = (JsonKeysList[i].split(":"));
						//check it is not null
						Assert.assertNotNull(res.getCookie(ps[1]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					}else{
						//check it is not null
						Assert.assertNotNull(res.path(JsonKeysList[i]),
								Params + (JsonKeysList[i]) + ": is NULL" + "\n");
						//check data type
						String s = res.path(JsonKeysList[i]).getClass().getName().toLowerCase();
						String actualDataType = s.split("\\.")[2];
						Assert.assertEquals(actualDataType, DataTypesList[i].toLowerCase(),
								Params + "It is: " + JsonKeysList[i] + "\n");
					}
				}
			}else{
				if(JsonKeysList[i].startsWith(Keywords.VALIDATE_HEADER)){
					String[] ps = (JsonKeysList[i].split(":"));
					//check it is not null
					Assert.assertNotNull(res.getHeader(ps[1]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//compare values
					Assert.assertEquals(res.getHeader(ps[1]).toString(),
							JsonValuesList[i],
							Params + "It's: " + JsonKeysList[i] + "\n");
				}else if(JsonKeysList[i].startsWith(Keywords.VALIDATE_COOKIE)){
					String[] ps = (JsonKeysList[i].split(":"));
					//check it is not null
					Assert.assertNotNull(res.getCookie(ps[1]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//compare values
					Assert.assertEquals(res.getCookie(ps[1]).toString(),
							JsonValuesList[i],
							Params + "It's: " + JsonKeysList[i] + "\n");
				}else{
					//check it is not null
					Assert.assertNotNull(res.path(JsonKeysList[i]),
							Params + (JsonKeysList[i]) + ": is NULL" + "\n");
					//check data type
					String s = (res.path(JsonKeysList[i])).getClass().getName().toLowerCase();
					String actualDataType = s.split("\\.")[2];
					Assert.assertEquals(actualDataType, DataTypesList[i].toLowerCase(),
							Params + "It is: " + JsonKeysList[i] + "\n");
					//compare values

						Assert.assertEquals(res.path(JsonKeysList[i]).toString(),
								JsonValuesList[i],
								Params + "It's: " + JsonKeysList[i] + "\n");

				}
				
			
			}

		}

	}


}
