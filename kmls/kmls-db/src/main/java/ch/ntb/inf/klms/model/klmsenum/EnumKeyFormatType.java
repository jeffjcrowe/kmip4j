/**
 * EnumKeyFormatType.java
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
package ch.ntb.inf.klms.model.klmsenum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class EnumKeyFormatType extends KLMSEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default						= -1;
	public static final int Raw 						= 0x01;
	public static final int Opaque 						= 0x02;
	public static final int PLCS_1 						= 0x03;
	public static final int PLCS_8 						= 0x04;
	public static final int X_509 						= 0x05;
	public static final int ECPrivateKey 				= 0x06;
	public static final int TransparentSymmetricKey 	= 0x07;
	public static final int TransparentDSAPrivateKey 	= 0x08;
	public static final int TransparentDSAPublicKey 	= 0x09;
	public static final int TransparentRSAPrivateKey 	= 0x0A;
	public static final int TransparentRSAPublicKey 	= 0x0B;
	public static final int TransparentDHPrivateKey 	= 0x0C;
	public static final int TransparentDHPublicKey 		= 0x0D;
	public static final int TransparentECDSAPrivateKey	= 0x0E;
	public static final int TransparentECDSAPublicKey 	= 0x0F;
	public static final int TransparentECDHPrivateKey 	= 0x10;
	public static final int TransparentECDHPublicKey 	= 0x11;
	public static final int TransparentECMQVPrivateKey	= 0x12;
	public static final int TransparentECMQVPublicKey 	= 0x13;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumKeyFormatType.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumKeyFormatType.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumKeyFormatType(){
		try {
			this.value = getEntry(EnumKeyFormatType.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumKeyFormatType(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumKeyFormatType(String key){
		try {
			this.value = getEntry(key, values);
		} catch (KLMSEnumUndefinedKeyException e) {
			e.printStackTrace();
		}
	}
	
	public void setValue(String value) {
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedKeyException e) {
			e.printStackTrace();
		}
	}
}
