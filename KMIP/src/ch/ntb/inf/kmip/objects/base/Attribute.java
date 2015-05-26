/**
 * Attribute.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * An Attribute object is a structure used for sending and receiving 
 * Managed Object attributes. It contains an Attribute Name, Index 
 * and Value. The Attribute Name is a text-string that is used to 
 * identify the attribute. The Attribute Index is an index number 
 * assigned by the key management server when a specified named 
 * attribute is allowed to have multiple instances. The Attribute 
 * Value is either a primitive data type or structured object, 
 * depending on the attribute.
 * The class Attribute extends BaseObject and is the Superclass of 
 * all Attributes. 
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

import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;


public abstract class Attribute extends BaseObject {

	/**  Text-String to identify the attribute. */
	private KMIPTextString attributeName;
	
	/**  The Attribute Index number is used to identify the instance of the attribute if there are multiple instances.*/
	private KMIPInteger attributeIndex;
	
	private EnumType type;
	
	protected KMIPAttributeValue[] values;
	
	private int length;
	
	/**
	* Constructor
	*
	* @param attributeName Text-String to identify the attribute
	* @param tag Tag value of the attribute
	*
	*/
	public Attribute(KMIPTextString attributeName, EnumTag tag, EnumType type) {
		super(tag);
		this.attributeName = attributeName;
		this.type = type;
	}
	
	/** @param sets Name of the Attribute */
	public void setAttributeName(KMIPTextString attributeName) {
		this.attributeName = attributeName;
	}
	
	/** @return gets Index of the Attribute */
	public KMIPInteger getAttributeIndex() {
		return attributeIndex;
	}
	
	/** @param sets Index of the Attribute */
	public void setAttributeIndex(KMIPInteger attributeIndex) {
		this.attributeIndex = attributeIndex;
	}
	
	/** @return length */
	public int getLength() {
		return length;
	}
	
	/** @param length */
	private void setLength(int length) {
		this.length = length;
	}
	
	/** @return gets Type of the Attribute */
	public byte getAttributeType(){
		return (byte) type.getValue();
	}

	/** @return the values */
	public KMIPAttributeValue[] getValues() {
		return values;
	}

	/** @return gets encoded Name of the Attribute */
	public ArrayList<Byte> getEncodedAttributeName() {
		return toArrayList(attributeName.getValue());
	}
	
	/**
	 * @param nodeName
	 * @param textContent
	 */
	public void setValue(String value, String name) {
		if(name == null){
			values[0].setValue(value);
		}else{
			String valName = name.replaceAll("\\s","").toLowerCase();
			for(int i = 0; i < values.length; i++){
				String attributeName = values[i].getName().replaceAll("\\s","").toLowerCase();
				if(attributeName.equals(valName)){
					values[i].setValue(value);
					break;
				}
			}
		}
	}
	
	/** @return gets Name of the Attribute */
	public String getAttributeName() {
		return attributeName.getValue();
	}
	
	
	public boolean hasAttributeIndex(){
		if(this.attributeIndex != null){
			return true;
		}
		return false;
	}
	
	
	

	/** 
	 * @param String that needs to be converted to an ArrayList
	 * @return Byte ArrayList of the input String 
	 */
	public  ArrayList<Byte> toArrayList(String val){
		int length = val.getBytes().length;
		byte[] b = new byte[length];
		b = val.getBytes();
		ArrayList<Byte> value = new ArrayList<Byte>();
		for(int i=0; i<b.length; i++){
			value.add(b[i]);
		}
		setLength(value.size());
		
		int pLen = 8 - (length % 8);
		if((pLen>0) && (pLen<8)){
			value.addAll(pad(pLen));
		}
		return value;
	}


	/** 
	 * @param String that needs to be padded
	 * @return Byte ArrayList of the String with padding Bytes
	 */
	public ArrayList<Byte> pad(int n){
		ArrayList<Byte> al = new ArrayList<Byte>();
		for (int i = 0; i < n; i++) {
			al.add((byte) 0x00);
		}
		return al;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(attributeName);
		if(hasAttributeIndex()){
			sb.append("\nAttributeIndex = " + attributeIndex.getValueString());
		}
		sb.append(" [");
		for(int i = 0; i < values.length; i++){
			if(values[i].getName() != null){
				sb.append(values[i].getName() + ":" + values[i].getValueString());
			} else{
				sb.append(values[i].getValueString() + ", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}


}
