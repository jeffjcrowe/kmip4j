/**
 * KMIPBatch.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * This class holds the individual requests or responses in a batch 
 * and offers methods to assemble an appropriate Batch Item with 
 * the corresponding objects and attributes. The KMIPBatch is 
 * structured according to the KMIP message format defined in the 
 * specification.
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
package ch.ntb.inf.kmip.container;

import java.util.ArrayList;

import ch.ntb.inf.kmip.attributes.UniqueIdentifier;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.kmipenum.EnumResultReason;
import ch.ntb.inf.kmip.kmipenum.EnumResultStatus;
import ch.ntb.inf.kmip.objects.MessageExtension;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.KeyWrappingSpecification;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.managed.ManagedObject;
import ch.ntb.inf.kmip.operationparameters.AsynchronousCorrelationValue;
import ch.ntb.inf.kmip.operationparameters.DerivationParameters;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

/**
 * This class holds the individual requests or responses in a batch 
 * and offers methods to assemble an appropriate Batch Item with 
 * the corresponding objects and attributes. The KMIPBatch is 
 * structured according to the KMIP message format defined in the 
 * specification.
 */
public class KMIPBatch {

	/* Batch Item objects */
	
	// required in request and response
	private EnumOperation operation;
	
	// required Payload fields for request and response
	private ArrayList<Attribute> attributes;
	private ArrayList<TemplateAttributeStructure> templateAttributeStructures;
	private ArrayList<KMIPType> kmipTypes;
	private ManagedObject managedObject;
	
	// required only in response
	private EnumResultStatus resultStatus;
	
	// optional operation parameters, depending on the operation
	private DerivationParameters derivationParameters;
	private KeyWrappingSpecification keyWrappingSpecification;
	
	// optional in request and response
	private KMIPByteString uniqueBatchItemID;
	private MessageExtension messageExtension;
	
	// optional only in response
	private EnumResultReason resultReason; 
	private KMIPTextString resultMessage;
	private KMIPByteString asynchronousCorrelationValue;
	
	
	/**
	 * This constructor...
	 * <ul>
	 * 	<li>
	 * 		instantiates a KMIPBatch
	 * 	</li> 
	 * 	<li>
	 * 		initializes the field <code>attributes</code> with an empty 
	 * 		<code>ArrayList{@literal <}Attribute{@literal >}</code>
	 * 	</li> 
	 * 	<li>
	 * 		initializes the field 
	 * 		<code>templateAttributeStructures</code> with an 
	 * 		<code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code>
	 * 	</li>
	 * </ul>
	 */
	public KMIPBatch(){
		attributes = new ArrayList<Attribute>();
		templateAttributeStructures = new ArrayList<TemplateAttributeStructure>();
	}


	/**
	 * Adds an Attribute to the <code>ArrayList{@literal <}Attribute{@literal >}</code>.
	 * @param a :     	the <code>Attribute</code> to be added.
	 */
	public void addAttribute(Attribute a){
		this.attributes.add(a);
	}
	
	/**
	 * Adds all attributes from an <code>ArrayList{@literal <}Attribute{@literal >}</code>
	 * to the one of the batch.
	 * @param attributes :     	the <code>ArrayList{@literal <}Attribute{@literal >}</code>
	 * to be added all to the field attributes of the KMIPBatch.
	 */
	public void addAttributes(ArrayList<Attribute> attributes){
		this.attributes.addAll(attributes);
	}
	
	/**
	 * Adds a Template Attribute Structure to the 
	 * <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code> of the
	 * KMIPBatch.
	 * @param tas :     	the TAS to be added.
	 */
	public void addTemplateAttributeStructure(TemplateAttributeStructure tas){
		templateAttributeStructures.add(tas);
	}
	
	/**
	 * Adds a <code>KMIPType</code> to the 
	 * <code>ArrayList{@literal <}KMIPType{@literal >}</code> of the KMIPBatch.
	 * If the field <code>kmipTypes</code> doesn't reference such an ArrayList yet, 
	 * a new instance is generated, before adding the KMIPType.
	 * @param kmipType :     	the <code>KMIPType</code> to be added.
	 */
	public void addKMIPType(KMIPType kmipType) {
		if(this.kmipTypes == null){
			this.kmipTypes = new ArrayList<KMIPType>();
		}
		this.kmipTypes.add(kmipType);
	}
	
	// Getters & Setters
	
	/**
	 * Sets the reference of the field <code>attributes</code> to the transferred 
	 * <code>ArrayList{@literal <}Attribute{@literal >}</code>
	 * @param attributes :     	the 
	 * <code>ArrayList{@literal <}Attribute{@literal >}</code> to be set.
	 */
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
		
	/**
	 * Sets the Managed Object to the transferred ManagedObject value.
	 * @param managedObject :     	the <code>ManagedObject</code> to be set.
	 */
	public void setManagedObject(ManagedObject managedObject) {
		this.managedObject = managedObject;
	}
	
	/**
	 * Returns the Managed Object as ManagedObject.
	 * @return <code>ManagedObject</code>
	 */
	public ManagedObject getManagedObject(){
		return this.managedObject;
	}

	/**
	 * Sets the Operation to the transferred EnumOperation.
	 * @param operation : the <code>EnumOperation</code> to be set.
	 */
	public void setOperation(EnumOperation operation) {
		this.operation = operation;
	}
	
	/**
	 * Sets the Operation to the transferred <code>int</code> value.
	 * @param operation : the <code>int</code> to be set.
	 */
	public void setOperation(int operation) {
		this.operation = new EnumOperation(operation);
	}
	
	/**
	 * Sets the Result Status to the transferred EnumResultStatus.
	 * @param resultStatus : the <code>EnumResultStatus</code> to be set.
	 */
	public void setResultStatus(EnumResultStatus resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	/**
	 * Sets the Result Reason to the transferred EnumResultReason.
	 * @param resultReason : the <code>EnumResultReason</code> to be set.
	 */
	public void setResultReason(EnumResultReason resultReason) {
		this.resultReason = resultReason;
	}

	/**
	 * Returns the Operation as EnumOperation.
	 * @return <code>EnumOperation</code>
	 */
	public EnumOperation getOperation() {
		return this.operation;
	}

	/**
	 * Returns all Attributes as <code>ArrayList{@literal <}Attribute{@literal >}</code>.
	 * @return <code>ArrayList{@literal <}Attribute{@literal >}</code>
	 */
	public ArrayList<Attribute> getAttributes() {
		return this.attributes;
	}
		
	/**
	 * Returns the Template Attribute Structure from the 
	 * <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code> 
	 * at a specified index. 
	 * @param index : the index as <code>int</code>
	 * @return <code>TemplateAttributeStructure</code>
	 */
	public TemplateAttributeStructure getTemplateAttributeStructure(int index){
		return this.templateAttributeStructures.get(index);
	}
	
	/**
	 * Returns all Template Attribute Structures as an 
	 * <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code>.
	 * @return <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code>
	 */
	public ArrayList<TemplateAttributeStructure> getTemplateAttributeStructures(){
		return this.templateAttributeStructures;
	}
	
	/**
	 * Returns the Result Status as EnumResultStatus.
	 * @return <code>EnumResultStatus</code>
	 */
	public EnumResultStatus getResultStatus() {
		return this.resultStatus;
	}
	
	/**
	 * Returns the Result Reason as EnumResultReason.
	 * @return <code>EnumResultReason</code>
	 */
	public EnumResultReason getResultReason() {
		return this.resultReason;
	}
	
	/**
	 * Returns the Unique Batch Item ID as KMIPByteString.
	 * @return <code>KMIPByteString</code>
	 */
	public KMIPByteString getUniqueBatchItemID() {
		return this.uniqueBatchItemID;
	}

	/**
	 * Sets the Unique Batch Item ID the the transferred KMIPByteString.
	 * @param uniqueBatchItemID : the <code>KMIPByteString</code> to be set.
	 */
	public void setUniqueBatchItemID(KMIPByteString uniqueBatchItemID) {
		this.uniqueBatchItemID = uniqueBatchItemID;
	}

	/**
	 * Returns the Message Extension as MessageExtension object.
	 * @return <code>MessageExtension</code>
	 */
	public MessageExtension getMessageExtension() {
		return this.messageExtension;
	}

	/**
	 * Sets the Message Extension to the transferred MessageExtension object.
	 * @param messageExtension : the <code>MessageExtension</code> object to be set.
	 */
	public void setMessageExtension(MessageExtension messageExtension) {
		this.messageExtension = messageExtension;
	}
	
	/**
	 * Returns the Result Message as KMIPTextString.
	 * @return <code>KMIPTextString</code>
	 */
	public KMIPTextString getResultMessage() {
		return this.resultMessage;
	}

	/**
	 * Sets the Result Message to the transferred KMIPTextString
	 * @param resultMessage : the <code>KMIPTextString</code> to be set.
	 */
	public void setResultMessage(KMIPTextString resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * Sets the Result Message to the transferred String.
	 * @param string : the <code>String</code> to be set.
	 */
	public void setResultMessage(String string) {
		this.resultMessage = new KMIPTextString(string);
	}
	
	/**
	 * Returns the Asynchronous Correlation Value as KMIPByteString.
	 * @return <code>KMIPByteString</code>
	 */
	public KMIPByteString getAsynchronousCorrelationValue() {
		return this.asynchronousCorrelationValue;
	}

	/**
	 * Sets the Asynchronous Correlation Value to the transferred KMIPByteString.
	 * @param asynchronousCorrelationValue : the <code>KMIPByteString</code> to be set.
	 */
	public void setAsynchronousCorrelationValue(KMIPByteString asynchronousCorrelationValue) {
		this.asynchronousCorrelationValue = asynchronousCorrelationValue;
	}
	
	/**
	 * Sets the Asynchronous Correlation Value to the transferred String.
	 * @param asynchronousCorrelationValue : the <code>String</code> to be set.
	 */
	public void setAsynchronousCorrelationValue(String asynchronousCorrelationValue) {
		this.asynchronousCorrelationValue = new KMIPByteString(asynchronousCorrelationValue);
	}

	/**
	 * Returns all KMIPTypes as an <code>ArrayList{@literal <}KMIPType{@literal >}</code>.
	 * @return <code>ArrayList{@literal <}KMIPType{@literal >}</code>
	 */
	public ArrayList<KMIPType> getKMIPTypes() {
		return this.kmipTypes;
	}
	
	/**
	 * Sets the reference of the field <code>templateAttributeStructures</code>
	 * to the transferred <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code>.
	 * @param templateAttributeStructures : the 
	 * <code>ArrayList{@literal <}TemplateAttributeStructure{@literal >}</code> to be set.
	 */
	public void setTemplateAttributeStructures(ArrayList<TemplateAttributeStructure> templateAttributeStructures) {
		this.templateAttributeStructures = templateAttributeStructures;
	}

	/**
	 * Sets the reference of the field <code>kmipTypes</code> to the transferred
	 * <code>ArrayList{@literal <}KMIPType{@literal >}</code>.
	 * @param kmipTypes : the <code>ArrayList{@literal <}KMIPType{@literal >}</code>
	 * to be set.
	 */
	public void setKMIPTypes(ArrayList<KMIPType> kmipTypes) {
		this.kmipTypes = kmipTypes;
	}
	
	/**
	 * Returns the Derivation Parameters as DerivationParameters object.
	 * @return <code>DerivationParameters</code>
	 */
	public DerivationParameters getDerivationParameters(){
		return this.derivationParameters;
	}
	
	/**
	 * Sets the Derivation Parameters as DerivationParameters object.
	 * @param derivationParameters : the <code>DerivationParameters</code> to be set.
	 */
	public void setDerivationParameters(DerivationParameters derivationParameters){
		this.derivationParameters = derivationParameters;
	}
	
	/**
	 * Returns the Key Wrapping Specification as KeyWrappingSpecificatoion object.
	 * @return <code>KeyWrappingSpecification</code>
	 */
	public KeyWrappingSpecification getKeyWrappingSpecification() {
		return keyWrappingSpecification;
	}

	/**
	 * Sets the Key Wrapping Specification to the transferred KeyWrappingSpecification object.
	 * @param keyWrappingSpecification : the <code>KeyWrappingSpecification</code> to be set.
	 */
	public void setKeyWrappingSpecification(KeyWrappingSpecification keyWrappingSpecification) {
		this.keyWrappingSpecification = keyWrappingSpecification;
	}
	
	/**
	 * Returns the Asynchronous Correlation Value Attribute as a <code>String</code>.
	 * @return <code>String</code>
	 */
	public String getAsynchronousCorrelationValueAttributeString() {
		for(Attribute a : attributes){
			if(a instanceof AsynchronousCorrelationValue){
				return a.getValues()[0].getValueString();
			}
		}
		return null;
	}


	// has Methods
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>kmipTypes</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasKMIPTypes(){
		if(this.kmipTypes != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>managedObject</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasManagedObject(){
		if(this.managedObject != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>uniqueBatchItemID</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasUniqueBatchItemID(){
		if(this.uniqueBatchItemID != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>messageExtension</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasMessageExtension(){
		if(this.messageExtension != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>resultReason</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasResultReason(){
		if(this.resultReason != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>resultStatus</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasResultStatus(){
		if(this.resultStatus != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>resultMessage</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasResultMessage(){
		if(this.resultMessage != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>asynchronousCorrelationValue</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasAsynchronousCorrelationValue(){
		if(this.asynchronousCorrelationValue != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the Asynchronous Correlation Value exists in the 
	 * <code>ArrayList{@literal <}Attribute{@literal >}</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the attribute <code>AsynchronousCorrelationValue</code> 
	 * 		exists in the <code>ArrayList{@literal <}Attribute{@literal >}</code></li>
	 * 	<li>False: if it doesn't exist.</li>
	 * </ul>
	 */
	public boolean hasAsynchronousCorrelationValueAttribute(){
		for(Attribute a : attributes){
			if(a instanceof AsynchronousCorrelationValue){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>operation</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasOperation() {
		if(this.operation != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>derivationParameters</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasDerivationParameters() {
		if(this.derivationParameters != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the <code>keyWrappingSpecification</code> field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasKeyWrappingSpecification() {
		if(this.keyWrappingSpecification != null){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether there are one and more TemplateAttributeStructures or zero.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the there are one or more <code>TemplateAttributeStructure</code>s.</li>
	 * 	<li>False: if there aren't any.</li>
	 * </ul>
	 */
	public boolean hasTemplateAttributeStructures() {
		if(this.templateAttributeStructures.size() > 0){
			return true;
		}
		return false;
	}
	
	
	// toString Method
	
	/**
	 * Generates a <code>String</code> containing the whole content of the KMIPBatch.
	 * @return	a formatted <code>String</code>
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(hasOperation()){
			sb.append("\n\nOperation =\t" + operation.getKey());
			
			if(operation.getKey().equals("GetAttributeList")){
				if(attributes.size() > 0){
					sb.append("\nAttributes: ");
					for(Attribute a : attributes){
						if(a instanceof UniqueIdentifier){
							if(a.getValues()[0].getValueString() == null){
								sb.append("\t" + a.getAttributeName() + "\n");
							} else{
								sb.append("\t"+a.toString()+"\n");
							}
						} else{
							sb.append("\t" + a.getAttributeName() + "\n");
						}
					}
				}
			} else{
				if(attributes.size() > 0){
					sb.append("\nAttributes: ");
					for(Attribute a : attributes){
						sb.append("\t"+a.toString()+"\n");
					}
				}
			}
		}
		
		if(hasTemplateAttributeStructures()){
			sb.append("\nTemplateAttributeStructures: \t");
			for(TemplateAttributeStructure a : templateAttributeStructures){
				sb.append("\t"+a.toString()+"\n");
			}
		}
		
		if(hasManagedObject()){
			sb.append("\nObject: \t");
			sb.append(managedObject.toString() + "\n");
		}
		
		if(hasKMIPTypes()){
			sb.append("\nKMIPTypes: ");
			for(KMIPType t : kmipTypes){
				sb.append("\t"+t.toString()+"\n");
			}
		}
		
		if(hasResultStatus()){
			sb.append("\nresultStatus =\t" + resultStatus.getKey());	
		}
		
		if(hasResultReason()){
			sb.append("\nresultReason =\t" + resultReason.getKey());	
		}
		
		if(hasResultMessage()){
			sb.append("\nresultMessage =\t" + resultMessage.getValue());	
		}
		
		if(hasAsynchronousCorrelationValue()){
			sb.append("\nAsynchronousCorrelationValue = " + asynchronousCorrelationValue.getValueString());	
		}
		
		if(hasDerivationParameters()){
			sb.append("\n" + derivationParameters.toString());	
		}
		
		if(hasKeyWrappingSpecification()){
			sb.append("\n" + keyWrappingSpecification.toString());	
		}
		
		if(hasUniqueBatchItemID()){
			sb.append("\nUniqueBatchItemID = " + uniqueBatchItemID.getValueString());	
		}
		
		if(hasMessageExtension()){
			sb.append("\n" + messageExtension.toString());	
		}
		return sb.toString();
	}

}
