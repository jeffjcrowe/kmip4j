/**
 * KMIPLongInteger.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Long Integer.
 * Long Integers are encoded as eight-byte long (64 bit) binary 
 * signed numbers in 2's complement notation, transmitted big-endian.
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

public class KMIPLongInteger extends KMIPType {
	
	private final int defaultLength = 8;
	
	private long value;

	public KMIPLongInteger() {
		super();
	}
	
	public KMIPLongInteger(long value) {
		super();
		this.value = value;
	}
	
	public KMIPLongInteger(String value) {
		super();
		setValue(value);
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public void setValue(String value) {
        if(value.length() > 1 && value.substring(0,2).equals("0x")){
        	this.value = Long.parseLong(value.substring(2),16);
	     }
	     else{
	    	 this.value = Long.parseLong(value);          
	     }
	}

	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		ArrayList<Byte> value = new ArrayList<Byte>();
		value.add((byte) (this.value >> 56));
		value.add((byte) (this.value >> 48));
		value.add((byte) (this.value >> 40));
		value.add((byte) (this.value >> 32));
		value.add((byte) (this.value >> 24));
		value.add((byte) (this.value >> 16));
		value.add((byte) (this.value >> 8));
		value.add((byte) this.value);
		attributeValue.setLength(defaultLength);
		return value;
	}

	public int getDefaultLength() {
		return defaultLength;
	}

	public String getValueString() {
		return Long.toString(value);
	}
	
	public String toString(){
		return Long.toString(value);
	}
	
	
}