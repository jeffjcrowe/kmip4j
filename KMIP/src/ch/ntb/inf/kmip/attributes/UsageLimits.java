/**
 * UsageLimits.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Usage Limits attribute is a mechanism for limiting the usage 
 * of a Managed Cryptographic Object. It only applies to Managed 
 * Cryptographic Objects that are able to be used for applying 
 * cryptographic protection and it SHALL only reflect their usage 
 * for applying that protection.
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
import ch.ntb.inf.kmip.kmipenum.EnumUsageLimitsUnit;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPLongInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class UsageLimits extends Attribute {
	
	public UsageLimits(){
		super(new KMIPTextString("Usage Limits"), new EnumTag(EnumTag.UsageLimits), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[3];
		this.values[2] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.UsageLimitsUnit), 
				((KMIPEnumeration)new EnumUsageLimitsUnit()));
		this.values[2].setName("Usage Limits Unit");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.LongInteger), new EnumTag(EnumTag.UsageLimitsCount), new KMIPLongInteger());
		this.values[1].setName("Usage Limits Count");
		
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.LongInteger), new EnumTag(EnumTag.UsageLimitsTotal), new KMIPLongInteger());
		this.values[0].setName("Usage Limits Total");
	}
	
	public void setUsageLimitsCount(KMIPLongInteger value){
		this.values[1].setValue(value);
	}
	


}
