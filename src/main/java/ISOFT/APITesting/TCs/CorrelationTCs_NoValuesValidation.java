package ISOFT.APITesting.TCs;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import ISOFT.APITesting.RestAssuredModules.HTTPOperations;
import ISOFT.APITesting.Utilities.DocumentationUtilities;
import ISOFT.APITesting.Utilities.ExcelUtilities;
import ISOFT.APITesting.Utilities.ParsingTestData;
import ISOFT.APITesting.Utilities.variablesProvider;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;

public class CorrelationTCs_NoValuesValidation {
	
	@SuppressWarnings("unused")
	private ITestResult context = null;
	private Response [] previousJsonResponse = new Response [variablesProvider.maxRequests];
	private int Step = -1;
	
	@BeforeMethod
	public void SetUp(ITestResult ctx) {
		context = ctx;
	}
	
	@DataProvider()
	public Object[][] ProvideData(ITestContext context) throws Exception{
		String ExcelFile = context.getCurrentXmlTest().getAllParameters().get("ExcelFile");
		String ExcelSheet = context.getCurrentXmlTest().getAllParameters().get("ExcelSheet");
        Object[][] testObjArray = ExcelUtilities.get_data(ExcelFile, ExcelSheet, variablesProvider.dataProviderWithNoValuesCols);
        return (testObjArray);
	}
		
	@org.testng.annotations.Test(dataProvider="ProvideData")
	public void test_steps(String URL, String reguestMethod, String headerKeys,
			String headerValues, String postBody, String StatusCode,
			String JsonKeys, String DataTypes){
		RestAssured.defaultParser = Parser.JSON;
		
		Step++;
		//for header data
		String [] headerKeysList = headerKeys.split("\\r?\\n");
		String [] headerValuesList = headerValues.split("\\r?\\n");
		Assert.assertEquals(headerKeysList.length, headerValuesList.length);
		Map <String, String> headersMap = new HashMap<String, String>();
		for(int i=0 ; i<headerKeysList.length ; i++){
			if(headerKeysList[i].equals("") &&  headerValuesList[i].equals("")) break;
			headerKeysList[i] = ParsingTestData.getAllString(headerKeysList[i], previousJsonResponse);
			headerValuesList[i] = ParsingTestData.getAllString(headerValuesList[i], previousJsonResponse);
			headersMap.put(headerKeysList[i].trim(), headerValuesList[i].trim());
		}
		URL = ParsingTestData.getAllString(URL, previousJsonResponse);
		reguestMethod = ParsingTestData.getAllString(reguestMethod, previousJsonResponse);
		postBody = ParsingTestData.getAllString(postBody, previousJsonResponse);
		//HTTP Request
		Response res = HTTPOperations.HTTPRequest(URL, reguestMethod, headersMap, postBody);
		if (res == null){
			Assert.assertEquals(variablesProvider.availableRequestTypes, reguestMethod, "[Step " + Integer.toString(Step+1) + "]: Invalid Request type");
		}
		previousJsonResponse[Step] = res;
		
		int StCode = Integer.parseInt(ParsingTestData.getAllString(StatusCode, previousJsonResponse));
		//Prepare JSON keys and data types
		String [] JsonKeysList = JsonKeys.split("\\r?\\n");
		String [] DataTypesList = DataTypes.split("\\r?\\n");
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")){
				break;
			}else{
				JsonKeysList[i] = ParsingTestData.getAllString(JsonKeysList[i], previousJsonResponse);
				DataTypesList[i] = ParsingTestData.getAllString(DataTypesList[i], previousJsonResponse);
			}
		}
		String Params = DocumentationUtilities.getTestDataWithoutValues(URL, reguestMethod, headerKeysList,
				headerValuesList, postBody, StCode, JsonKeysList,DataTypesList);
				
	Params = "[Step " + Integer.toString(Step+1) + "]\n"+ Params +
				"The returned baw response:\n\n"+
				"Headers:\n" + res.headers().toString()+ "\n\n" +
				"Cookies:\n" + res.cookies().toString()+ "\n\n" +
				"Body:\n" + res.getBody().prettyPrint() + "\n";

		Assert.assertEquals((int)res.getStatusCode(),
				StCode, Params);
		
		//assert status code
		/*Assert.assertEquals((int)res.getStatusCode(),
				StCode,
				"[Step " + Integer.toString(Step+1) + "]\n"+ Params + res.getBody().asString() + "\n");*/
		
		/*this section to check:
		 * 1) Key Data type
		 * 2) Key is exists
		 * 3) Key with specific value
		*/
		for (int i = 0 ; i<JsonKeysList.length ; i++){
			if(JsonKeysList[i].equals("")) break;	
			//check it is not null
			Assert.assertNotNull(res.path(JsonKeysList[i]),
					Params + (JsonKeysList[i]) + ": is NULL" + "\n");
			//check data type			
			String s = res.path(JsonKeysList[i]).getClass().getName().toLowerCase();
			String actualDataType = s.split("\\.")[2];
			Assert.assertEquals(actualDataType, DataTypesList[i],
					Params + "It is: " + JsonKeysList[i] + "\n");
		}
		
	}
}
