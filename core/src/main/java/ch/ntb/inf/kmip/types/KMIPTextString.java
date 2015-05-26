/**
 * KMIPTextString.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Text String. 
 * Text Strings are sequences of bytes that encode character values 
 * according to the UTF-8 encoding standard. There SHALL NOT be 
 * null-termination at the end of such strings.
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

package ch.ntb.inf.kmip.types;

import java.util.ArrayList;

import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;

public class KMIPTextString extends KMIPType{
	
	private final int defaultLength = 0;	// Varies, Multiple of 8. Use length!
	
	private String value;
	
	private int length;

	public KMIPTextString(String value) {
		this.value = value;
	}

	public KMIPTextString() { }
	
	public int getDefaultLength() {
		return defaultLength;
	}
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	// used for Attributes
	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		if(attributeValue == null){
			return toArrayList();
		} else{
			int length = this.value.getBytes().length;
			byte[] b = new byte[length];
			b = this.value.getBytes();
			ArrayList<Byte> value = new ArrayList<Byte>();
			for (int i = 0; i < b.length; i++) {
				value.add(b[i]);
			}
			attributeValue.setLength(value.size());

			int pLen = 8 - (length % 8);
			if ((pLen > 0) && (pLen < 8)) {
				value.addAll(pad(pLen));
			}
			return value;
		}
	}
	
	// used for other objects
	public ArrayList<Byte> toArrayList() {
		int length = this.value.getBytes().length;
		byte[] b = new byte[length];
		b = this.value.getBytes();
		ArrayList<Byte> value = new ArrayList<Byte>();
		for (int i = 0; i < b.length; i++) {
			value.add(b[i]);
		}
		this.setLength(value.size());

		int pLen = 8 - (length % 8);
		if ((pLen > 0) && (pLen < 8)) {
			value.addAll(pad(pLen));
		}
		return value;
	}
	
	public ArrayList<Byte> pad(int n){
		ArrayList<Byte> al = new ArrayList<Byte>();
		for (int i = 0; i < n; i++) {
			al.add((byte) 0x00);
		}
		return al;
	}

	public String getValue() {
		return value;
	}
	
	public String getValueString() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
		
	public String toString(){
		return value;
	}


}
