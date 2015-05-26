/**
 * KMIPContainer.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * The class KMIPContainer encapsulates the object based structure 
 * of a KMIP message and offers methods to assemble a KMIP message 
 * with the corresponding objects and attributes. The container is 
 * structured according to the KMIP message format defined in the 
 * specification. This includes the header informations and an 
 * ArrayList of KMIP-Batches for multiple Batch Items.
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

import ch.ntb.inf.kmip.kmipenum.EnumBatchError;
import ch.ntb.inf.kmip.objects.Authentication;
import ch.ntb.inf.kmip.types.KMIPBoolean;
import ch.ntb.inf.kmip.types.KMIPDateTime;
import ch.ntb.inf.kmip.types.KMIPInteger;

/**
 * The class KMIPContainer encapsulates the object based structure 
 * of a KMIP message and offers methods to assemble a KMIP message 
 * with the corresponding objects and attributes. The container is 
 * structured according to the KMIP message format defined in the 
 * specification. This includes the header informations and an 
 * ArrayList of KMIP-Batches for multiple Batch Items.
 */
public class KMIPContainer {

	/* Header Objects */	
	// required in Response- and Request-Message
	private KMIPInteger batchCount;
	private ArrayList<KMIPBatch> batches;
	
	// optional in Request-Message, required in Response-Message
	private KMIPDateTime timeStamp;
		
	// optional in Request-Message
	private KMIPInteger maximumResponseSize;
	private KMIPBoolean asynchronousIndicator;
	private EnumBatchError batchErrorContinuationOption;
	private KMIPBoolean batchOrderOption;
	private Authentication authentication;
	
	/**
	 * Instantiates a KMIPContainer, initializes an empty 
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code> and sets
	 * the Batch-Count to zero. 
	 */
	public KMIPContainer(){
		batches = new ArrayList<KMIPBatch>();
		batchCount = new KMIPInteger(0);
	}
	
	/**
	 * Adds a Batch-Item to the <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>. 
	 * @param batch :     	the <code>KMIPBatch</code> to be added.
	 */
	public void addBatch(KMIPBatch batch) {
		batches.add(batch);
	}
	
	/**
	 * Instantiates the elements of the <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>
	 * with the number of new KMIPBatches, defined in the Batch-count
	 * @param batchCount :     	the number of Batch-Items in the KMIP-Message, defined as 
	 * 							<code>KMIPInteger</code>
	 */
	public void createBatches(int batchCount) {
		this.batchCount = new KMIPInteger(batchCount);
		for(int i = 0; i < batchCount; i++){
			batches.add(new KMIPBatch());
		}
	}
	
	/**
	 * Sets one of the following Header Options:
	 * <ul>
	 * 	<li>Maximum Response Size</li>
	 * 	<li>Asynchronous Indicator</li>
	 * 	<li>Batch Error Continuation Option</li>
	 * 	<li>Batch Order Option</li>
	 * 	<li>Time Stamp</li>
	 * </ul>
	 * @param name :     	the option name without white-spaces as a <code>String</code> (e.g.: "TimeStamp")
	 * @param value :     	the option value as <code>String</code> (e.g.: "TRUE")
	 */
	public void setOption(String name, String value){
		if(name.equals("MaximumResponseSize")){
			this.maximumResponseSize = new KMIPInteger(value);
		} else if(name.equals("AsynchronousIndicator")){
			this.asynchronousIndicator = new KMIPBoolean(value);
		} else if(name.equals("BatchErrorContinuationOption")){
			this.batchErrorContinuationOption = new EnumBatchError(value);
		} else if(name.equals("BatchOrderOption")){
			this.batchOrderOption = new KMIPBoolean(value);
		} else if(name.equals("TimeStamp")){
			this.timeStamp = new KMIPDateTime(value);
		}
	}
	
	/**
	 * Sets the Batch Count in the container to the number of batches in the
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>
	 */
	public void calculateBatchCount() {
		this.batchCount.setValue(batches.size());
	}
	
	// Getters & Setters
	
	/**
	 * Returns the Time Stamp as KMIPDateTime.
	 * @return	<code>KMIPDateTime</code>
	 */
	public KMIPDateTime getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Returns the Maximum Response Size as KMIPInteger.
	 * @return	<code>KMIPInteger</code>
	 */
	public KMIPInteger getMaximumResponseSize() {
		return maximumResponseSize;
	}

	/**
	 * Sets the Maximum Response Size to the transfered KMIPInteger value.
	 * @param maximumResponseSize :     	the <code>KMIPInteger</code> to be set.
	 */
	public void setMaximumResponseSize(KMIPInteger maximumResponseSize) {
		this.maximumResponseSize = maximumResponseSize;
	}

	/**
	 * Returns the Asynchronous Indicator as KMIPBoolean.
	 * @return 	<code>KMIPBoolean</code>
	 */
	public KMIPBoolean getAsynchronousIndicator() {
		return asynchronousIndicator;
	}

	/**
	 * Sets the Asynchronous Indicator to the transfered KMIPBoolean value.
	 * @param asynchronousIndicator :    	the <code>KMIPBoolean</code> to be set.
	 */
	public void setAsynchronousIndicator(KMIPBoolean asynchronousIndicator) {
		this.asynchronousIndicator = asynchronousIndicator;
	}

	/**
	 * Returns the Batch Error Continuation Option as concrete KMIPEnum, EnumBatchError. 
	 * @return	<code>EnumBatchError</code>
	 */
	public EnumBatchError getBatchErrorContinuationOption() {
		return batchErrorContinuationOption;
	}

	/**
	 * Sets the Batch Error Continuation Option to the transfered EnumBatchError value.
	 * @param batchErrorContinuationOption :     	the <code>EnumBatchError</code> to be set.
	 */
	public void setBatchErrorContinuationOption(EnumBatchError batchErrorContinuationOption) {
		this.batchErrorContinuationOption = batchErrorContinuationOption;
	}

	/**
	 * Returns the Batch Order Option as KMIPBoolean.
	 * @return	<code>KMIPBoolean</code>
	 */
	public KMIPBoolean getBatchOrderOption() {
		return batchOrderOption;
	}

	/**
	 * Sets the Batch Order Option to the transfered KMIPBoolean value.
	 * @param batchOrderOption :    	the <code>KMIPBoolean</code> to be set.
	 */
	public void setBatchOrderOption(KMIPBoolean batchOrderOption) {
		this.batchOrderOption = batchOrderOption;
	}

	/**
	 * Returns the Authentication object of the container.
	 * @return <code>Authentication</code>
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * Sets the Authentication to the transfered Authentication object.
	 * @param authentication :     	the <code>Authentication</code> to be set.
	 */
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * Sets the Time Stamp to the transfered KMIPDateTime value.
	 * @param timeStamp :     	the <code>KMIPDateTime</code> to be set.
	 */
	public void setTimeStamp(KMIPDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Sets the Batch Count to the transfered <code>int</code> value
	 * @param batchCount :     	the <code>int</code> to be set.
	 */
	public void setBatchCount(int batchCount) {
		this.batchCount.setValue(batchCount);
	}
	
	/**
	 * Returns the Batch Count as KMIPInteger.
	 * @return	<code>KMIPInteger</code>
	 */
	public KMIPInteger getBatchCountAsKMIPInteger() {
		return batchCount;
	} 
	
	/**
	 * Returns the Batch Count as <code>int</code>
	 * @return	<code>int</code>
	 */
	public int getBatchCount() {
		return batchCount.getValue();
	}
	
	/**
	 * Sets the Batch Count to the transfered KMIPInteger value.
	 * @param batchCount :     	the <code>KMIPInteger</code> to be set.
	 */
	public void setBatchCount(KMIPInteger batchCount) {
		this.batchCount = batchCount;
	}

	/**
	 * Returns the KMIPBatch from the 
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code> at an index i.
	 * @param i :     	the index as <code>int</code>
	 * @return			<code>KMIPBatch</code> at index i of the 
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>
	 */
	public KMIPBatch getBatch(int i) {
		return batches.get(i);
	}
		
	/**
	 * Returns all Batch Items collected in an
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>.
	 * @return	<code>ArrayList{@literal <}KMIPBatch{@literal >}</code>
	 */
	public ArrayList<KMIPBatch> getBatches() {
		return batches;
	}

	/**
	 * Sets all Batch Items collected in an 
	 * <code>ArrayList{@literal <}KMIPBatch{@literal >}</code>
	 * @param batches :     	the <code>ArrayList{@literal <}KMIPBatch{@literal >}</code> to be set.
	 */
	public void setBatches(ArrayList<KMIPBatch> batches) {
		this.batches = batches;
	}

	// has Methods
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Time Stamp field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasTimeStamp(){
		if(this.timeStamp != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Maximum Response Size field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasMaximumResponseSize(){
		if(this.maximumResponseSize != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Asynchronous Indicator field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasAsynchronousIndicator(){
		if(this.asynchronousIndicator != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Batch Error Continuation Option field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasBatchErrorContinuationOption(){
		if(this.batchErrorContinuationOption != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Batch Order Option field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasBatchOrderOption(){
		if(this.batchOrderOption != null)
			return true;
		return false;
	}
	
	/**
	 * Checks whether the qualified field is <code>null</code> or not.
	 * @return 
	 * <ul>
	 * 	<li>True:  if the Authentication field of the container has a reference.</li>
	 * 	<li>False: if it is <code>null</code>.</li>
	 * </ul>
	 */
	public boolean hasAuthentication(){
		if(this.authentication != null)
			return true;
		return false;
	}
	
	// toString Method
	/**
	 * Generates a <code>String</code> containing the whole content of the KMIPContainer.
	 * @return	a formatted <code>String</code>
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("batchCount =\t"+ batchCount + "\ntimeStamp =\t" + timeStamp);
		for(KMIPBatch b : batches){
			sb.append(b.toString());
		}
		
		if(hasMaximumResponseSize()){
			sb.append("\nMaximumResponseSize = " + maximumResponseSize.getValueString());	
		}
		
		if(hasBatchErrorContinuationOption()){
			sb.append("\nBatchErrorContinuationOption = " + batchErrorContinuationOption.getValueString());	
		}
		
		if(hasBatchOrderOption()){
			sb.append("\nBatchOrderOption = " + batchOrderOption.getValueString());	
		}
		
		if(hasAuthentication()){
			sb.append(authentication.toString());	
		}
		
		return  sb.toString();
	}

}
