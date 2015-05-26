/**
 * CryptographicParameters.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Cryptographic Parameters attribute is a structure that 
 * contains a set of OPTIONAL 533 fields that describe certain 
 * cryptographic parameters to be used when performing cryptographic 
 * operations using the object.
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

import ch.ntb.inf.kmip.kmipenum.EnumBlockCipherMode;
import ch.ntb.inf.kmip.kmipenum.EnumHashingAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumKeyRoleType;
import ch.ntb.inf.kmip.kmipenum.EnumPaddingMethod;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPTextString;

public class CryptographicParameters extends Attribute {

	public CryptographicParameters(){
		super(new KMIPTextString("Cryptographic Parameters"), new EnumTag(EnumTag.CryptographicParameters), new EnumType(EnumType.Structure));
		this.values = new KMIPAttributeValue[4];
		this.values[0] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.BlockCipherMode), (KMIPEnumeration)new EnumBlockCipherMode());
		this.values[0].setName("Block Cipher Mode");
		
		this.values[1] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.PaddingMethod), (KMIPEnumeration)new EnumPaddingMethod());
		this.values[1].setName("Padding Method");
		
		this.values[2] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.HashingAlgorithm), (KMIPEnumeration)new EnumHashingAlgorithm());
		this.values[2].setName("Hashing Algorithm");
		
		this.values[3] = new KMIPAttributeValue(new EnumType(EnumType.Enumeration), new EnumTag(EnumTag.KeyRoleType), (KMIPEnumeration)new EnumKeyRoleType());
		this.values[3].setName("Key Role Type");
	}
	
	public void setBlockCipherMode(EnumBlockCipherMode bcm){
		this.values[0].setValue(bcm);
	}
	
	public void setPaddingMethod(EnumPaddingMethod bcm){
		this.values[1].setValue(bcm);
	}
	
	public void setHashingAlgorithm(EnumHashingAlgorithm bcm){
		this.values[2].setValue(bcm);
	}
	
	public void setKeyRoleType(EnumKeyRoleType bcm){
		this.values[3].setValue(bcm);
	}

}
