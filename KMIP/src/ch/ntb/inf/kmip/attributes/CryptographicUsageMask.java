/**
 * CryptographicUsageMask.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Cryptographic Usage Mask defines the cryptographic usage of 
 * a key.
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

package ch.ntb.inf.kmip.attributes;

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class CryptographicUsageMask extends Attribute {

	
	public CryptographicUsageMask(){
		super(new KMIPTextString("Cryptographic Usage Mask"), new EnumTag(EnumTag.CryptographicUsageMask), new EnumType(EnumType.Integer));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Integer), new EnumTag(EnumTag.CryptographicUsageMask), new KMIPInteger());
		this.values[0].setName(this.getAttributeName());
	}
	
	
	public CryptographicUsageMask(KMIPType value){
		this();
		this.values[0].setValue(value);
	}
	
	public CryptographicUsageMask(int value){
		this();
		this.values[0].setValue(new KMIPInteger(value));
	}
	
}
