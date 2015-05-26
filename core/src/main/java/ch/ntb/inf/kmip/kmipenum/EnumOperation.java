/**
 * EnumOperation.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete KMIPEnumeration: Operation
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

public class EnumOperation extends KMIPEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default				= -1;
	public static final int Create 				= 0x01;
	public static final int CreateKeyPair		= 0x02;
	public static final int Register 			= 0x03;
	public static final int ReKey 				= 0x04;
	public static final int DeriveKey 			= 0x05;
	public static final int Certify 			= 0x06;
	public static final int ReCertify 			= 0x07;
	public static final int Locate 				= 0x08;
	public static final int Check 				= 0x09;
	public static final int Get 				= 0x0A;
	public static final int GetAttributes 		= 0x0B;
	public static final int GetAttributeList 	= 0x0C;
	public static final int AddAttribute 		= 0x0D;
	public static final int ModifyAttribute 	= 0x0E;
	public static final int DeleteAttribute 	= 0x0F;
	public static final int ObtainLease 		= 0x10;
	public static final int GetUsageAllocation 	= 0x11;
	public static final int Activate 			= 0x12;
	public static final int Revoke 				= 0x13;
	public static final int Destroy 			= 0x14;
	public static final int Archive				= 0x15;
	public static final int Recover 			= 0x16;
	public static final int Validate 			= 0x17;
	public static final int Query 				= 0x18;
	public static final int Cancel 				= 0x19;
	public static final int Poll 				= 0x1A;
	public static final int Notify 				= 0x1B;
	public static final int Put 				= 0x1C;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumOperation.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumOperation.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumOperation(){
		try {
			this.value = getEntry(EnumOperation.Default, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumOperation(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumOperation(String key){
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
