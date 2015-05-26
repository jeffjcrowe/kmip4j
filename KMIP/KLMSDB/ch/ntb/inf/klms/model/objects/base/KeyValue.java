/**
 * KeyValue.java
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
import java.util.HashMap;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.Digest;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.KeyMaterial;


@Entity
public class KeyValue  {

	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private KeyMaterial keyMaterial;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="KEYVALUE_ATTRIBUTES",
		    joinColumns= @JoinColumn(name="KEYVALUE_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="ATTRIBUTE_ID", referencedColumnName="ID")
	)
	private Set<Attribute> attributes;
	
	
	
	
	public KeyValue(CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength, Digest digest) throws NoSuchAlgorithmException {
		super();
		this.keyMaterial = new KeyMaterial(cryptographicAlgorithm, cryptographicLength, digest);
	}
	
	public KeyValue(HashMap<String, String> parameters, Digest digest) {
		super();
		this.keyMaterial = new KeyMaterial(parameters, digest);
	}
	
	public KeyValue(HashMap<String, String> parameters) {
		super();
		this.keyMaterial = new KeyMaterial(parameters);
	}
	
	public KeyValue(KeyMaterial keyMaterial) {
		super();
		this.keyMaterial = keyMaterial;
	}
	
	public KeyValue() {}

	public KeyValue(byte[] keyMaterial, Digest digest) {
		this.keyMaterial = new KeyMaterial(keyMaterial, digest);
	}
	
	
	
	

	public KeyMaterial getKeyMaterial() {
		return keyMaterial;
	}

	private void setKeyMaterial(KeyMaterial keyMaterial) {
		this.keyMaterial = keyMaterial;
	}
	
	public Set<Attribute> getAttributes(){
		return attributes;
	}

	public KeyValue renew(CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength, Digest digest) throws NoSuchAlgorithmException {
		KeyValue newKeyValue = new KeyValue();
		newKeyValue.setKeyMaterial(this.keyMaterial.renew(cryptographicAlgorithm, cryptographicLength, digest));
		return newKeyValue;
	}


}
