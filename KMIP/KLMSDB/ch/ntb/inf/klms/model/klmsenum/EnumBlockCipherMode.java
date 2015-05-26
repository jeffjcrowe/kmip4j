/**
 * EnumBlockCipherMode.java
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

public class EnumBlockCipherMode extends KLMSEnumeration{

	private static HashMap<String, Integer> values;
	
	public static final int Default				= 0x00;
	public static final int CBC					= 0x01;
	public static final int ECB					= 0x02;
	public static final int PCBC				= 0x03;
	public static final int CFB					= 0x04;
	public static final int OFB					= 0x05;
	public static final int CTR					= 0x06;
	public static final int CMAC				= 0x07;
	public static final int CCM					= 0x08;
	public static final int GCM					= 0x09;
	public static final int CBC_MAC				= 0x0A;
	public static final int XTS					= 0x0B;
	public static final int AESKeyWrapPadding	= 0x0C;
	public static final int NISTKeyWrap			= 0x0D;
	public static final int X9_102_AESKW		= 0x0E;
	public static final int X9_102_TDKW			= 0x0F;
	public static final int X9_102_AKW1			= 0x10;
	public static final int X9_102_AKW2			= 0x11;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumBlockCipherMode.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumBlockCipherMode.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumBlockCipherMode(){
		try {
			this.value = getEntry(EnumBlockCipherMode.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumBlockCipherMode(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumBlockCipherMode(String key){
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
