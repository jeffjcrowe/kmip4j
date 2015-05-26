/**
 * KLMSAttributeValue.java
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
package ch.ntb.inf.klms.model.attributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import ch.ntb.inf.klms.model.klmsenum.EnumTag;
import ch.ntb.inf.klms.model.klmsenum.EnumTypeKLMS;

@Entity
public class KLMSAttributeValue {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	private String value;
	
	private String type;
	
	private String tag;
	
	private String name;
	
	private int length;
	

	
	public KLMSAttributeValue(){};
	
	public KLMSAttributeValue(String type, String tag, String value, String name) {
		this.type = type;
		this.tag = tag;
		this.value = value;
		this.name = name;
		this.length = getDefaultLength(new EnumTypeKLMS(type));
	}

	public KLMSAttributeValue(String type, String tag) {
		this.type = type;
		this.tag = tag;
		this.length = getDefaultLength(new EnumTypeKLMS(type));
	}
	
	
	private int getDefaultLength(EnumTypeKLMS type) {
		switch(type.getValue()){
		case EnumTypeKLMS.Boolean:
		case EnumTypeKLMS.DateTime:
		case EnumTypeKLMS.Enumeration:
		case EnumTypeKLMS.Integer:
		case EnumTypeKLMS.Interval:
			return 8;
		default:
			return 0;
		}
	}

	public void setValue(String value){
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

	public String getValueString(){
		if(type.equals("DateTime")){

			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);  

			try {
				return Long.toString(sdf.parse(value).getTime() / 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public int getTag() {
		return new EnumTag(tag).getValue();
	}
	
	public byte getType() {
		return (byte) new EnumTypeKLMS(type).getValue();
	}
	
	public EnumTypeKLMS getTypeAsEnumType() {
		return new EnumTypeKLMS(type);
	}
	
	public boolean equals(KLMSAttributeValue other) {
		if(this.value.equals(other.value)){
			return true;
		}
		return false;
	}
	
}
