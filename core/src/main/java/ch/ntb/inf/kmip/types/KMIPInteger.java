/**
 * KMIPInteger.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Integer.
 * Integers are encoded as four-byte long (32 bit) binary signed 
 * numbers in 2's complement notation, transmitted big-endian.
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


public class KMIPInteger extends KMIPType{
	
	private final int defaultLength = 4;
	
	private int value;
	
	
	public KMIPInteger() { }
	

	public KMIPInteger(int value) {
		super();
		this.value = value;
	}
	
	public KMIPInteger(String value) {
		super();
		setValue(value);
	}

	public int getDefaultLength() {
		return this.defaultLength;
	}

	public int getValue() {
		return value;
	}
	
	public String getValueString() {
		return Integer.toString(value);
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public void setValue(String value) {
        if(value.length() > 1 && value.substring(0,2).equals("0x")){
        	this.value = Integer.parseInt(value.substring(2), 16);
	     }
	     else{
	    	 this.value = Integer.parseInt(value);          
	     }
	}
	
	public String toString(){
		return Integer.toString(value);
	}


	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		ArrayList<Byte> value = new ArrayList<Byte>();
		value.add((byte) (this.value >> 24));
		value.add((byte) (this.value >> 16));
		value.add((byte) (this.value >> 8));
		value.add((byte) this.value);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		attributeValue.setLength(defaultLength);
		return value;
	}





}

