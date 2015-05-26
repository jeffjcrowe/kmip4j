/**
 * EnumCryptographicAlgoritm.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete KMIPEnumeration: Cryptographic Algorithm
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
package ch.ntb.inf.kmip.kmipenum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import ch.ntb.inf.kmip.types.KMIPEnumeration;

public class EnumCryptographicAlgorithm extends KMIPEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default		= -1;
	public static final int DES 		= 0x01;
	public static final int DESede 		= 0x02;		// 3DES
	public static final int AES 		= 0x03;
	public static final int RSA 		= 0x04;
	public static final int DSA 		= 0x05;
	public static final int ECDSA 		= 0x06;
	public static final int HMAC_SHA1 	= 0x07;
	public static final int HMAC_SHA224 = 0x08;
	public static final int HMAC_SHA256 = 0x09;
	public static final int HMAC_SHA384 = 0x0A;
	public static final int HMAC_SHA512 = 0x0B;
	public static final int HMAC_MD5 	= 0x0C;
	public static final int DH 			= 0x0D;
	public static final int ECDH 		= 0x0E;
	public static final int ECMQV 		= 0x0F;
	public static final int Blowfish 	= 0x10;
	public static final int Camellia 	= 0x11;
	public static final int IDEA 		= 0x12;
	public static final int MARS 		= 0x13;
	public static final int RC2 		= 0x14;
	public static final int RC4 		= 0x15;
	public static final int RC5 		= 0x16;
	public static final int SKIPJACK 	= 0x17;
	public static final int Twofish 	= 0x18;
	public static final int PGP 		= 0x19;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumCryptographicAlgorithm.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumCryptographicAlgorithm.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumCryptographicAlgorithm(){
		try {
			this.value = getEntry(EnumCryptographicAlgorithm.Default, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumCryptographicAlgorithm(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumCryptographicAlgorithm(String key){
		setValue(key);
	}
	
	public void setValue(String value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedKeyException e) {
			try{
				int intValue;
				if(value.length() > 1 && value.substring(0,2).equals("0x")){
					intValue = java.lang.Integer.parseInt(value.substring(2), 16);
				}
				else{
					intValue = java.lang.Integer.parseInt(value);
				}
				this.value = getEntry(intValue, values);
			}
			catch(KMIPEnumUndefinedValueException e1){
				e1.printStackTrace();
			}	
		}
	}
	
	
}
