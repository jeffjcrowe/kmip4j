/**
 * SecretData.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Secret Data object is a Managed Cryptographic Object 
 * containing a shared secret value that is not a key or certificate. 
 * The Key Block of the Secret Data object contains a Key Value of 
 * the Opaque type. The Key Value MAY be wrapped.
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

import ch.ntb.inf.kmip.kmipenum.EnumSecretDataType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.base.KeyBlock;


public class SecretData extends CryptographicObject {

	private EnumSecretDataType secretDataType;
	private KeyBlock keyBlock;

	public SecretData(){
		super(new EnumTag(EnumTag.SecretData));
	}
	
	public SecretData(KeyBlock keyBlock, EnumSecretDataType secretDataType){
		this();
		this.keyBlock = keyBlock;
		this.secretDataType = secretDataType;
	}
	

	public EnumSecretDataType getSecretDataType() {
		return secretDataType;
	}

	public void setSecretDataType(EnumSecretDataType secretDataType) {
		this.secretDataType = secretDataType;
	}
	
	public void setKeyBlock(KeyBlock keyBlock) {
		this.keyBlock = keyBlock;
	}

	public KeyBlock getKeyBlock() {
		return keyBlock;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Secret Data");
		sb.append("\nSecretDataType: "+ secretDataType.getValueString());
		sb.append("\n" + keyBlock.toString());	
		return sb.toString();
	}
	
}
