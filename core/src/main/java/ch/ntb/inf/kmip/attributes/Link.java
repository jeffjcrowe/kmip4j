/**
 * Link.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Link attribute is a structure used to create a link from one 
 * Managed Cryptographic Object to another, closely related target 
 * Managed Cryptographic Object. The link has a type and a Linked 
 * Object Identifier that identifies the target Managed 
 * Cryptographic Object by its Unique Identifier.
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

import ch.ntb.inf.kmip.kmipenum.EnumLinkType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class Link extends Attribute {

	public Link(){
		super(new KMIPTextString("Link"), new EnumTag(EnumTag.Link), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[2];
		
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.LinkType), ((KMIPEnumeration)new EnumLinkType()));
		this.values[0].setName("Link Type");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.TextString), new EnumTag(EnumTag.LinkedObjectIdentifier), new KMIPTextString());
		this.values[1].setName("Linked Object Identifier");
	}

}
