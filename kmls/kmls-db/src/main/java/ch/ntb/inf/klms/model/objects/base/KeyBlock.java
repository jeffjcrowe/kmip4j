/**
 * KeyBlock.java
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

package ch.ntb.inf.klms.model.objects.base;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.defaultvalues.DefaultValues;
import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.Digest;

@Entity
public class KeyBlock {

	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	@ManyToOne ( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private CryptographicLength cryptographicLength;
	
	@ManyToOne ( cascade = {CascadeType.PERSIST,  CascadeType.MERGE, CascadeType.REFRESH})
	private CryptographicAlgorithm cryptographicAlgorithm;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private KeyValue keyValue;
	
	private String keyCompressionType;
	
	private String keyFormatType;
	
	/**
	 * Key Wrapping Data not supported in this KLMS
	 */
//	@ManyToOne ( cascade = CascadeType.ALL)
//	private KeyWrappingData keyWrappingData;

	
	public KeyBlock(){	}
	
	public KeyBlock(HashMap<String, String> parameters, Digest digest) throws NoSuchAlgorithmException {
		super();
		
		if(parameters.containsKey("Key Format Type")){
			this.keyFormatType = parameters.get("Key Format Type" + "value" + "1");
		} else{
			this.keyFormatType = Integer.toString(DefaultValues.KEY_FORMAT_TYPE);
		}

		if(parameters.containsKey("Cryptographic Algorithm")){
			this.cryptographicAlgorithm = new CryptographicAlgorithm(parameters.get("Cryptographic Algorithm" + "value" + "1") );
		} else {
			this.cryptographicAlgorithm = new CryptographicAlgorithm();
		}
		
		if(parameters.containsKey("Cryptographic Length")){
			this.cryptographicLength = new CryptographicLength(parameters.get("Cryptographic Length"+ "value" + "1"));
		} else {
			this.cryptographicLength = new CryptographicLength();
		}

		if(parameters.containsKey("Key Value")){
			this.keyValue = new KeyValue(parameters, digest); 
		} else{
			this.keyValue = new KeyValue(cryptographicAlgorithm, cryptographicLength, digest); 
		}
	}
	
	public KeyBlock(HashMap<String, String> parameters) {
		super();

		if(parameters.containsKey("Key Format Type" + "value" + "1")){
			this.keyFormatType = parameters.get("Key Format Type");
		} else{
			this.keyFormatType = Integer.toString(DefaultValues.KEY_FORMAT_TYPE);
		}
		this.keyValue = new KeyValue(parameters); 
	}


	public KeyBlock(CryptographicAlgorithm ca, CryptographicLength len, byte[] keyMaterial, Digest digest) {
		this.keyFormatType = Integer.toString(DefaultValues.KEY_FORMAT_TYPE);
		this.cryptographicAlgorithm = ca;
		this.cryptographicLength = len;
		this.keyValue = new KeyValue(keyMaterial, digest);
	}

	
	
	
	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		if(hasCryptographicLength()){
			attributes.add(cryptographicLength);
		}
		if(hasCryptographicAlgorithm()){
			attributes.add(cryptographicAlgorithm);
		}
		return attributes;
	}
	
	public void setAttribute(Attribute a) {
		if(a instanceof CryptographicLength){
			this.cryptographicLength = (CryptographicLength) a;
		}
		else if(a instanceof CryptographicAlgorithm){
			this.cryptographicAlgorithm = (CryptographicAlgorithm) a;
		}
	}

	public KeyValue getKeyValue() {
		return keyValue;
	}

	public String getKeyCompressionType() {
		return keyCompressionType;
	}

	public String getKeyFormatType() {
		return keyFormatType;
	}

//	public KeyWrappingData getKeyWrappingData() {
//		return keyWrappingData;
//	}
	
	public void setKeyValue(KeyValue keyValue) {
		this.keyValue = keyValue;
	}

	public void setKeyCompressionType(String keyCompressionType) {
		this.keyCompressionType = keyCompressionType;
	}

	public void setKeyFormatType(String keyFormatType) {
		this.keyFormatType = keyFormatType;
	}

//	public void setKeyWrappingData(KeyWrappingData keyWrappingData) {
//		this.keyWrappingData = keyWrappingData;
//	}

	
	
	
	public boolean hasCryptographicAlgorithm(){
		if(this.cryptographicAlgorithm != null){
			return true;
		}
		return false;
	}
	
	public boolean hasCryptographicLength(){
		if(this.cryptographicLength != null){
			return true;
		}
		return false;
	}
	
	public boolean hasKeyCompressionType(){
		if(this.keyCompressionType != null){
			return true;
		}
		return false;
	}
	
	public boolean hasKeyFormatType(){
		if(this.keyFormatType != null){
			return true;
		}
		return false;
	}
	
//	public boolean hasKeyWrappingData(){
//		if(this.keyWrappingData != null){
//			return true;
//		}
//		return false;
//	}
	
	

	public KeyBlock renew(CryptographicAlgorithm ca, CryptographicLength cl, Digest digest) throws NoSuchAlgorithmException {
		KeyBlock newKeyBlock = new KeyBlock();
		CryptographicAlgorithm cryptographicAlgorithm = ca;
		CryptographicLength cryptographicLength = cl;
		
		if(hasCryptographicLength()){
			newKeyBlock.setAttribute(cryptographicLength);
			cryptographicLength = this.cryptographicLength;
		}
		if(hasCryptographicAlgorithm()){
			newKeyBlock.setAttribute(cryptographicAlgorithm);
			cryptographicAlgorithm = this.cryptographicAlgorithm;
		}
		if(hasKeyCompressionType()){
			newKeyBlock.setKeyCompressionType(keyCompressionType);
		}
		if(hasKeyFormatType()){
			newKeyBlock.setKeyFormatType(keyFormatType);
		}
//		if(hasKeyWrappingData()){
//			newKeyBlock.setKeyWrappingData(keyWrappingData);
//		}
		
		newKeyBlock.setKeyValue(this.keyValue.renew(cryptographicAlgorithm, cryptographicLength, digest));

		return newKeyBlock;
	}
	
	
	
}
