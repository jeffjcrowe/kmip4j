/**
 * UserPermissionEntry.java
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
package ch.ntb.inf.klms.accesscontrol;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ch.ntb.inf.klms.model.objects.base.Credential;

@Entity
@Table(name = "USERPERMISSIONLIST")
public class UserPermissionEntry {

	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private Credential credential;
	
	private String uid;
	
	
	public UserPermissionEntry(){
	}
	
	public UserPermissionEntry(String uid, Credential credential){
		this.credential = credential;
		this.uid = uid;
	}
	
	public UserPermissionEntry(String uid){
		this.uid = uid;
	}

	
	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public String getUniqueIdentifier() {
		return uid;
	}

	public Credential getCredential() {
		return credential;
	}

	public void setUniqueIdentifier(String uid) {
		this.uid = uid;
	}
	
	public boolean hasCredential(){
		if(this.credential != null){
			return true;
		}
		return false;
	}
	
	public boolean equals(UserPermissionEntry otherUserPermissionEntry){
		if(this.uid.equals(otherUserPermissionEntry.getUniqueIdentifier())){
			if(this.credential.equals(otherUserPermissionEntry.getCredential())){
				return true;
			}
		}
		return false;
	}
	
}
