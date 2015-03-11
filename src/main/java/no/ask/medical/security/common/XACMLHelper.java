package no.ask.medical.security.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XACMLHelper {

	public static String createXACMLRequest(String userName, String action, String environment, String resource) {

		return "<Request xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\" CombinedDecision=\"false\" ReturnPolicyIdList=\"false\">\n" 
		+

		"<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n" + "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\" IncludeInResult=\"false\">\n" + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + userName + "</AttributeValue>\n" + "</Attribute>\n" + "</Attributes>\n" +

		"<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">\n" + "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:resource:resource-id\" IncludeInResult=\"false\">\n" + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + resource + "</AttributeValue>\n" + "</Attribute>\n" + "</Attributes>\n" +

		"<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:environment\">\n" + "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:environment:environment-id\" IncludeInResult=\"false\">\n" + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + environment + "</AttributeValue>\n" + "</Attribute>\n" + "</Attributes>\n" + "<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n" + "<Attribute AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\" IncludeInResult=\"false\">\n" + "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + action + "</AttributeValue>\n" + "</Attribute>\n" + "</Attributes>\n" +

		"</Request>";

	}
	
	 /**
     * Creates DOM representation of the XACML request
     *
     * @param response  XACML request as a String object
     * @return XACML request as a DOM element
     */
    public static Element getXacmlResponse(String response) {

        ByteArrayInputStream inputStream;
        DocumentBuilderFactory dbf;
        Document doc;

        inputStream = new ByteArrayInputStream(response.getBytes());
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        try {
            doc = dbf.newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            System.err.println("DOM of request element can not be created from String");
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
               System.err.println("Error in closing input stream of XACML response");
            }
        }
        return doc.getDocumentElement();
    }    
}
