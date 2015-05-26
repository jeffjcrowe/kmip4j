/**
 * TemplateAttributeStructure.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * This class is the Superclass of all Template Attribute Structures. 
 * These structures are used in various operations to provide the 
 * desired attribute values and/or template names in the request and 
 * to return the actual attribute values in the response.
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

import ch.ntb.inf.kmip.attributes.Name;
import ch.ntb.inf.kmip.kmipenum.EnumTag;

public abstract class TemplateAttributeStructure extends BaseObject {

	private ArrayList<Attribute> attributes;
	private ArrayList<Name> names;
	
	
	public TemplateAttributeStructure(EnumTag tag) {
		super(tag);
		this.attributes = new ArrayList<Attribute>();
		this.names = new ArrayList<Name>();
	}
	
	public TemplateAttributeStructure(EnumTag tag, ArrayList<Attribute> attributes, ArrayList<Name> names) {
		super(tag);
		this.attributes = attributes;
		this.names = names;
	}
	
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}
	
	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void addAttribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	public ArrayList<Name> getNames() {
		return names;
	}
	
	public void setNames(ArrayList<Name> names) {
		this.names = names;
	}
	
	public void addName(Name name) {
		this.names.add(name);
	}
	
	
	
	public boolean hasAttributes(){
		if(attributes.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean hasNames(){
		if(names.size() > 0){
			return true;
		}
		return false;
	}
	
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nTemplate Attribute Structure: ");
		
		if(hasAttributes()){
			for(Attribute a : attributes){
				sb.append("\t" + a.toString() +"\n");
			}
		}	
		
		if(hasNames()){
			for(Name n : names){
				sb.append("\t" + n.toString() +"\n");
			}
		}		
		return sb.toString();
	}

	
}
