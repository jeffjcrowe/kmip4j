/**
 * ProtectStopDate.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This is the date and time when a Managed Symmetric Key Object 
 * SHALL NOT be used for applying cryptographic protection (e.g., 
 * encryption or wrapping), depending on the value of its 
 * Cryptographic Usage Mask attribute.
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
import ch.ntb.inf.kmip.types.KMIPDateTime;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class ProtectStopDate extends Attribute {

	public ProtectStopDate(){
		super(new KMIPTextString("Protect Stop Date"), new EnumTag(EnumTag.ProtectStopDate), new EnumType(EnumType.DateTime));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.DateTime), new EnumTag(EnumTag.ProtectStopDate), new KMIPDateTime());
		this.values[0].setName(this.getAttributeName());
	}
	
	public ProtectStopDate(KMIPType value){
		this();
		this.values[0].setValue(value);
	}
}