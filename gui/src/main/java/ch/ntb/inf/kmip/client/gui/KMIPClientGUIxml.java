/**
 * KMIPClientGUIxml.java
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
package ch.ntb.inf.kmip.client.gui;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ch.ntb.inf.kmip.attributes.Link;
import ch.ntb.inf.kmip.attributes.Name;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.kmipenum.EnumSecretDataType;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.Authentication;
import ch.ntb.inf.kmip.objects.KeyMaterial;
import ch.ntb.inf.kmip.objects.MessageExtension;
import ch.ntb.inf.kmip.objects.VendorExtension;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.KeyBlock;
import ch.ntb.inf.kmip.objects.base.KeyValue;
import ch.ntb.inf.kmip.objects.base.KeyWrappingData;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.managed.PrivateKey;
import ch.ntb.inf.kmip.objects.managed.PublicKey;
import ch.ntb.inf.kmip.objects.managed.SecretData;
import ch.ntb.inf.kmip.objects.managed.SymmetricKey;
import ch.ntb.inf.kmip.objects.managed.Template;
import ch.ntb.inf.kmip.types.KMIPByteString;


public class KMIPClientGUIxml{

	private KMIPClientGUI gui;
	private File fXmlFile;
	private NodeList nListUseCase;
	private Node n;		// actual selected node
	private int numberOfUseCases = 0;
	private boolean hasTwoUIDs = false;
	
	public KMIPClientGUIxml(KMIPClientGUI gui) {
		this.gui = gui;
		try {
			fXmlFile = new File("src/ch/ntb/inf/kmip/client/gui/xml/UseCases.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			nListUseCase = doc.getElementsByTagName("usecase");		
			numberOfUseCases = nListUseCase.getLength();
			n = nListUseCase.item(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Constructor for Custom XML Request
	 */
	public KMIPClientGUIxml(KMIPClientGUI gui, File f) {
		try {
			fXmlFile = f;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			nListUseCase = doc.getElementsByTagName("usecase");		
			numberOfUseCases = nListUseCase.getLength();
			n = nListUseCase.item(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Getters & Setters for Client GUI
	
	public void setHasTwoUIDs(boolean val){
		this.hasTwoUIDs = val;
	}
	
	public void setSelectedUseCase(int id){
		n = nListUseCase.item(id);
	}

	public NodeList getnListUseCase() {
		return nListUseCase;
	}
	
	public String getUseCaseDescription(){
		return ((Element)n).getElementsByTagName("description").item(0).getTextContent();
	}
	
	public String getUseCaseDetails(){
		return ((Element)n).getElementsByTagName("details").item(0).getTextContent();
	}
	
	public String getUseCaseName(){
		return ((Element)n).getElementsByTagName("name").item(0).getTextContent();
	}
	
	public String[] getNamesOfUseCases(){
		String[] useCases = new String[numberOfUseCases];
		for (int i = 0; i < numberOfUseCases; i++) {
			Node n = nListUseCase.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {			 
				Element e = (Element) n;
				useCases[i] = e.getElementsByTagName("name").item(0).getTextContent();
			}
		}
		return useCases;
	}
	
	public int getNumberOfUseCases(){
		return numberOfUseCases;
	}
	
	public String getExpectedTTLVRequest(){
		return ((Element)n).getElementsByTagName("ttlvrequest").item(0).getTextContent();
	}
	
	public String getExpectedTTLVResponse(){
		return ((Element)n).getElementsByTagName("ttlvresponse").item(0).getTextContent();
	}
		
	// Support to generate and fill the KMIPContainer from XML-Node-UseCase
	
	public KMIPContainer getKMIPContainer() {
		KMIPContainer container = new KMIPContainer();
		addRequestHeaderOptionsToKMIPContainer(container);
		addRequestBatchesToKMIPContainer(container);
		return container;
	}
	
	private void addRequestHeaderOptionsToKMIPContainer(KMIPContainer container){
		Element requestHeaderOptions = getElement(n, "requestheaderoptions");
		if(requestHeaderOptions != null){
			NodeList optionList = getNodeList(requestHeaderOptions, "option");
			int optionCount = optionList.getLength();
			for (int j = 0; j < optionCount; j++) {
				Element option = (Element) optionList.item(j);
				addHeaderOption(container, option);
			}
		}
	}
	
	private void addHeaderOption(KMIPContainer container, Element option){
		String name = getTextFromElement(option, "name");
		if(name.equals("Authentication")){
			addAuthenticationToKMIPContainer(container, option);
		} else{
			String value = getTextFromElement(option, "value");
			container.setOption(name, value);
		}
	}
			
	private void addAuthenticationToKMIPContainer(KMIPContainer container, Element option) {
		Credential credential = new Credential();
		Authentication authentication = new Authentication(credential);
		Element values = getElement(option, "values");
		NodeList valueNameList = getNodeList(values, "valuename");
		NodeList valueList = getNodeList(values, "value");
		for(int i = 0; i < valueList.getLength(); i++){
			String valueName = getTextFromNode(valueNameList.item(i));
			String value = getTextFromNode(valueList.item(i));
			credential.setValue(valueName, value);
		}
		container.setAuthentication(authentication);	
	}

	private void addRequestBatchesToKMIPContainer(KMIPContainer container){
		Element batches = getElement(n, "batches");
		NodeList batchList = getNodeList(batches, "batch");
		container.setBatchCount(batchList.getLength());
		for (int i = 0; i < container.getBatchCount(); i++) {
			Element batchElement = (Element) batchList.item(i);
			addBatch(container, batchElement);		
		}
	}

	private void addBatch(KMIPContainer container, Element batchElement ){
		KMIPBatch batch = new KMIPBatch();
		container.addBatch(batch);
		// add Operation
		String op = getTextFromElement(batchElement, "operation");
		batch.setOperation(new EnumOperation(op));				
		// add Batch Options
		addBatchOptionsToBatch(batchElement, batch);
		// add Attributes
		addAttributesToBatch(batchElement, batch);
		// add TemplateAttributeStructures
		addTemplateAttributeStructuresToBatch(batchElement,batch);
		// add KMIPObjects
		addTemplateToBatch(batchElement, batch);
		addSecretDataToBatch(batchElement, batch);
		addSymmetricKeyToBatch(batchElement, batch);
		addPrivateKeyToBatch(batchElement, batch);
		addPublicKeyToBatch(batchElement, batch);	
	}


	private void addBatchOptionsToBatch(Element batchElement, KMIPBatch batch){
		Element batchOptions = getElement(batchElement, "requestbatchitemoptions");
		if(batchOptions != null){
			NodeList optionList = getNodeList(batchOptions, "option");
			int optionCount = optionList.getLength();
			for (int j = 0; j < optionCount; j++) {
				Element option = (Element) optionList.item(j);
				addBatchOption(batch, option);
			}
		}
	}
	
	private void addBatchOption(KMIPBatch batch, Element option){
		String name = getTextFromElement(option, "name");
		if(name.equals("MessageExtension")){
			addMessageExtensionToBatch(batch, option);	
		} else if(name.equals("UniqueBatchItemID")){
			String value = getTextFromElement(option, "value");
			batch.setUniqueBatchItemID(new KMIPByteString(value));
		} 
	}
	
	private void addMessageExtensionToBatch(KMIPBatch batch, Element option){
		MessageExtension messageExtension = new MessageExtension();
		messageExtension.setCriticalityIndicator(getTextFromElement(option, "criticalityindicator"));
		messageExtension.setVendorIdentification(getTextFromElement(option, "vendoridentification"));
		
		Element vendorExtensionElement = getElement(option, "vendorextension");
		String tag = getTextFromElement(vendorExtensionElement, "tag");
		String type = getTextFromElement(vendorExtensionElement, "type");
		String value = getTextFromElement(vendorExtensionElement, "value");
		VendorExtension vendorExtension = new VendorExtension(tag, type, value);
		
		messageExtension.setVendorExtension(vendorExtension);
		batch.setMessageExtension(messageExtension);
	}

	private void addAttributesToBatch(Element batchElement, KMIPBatch batch) {
		Element attrib = getElement(batchElement, "attributes");
		if (attrib != null) {
			NodeList attribList = getNodeList(attrib, "attribute");
			int attributesCount = attribList.getLength();
			for (int j = 0; j < attributesCount; j++) {
				Element attribute = (Element) attribList.item(j);
				addAttribute(attribute, batch);
			}
		}
	}
		
	private void addAttribute(Element attribute, KMIPBatch batch){
		String className = getClassName(attribute);

		Attribute a = createAttributeInstance(className);

		if (className.equals("UniqueIdentifier")) {
			addUniqueIdentifier(attribute, a);
		} else if (className.equals("Link")) {
			addLink(attribute, a);
		} else if (className.equals("AsynchronousCorrelationValue")) {
			addAsynchronousCorrelationValue(attribute, a);
		} else {
			addOtherAttribute(attribute, a);
		}
		batch.addAttribute(a);
	}
	
	private void addUniqueIdentifier(Element attribute, Attribute a){
		String uid = gui.ucc.getSelectedUID();
		String value;
		if ((uid.equals("Default"))) {	// get value from XML
			value = getTextFromElement(attribute, "value");	
		} else {	// get value from GUI
			value = uid;
		}
		a.setValue(value, null);
	}
	
	private void addAsynchronousCorrelationValue(Element attribute, Attribute a){
		String value;
		if ((gui.ucc.getSelectedAsynchronousCorrelationValue().equals("Default"))) {	// get value from XML
			value = getTextFromElement(attribute, "value");
		} else {	
			value = gui.ucc.getSelectedAsynchronousCorrelationValue();
		}
		a.setValue(value, null);
	}
	
	private void addLink(Element attribute, Attribute a){
		String uid = gui.ucc.getSelectedUID();
		String value;
		if ((uid.equals("Default"))) {	// get value from XML
			value = getTextFromElement(attribute, "value");	
		} else {	
			value = getUIDValueFromGUI();
		}
		addComplexValue(a, attribute);
		a.setValue(value, "Linked Object Identifier");
	}
	

	private String getUIDValueFromGUI() {
		String value;
		if(hasTwoUIDs){
			value = gui.ucc.getSecondUID();
		}else{
			value = gui.ucc.getSelectedUID();
		}
		return value;
	}


	private void addOtherAttribute(Element attribute, Attribute a){
		String value = getTextFromElement(attribute, "value");
		if (!value.equals("")) {
			if (a.getAttributeType() == EnumType.Structure) {
				addComplexValue(a, attribute);
			} else {
				a.setValue(value, null);
			}
		}
	}
	

	private void addComplexValue(Attribute a, Element attribute) {
		Element values = getElement(attribute, "values");
		NodeList valueNameList = getNodeList(values, "valuename");
		NodeList valueList = getNodeList(values, "value");
		for(int i = 0; i < valueList.getLength(); i++){
			String value = getTextFromNode(valueList.item(i));
			String valueName = getTextFromNode(valueNameList.item(i));
			a.setValue(value, valueName);
		}	
	}


	private void addTemplateAttributeStructuresToBatch(Element batchElement, KMIPBatch batch){
		NodeList tasList = getNodeList(batchElement, "templateattributestructure");
		for(int i = 0; i < tasList.getLength(); i++){
			Element tasElement = (Element) tasList.item(i);
			addTemplateAttributeStructure(tasElement, batch);
		}	
	}
	
		
	private void addTemplateAttributeStructure(Element tasElement, KMIPBatch batch){
		if(tasElement != null){
			String tasType = getTextFromElement(tasElement, "type");
			TemplateAttributeStructure tas = createTemplateAttributeStructureInstance(tasType);
			addNameToTemplateAttribute(tas, tasElement);
			addAttributesToTemplateAttribute(tas, tasElement);
			batch.addTemplateAttributeStructure(tas);
		}
	}
	
	
	private void addNameToTemplateAttribute(TemplateAttributeStructure tas, Element tasElement) {
		NodeList templateAttribList = getNodeList(tasElement, "nameattribute");
		int templateAttributesCount = templateAttribList.getLength();		
		for (int j = 0; j < templateAttributesCount; j++) {
			Element attribute = (Element) templateAttribList.item(j);
			Name name = new Name();
			addComplexValue(name, attribute);
			tas.addName(name);
		}
	}


	private void addAttributesToTemplateAttribute(TemplateAttributeStructure tas, Element templateAttributeElement) {
		NodeList templateAttribList = getNodeList(templateAttributeElement, "attribute");
		int templateAttributesCount = templateAttribList.getLength();		
		for (int j = 0; j < templateAttributesCount; j++) {
			Element attributeElement = (Element) templateAttribList.item(j);
			tas.addAttribute(getAttributeFromElement(attributeElement));
		}
	}
	

	private void addTemplateToBatch(Element batchElement, KMIPBatch batch) {
		Element templateElement = getElement(batchElement, "template");
		if(templateElement != null){
			Template template = new Template();
			NodeList attribList = getNodeList(templateElement, "attribute");
			int attribListCount = attribList.getLength();
			for(int i = 0; i < attribListCount; i++){
				Element attributeElement = (Element) attribList.item(i);
				template.addAttribute(getAttributeFromElement(attributeElement));
			}
			batch.setManagedObject(template);
		}
	}
	

	private void addSecretDataToBatch(Element batchElement, KMIPBatch batch) {
		Element secretDataElement = getElement(batchElement, "secretdata");
		if(secretDataElement != null){
			SecretData secretData = new SecretData();
			batch.setManagedObject(secretData);
			String secretDataType = getTextFromElement(secretDataElement, "secretdatatype");
			secretData.setSecretDataType(new EnumSecretDataType(parseInt(secretDataType)));
			Element keyBlockElement = getElement(secretDataElement, "keyblock");
			secretData.setKeyBlock(getKeyBlock(keyBlockElement));
		}
	}

	
	private void addSymmetricKeyToBatch(Element batchElement, KMIPBatch batch) {
		Element symmetricKeyElement = getElement(batchElement, "symmetrickey");
		if(symmetricKeyElement != null){
			SymmetricKey symmetricKey = new SymmetricKey();
			batch.setManagedObject(symmetricKey);
			Element keyBlockElement = getElement(symmetricKeyElement, "keyblock");
			symmetricKey.setKeyBlock(getKeyBlock(keyBlockElement));
		}
	}
	
	private void addPrivateKeyToBatch(Element batchElement, KMIPBatch batch) {
		Element privateKeyElement = getElement(batchElement, "privatekey");
		if(privateKeyElement != null){
			PrivateKey privateKey = new PrivateKey();
			batch.setManagedObject(privateKey);
			Element keyBlockElement = getElement(privateKeyElement, "keyblock");
			privateKey.setKeyBlock(getKeyBlock(keyBlockElement));
		}
	}
	
	private void addPublicKeyToBatch(Element batchElement, KMIPBatch batch) {
		Element publicKeyElement = getElement(batchElement, "publickey");
		if(publicKeyElement != null){
			PublicKey publicKey = new PublicKey();
			batch.setManagedObject(publicKey);
			Element keyBlockElement = getElement(publicKeyElement, "keyblock");
			publicKey.setKeyBlock(getKeyBlock(keyBlockElement));
		}
	}
	
	private KeyBlock getKeyBlock(Element keyBlockElement) {
		KeyBlock keyBlock = new KeyBlock();

		// required
		addKeyFormatTypeToKeyBlock(keyBlock, keyBlockElement);
		addKeyValueToKeyBlock(keyBlock, keyBlockElement);
		addAttributesToKeyBlock(keyBlock, keyBlockElement);
		
		// optional
		addKeyCompressionTypeToKeyBlockIfExists(keyBlock, keyBlockElement);
		addKeyWrappingDataToKeyBlockIfExists(keyBlock, keyBlockElement);
	
		return keyBlock;
	}
	
	private void addKeyFormatTypeToKeyBlock(KeyBlock keyBlock, Element keyBlockElement) {
		String keyFormatType = getTextFromElement(keyBlockElement, "keyformattype");
		keyBlock.setKeyFormatType(keyFormatType);
	}

	private void addKeyValueToKeyBlock(KeyBlock keyBlock, Element keyBlockElement) {
		Element keyValueElement = getElement(keyBlockElement, "keyvalue");
		keyBlock.setKeyValue(getKeyValue(keyValueElement));
	}
	
	private void addAttributesToKeyBlock(KeyBlock keyBlock, Element keyBlockElement) {
		NodeList attribList = getNodeList(keyBlockElement, "attribute");
		int attribListCount = attribList.getLength();
		for(int i = 0; i < attribListCount; i++){
			Element attributeElement = (Element) attribList.item(i);
			keyBlock.addAttribute(getAttributeFromElement(attributeElement));
		}
	}
	
	private void addKeyCompressionTypeToKeyBlockIfExists(KeyBlock keyBlock, Element keyBlockElement) {
		Element keyCompressionTypeElement = getElement(keyBlockElement, "keycompressiontype");
		if(keyCompressionTypeElement != null){
			String keyCompressionType = getTextFromElement(keyCompressionTypeElement, "keycompressiontype");
			keyBlock.setKeyCompressionType(keyCompressionType);
		}
	}
	
	private void addKeyWrappingDataToKeyBlockIfExists(KeyBlock keyBlock, Element keyBlockElement) {
		Element keyWrappingDataElement = getElement(keyBlockElement, "keywrappingdata");

		if(keyWrappingDataElement != null){
			keyBlock.setKeyWrappingData(getKeyWrappingData(keyWrappingDataElement));
		}
	}
	
	private KeyValue getKeyValue(Element keyValueElement) {
		KeyValue keyValue = new KeyValue();
	
		// optional
		addAttributesToKeyValue(keyValue, keyValueElement);
		
		// required
		String keyMaterial = getTextFromElement(keyValueElement, "keymaterial");
		keyValue.setKeyMaterial(new KeyMaterial(keyMaterial));
		
		return keyValue;
	}
	
	private void addAttributesToKeyValue(KeyValue keyValue, Element keyValueElement) {
		NodeList attribList = getNodeList(keyValueElement, "attribute");
		int attribListCount = attribList.getLength();
		for(int i = 0; i < attribListCount; i++){
			Element attributeElement = (Element) attribList.item(i);
			keyValue.addAttribute(getAttributeFromElement(attributeElement));
		}
	}
	
	private KeyWrappingData getKeyWrappingData(Element keyWrappingDataElement) {
		// TODO implementieren
		return null;
	}
	
	
	
	
	
	
	
	// Support Methods
	
	private Element getElement(Node node, String tagName){
		return (Element) ((Element)node).getElementsByTagName(tagName).item(0);
	}
	
	private Element getElement(Element e, String tagName){
		return (Element) e.getElementsByTagName(tagName).item(0);
	}
	
	private NodeList getNodeList(Element e, String tagName){
		return e.getElementsByTagName(tagName);
	}
	
	private String getTextFromElement(Element element, String name){
		if(name.equals("value")){
			return element.getElementsByTagName(name).item(0).getTextContent();
		} else{
			return element.getElementsByTagName(name).item(0).getTextContent().replaceAll("\\s", "");
		}
	}
	
	private String getTextFromNode(Node node){
		return ((Element) node).getTextContent().replaceAll("\\s","");
	}
	
	private int parseInt(String value){
        if(value.length() > 1 && value.substring(0,2).equals("0x")){
        	return Integer.parseInt(value.substring(2),16);
	     }
	     else{
	    	 return Integer.parseInt(value);          
	     }
	}
	
	private String getClassName(Element attribute) {
		String className = getTextFromElement(attribute, "name").replaceAll("-", "");
		return Character.toUpperCase(className.charAt(0)) + className.substring(1); 
	}
	
	private Attribute createAttributeInstance(String className){
		try{
			return (Attribute) Class.forName("ch.ntb.inf.kmip.attributes." + className).newInstance();
		} catch(Exception e){
			try{
				return (Attribute) Class.forName("ch.ntb.inf.kmip.operationparameters." + className).newInstance();
			} catch(Exception e2){
				e2.printStackTrace();
			}
		}
		return null;
	}
	
	private TemplateAttributeStructure createTemplateAttributeStructureInstance(String className){
		try{
			return (TemplateAttributeStructure) Class.forName("ch.ntb.inf.kmip.objects.base." + className).newInstance();
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private Attribute getAttributeFromElement(Element attributeElement){
		String name = getTextFromElement(attributeElement, "name");
		Attribute a = createAttributeInstance(name);

		if(a.getAttributeType() == EnumType.Structure){
			addComplexValue(a, attributeElement);
			if (a instanceof Link && !gui.ucc.getSelectedUID().equals("Default")){
				setLinkedObjectIdentifier(a, attributeElement);
			}
		} else{
			String value = getTextFromElement(attributeElement, "value");
			a.setValue(value, null);
		}
		return a;
	}


	private void setLinkedObjectIdentifier(Attribute a, Element attributeElement) {
		String value;
		if(hasTwoUIDs){
			value = gui.ucc.getSecondUID();
		} else{
			value = gui.ucc.getSelectedUID();
		}
		a.setValue(value, "Linked Object Identifier");
	}

}
