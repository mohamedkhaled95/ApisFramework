package ISOFT.APITesting.Runners;

import java.util.ArrayList;
import java.util.List;


import org.testng.TestNG;
//java -jar Main.jar DataSet/TestData.xls

public class MainRunner {
	public static void main(String[] args) {

		String xmlRunner = "";
		try {
			xmlRunner = args[0];
			//xmlRunner = "D:\\JAR\\Apis-Testing-Framework\\runner.xml";
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Please Enter xmlRunner file directory!!");
			return;
		}
		System.out.println("XML Runner file: " + xmlRunner);
		System.out.println("==================================");
		TestNG runner = new TestNG();
		List<String> suitefiles=new ArrayList<String>();
		suitefiles.add(xmlRunner);
		runner.setTestSuites(suitefiles);
		runner.run();

	}

}