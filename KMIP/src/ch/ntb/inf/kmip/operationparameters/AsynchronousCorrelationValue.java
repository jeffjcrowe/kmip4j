/**
 * AsynchronousCorrelationValue.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Asynchronous Correlation Value is returned in the immediate 
 * response to an operation that is pending and that requires 
 * asynchronous polling. A server-generated correlation value SHALL 
 * be specified in any subsequent Poll or Cancel operations that 
 * pertain to the original operation.
 *
 * The message payload is determined by the specific operation being 
 * requested or to which is being replied. There are additional 
 * parameters depending on the operation, which are placed in the 
 * package ch.ntb.inf.kmip.operationparameters. These parameters are 
 * not defined as Attributes according to the KMIP 1.0 specification,
 * but they are handled like these. That's why they all extend the
 * superclass Attribute. 
 * 
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
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;


public class AsynchronousCorrelationValue extends Attribute{

	
	public AsynchronousCorrelationValue(){
		super(new KMIPTextString("Asynchronous Correlation Value"), new EnumTag(EnumTag.AsynchronousCorrelationValue), new EnumType(EnumType.ByteString));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.ByteString), new EnumTag(EnumTag.AsynchronousCorrelationValue), new KMIPByteString());
		this.values[0].setName(this.getAttributeName());
	}
	
	public AsynchronousCorrelationValue(KMIPType value){
		this();
		this.values[0].setValue(value);
	}

	
}
