/**
 * KLMSUtils.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
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
package ch.ntb.inf.klms.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import ch.ntb.inf.klms.manager.KLMSUniqueIdentifierMissingException;
import ch.ntb.inf.klms.model.attributes.KLMSAttributeValue;
import ch.ntb.inf.klms.model.attributes.Offset;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.klmsenum.EnumTypeKLMS;
import ch.ntb.inf.klms.model.objects.CredentialValue;
import ch.ntb.inf.klms.model.objects.KeyMaterial;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.base.Credential;
import ch.ntb.inf.klms.model.objects.base.KeyBlock;
import ch.ntb.inf.klms.model.objects.base.KeyValue;
import ch.ntb.inf.klms.model.objects.managed.ManagedObject;
import ch.ntb.inf.klms.model.objects.managed.SymmetricKey;



public class KLMSUtils {
	
	private static final String ATTRIBUTE_LOCATION = "ch.ntb.inf.klms.model.attributes.";

/////////////////////////////////////////////////////////////////////////////////////// Create Parameter Map	

	
	public static void addAttributeListToParameterMap(ArrayList<Attribute> attributes, HashMap<String, String> requestParameters){
		for(Attribute b : attributes){
			addAttributeToParameterMap(b, requestParameters);
		}
	}
	
	public static void addManagedObjectToParameterMap(ManagedObject object, HashMap<String, String> parameters){
		if(object instanceof SymmetricKey){
			createParameterMapForSymmetricKey((SymmetricKey)object, parameters);
		}
	}
	
	public static void addAttributeToParameterMap(Attribute a, HashMap<String, String> requestParameters){
		int count = addAttributeName(a.getAttributeName(), requestParameters);
		if(a.getAttributeType() == EnumTypeKLMS.Structure){
			addValueStructure(a.getAttributeName(), a.getValues(), count, requestParameters);
		} else{
			addSimpleValue(a.getAttributeName(), a.getValues()[0].getValueString(), count,  requestParameters);
		}
	}
	

	public static void addAttributeNamesToParameterMap(ArrayList<String> attributeNames, HashMap<String, String> response) {
		for(String attributeName : attributeNames){
			addAttributeName(attributeName, response);
		}
	}
	
	private static int addAttributeName(String attributeName, HashMap<String, String> requestParameters){
		if(requestParameters.containsKey(attributeName)){
			int count = Integer.parseInt(requestParameters.get(attributeName));
			count++;
			replaceAttributeEntry(attributeName, count, requestParameters);
			return count;
		}
		else{
			createAttributeEntry(attributeName, 1, requestParameters);
			return 1;
		}
	}
	
	private static void addValueStructure(String attributeName, KLMSAttributeValue[] values, int count, HashMap<String, String> requestParameters) {
		for(int i = 0; i < values.length; i++){
			createAttributeValueEntry(values[i], count, requestParameters);
		}
	}
	
	private static void addSimpleValue(String attributeName, String attributeValue, int count, HashMap<String, String> requestParameters) {
		createAttributeValueEntry(attributeName, attributeValue, count, requestParameters);
	}
	
	
	public static void createParameterMapForSymmetricKey(SymmetricKey object, HashMap<String, String> parameters){
		KeyBlock keyBlock = object.getKeyBlock();
		KeyValue keyValue = keyBlock.getKeyValue();
		KeyMaterial keyMaterial = keyValue.getKeyMaterial();
		Set<Attribute> keyValueAttributes = keyValue.getAttributes();
		
		parameters.put("Symmetric Key", "1");
		parameters.put("Key Block", "1");
		parameters.put("Key Format Type", keyBlock.getKeyFormatType());
		if (keyBlock.getKeyCompressionType() != null) {
			parameters.put("Key Compression Type", keyBlock.getKeyCompressionType());
		}
		parameters.put("Key Value", "1");
		parameters.put("Key Material", "1");
		parameters.put("Key Material Byte String", keyMaterial.getKMString());
		
		// Transparent Key Structure not supported in Test KLMS
//		if (keyMaterial.getTransparentKeyStructure() != null) {
//		}
		if (keyValueAttributes != null) {
			for (Attribute a : keyValueAttributes) {
				addAttributeToParameterMap(a, parameters);
			}
		}
		for (Attribute a : keyBlock.getAttributes()) {
			addAttributeToParameterMap(a, parameters);
		}
		
		// Key Wrapping Data not supported in Test KLMS
//		if (keyBlock.getKeyWrappingData() != null) {
//		}	
	}
	
	
	

	private static void createAttributeValueEntry(String attributeName, String attributeValue, int count, HashMap<String, String> requestParameters) {
		requestParameters.put(attributeName + "value" + count, attributeValue);
	}
	
	private static void createAttributeValueEntry(KLMSAttributeValue value, int count, HashMap<String, String> requestParameters) {
		createAttributeValueEntry(value.getName(), value.getValueString(), count,  requestParameters);
	}

	private static void createAttributeEntry(String attributeName, int count, HashMap<String, String> requestParameters) {
		requestParameters.put(attributeName, Integer.toString(count));
	}
	
	private static void replaceAttributeEntry(String attributeName, int count, HashMap<String, String> requestParameters) {
		requestParameters.remove(attributeName);
		createAttributeEntry(attributeName, count, requestParameters);
	}

	
	
	public static String getAttributeValue(String valueName, int count, HashMap<String, String> parameters){
		String value = parameters.get(valueName + "value" + count);
		parameters.remove(valueName + "value" + count);
		return value;
	}
	
	
	public static void addIDPlaceholderIfNecessary(HashMap<String, String> parameters, String idPlaceholder) {
		if(!parameters.containsKey("Unique Identifier")){
			createAttributeEntry("Unique Identifier", 1, parameters);
			createAttributeValueEntry("Unique Identifier", idPlaceholder, 1, parameters);
		}
	}
	
	
/////////////////////////////////////////////////////////////////////////////////////// Decode Parameter Map	
	
	
	public static ArrayList<Attribute> createAttributesFromHashMap(HashMap<String, String> parameters){
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		int counter = 0;
		while(!parameters.isEmpty()){
			Iterator<Entry<String, String>> it = parameters.entrySet().iterator();
			Entry<String, String> e = it.next();
			for(int i = 0; i < counter; i++){
				e = it.next();
			}
			
			try {
				Class<?> attributeClass = getAttributeClass(e.getKey());
				int count = Integer.parseInt(e.getValue());
				
				for (int i = 1; i <= count; i++) {
					attributes.add(createAttribute(attributeClass, parameters, i));
				}
				parameters.remove(e.getKey());
				counter = 0;
			} catch (Exception e1) {
				counter++;			// if Entry is not an Attribute Name
			} 
		}
		return attributes;
	}
	
	
	public static HashMap<String, ArrayList<Attribute>> createAttributeListsForKeyPairFromHashMap(HashMap<String, String> parameters){
		HashMap<String, ArrayList<Attribute>> attributeLists = new HashMap<String, ArrayList<Attribute>>();
		ArrayList<Attribute> common = new ArrayList<Attribute>();
		ArrayList<Attribute> privateKey = new ArrayList<Attribute>();
		ArrayList<Attribute> publicKey = new ArrayList<Attribute>();
		attributeLists.put("Common", common);
		attributeLists.put("PrivateKey", privateKey);
		attributeLists.put("PublicKey", publicKey);
		
	
		int counter = 0;
		while(!parameters.isEmpty()){
			Iterator<Entry<String, String>> it = parameters.entrySet().iterator();
			Entry<String, String> e = it.next();
			for(int i = 0; i < counter; i++){
				e = it.next();
			}
			
			String type = null;
			String name = e.getKey();
			if(name.startsWith("Common")){
				type = "Common";
				name = name.replaceFirst(type, "");
			}
			else if(name.startsWith("PrivateKey")){
				type = "PrivateKey";
				name = name.replaceFirst(type, "");
			}
			else if(name.startsWith("PublicKey")){
				type = "PublicKey";
				name = name.replaceFirst(type, "");
			}
			
			try {
				Class<?> attributeClass = getAttributeClass(name);
				int count = Integer.parseInt(e.getValue());
				
				for (int i = 1; i <= count; i++) {
					if(type.equals("Common")){
						common.add(createAttribute(attributeClass, parameters, i, type));
					}
					else if(type.equals("PrivateKey")){
						privateKey.add(createAttribute(attributeClass, parameters, i, type));
					}
					else if(type.equals("PublicKey")){
						publicKey.add(createAttribute(attributeClass, parameters, i, type));
					}
				}
				parameters.remove(e.getKey());
				counter = 0;
			} catch (Exception e1) {
				counter++;			// if Entry is not an Attribute Name
			} 
		}
		return attributeLists;
	}
	
	
	private static Class<?> getAttributeClass(String nameOfClass) throws ClassNotFoundException{
		String className = nameOfClass.replaceAll("\\s","").replaceAll("-", "");
		String classNameFinal = Character.toUpperCase(className.charAt(0)) + className.substring(1);
		return Class.forName(ATTRIBUTE_LOCATION + classNameFinal);
	}
	
	private static Attribute createAttribute(Class<?> attributeClass, HashMap<String, String> parameters, int i) throws InstantiationException, IllegalAccessException {
		Attribute attribute = (Attribute) attributeClass.newInstance();

		if (attribute.getAttributeType() == EnumTypeKLMS.Structure) {
			createComplexValue(attribute, parameters, i);
		} else {
			if(parameters.containsKey(attribute.getAttributeName() + "value"+ i)){
				attribute.setValue(parameters.get(attribute.getAttributeName() + "value"+ i), null);
				parameters.remove(attribute.getAttributeName() + "value" + i);
			} 
		}
		return attribute;
	}
	
	
	private static Attribute createAttribute(Class<?> attributeClass, HashMap<String, String> parameters, int i, String type) throws InstantiationException, IllegalAccessException {
		Attribute attribute = (Attribute) attributeClass.newInstance();

		if (attribute.getAttributeType() == EnumTypeKLMS.Structure) {
			createComplexValue(attribute, parameters, i, type);
		} else {
			if(parameters.containsKey(type + attribute.getAttributeName() + "value"+ i)){
				attribute.setValue(parameters.get(type + attribute.getAttributeName() + "value"+ i), null);
				parameters.remove(type + attribute.getAttributeName() + "value" + i);
			} 
		}
		return attribute;
	}
	
	
	private static void createComplexValue(Attribute a, HashMap<String, String> parameters, int i) {
		KLMSAttributeValue[] values = a.getValues();
	
		for(int j = 0; j < values.length; j++){
			if(parameters.containsKey((values[j].getName() + "value" + i))){
				a.setValue(parameters.get(values[j].getName() + "value" + i), values[j].getName());
				parameters.remove(values[j].getName() + "value" + i);
			}

		}	
	}

	private static void createComplexValue(Attribute a, HashMap<String, String> parameters, int i, String type) {
		KLMSAttributeValue[] values = a.getValues();
	
		for(int j = 0; j < values.length; j++){
			if(parameters.containsKey((type + values[j].getName() + "value" + i))){
				a.setValue(parameters.get(type + values[j].getName() + "value" + i), values[j].getName());
				parameters.remove(type + values[j].getName() + "value" + i);
			}

		}	
	}
	
	
	
	public static UniqueIdentifier getUniqueIdentifierFromParameters(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException {
		UniqueIdentifier uid;
		for(Map.Entry<String, String> e : parameters.entrySet()){
			if(e.getKey().equals("Unique Identifier" + "value" + "1")){
				uid = new UniqueIdentifier(e.getValue());
				parameters.remove("Unique Identifier" + "value" + "1");
				parameters.remove("Unique Identifier");
				return uid;
			}
		}
		throw new KLMSUniqueIdentifierMissingException();
	}
	
	public static Offset getOffsetFromParameters(HashMap<String, String> parameters){
		Offset offset;
		for(Map.Entry<String, String> e : parameters.entrySet()){
			if(e.getKey().equals("Offset" + "value" + "1")){
				offset = new Offset(e.getValue());
				parameters.remove("Offset" + "value" + "1");
				parameters.remove("Offset");
				return offset;
			}
		}
		return null;
	}

	public static Credential getCredentialFromParameters(HashMap<String, String> parameters) {
		CredentialValue credentialValue = new CredentialValue();
		Credential credential = new Credential(credentialValue);
		credential.setCredentialType(parameters.get("Credential Type"));
		credentialValue.setUsername(parameters.get("Username"));
		credentialValue.setPassword(parameters.get("Password"));
		return credential;
	}

	
}
