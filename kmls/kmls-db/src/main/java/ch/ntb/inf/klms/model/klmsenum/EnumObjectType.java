/**
 * EnumObjectType.java
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

public class EnumObjectType extends KLMSEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default		 	= 0x00;
	public static final int Certificate 	= 0x01;
	public static final int SymmetricKey 	= 0x02;
	public static final int PublicKey 		= 0x03;
	public static final int PrivateKey 		= 0x04;
	public static final int SplitKey 		= 0x05;
	public static final int Template 		= 0x06;
	public static final int SecretData 		= 0x07;
	public static final int OpaqueObject 	= 0x08;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumObjectType.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumObjectType.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumObjectType(){
		try {
			this.value = getEntry(EnumObjectType.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumObjectType(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumObjectType(String key){
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
