/**
 * EnumNameType.java
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

public class EnumNameType extends KLMSEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default					= -1;
	public static final int UninterpretedTextString = 0x01;
	public static final int URI						= 0x02;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumNameType.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumNameType.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	
	public EnumNameType(){
		try {
			this.value = getEntry(EnumNameType.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumNameType(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumNameType(String key){
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
