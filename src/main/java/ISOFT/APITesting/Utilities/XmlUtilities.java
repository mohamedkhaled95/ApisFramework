package ISOFT.APITesting.Utilities;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.File;


public class XmlUtilities {




    public static Object[][] xmlreader(String filepath) throws Exception{

        File fXmlFile = new File(filepath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList Root = doc.getElementsByTagName("test");
        Object[][] testdata=new Object[Root.getLength()][9];
        for(int i=0;i<Root.getLength();i++)
        {
            Element test = ((Element)Root.item(i));
            testdata[i][0] = test.getElementsByTagName(variablesProvider.XmlUrl).item(0).getTextContent();
            testdata[i][1] = test.getElementsByTagName(variablesProvider.XmlType).item(0).getTextContent();
            testdata[i][2] = test.getElementsByTagName(variablesProvider.XmlHeaderKeys).item(0).getTextContent();
            testdata[i][3] = test.getElementsByTagName(variablesProvider.XmlHeaderValues).item(0).getTextContent();
            testdata[i][4] = test.getElementsByTagName(variablesProvider.XmlRequestBody).item(0).getTextContent();
            testdata[i][5] = test.getElementsByTagName(variablesProvider.XmlStatusCode).item(0).getTextContent();
            testdata[i][6] = test.getElementsByTagName(variablesProvider.XmlValidationKeys).item(0).getTextContent();
            testdata[i][7] = test.getElementsByTagName(variablesProvider.XmlValidationValues).item(0).getTextContent();
            testdata[i][8] = test.getElementsByTagName(variablesProvider.XmlValidationDataType).item(0).getTextContent();
        }
        return testdata;
    }

}
