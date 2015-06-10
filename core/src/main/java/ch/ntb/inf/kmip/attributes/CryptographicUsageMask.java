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
 * @copyright  Copyright ï¿½ 2013, Stefanie Meile, Michael Guster
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

public class CryptographicUsageMask extends SingleAttribute<KMIPInteger> {

	public CryptographicUsageMask(KMIPInteger value){
		super("Cryptographic Usage Mask",
				new EnumTag(EnumTag.CryptographicUsageMask),
				new EnumType(EnumType.Integer),
				value);
	}

	public CryptographicUsageMask(){
		this(new KMIPInteger());
	}

	public CryptographicUsageMask(KMIPType value){
		this((KMIPInteger) value);
	}

	public CryptographicUsageMask(int value){
		this(new KMIPInteger(value));
	}
}
