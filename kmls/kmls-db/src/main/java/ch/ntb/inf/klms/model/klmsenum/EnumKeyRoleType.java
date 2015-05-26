/**
 * EnumKeyRoleType.java
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

public class EnumKeyRoleType extends KLMSEnumeration{

	private static HashMap<String, Integer> values;
	
	public static final int Default		= -1;
	public static final int BDK 		= 0x01;
	public static final int CVK 		= 0x02;
	public static final int DEK 		= 0x03;
	public static final int MKAC 		= 0x04;
	public static final int MKSMC 		= 0x05;
	public static final int MKSMI 		= 0x06;
	public static final int MKDAC 		= 0x07;
	public static final int MKDN 		= 0x08;
	public static final int MKCP 		= 0x09;
	public static final int MKOTH 		= 0x0A;
	public static final int KEK 		= 0x0B;
	public static final int MAC16609 	= 0x0C;
	public static final int MAC97971 	= 0x0D;
	public static final int MAC97972 	= 0x0E;
	public static final int MAC97973 	= 0x0F;
	public static final int MAC97974 	= 0x10;
	public static final int MAC97975 	= 0x11;
	public static final int ZPK 		= 0x12;
	public static final int PVKIBM 		= 0x13;
	public static final int PVKPVV 		= 0x14;
	public static final int PVKOTH 		= 0x15;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumKeyRoleType.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumKeyRoleType.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumKeyRoleType(){
		try {
			this.value = getEntry(EnumKeyRoleType.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumKeyRoleType(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumKeyRoleType(String key){
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
