/**
 * Offset.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * An Offset MAY be used to indicate the difference between the 
 * Initialization Date and the Activation Date of the new 
 * certificate. If Offset is set, then the dates of the new 
 * certificate SHALL be set based on the dates of the existing 
 * certificate. This parameter is used for the operations Re-Key and 
 * Re-Certify. 
 *
 * The message payload is determined by the specific operation being 
 * requested or to which is being replied. There are additional 
 * parameters depending on the operation, which are placed in the 
 * package ch.ntb.inf.kmip.operationparameters. These parameters are 
 * not defined as Attributes according to the KMIP 1.0 specification,
 * but they are handled like these. That's why they all extend the
 * superclass Attribute. 
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

package ch.ntb.inf.kmip.operationparameters;

import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPInterval;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;


public class Offset extends Attribute {

	public Offset(){
		super(new KMIPTextString("Offset"), new EnumTag(EnumTag.Offset), new EnumType(EnumType.Interval));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Interval), new EnumTag(EnumTag.Offset), new KMIPInterval());
		this.values[0].setName(this.getAttributeName());
	}
	
	public Offset(KMIPType value){
		this();
		this.values[0].setValue(value);
	}
	
}
