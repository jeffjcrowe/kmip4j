/**
 * KMIPByteString.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Byte String. 
 * Byte Strings are sequences of bytes containing individual 
 * unspecified eight-bit binary values, and are interpreted in the 
 * same sequence order.
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright ï¿½ 2013, Stefanie Meile, Michael Guster
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

public class KMIPByteString extends KMIPType {
	
	private final int defaultLength = 0;	// Varies, Multiple of 8. Use length!
	private byte[] value;

	public KMIPByteString(byte[] value) {
		super();
		setValue(value);
	}
	
	public KMIPByteString(String value) {
		super();
		setValue(value);
	}

	public KMIPByteString() {
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	
	public void setValue(String value) {
		setValue(KMIPUtils.convertHexStringToByteArray(value));
	}

	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		ArrayList<Byte> returnValue = new ArrayList<>();
		for (byte aValue : value) {
			returnValue.add(aValue);
		}
		attributeValue.setLength(returnValue.size());

		int pLen = 8 - (value.length % 8);
		if ((pLen > 0) && (pLen < 8)) {
			returnValue.addAll(pad(pLen));
		}
		return returnValue;
	}
	
	public ArrayList<Byte> toArrayList(){
		ArrayList<Byte> returnValue = new ArrayList<>();
		for (byte aValue : value) {
			returnValue.add(aValue);
		}

		int pLen = 8 - (value.length % 8);
		if ((pLen > 0) && (pLen < 8)) {
			returnValue.addAll(pad(pLen));
		}
		return returnValue;
	}
	
	public ArrayList<Byte> pad(int n){
		ArrayList<Byte> al = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			al.add((byte) 0x00);
		}
		return al;
	}

	@Override
	public int getDefaultLength() {
		return defaultLength;
	}

	@Override
	public String getValueString() {
		return KMIPUtils.convertByteStringToHexString(value);
	}

}
