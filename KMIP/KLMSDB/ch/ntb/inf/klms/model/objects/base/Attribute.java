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
 * An Attribute object is a structure used for sending and receiving Managed Object attributes.
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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.IndexColumn;

import ch.ntb.inf.klms.model.attributes.KLMSAttributeValue;
import ch.ntb.inf.klms.model.klmsenum.EnumTypeKLMS;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "AttributTyp", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class Attribute {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	
	/**  Text-String to identify the attribute. */
	private String attributeName;
	
	/**  Tag to identify the attribute. */
	private String tag; 
	
	/**  The Attribute Index number is used to identify the instance of the attribute if there are multiple instances.*/
	private int attributeIndex;

	/**  Type of the attribute. */
	private String type;
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@IndexColumn(name = "index")
	protected List<KLMSAttributeValue> values;
	
	@Transient
	private int length;
	


	/**
	* Constructor
	*
	* @param attributeName Text-String to identify the attribute
	* @param tag Tag value of the attribute
	*
	*/
	public Attribute(String attributeName, String tag, String type) {
		super();
		this.attributeName = attributeName;
		this.tag = tag;
		this.type = type;
	}
	
	/** @param sets Name of the Attribute */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	/** @return gets Index of the Attribute */
	public int getAttributeIndex() {
		return attributeIndex;
	}
	
	/** @param sets Index of the Attribute */
	public void setAttributeIndex(int attributeIndex) {
		this.attributeIndex = attributeIndex;
	}
	
	/** @return gets Tag of the Attribute */
	public String getTag() {
		return tag;
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
		return (byte) new EnumTypeKLMS(type).getValue();
	}

	/** @return the values */
	public KLMSAttributeValue[] getValues() {
		return (KLMSAttributeValue[]) values.toArray(new KLMSAttributeValue[values.size()]);
	}

	/** @return gets encoded Name of the Attribute */
	public ArrayList<Byte> getEncodedAttributeName() {
		return toArrayList(attributeName);
	}
	
	/**
	 * @param nodeName
	 * @param textContent
	 */
	public void setValue(String value, String name) {
		if(name == null){
			values.get(0).setValue(value);
		}else{
			String valName = name.replaceAll("\\s","").toLowerCase();
			for(int i = 0; i < values.size(); i++){
				String attributeName = values.get(i).getName().replaceAll("\\s","").toLowerCase();
				if(attributeName.equals(valName)){
					values.get(i).setValue(value);
					break;
				}
			}
		}
	}
	
	/** @return gets Name of the Attribute */
	public String getAttributeName() {
		return attributeName;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(attributeName + " [");
		for(int i = 0; i < values.size(); i++){
			if(values.get(i).getName() != null){
				sb.append(values.get(i).getName() + ":" + values.get(i).getValueString());
			} else{
				sb.append(values.get(i).getValueString());
			}
		}
		sb.append("]");
		return sb.toString();
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
	
	
	public boolean equals(Attribute other) {
		if(this.attributeName.equals(other.attributeName)){
			for(int i = 0; i < values.size(); i++){
				if(! this.values.get(i).equals(other.values.get(i))){
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
