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
package ch.ntb.inf.klms.model.objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class CredentialValue {

	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	
	private String username;
	private String password;


	public CredentialValue(){
		
	}
	
	public CredentialValue(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	
	
	public void setUsername(String value) {
		this.username = new String(value);
	}
	

	public void setPassword(String value) {
		this.password = new String(value);
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean equals(CredentialValue otherCredentialValue){
		if(this.username.equals(otherCredentialValue.getUsername())){
			if(this.password.equals(otherCredentialValue.getPassword())){
				return true;
			}
		}
		return false;
	}
	
}
