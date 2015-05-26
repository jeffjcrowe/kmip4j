/**
 * VendorExtension.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Vendor Extension structure SHALL contain vendor-specific 
 * extensions. As it is not exactly defined in KMIP Version 1.0, 
 * the vendor extensions are stored in tag, type, value fields. 
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
package ch.ntb.inf.kmip.objects;

import java.util.ArrayList;

import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.types.*;



public class VendorExtension {

	private int tag;
	private EnumType type;
	private KMIPType value;
	
	
	public VendorExtension(String tag, String type, String value) {
		this.tag = Integer.parseInt(tag.substring(2), 16);
		this.type = new EnumType(type);
		setValue(value);
	}
	
	public VendorExtension(int tag, EnumType type, KMIPType value) {
		this.tag = tag;
		this.type = type;
		this.value = value;
	}


	public int getTag() {
		return tag;
	}
	
	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getType() {
		return type.getValue();
	}

	public void setType(EnumType type) {
		this.type = type;
	}

	public ArrayList<Byte> getValue() {
		return value.toArrayList(null);
	}
	

	public KMIPType getKMIPType() {
		return value;
	}
	
	public void setValue(KMIPType value){
		this.value = value;
	}

	public void setValue(String value) {
		
		switch(this.type.getValue()){
		
			case EnumType.Integer:
				this.value = new KMIPInteger(value);
				break;
			case EnumType.LongInteger:
				this.value = new KMIPLongInteger(value);
				break;
			case EnumType.BigInteger:
				this.value = new KMIPBigInteger(value);
				break;
			case EnumType.Boolean:
				this.value = new KMIPBoolean(value);
				break;
			case EnumType.TextString:
				this.value = new KMIPTextString(value);
				break;
			case EnumType.ByteString:
				this.value = new KMIPByteString(value);
				break;
			case EnumType.DateTime:
				this.value = new KMIPDateTime(value);
				break;
			case EnumType.Interval:
				this.value = new KMIPInterval(value);
				break;
			default:
			try {
				throw new Exception("Please use the following method: setValue(KMIPType value)");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Vendor Extension");
		sb.append("\nTag = "+ tag);
		sb.append("\nType = "+ type.getValue());
		sb.append("\nValue = "+ value.getValueString());
		return sb.toString();
	}

}
