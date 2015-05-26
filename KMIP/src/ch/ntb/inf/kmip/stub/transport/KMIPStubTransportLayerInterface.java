/**
 * KMIPStubTransportLayerInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPStubTransportLayerInterface provides the needful 
 * flexibility for the interchangeability of the Transport Layer on 
 * the client side. It offers one method to send a message and three
 * methods to set dynamically loaded parameters. 
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

package ch.ntb.inf.kmip.stub.transport;

import java.util.ArrayList;

/**
 * The KMIPStubTransportLayerInterface provides the needful 
 * flexibility for the interchangeability of the Transport Layer on 
 * the client side. It offers one method to send a message and three
 * methods to set dynamically loaded parameters. 
 */
public interface KMIPStubTransportLayerInterface {
	
	/**
	 * Sends a KMIP-Request-Message as a TTLV-encoded hexadecimal string stored in an 
	 * <code>ArrayList{@literal <}Byte{@literal >}</code> to a defined target and returns 
	 * a corresponding KMIP-Response-Message.
	 * 
	 * @param al :     	the <code>ArrayList{@literal <}Byte{@literal >}</code> to be sent.
	 * @return			<code>ArrayList{@literal <}Byte{@literal >}</code>: the response message.
	 */
	public ArrayList<Byte> send(ArrayList<Byte> al);

	/**
	 * Sets the target host name to the defined value
	 * 
	 * @param value :     	the target host name defined as <code>String</code> to be set. 
	 */
	public void setTargetHostname(String value);
	
	/**
	 * Sets the keystore location of the used keystore (e.g.: D:\\keystore\\keystore.jks).
	 * Only for HTTPS support. IF HTTP: nothing to do here-> empty implementation.
	 * 
	 * @param property :     the key store location defined as <code>String</code> to be set. 
	 */
	public void setKeyStoreLocation(String property);
	
	/**
	 * Sets the keystore password of the used keystore.
	 * Only for HTTPS support. IF HTTP: nothing to do here-> empty implementation.
	 * 
	 * @param property :     the key store password defined as <code>String</code> to be set.
	 */
	public void setKeyStorePW(String property);
	
}
