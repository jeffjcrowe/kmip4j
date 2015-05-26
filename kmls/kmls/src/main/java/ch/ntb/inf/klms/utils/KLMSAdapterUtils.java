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
import java.util.Map.Entry;

import ch.ntb.inf.kmip.attributes.CryptographicAlgorithm;
import ch.ntb.inf.kmip.attributes.CryptographicLength;
import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.KeyMaterial;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.CommonTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.KeyBlock;
import ch.ntb.inf.kmip.objects.base.KeyValue;
import ch.ntb.inf.kmip.objects.base.PrivateKeyTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.PublicKeyTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.managed.ManagedObject;
import ch.ntb.inf.kmip.objects.managed.PrivateKey;
import ch.ntb.inf.kmip.objects.managed.PublicKey;
import ch.ntb.inf.kmip.objects.managed.SecretData;
import ch.ntb.inf.kmip.objects.managed.SymmetricKey;
import ch.ntb.inf.kmip.objects.managed.Template;


public class KLMSAdapterUtils {

///////////////////////////////////////////////////////////////////////////////////////// create HashMap
	
	public static HashMap<String, String> createRequestParameters(ArrayList<Attribute> attributes, ArrayList<TemplateAttributeStructure> templateAttributeStructures, ManagedObject managedObject, Credential credential){
		HashMap<String, String> requestParameters = new HashMap<String, String>();
		
		addTemplateAttributeStructures(templateAttributeStructures, attributes, requestParameters);
		addManagedObject(managedObject, attributes, requestParameters);
		
		for(Attribute b : attributes){
			addAttribute(b, requestParameters);
		}
		
		if(credential != null){
			addCredential(credential, requestParameters);
		}
		
		return requestParameters;	
	}
	
	private static void addTemplateAttributeStructures(ArrayList<TemplateAttributeStructure> templateAttributeStructures, ArrayList<Attribute> attributes, HashMap<String, String> requestParameters) {
		if(templateAttributeStructures != null){	
			Iterator<TemplateAttributeStructure> itTas = templateAttributeStructures.iterator();
			while(itTas.hasNext()){
				TemplateAttributeStructure actualTas = itTas.next();
				if(actualTas instanceof CommonTemplateAttribute){			
					addTemplateAttributesByStructureName(actualTas, requestParameters, "Common");
				}
				else if(actualTas instanceof PrivateKeyTemplateAttribute){				
					addTemplateAttributesByStructureName(actualTas, requestParameters, "PrivateKey");
				}
				else if(actualTas instanceof PublicKeyTemplateAttribute){	
					addTemplateAttributesByStructureName(actualTas, requestParameters, "PublicKey");
				} else{
					// TemplateAttribute
					attributes.addAll(actualTas.getAttributes());
					attributes.addAll(actualTas.getNames());
				}
			}
		}
	}
	
	private static void addTemplateAttributesByStructureName(TemplateAttributeStructure templateAttribute, HashMap<String, String> parameters, String name){
		if(templateAttribute.getAttributes() != null){
			for(Attribute a : templateAttribute.getAttributes()){
				addAttribute(a, parameters, name);
			}
		}
		if(templateAttribute.getNames() != null){
			for(Attribute a : templateAttribute.getNames()){
				addAttribute(a, parameters, name);
			}
		}
	}
	
	private static void addManagedObject(ManagedObject managedObject, ArrayList<Attribute> attributes, HashMap<String, String> requestParameters) {
		if(managedObject != null){
			if(managedObject instanceof SecretData){
				addSecretData((SecretData) managedObject, requestParameters);
			} 
			else if(managedObject instanceof SymmetricKey){
				addSymmetricKey((SymmetricKey) managedObject, requestParameters);
			} 
			else if(managedObject instanceof PrivateKey){
				addPrivateKey((PrivateKey) managedObject, requestParameters);
			} 
			else if(managedObject instanceof PublicKey){
				addPublicKey((PublicKey) managedObject, requestParameters);
			} 
			else if(managedObject instanceof Template && ((Template)managedObject).getAttributes() != null){
				attributes.addAll(((Template)managedObject).getAttributes());
			}
		}
	}

	
	public static void addSecretData(SecretData secretData, HashMap<String, String> parameters){
		parameters.put("Secret Data", "1");
		parameters.put("Secret Data Type", secretData.getSecretDataType().getKey());
		addKeyBlock(secretData.getKeyBlock(), parameters);

	}
	
	public static void addSymmetricKey(SymmetricKey symmetricKey, HashMap<String, String> parameters){
		parameters.put("Symmetric Key", "1");
		addKeyBlock(symmetricKey.getKeyBlock(), parameters);
	}
	
	public static void addPrivateKey(PrivateKey privateKey, HashMap<String, String> parameters){
		parameters.put("Private Key", "1");
		addKeyBlock(privateKey.getKeyBlock(), parameters);
	}
	
	public static void addPublicKey(PublicKey publicKey, HashMap<String, String> parameters){
		parameters.put("Public Key", "1");
		addKeyBlock(publicKey.getKeyBlock(), parameters);
	}
	
	public static void addKeyBlock(KeyBlock keyBlock, HashMap<String, String> parameters){
		KeyValue keyValue = keyBlock.getKeyValue();
		KeyMaterial keyMaterial = keyValue.getKeyMaterial();
		ArrayList<Attribute> keyValueAttributes = keyValue.getAttributes();
		
		parameters.put("Key Block", "1");
		parameters.put("Key Format Type", keyBlock.getKeyFormatType().getKey());
		if (keyBlock.getKeyCompressionType() != null) {
			parameters.put("Key Compression Type", keyBlock.getKeyCompressionType().getKey());
		}
		parameters.put("Key Value", "1");
		parameters.put("Key Material", "1");
		parameters.put("Key Material Byte String", keyMaterial.getKeyMaterialByteString().getValueString());
		if (keyMaterial.getTransparentKeyStructure() != null) {
			// not supported in Test KLMS
		}
		if (keyValueAttributes != null) {
			for (Attribute a : keyValueAttributes) {
				addAttribute(a, parameters);
			}
		}
		for (Attribute a : keyBlock.getAttributes()) {
			addAttribute(a, parameters);
		}
		if (keyBlock.getKeyWrappingData() != null) {
			// not supported in Test KLMS
		}	
	}
	
	
	public static void addAttribute(Attribute attribute, HashMap<String, String> parameters, String templateAttributeType){
		int count = addAttributeName(attribute.getAttributeName(), parameters, templateAttributeType);
		if(attribute.getAttributeType() == EnumType.Structure){
			addValueStructure(attribute.getAttributeName(), attribute.getValues(), count, parameters, templateAttributeType);
		} else{
			addSimpleValue(attribute.getAttributeName(), attribute.getValues()[0].getValueString(), count,  parameters, templateAttributeType);
		}
	}
	
	public static void addAttribute(Attribute attribute, HashMap<String, String> parameters){
		int count = addAttributeName(attribute.getAttributeName(), parameters);
		if(attribute.getAttributeType() == EnumType.Structure){
			addValueStructure(attribute.getAttributeName(), attribute.getValues(), count, parameters);
		} else{
			addSimpleValue(attribute.getAttributeName(), attribute.getValues()[0].getValueString(), count,  parameters);
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
	
	private static int addAttributeName(String attributeName, HashMap<String, String> requestParameters, String templateAttributeType){
		if(requestParameters.containsKey(templateAttributeType + attributeName)){
			int count = Integer.parseInt(requestParameters.get(templateAttributeType + attributeName));
			count++;
			replaceAttributeEntry(templateAttributeType + attributeName, count, requestParameters);
			return count;
		}
		else{
			createAttributeEntry(templateAttributeType + attributeName, 1, requestParameters);
			return 1;
		}
	}
	
	private static void addValueStructure(String attributeName, KMIPAttributeValue[] values, int count, HashMap<String, String> requestParameters) {
		for(int i = 0; i < values.length; i++){
			createAttributeValueEntry(values[i], count, requestParameters);
		}
	}
	
	private static void addValueStructure(String attributeName, KMIPAttributeValue[] values, int count, HashMap<String, String> requestParameters, String templateAttributeType) {
		for(int i = 0; i < values.length; i++){
			createAttributeValueEntry(values[i], count, requestParameters, templateAttributeType);
		}
	}
	
	private static void addSimpleValue(String attributeName, String attributeValue, int count, HashMap<String, String> requestParameters) {
		createAttributeValueEntry(attributeName, attributeValue, count, requestParameters);
	}
	
	private static void addSimpleValue(String attributeName, String attributeValue, int count, HashMap<String, String> requestParameters, String templateAttributeType) {
		createAttributeValueEntry(templateAttributeType + attributeName, attributeValue, count, requestParameters);
	}
	
	
	private static void createAttributeValueEntry(String attributeName, String attributeValue, int count, HashMap<String, String> requestParameters) {
		requestParameters.put(attributeName + "value" + count, attributeValue);
	}
	
	private static void createAttributeValueEntry(KMIPAttributeValue value, int count, HashMap<String, String> requestParameters) {
		createAttributeValueEntry(value.getName(), value.getValueString(), count,  requestParameters);
	}
	
	private static void createAttributeValueEntry(KMIPAttributeValue value, int count, HashMap<String, String> requestParameters, String templateAttributeType) {
		createAttributeValueEntry(templateAttributeType + value.getName(), value.getValueString(), count,  requestParameters);
	}

	private static void createAttributeEntry(String attributeName, int count, HashMap<String, String> requestParameters) {
		requestParameters.put(attributeName, Integer.toString(count));
	}
	
	private static void replaceAttributeEntry(String attributeName, int count, HashMap<String, String> requestParameters) {
		requestParameters.remove(attributeName);
		createAttributeEntry(attributeName, count, requestParameters);
	}
	

	private static void addCredential(Credential credential, HashMap<String, String> parameters) {
		parameters.put("Credential", "1");
		parameters.put("Credential Type", credential.getCredentialType().getKey());
		parameters.put("Credential Value", "1");
		parameters.put("Username", credential.getCredentialValue().getUsername().getValueString());
		parameters.put("Password", credential.getCredentialValue().getPassword().getValueString());
	}
	
	
	

///////////////////////////////////////////////////////////////////////////////////////////decode HashMap
	
	public static void addAttributesToBatch(HashMap<String, String> parameters, KMIPBatch batch){
		batch.addAttributes(getAttributesFromHashMap(parameters));
	}
	
	
	private static ArrayList<Attribute> getAttributesFromHashMap(HashMap<String, String> parameters){
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
	
	private static Class<?> getAttributeClass(String nameOfClass) throws ClassNotFoundException{
		String className = nameOfClass.replaceAll("\\s","").replaceAll("-", "");
		String classNameFinal = Character.toUpperCase(className.charAt(0)) + className.substring(1);
		try{
			return Class.forName("ch.ntb.inf.kmip.attributes." + classNameFinal);
		} catch (Exception e){
			return Class.forName("ch.ntb.inf.kmip.operationparameters." + classNameFinal);
		}
	}
	
	private static Attribute createAttribute(Class<?> attributeClass, HashMap<String, String> parameters, int i) throws InstantiationException, IllegalAccessException {
		Attribute attribute = (Attribute) attributeClass.newInstance();

		if (attribute.getAttributeType() == EnumType.Structure) {
			createComplexValue(attribute, parameters, i);
		} else {
			if(parameters.containsKey(attribute.getAttributeName() + "value"+ i)){
				attribute.setValue(parameters.get(attribute.getAttributeName() + "value"+ i), null);
				parameters.remove(attribute.getAttributeName() + "value" + i);
			} 
		}
		return attribute;
	}

	
	public static void addCertainAttributeToBatch(HashMap<String, String> parameters, KMIPBatch batch,  String attributeName){
		Attribute attribute = null;
		
		try {
			attribute = createAttribute(getAttributeClass(attributeName), parameters, 1);
			parameters.remove(attributeName);
			batch.addAttribute(attribute);
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
	}
	
	private static void createComplexValue(Attribute a, HashMap<String, String> parameters, int i) {
		KMIPAttributeValue[] values = a.getValues();
	
		for(int j = 0; j < values.length; j++){
			String valueName = values[j].getName() + "value" + i;
			if(parameters.containsKey(valueName)){
				a.setValue(parameters.get(valueName), values[j].getName());
				parameters.remove(valueName);
			}
		}	
	}



	public static void addObjectToBatch(HashMap<String, String> parameters, KMIPBatch batch) {
		// only Symmetric Key supported in Test KLMS
		// Transparent Key Structure and Key Wrapping Data not supported
		
		if(parameters.containsKey("Symmetric Key")){		
			
			KeyMaterial keyMaterial = new KeyMaterial();
			String keyMaterialString = parameters.get("Key Material Byte String");
			keyMaterial.setKeyMaterialByteString(keyMaterialString);
			
			KeyValue keyValue = new KeyValue(keyMaterial);
			
			KeyBlock keyBlock = new KeyBlock(keyValue);
			keyBlock.setKeyFormatType(parameters.get("Key Format Type"));
			if(parameters.containsKey("Key Compression Type")){
				keyBlock.setKeyCompressionType(parameters.get("Key Compression Type"));
			}
	
			SymmetricKey symmetricKey = new SymmetricKey(keyBlock);
			
			parameters.remove("Symmetric Key");
			parameters.remove("Key Block");
			parameters.remove("Key Format Type");
			parameters.remove("Key Compression Type");
			parameters.remove("Key Value");
			parameters.remove("Key Material");
			parameters.remove("Key Material Byte String");

			ArrayList<Attribute> attributes = getAttributesFromHashMap(parameters);
			
			ArrayList<Attribute> keyValueAttributes = new ArrayList<Attribute>();
			keyValueAttributes.addAll(attributes);
			
			for(Attribute a : attributes){
				if(a instanceof CryptographicAlgorithm){
					keyBlock.setCryptographicAlgorithm(a);
					keyValueAttributes.remove(a);
				} else if(a instanceof CryptographicLength){
					keyBlock.setCryptographicLength(a);
					keyValueAttributes.remove(a);
				}
			}
			keyValue.setAttributes(keyValueAttributes);
			
			batch.setManagedObject(symmetricKey);	
		} 
		
	}

}
