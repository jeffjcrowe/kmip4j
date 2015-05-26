/**
 * Template.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * The Template object is a named Managed Object containing the 
 * client-settable attributes of a Managed Cryptographic Object 
 * (i.e., a stored, named list of attributes). A Template is used to 
 * specify the attributes of a new Managed Cryptographic Object in 
 * various operations. It is intended to be used to specify the 
 * cryptographic attributes of new objects in a standardized or 
 * convenient way. None of the client-settable attributes specified 
 * in a Template except the Name attribute apply to the template 
 * object itself, but instead apply to any object created using the 
 * Template.
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
package ch.ntb.inf.kmip.objects.managed;

import java.util.ArrayList;

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.base.Attribute;


public class Template extends ManagedObject {

	
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
	public Template(){
		super(new EnumTag(EnumTag.Template));
	}
	
	public Template(ArrayList<Attribute> attributes){
		this();
		this.attributes = attributes;
	}
	
	
	
	public ArrayList<Attribute> getAttributes(){
		return attributes;
	}
	
	public void addAttribute(Attribute a){
		attributes.add(a);
	}
	
	public void setAttributes(ArrayList<Attribute> attributes){
		this.attributes = attributes;
	}
	
	
	
	public boolean hasAttributes(){
		if(attributes.size() > 0){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Template");
		if(hasAttributes()){
			for(Attribute a : attributes){
				sb.append(a.toString());
			}
		}

		return sb.toString();
	}
	
}

