/**
 * OpaqueObject.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Opaque Object is a Managed Object that the key management 
 * server is possibly not able to interpret. It contains an opaque
 * data type and an opaque data value. 
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
package ch.ntb.inf.kmip.objects.managed;

import ch.ntb.inf.kmip.kmipenum.EnumOpaqueDataType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.types.KMIPByteString;


public class OpaqueObject extends ManagedObject {

	private EnumOpaqueDataType opaqueDataType;
	private KMIPByteString opaqueDataValue;

	public OpaqueObject(){
		super(new EnumTag(EnumTag.OpaqueObject));
	}
	
	public OpaqueObject(EnumOpaqueDataType opaqueDataType, KMIPByteString opaqueDataValue){
		this();
		this.opaqueDataType = opaqueDataType;
		this.opaqueDataValue = opaqueDataValue;
	}
	
	// Getters & Setters

	public EnumOpaqueDataType getOpaqueDataType() {
		return opaqueDataType;
	}

	public void setOpaqueDataType(EnumOpaqueDataType opaqueDataType) {
		this.opaqueDataType = opaqueDataType;
	}

	public KMIPByteString getOpaqueDataValue() {
		return opaqueDataValue;
	}

	public void setOpaqueDataValue(KMIPByteString opaqueDataValue) {
		this.opaqueDataValue = opaqueDataValue;
	}
		

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Opaque Object");
		sb.append("\nOpaqueDataType: "+ opaqueDataType.getValueString());
		sb.append("\nOpaqueDataValue: "+opaqueDataValue.getValueString());	
		return sb.toString();
	}
}
