/**
 * CryptographicDomainParameters.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Cryptographic Domain Parameters attribute is a structure 
 * that contains a set of 549 OPTIONAL fields that MAY need to be 
 * specified in the Create Key Pair Request Payload.
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

import ch.ntb.inf.kmip.kmipenum.EnumRecommendedCurve;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class CryptographicDomainParameters extends Attribute {
	
	public CryptographicDomainParameters(){
		super(new KMIPTextString("Cryptographic Domain Parameters"), new EnumTag(EnumTag.CryptographicDomainParameters), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[2];
		
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Integer), new EnumTag(EnumTag.Qlength), new KMIPInteger());
		this.values[0].setName("Qlength");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.RecommendedCurve), (KMIPEnumeration)new EnumRecommendedCurve());
		this.values[1].setName("Recommended Curve");
	}
	
}
