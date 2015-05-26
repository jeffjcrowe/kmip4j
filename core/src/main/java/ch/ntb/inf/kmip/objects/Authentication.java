/**
 * Authentication.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This is used to authenticate the requester. It is an OPTIONAL 
 * information item, depending on the type of request being issued 
 * and on server policies. The Authentication structure SHALL 
 * contain a Credential structure.
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

import ch.ntb.inf.kmip.objects.base.Credential;


public class Authentication {

	private Credential credential;

	public Authentication(Credential credential) {
		this.credential = credential;
	}

	
	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Authentication =\t");
		sb.append("\nCredential = " + credential.toString());
		return  sb.toString();
	}
	
	
}
