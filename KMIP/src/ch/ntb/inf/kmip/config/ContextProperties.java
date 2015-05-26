/**
 * Properties.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * This class parses an xml configuration file and easily offers the 
 * init-parameters as Properties stored in a HashTable, which the 
 * class Properties extends. 
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright © 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */
package ch.ntb.inf.kmip.config;

import java.io.File;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ContextProperties extends Properties {

	private static final long serialVersionUID = 5523640676789403887L;

	public ContextProperties(String xmlPath, String name) {
		
		try {
			File fXmlFile = new File(xmlPath + name);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			getInitParameters(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public void getInitParameters(Document doc){
		NodeList config = doc.getElementsByTagName("init-param");
		
		for(int i=0; i<config.getLength(); i++){
			String paramName = ((Element)config.item(i)).getElementsByTagName("param-name").item(0).getTextContent();
			String paramValue = ((Element)config.item(i)).getElementsByTagName("param-value").item(0).getTextContent();
			put(paramName, paramValue);
		}
	}

	public String getStringProperty(String aName) throws Exception {
		String val = getProperty(aName);
		if (val == null)
			throw new Exception("Parameter " + aName + " not defined");
		else if (val.length() == 0)
			throw new Exception("Parameter " + aName + " is empty");
		else
			return val;
	} 

	public String getStringProperty(String aName, String aDef) {
		try {
			return getStringProperty(aName);
		} catch (Exception e) {
			return aDef;
		} 
	} 

	public int getIntProperty(String aName) throws Exception {
		return Integer.parseInt(getStringProperty(aName));
	} 

	public int getIntProperty(String aName, int aDef) {
		try {
			return getIntProperty(aName);
		} catch (Exception e) {
			return aDef;
		}
	}

	public boolean getBooleanProperty(String aName) throws Exception {
		String val = getStringProperty(aName).toLowerCase();
		if ((val.equalsIgnoreCase("true")) || (val.equalsIgnoreCase("on"))
				|| (val.equalsIgnoreCase("yes"))) {
			return true;
		} else if ((val.equalsIgnoreCase("false"))
				|| (val.equalsIgnoreCase("off"))
				|| (val.equalsIgnoreCase("no"))) {
			return false;
		} else {
			throw new Exception("Parameter " + aName + " is not a boolean");
		} 
	}

	public boolean getBooleanProperty(String aName, boolean aDef) {
		try {
			return getBooleanProperty(aName);
		} catch (Exception e) {
			return aDef;
		} 
	}

} 
