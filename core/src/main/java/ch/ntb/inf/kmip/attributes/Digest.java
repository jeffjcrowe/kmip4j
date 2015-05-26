/**
 * Digest.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Digest attribute is a structure that contains the digest 
 * value of the key or secret data, certificate, or opaque object.
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

import ch.ntb.inf.kmip.kmipenum.EnumHashingAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class Digest extends Attribute {

	
	public Digest(){
		super(new KMIPTextString("Digest"), new EnumTag(EnumTag.Digest), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[2];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.HashingAlgorithm), 
				((KMIPEnumeration)new EnumHashingAlgorithm()));
		this.values[0].setName("Hashing Algorithm");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.ByteString), new EnumTag(EnumTag.DigestValue), new KMIPByteString());
		this.values[1].setName("Digest Value");
	}
}
