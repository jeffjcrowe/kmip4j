/**
 * EnumLinkType.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete KMIPEnumeration: Link Type
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

public class EnumLinkType extends KMIPEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default						= -1;
	public static final int CertificateLink				= 0x101;
	public static final int PublicKeyLink 				= 0x102;
	public static final int PrivateKeyLink 				= 0x103;
	public static final int DerivationBaseObjectLink	= 0x104;
	public static final int DerivedKeyLink 				= 0x105;
	public static final int ReplacementObjectLink 		= 0x106;
	public static final int ReplacedObjectLink 			= 0x107;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumLinkType.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumLinkType.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumLinkType(){
		try {
			this.value = getEntry(EnumLinkType.Default, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumLinkType(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumLinkType(String key){
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
