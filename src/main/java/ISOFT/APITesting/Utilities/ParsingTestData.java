package ISOFT.APITesting.Utilities;
import com.jayway.restassured.response.Response;
//import atu.alm.wrapper.exceptions.ALMServiceException;
//import VSSE.APITesting.Utilities.ALM;


public class ParsingTestData {

	//important case to handle. It is already handled
	//https://billingservice-es-test.myvdf.aws.cps.vodafone.com/v2/payment/customerBills/[+]#prev_body_res[2]:items[0].id
	public static String getValue( String str , Response [] previousJsonResponse){
		String temp = "";
		String ret_value = "";
int ret_value_int = 0;
int temp2 = 0 ;
		if(str.contains(Keywords.PREV_COOKIE)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse[(index-1)]).getCookie(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp;
			else ret_value = temp;
			if(str.endsWith(" ")) ret_value += " ";
		}else if(str.contains(Keywords.PREV_HEADER)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse[(index-1)]).header(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp;
			else ret_value = temp;
			if(str.endsWith(" ")) ret_value += " ";
		}else if(str.contains(Keywords.PREV_BODY)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp = (previousJsonResponse[(index-1)]).path(params[1]);
			if(str.startsWith(" ")) ret_value = " " + temp;
			else ret_value = temp;
			if(str.endsWith(" ")) ret_value += " ";
		}
		else if(str.contains(Keywords.PREV_BODY_integer)){
			String[] params = (str.split(":"));
			String result = params[0].substring((params[0].indexOf("[")) + 1, (params[0].indexOf("]")));
			int index = Integer.parseInt(result);
			temp2 = (previousJsonResponse[(index-1)]).path(params[1]);
			if(str.startsWith(" ")) ret_value_int = temp2;
			else ret_value_int = temp2;
			ret_value = Integer.toString(ret_value_int);
			if(str.endsWith(" ")) ret_value += " ";
		}

		else ret_value = str;
		return ret_value;
	}
	
	public static String getAllString(String str, Response [] previousJsonResponse) {
		String[] params = (str.split("\\[\\+\\]"));
		String total = "";
		for (int i=0 ; i<params.length ; i++){
//
//			String temp = params[i];
//
//			System.out.println(temp);
//
//			int firstIndex = params[i].codePointAt(0);
//
//			if(firstIndex >= 0x0600 && firstIndex <= 0x06E0){
//
//				try {
//					params[i] = new String (params[i].getBytes() , "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//
//					params[i] = temp;
//				}
//			}
			if (i==0) total = getValue(params[i], previousJsonResponse);
			else total = total + getValue(params[i], previousJsonResponse);
		}
		return total;
	}

}
