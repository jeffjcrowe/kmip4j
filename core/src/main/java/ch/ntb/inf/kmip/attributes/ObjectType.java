/**
 * ObjectType.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Object Type of a Managed Object 
 * (e.g., public key, private key, symmetric key, etc)
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

import ch.ntb.inf.kmip.kmipenum.EnumObjectType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class ObjectType extends Attribute {

	public ObjectType(){
		super(new KMIPTextString("Object Type"), new EnumTag(EnumTag.ObjectType), new EnumType(EnumType.Enumeration));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.ObjectType),
				(KMIPEnumeration) new EnumObjectType());
		this.values[0].setName(this.getAttributeName());
	}
	
	public ObjectType(KMIPType value) {
		this();
		this.values[0].setValue(value);
	}
	
	public ObjectType(int value) {
		this();
		this.values[0].setValue(new EnumObjectType(value));
	}
	
	public KMIPType getObjectType() {
		return this.values[0].getValueAsKMIPType();
	}

}
