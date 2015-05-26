/**
 * CryptographicAlgorithm.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Cryptographic Algorithm used by the object 
 * (e.g., RSA, DSA, DES, 3DES, AES, etc).
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

import ch.ntb.inf.kmip.kmipenum.EnumCryptographicAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class CryptographicAlgorithm extends Attribute {

	public CryptographicAlgorithm(){
		super(new KMIPTextString("Cryptographic Algorithm"), new EnumTag(EnumTag.CryptographicAlgorithm), new EnumType(EnumType.Enumeration));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.CryptographicAlgorithm), 
				((KMIPEnumeration)new EnumCryptographicAlgorithm()));
		this.values[0].setName(this.getAttributeName());
	}
	
	public CryptographicAlgorithm(KMIPType value){
		this();
		this.values[0].setValue(value);
	}
	
	public CryptographicAlgorithm(int value){
		this();
		this.values[0].setValue(new EnumCryptographicAlgorithm(value));
	}
	
}
