/**
 * EnumHashingAlgorithm.java
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

public class EnumHashingAlgorithm extends KLMSEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default		= -1;
	public static final int MD2			= 0x01;
	public static final int MD4			= 0x02;
	public static final int MD5			= 0x03;
	public static final int SHA1		= 0x04;
	public static final int SHA224		= 0x05;
	public static final int SHA256		= 0x05;
	public static final int SHA384		= 0x06;
	public static final int SHA512		= 0x07;
	public static final int RIPEMD160	= 0x08;
	public static final int Tiger		= 0x0A;
	public static final int Whirlpool	= 0x0B;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumHashingAlgorithm.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumHashingAlgorithm.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumHashingAlgorithm(){
		try {
			this.value = getEntry(EnumHashingAlgorithm.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumHashingAlgorithm(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumHashingAlgorithm(String key){
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
