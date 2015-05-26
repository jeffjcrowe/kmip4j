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
 * Description:
 * The Key Value is a structure and is used only inside a Key Block. 
 * The Key Value structure contains the key material, either as a 
 * byte string or as a Transparent Key structure, and OPTIONAL 
 * attribute information that is associated and encapsulated with 
 * the key material.
 * 
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
import ch.ntb.inf.kmip.objects.KeyMaterial;


public class KeyValue extends BaseObject {

	private ArrayList<Attribute> attributes;
	private KeyMaterial keyMaterial;
	
	
	public KeyValue() {
		super(new EnumTag(EnumTag.KeyValue));
		attributes = new ArrayList<Attribute>();
	}
	
	public KeyValue(KeyMaterial keyMaterial) {
		this();
		this.keyMaterial = keyMaterial;
	}

	public KeyValue(KeyMaterial keyMaterial, ArrayList<Attribute> attributes) {
		this();
		this.keyMaterial = keyMaterial;
		this.attributes = attributes;
	}



	public KeyMaterial getKeyMaterial() {
		return keyMaterial;
	}
	
	public void setKeyMaterial(KeyMaterial keyMaterial) {
		this.keyMaterial = keyMaterial;
	}
	

	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(Attribute a){
		this.attributes.add(a);
	}
	
	
	
	public boolean hasAttributes(){
		if(attributes.size() > 0){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nKey Value: ");
		sb.append(keyMaterial.toString());
		
		if(hasAttributes()){
			for(Attribute a : attributes){
				sb.append("\t" + a.toString());
			}
		}		
		return sb.toString();
	}



}
