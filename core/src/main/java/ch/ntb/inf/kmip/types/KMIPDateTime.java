/**
 * KMIPDateTime.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class encapsulates the concrete KMIPType, Date Time. 
 * Date-Time values are POSIX Time values encoded as Long Integers. 
 * POSIX Time, as described in IEEE Standard 1003.1, is the number 
 * of seconds since the Epoch (1970 Jan 1, 00:00:00 UTC), not 
 * counting leap seconds.
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
import java.util.Date;

import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;

public class KMIPDateTime extends KMIPType {
	
	private final int defaultLength = 8;
	
	private Date value;

	public KMIPDateTime(Date value) {
		super();
		this.value = value;
	}
	
	public KMIPDateTime(long value) {
		super();
		this.value = new Date(value);
	}
	
	public KMIPDateTime(String value){
		super();
		this.value = new Date(Long.parseLong(value));
	}
	
	public KMIPDateTime() {
	}

	public static long createCurrentDateTime(){
		return new Date().getTime();
	}
	
	public long getValue() {
		return value.getTime();
	}

	public void setValue(Date value) {
		this.value = value;
	}
	
	public String toString(){
		return value.toString();
	}

	public void setValue(String value) {
		if(value.length() >=2 && value.substring(0, 2).equals("0x")){
			this.value = new Date(Long.parseLong(value.substring(2), 16) * 1000);
		} else{
			this.value = new Date(Long.parseLong(value) * 1000);
		}
	
	}

	public ArrayList<Byte> toArrayList(KMIPAttributeValue attributeValue) {
		long dateValue = this.value.getTime() / 1000;
		ArrayList<Byte> al = new ArrayList<Byte>();
		al.add((byte) (dateValue >> 56));
		al.add((byte) (dateValue >> 48));
		al.add((byte) (dateValue >> 40));
		al.add((byte) (dateValue >> 32));
		al.add((byte) (dateValue >> 24));
		al.add((byte) (dateValue >> 16));
		al.add((byte) (dateValue >> 8));
		al.add((byte) dateValue);
		attributeValue.setLength(defaultLength);
		return al;
	}

	public int getDefaultLength() {
		return this.defaultLength;
	}

	public String getValueString() {
		if(value == null){
			return "";
		}
		return this.value.toString();
	}
}
