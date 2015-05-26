/**
 * KMIPAttributeValue.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * TODO
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
package ch.ntb.inf.kmip.attributes;

import java.util.ArrayList;

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.types.KMIPType;


public class KMIPAttributeValue {

	private KMIPType value;
	private EnumType type;
	private EnumTag tag;
	private String name;
	private int length;
	
	
	public KMIPAttributeValue(EnumType type, EnumTag tag, KMIPType value, String name) {
		this.type = type;
		this.tag = tag;
		this.value = value;
		this.name = name;
		this.length = value.getDefaultLength();
	}

	public KMIPAttributeValue(EnumType type, EnumTag tag, KMIPType value) {
		this.type = type;
		this.tag = tag;
		this.value = value;
		this.length = value.getDefaultLength();
	}
	
	public void setValue(String value){
		this.value.setValue(value);
	}
	
	public void setValue(KMIPType value){
		this.value = value;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}

	public ArrayList<Byte> getValue() {
		return value.toArrayList(this);
	}
	
	public KMIPType getValueAsKMIPType() {
		return value;
	}
	
	public String getValueString() {
		return value.getValueString();
	}

	public int getTag() {
		return tag.getValue();
	}
	
	public byte getType() {
		return (byte) type.getValue();
	}
	
	public EnumType getTypeAsEnumType() {
		return type;
	}
	

}

