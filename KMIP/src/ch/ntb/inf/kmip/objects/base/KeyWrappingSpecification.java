/**
 * KeyWrappingSpecification.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This is a separate structure that is defined for operations that 
 * provide the option to return wrapped keys.
 * 
 * This structure contains: 
 * - A Wrapping Method that indicates the method used to wrap the 
 * 	 Key Value. 
 * - Encryption Key Information with the Unique Identifier value of 
 * 	 the encryption key and associated cryptographic parameters.
 * - MAC/Signature Key Information with the Unique Identifier value 
 * 	 of the MAC/signature key and associated cryptographic 
 * 	 parameters.
 * - Zero or more Attribute Names to indicate the attributes to be 
 * 	 wrapped with the key material.
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

package ch.ntb.inf.kmip.objects.base;

import java.util.ArrayList;

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumWrappingMethod;
import ch.ntb.inf.kmip.objects.EncryptionKeyInformation;
import ch.ntb.inf.kmip.objects.MACorSignatureKeyInformation;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class KeyWrappingSpecification extends BaseObject {

	private EnumWrappingMethod wrappingMethod;
	private EncryptionKeyInformation encryptionKeyInformation;
	private MACorSignatureKeyInformation macSignatureKeyInformation;
	private ArrayList<KMIPTextString> names;

	public KeyWrappingSpecification(){
		super(new EnumTag(EnumTag.KeyWrappingSpecification));
	}
		
	// Getters & Setters
	public EnumWrappingMethod getWrappingMethod() {
		return wrappingMethod;
	}

	public void setWrappingMethod(EnumWrappingMethod wrappingMethod) {
		this.wrappingMethod = wrappingMethod;
	}
	
	public EncryptionKeyInformation getEncryptionKeyInformation() {
		return encryptionKeyInformation;
	}

	public void setEncryptionKeyInformation(EncryptionKeyInformation encryptionKeyInformation) {
		this.encryptionKeyInformation = encryptionKeyInformation;
	}

	public MACorSignatureKeyInformation getMacSignatureKeyInformation() {
		return macSignatureKeyInformation;
	}

	public void setMacSignatureKeyInformation(MACorSignatureKeyInformation macSignatureKeyInformation) {
		this.macSignatureKeyInformation = macSignatureKeyInformation;
	}

	public ArrayList<KMIPTextString> getNames() {
		return names;
	}

	public void setNames(ArrayList<KMIPTextString> names) {
		this.names = names;
	}
	
	public void setName(KMIPTextString name) {
		if(this.names == null){
			this.names = new ArrayList<KMIPTextString>();
		}
		this.names.add(name);
	}
	
	

	
	public boolean hasEncryptionKeyInformation(){
		if(this.encryptionKeyInformation != null){
			return true;
		}
		return false;
	}
	
	public boolean hasMACSignatureKeyInformation(){
		if(this.macSignatureKeyInformation != null){
			return true;
		}
		return false;
	}
	
	public boolean hasAttributeNames(){
		if(this.names != null){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Key Wrapping Specification");
		sb.append("\nWrapping Method"+ this.wrappingMethod.getValueString());

		
		if(hasEncryptionKeyInformation()){
			sb.append("\n"+ this.encryptionKeyInformation.toString());
		}
		
		if(hasMACSignatureKeyInformation()){
			sb.append("\n"+ this.macSignatureKeyInformation.toString());
		}
		
		if(hasAttributeNames()){
			sb.append("\nAttribute Names: ");
			for(KMIPTextString s : names){
				sb.append("\nAttribute Name: "+ s.getValueString());
			}
		
		}
	
		return sb.toString();
	}
}
