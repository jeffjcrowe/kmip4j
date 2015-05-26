/**
 * CryptographicObject.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * This class is the Superclass of all Managed Cryptographic Objects. 
 * Managed Cryptographic Objects are the subset of Managed Objects 
 * that contain cryptographic material :
 * (e.g. certificates, keys, and secret data).
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



public abstract class CryptographicObject extends ManagedObject {

	public CryptographicObject(EnumTag tag){
		super(tag);
	}

}
