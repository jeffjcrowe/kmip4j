/**
 * CryptographicLength.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * Cryptographic Length is the length in bits of the clear-text 
 * cryptographic key material of the Managed Cryptographic Object.
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright � 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */

package ch.ntb.inf.kmip.attributes;

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.SingleAttribute;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPType;

public class CryptographicLength extends SingleAttribute<KMIPInteger> {

	public CryptographicLength(KMIPInteger value){
		super("Cryptographic Length",
				new EnumTag(EnumTag.CryptographicLength),
				new EnumType(EnumType.Integer),
				value);
	}

	public CryptographicLength(){
		this(new KMIPInteger());
	}

	public CryptographicLength(KMIPType value){
		this((KMIPInteger)value);
	}
	
	public CryptographicLength(int value){
		this(new KMIPInteger(value));
	}
	
}
