/**
 * KMIPBigInteger.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Big Integer. 
 * Big Integers are encoded as a sequence of eight-bit bytes, in 
 * two's complement notation, transmitted big-endian. If the length 
 * of the sequence is not a multiple of eight bytes, then Big 
 * Integers SHALL be padded with the minimal number of leading 
 * sign-extended bytes to make the length a multiple of eight bytes. 
 * These padding bytes are part of the TTLV-Item Value and SHALL be 
 * counted in the TTLV-Item Length.
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
import ch.ntb.inf.kmip.utils.KMIPUtils;

public class KMIPBigInteger extends KMIPType {
	
	private final int defaultLength = 0;	// Varies, Multiple of 8. Use length!
	
	private ArrayList<Byte> value = new ArrayList<Byte>();
	private int length;

	public KMIPBigInteger(ArrayList<Byte> value) {
		super();
		this.value = value;
	}
	
	public KMIPBigInteger(String value) {
		super();
		setValue(value);
	}

	public ArrayList<Byte> getValue() {
		return value;
	}

	public void setValue(ArrayList<Byte> value) {
		this.value = value;
	}
	
	public void setValue(String value) {
		this.value = KMIPUtils.convertHexStringToArrayList(value);
	}
	
	private void setLength(int size) {
		this.length = size;
	}
	
	public int getLength(){
		return this.length;
	}

	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		ArrayList<Byte> returnValue = new ArrayList<Byte>();
		for (int i = 0; i < value.size(); i++) {
			returnValue.add(value.get(i));
		}
		this.setLength(returnValue.size());

		int pLen = 8 - (length % 8);
		if ((pLen > 0) && (pLen < 8)) {
			returnValue.addAll(pad(pLen));
		}
		return returnValue;
	}
	

	public ArrayList<Byte> toArrayList(){
		return toArrayList(null);
	}
	
	public ArrayList<Byte> pad(int n){
		ArrayList<Byte> al = new ArrayList<Byte>();
		for (int i = 0; i < n; i++) {
			al.add((byte) 0x00);
		}
		return al;
	}

	public int getDefaultLength() {
		return this.defaultLength;
	}

	public String getValueString() {
		return KMIPUtils.convertArrayListToHexString(this.value);
	}
	
	
}