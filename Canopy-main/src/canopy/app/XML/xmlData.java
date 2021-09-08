package canopy.app.XML;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import canopy.app.MainApp.person;

public class xmlData
{
	public static class person
	{
		public String getKey(String key) { return key; }	//Returns a key and salt to the xml insertion function
		public String getSalt(String salt) { return salt; }
		
	}
	
	public static void createXMLTable(String filePath, String id, String key, String salt) throws Exception
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(filePath);
        Element root = document.getDocumentElement();
        
        Collection<person> peopleIDs = new ArrayList<person>();
        peopleIDs.add(new person());
        
        for (person People : peopleIDs)
        {
        	// add xml elements
        	Element newID = document.createElement("ID");
        	newID.setAttribute("value", id);	//Insert a incrementing ID variable as the number
        	
        	Element newKey = document.createElement("secretKey");
        	newKey.appendChild(document.createTextNode(People.getKey(key)));
        	newID.appendChild(newKey);
        	
        	Element newSalt = document.createElement("salt");
        	newSalt.appendChild(document.createTextNode(People.getSalt(salt)));
        	newID.appendChild(newSalt);
        	
        	root.appendChild(newID);
        }
        
        DOMSource source = new DOMSource(document);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        //Formats the XML file into a more visually pleasing format
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        
        StreamResult result = new StreamResult(filePath);
        transformer.transform(source, result);
    }

	
	
	
	public static void receiveXMLData(String filePath) throws Exception		//gets all of the information from all of the nodes in the XML file
	{
		//Create document builder
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    
	    try
	    {
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	
	    	//Get the xml document
	    	Document documentRead = builder.parse(new File(filePath));
	    	
	    	//Normalise the xml structure
	    	documentRead.getDocumentElement().normalize();
	    	
	    	//Get all of the elements by their tag name.
	    	NodeList IDList = documentRead.getElementsByTagName("ID");
	    	
	    	for(int i = 0; i < IDList.getLength(); i++)	//gets all the information from the XML file
	    	{
	    		Node encID = IDList.item(i);
	    		if(encID.getNodeType() == Node.ELEMENT_NODE)	//AND id is equal to the id you want?
	    		{
	    			
	    			Element element = (Element) encID;
	    			String recievedID = element.getAttribute("value");	//gets the ID value of the key and salt from the XML file
	    			String recievedSecretKey = element.getElementsByTagName("secretKey").item(0).getTextContent();	//gets the key from the XML file
					String recievedSalt = element.getElementsByTagName("salt").item(0).getTextContent();	//gets the salt from the XML file
					
					System.out.println("\nInformation from XML file-----");
	    			System.out.println("Encryption ID: " + recievedID);
	    			System.out.println("Encryption secretKey: " + recievedSecretKey);
	    			System.out.println("Encryption Salt: " + recievedSalt);
	    		}
	    	}
	    }
	    
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	
	
	public static int receiveLastXMLID(String filePath) throws Exception	//gets the current ID of the most recently added node in the XML file
	{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(filePath);
		
		XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
		
        //XPathExpression expr = xpath.compile("//ID[@value=1]/@value");	//Gets the ID of the node with an ID of 1.
        XPathExpression expr = xpath.compile("//ID[last()]/@value");	//Gets the ID of the last node
        
        Object result = expr.evaluate(document, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        
        String strLastID = "";	//Initialise strLastID as an empty string, I don't know why it needs this, but it works :)
        
        for (int i = 0; i < nodes.getLength(); i++)
        {
            strLastID = nodes.item(i).getNodeValue();
        }
        int lastID = Integer.parseInt(strLastID);	//Cast strLastID to an integer
		return lastID;	//returns the ID of the last node in the XML file
        
        
	}
	
}