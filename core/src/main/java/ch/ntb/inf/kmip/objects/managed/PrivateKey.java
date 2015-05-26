/**
 * PrivateKey.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Private Key object is a Managed Cryptographic Object that is 
 * the private portion of an asymmetric key pair.
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

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.base.KeyBlock;

public class PrivateKey extends CryptographicObject{
	
	private KeyBlock keyBlock;
	
	public PrivateKey() {
		super(new EnumTag(EnumTag.PrivateKey));
	}
	
	public PrivateKey(KeyBlock keyBlock) {
		this();
		this.keyBlock = keyBlock;
	}
	
	
	// Getters & Setters
	public KeyBlock getKeyBlock() {
		return keyBlock;
	}

	public void setKeyBlock(KeyBlock keyBlock) {
		this.keyBlock = keyBlock;
	}
	
	
	// to String
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Private Key");
		sb.append("\n" + keyBlock.toString());	
		return sb.toString();
	}

}
