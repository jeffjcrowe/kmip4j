/**
 * CredentialValue.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * Credential Value is a structure containing a Username and a 
 * Password. The Username field identifies the client, and the 
 * Password field is a secret that authenticates the client.
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
package ch.ntb.inf.kmip.objects;

import ch.ntb.inf.kmip.types.KMIPTextString;


public class CredentialValue {

	private KMIPTextString username;
	private KMIPTextString password;

	public CredentialValue(){
		
	}
	
	public CredentialValue(String username, String password){
		setUsername(username);
		setPassword(password);
	}
	
	public CredentialValue(KMIPTextString username, KMIPTextString password){
		this.username = username;
		this.password = password;
	}

	
	public void setUsername(String value) {
		this.username = new KMIPTextString(value);
	}
	
	public void setUsername(KMIPTextString value) {
		this.username = value;
	}

	public void setPassword(String value) {
		this.password = new KMIPTextString(value);
	}
	
	public void setPassword(KMIPTextString value) {
		this.password = value;
	}

	public KMIPTextString getUsername() {
		return username;
	}

	public KMIPTextString getPassword() {
		return password;
	}
	
	
	
	public boolean hasUsername(){
		if(username != null){
			return true;
		}
		return false;
	}
	
	public boolean hasPassword(){
		if(password != null){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("CredentialValue =\t");
		if(hasUsername()){
			sb.append("\nUsername= " + username.getValueString());	
		}
		if(hasPassword()){
			sb.append("\nPassword= " + password.getValueString());	
		}

		return  sb.toString();
	}
	
}
