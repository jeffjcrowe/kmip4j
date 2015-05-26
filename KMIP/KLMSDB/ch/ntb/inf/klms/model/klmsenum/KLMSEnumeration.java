/**
 * KMIPEnumeration.java
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public abstract class KLMSEnumeration{

	public final int defaultLength = 4;
	
	protected Entry<String, Integer> value; 

	

	public abstract void setValue(String value);


	public int getDefaultLength() {
		return defaultLength;
	}

	public String getValueString() {
		return Integer.toString(value.getValue());
	}
	
	protected Entry<String, Integer> getEntry(int value, HashMap<String, Integer> values) throws KLMSEnumUndefinedValueException{
		for (Map.Entry<String, Integer> e : values.entrySet()){
		    if(e.getValue()==value){
		    	return e;
		    }
		}
		throw new KLMSEnumUndefinedValueException(value, this.getClass().getName());
	}
	
	protected Entry<String, Integer> getEntry(String key, HashMap<String, Integer> values) throws KLMSEnumUndefinedKeyException{
		for (Map.Entry<String, Integer> e : values.entrySet()){
		    if(e.getKey().equals(key)){
		    	return e;
		    }
		}
		throw new KLMSEnumUndefinedKeyException(key, this.getClass().getName());
	}
	
	public String getKey(){
		return value.getKey();
	}
	
	public int getValue(){
		return value.getValue();
	}
	
}

