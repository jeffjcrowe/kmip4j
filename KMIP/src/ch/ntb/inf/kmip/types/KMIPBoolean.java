/**
 * KMIPBoolean.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Boolean. 
 * Booleans are encoded as an eight-byte value that SHALL either 
 * contain the hex value 0000000000000000, indicating the Boolean 
 * value False, or the hex value 0000000000000001, transmitted 
 * big-endian, indicating the Boolean value True.
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

public class KMIPBoolean extends KMIPType {
	
	private final int defaultLength = 8;
	
	private boolean value;

	public KMIPBoolean(boolean value) {
		super();
		this.value = value;
	}
	
	public KMIPBoolean(String value){
		super();
		setValue(value);
	}
	
	public KMIPBoolean(long value){
		super();
		setValue(value);
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public void setValue(String value){
		if(value.toLowerCase().equals("true")){
			this.value = true;
		} else if(value.toLowerCase().equals("false")){
			this.value = false;
		} 
	}
	
	public void setValue(long value) {
		if(value == 1){
			this.value = true;
		} else if(value == 0){
			this.value = false;
		} 
	}
	
	public boolean getValue(){
		return value;
	}
	
	public byte getByteValue(){
		return value? (byte) 1 : (byte) 0;
	}

	public int getDefaultLength() {
		return defaultLength;
	}

	public String getValueString() {
		return String.valueOf(value);
	}
	
	public String toString(){
		return String.valueOf(value);
	}
	
	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		ArrayList<Byte> value = new ArrayList<Byte>();
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) 0x00);
		value.add((byte) getByteValue());
		attributeValue.setLength(defaultLength);
		return value;
	}

}
