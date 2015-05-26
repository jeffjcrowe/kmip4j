/**
 * Credential.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description for class
 * A Credential is a structure used for client identification 
 * purposes and is not managed by the key management system. It MAY 
 * be used for authentication purposes as indicated in KMIP-Profiles.
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

import ch.ntb.inf.kmip.kmipenum.EnumCredentialType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.CredentialValue;

public class Credential extends BaseObject {

	private EnumCredentialType credentialType;
	private CredentialValue credentialValue;
	
	
	
	public Credential(){
		super(new EnumTag(EnumTag.Credential));
		this.credentialValue = new CredentialValue();
	}
	
	public Credential(EnumCredentialType credentialType, CredentialValue credentialValue){
		super(new EnumTag(EnumTag.Credential));
		this.credentialValue = credentialValue;
		this.credentialType = credentialType;
	}
	
	
	public void setValue(String valueName, String value){
		if(valueName.equals("CredentialType")){
			this.credentialType = new EnumCredentialType(value);
		} 
		else if(valueName.equals("Username")){
			credentialValue.setUsername(value);
		} 
		else if(valueName.equals("Password")){
			credentialValue.setPassword(value);
		}
	}


	public EnumCredentialType getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(EnumCredentialType credentialType) {
		this.credentialType = credentialType;
	}

	public CredentialValue getCredentialValue() {
		return credentialValue;
	}

	public void setCredentialValue(CredentialValue credentialValue) {
		this.credentialValue = credentialValue;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\nCredential: ");
		sb.append("\nCredentialType"+ credentialType.getValueString());
		sb.append(credentialValue.toString());
		
		return sb.toString();
	}

}