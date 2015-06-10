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
 * @copyright  Copyright ï¿½ 2013, Stefanie Meile, Michael Guster
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
import ch.ntb.inf.kmip.objects.base.SingleAttribute;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class CryptographicAlgorithm extends SingleAttribute<EnumCryptographicAlgorithm> {

	public CryptographicAlgorithm(EnumCryptographicAlgorithm value){
		super("Cryptographic Algorithm",
				new EnumTag(EnumTag.CryptographicAlgorithm),
				new EnumType(EnumType.Enumeration),
				value);
	}

	public CryptographicAlgorithm(){
		this(new EnumCryptographicAlgorithm());
	}

	public CryptographicAlgorithm(int value){
		this(new EnumCryptographicAlgorithm(value));
	}

	public CryptographicAlgorithm(KMIPType type) {
		this((EnumCryptographicAlgorithm) type);
	}
}
