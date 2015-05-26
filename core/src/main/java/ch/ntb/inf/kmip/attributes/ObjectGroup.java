/**
 * ObjectGroup.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * An object MAY be part of a group of objects. An object MAY belong 
 * to more than one group of objects. To assign an object to a group 
 * of objects, the object group name SHOULD be set into this 
 * attribute.
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
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class ObjectGroup extends Attribute {

	public ObjectGroup(){
		super(new KMIPTextString("Object Group"), new EnumTag(EnumTag.ObjectGroup), new EnumType(EnumType.TextString));
		this.values = new KMIPAttributeValue[1];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.TextString), new EnumTag(EnumTag.ObjectGroup), new KMIPTextString());
		this.values[0].setName(this.getAttributeName());
	}
	
	public ObjectGroup(KMIPType value){
		this();
		this.values[0].setValue(value);
	}

}
