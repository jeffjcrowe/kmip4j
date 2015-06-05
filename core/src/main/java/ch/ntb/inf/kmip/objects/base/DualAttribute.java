/**
 * Attribute.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * An Attribute object is a structure used for sending and receiving 
 * Managed Object attributes. It contains an Attribute Name, Index 
 * and Value. The Attribute Name is a text-string that is used to 
 * identify the attribute. The Attribute Index is an index number 
 * assigned by the key management server when a specified named 
 * attribute is allowed to have multiple instances. The Attribute 
 * Value is either a primitive data type or structured object, 
 * depending on the attribute.
 * The class Attribute extends BaseObject and is the Superclass of 
 * all Attributes. 
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

package ch.ntb.inf.kmip.objects.base;

import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;


public abstract class DualAttribute<S extends KMIPType, T extends KMIPType> extends Attribute {
	protected DualAttribute(String attributeName, EnumTag tag,
							String name1, EnumTag tag1, EnumType type1, S value1,
							String name2, EnumTag tag2, EnumType type2, T value2) {
		super(new KMIPTextString(attributeName), tag, new EnumType(EnumType.Structure));

		this.values = new KMIPAttributeValue[2];
		this.values[0] = new KMIPAttributeValue(type1, tag1, value1);
		this.values[0].setName("Name Value");

		this.values[1] = new KMIPAttributeValue(type2, tag2, value2);
		this.values[1].setName("Name Type");
	}

	@SuppressWarnings("unchecked")
	public S getValue1() {
		return (S)this.values[0].getValueAsKMIPType();
	}

	public void setValue1(S value) {
		this.values[0].setValue(value);
	}

	@SuppressWarnings("unchecked")
	public T getValue2() {
		return (T)this.values[1].getValueAsKMIPType();
	}

	public void setValue2(T value) {
		this.values[1].setValue(value);
	}
}
