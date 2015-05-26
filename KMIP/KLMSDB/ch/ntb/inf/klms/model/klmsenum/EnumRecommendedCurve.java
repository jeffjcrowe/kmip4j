/**
 * EnumRecommendedCurve.java
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

public class EnumRecommendedCurve extends KLMSEnumeration{

	private static HashMap<String, Integer> values;
	
	public static final int Default	= -1;
	public static final int P_192 = 0x01;
	public static final int K_163 = 0x02;
	public static final int B_163 = 0x03;
	public static final int P_224 = 0x04;
	public static final int K_233 = 0x05;
	public static final int B_233 = 0x06;
	public static final int P_256 = 0x07;
	public static final int K_283 = 0x08;
	public static final int B_283 = 0x09;
	public static final int P_384 = 0x0A;
	public static final int K_409 = 0x0B;
	public static final int B_409 = 0x0C;
	public static final int P_521 = 0x0D;
	public static final int K_571 = 0x0E;
	public static final int B_571 = 0x0F;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumRecommendedCurve.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumRecommendedCurve.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumRecommendedCurve(){
		try {
			this.value = getEntry(EnumRecommendedCurve.Default, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumRecommendedCurve(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KLMSEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumRecommendedCurve(String key){
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
