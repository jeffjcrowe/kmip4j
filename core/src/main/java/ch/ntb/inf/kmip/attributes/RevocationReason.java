/**
 * RevocationReason.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Revocation Reason attribute is a structure used to indicate 
 * why the Managed 854 Cryptographic Object was revoked 
 * (e.g., “compromised”, “expired”, “no longer used”, etc).
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

import ch.ntb.inf.kmip.kmipenum.EnumRevocationReasonCode;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class RevocationReason extends Attribute {
	
	public RevocationReason(){
		super(new KMIPTextString("Revocation Reason"), new EnumTag(EnumTag.RevocationReason), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[2];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.RevocationReasonCode), 	
				((KMIPEnumeration)new EnumRevocationReasonCode()));
		this.values[0].setName("Revocation Reason Code");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.TextString), new EnumTag(EnumTag.RevocationMessage), new KMIPTextString());
		this.values[1].setName("Revocation Message");
	}
	}
