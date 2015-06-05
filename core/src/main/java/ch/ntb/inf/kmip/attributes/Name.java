/**
 * Name.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Name attribute is a structure used to identify and locate 
 * the object. This attribute is assigned by the client, and 
 * the Name Value is intended to be in a form that humans are able 
 * to interpret.
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright � 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */

package ch.ntb.inf.kmip.attributes;

import ch.ntb.inf.kmip.kmipenum.*;
import ch.ntb.inf.kmip.objects.base.DualAttribute;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class Name extends DualAttribute<KMIPTextString, EnumNameType> {
	public Name(){
		super("Name", new EnumTag(EnumTag.Name),
				"Name Value", new EnumTag(EnumTag.NameValue), new EnumType(EnumType.TextString), new KMIPTextString(),
				"Name Type", new EnumTag(EnumTag.NameType), new EnumType(EnumType.Enumeration), new EnumNameType());
	}

	public Name(String name) {
		this();
		setValue1(new KMIPTextString(name));
		setValue2(new EnumNameType(EnumNameType.UninterpretedTextString));
	}
}
