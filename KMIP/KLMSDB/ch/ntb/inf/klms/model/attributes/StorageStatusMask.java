/**
 * StorageStatusMask.java
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
package ch.ntb.inf.klms.model.attributes;

import java.util.ArrayList;

import ch.ntb.inf.klms.model.objects.base.Attribute;


public class StorageStatusMask extends Attribute{

	public StorageStatusMask(){
		super("Storage Status Mask", "StorageStatusMask", "Integer");
		this.values = new ArrayList<KLMSAttributeValue>();
		this.values.add(new KLMSAttributeValue("Integer", "StorageStatusMask"));
		this.values.get(0).setName(this.getAttributeName());
	}
	
	public StorageStatusMask(String value){
		this();
		this.values.get(0).setValue(value);
	}
	
	public int getStorageStatusMask(){
		return Integer.parseInt(this.values.get(0).getValueString());
	}
}
