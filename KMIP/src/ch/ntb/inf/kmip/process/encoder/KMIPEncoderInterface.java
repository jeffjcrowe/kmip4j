/**
 * KMIPEncoderInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPEncoderInterface provides the needful flexibility for
 * the interchangeability of the Encoder. It offers two methods to 
 * encode a message dependent on whether it is a request or a 
 * response.
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

package ch.ntb.inf.kmip.process.encoder;

import java.util.ArrayList;
import ch.ntb.inf.kmip.container.KMIPContainer;

/**
 * The KMIPEncoderInterface is the interface for all encoders. It 
 * provides the needful flexibility for the interchangeability of 
 * the encoder. It offers two methods to encode a message 
 * dependent on whether it is a request or a response.
 */
public interface KMIPEncoderInterface {
	
	/**
	 * Encodes a <code>KMIPContainer</code> and returns a KMIP-Request-Message as a TTLV-encoded 
	 * hexadecimal string stored in an <code>ArrayList{@literal <}Byte{@literal >}</code>.
	 * 
	 * @param c : 	the <code>KMIPContainer</code> to be encoded.
	 * @return		<code>ArrayList{@literal <}Byte{@literal >}</code>
	 */
	public ArrayList<Byte> encodeRequest(KMIPContainer c);
	
	/**
	 * Encodes a KMIPContainer and returns a KMIP-Response-Message as a TTLV-encoded hexadecimal 
	 * string stored in an <code>ArrayList{@literal <}Byte{@literal >}</code>.
	 * 
	 * @param c : 	the <code>KMIPContainer</code> to be encoded.
	 * @return		<code>ArrayList{@literal <}Byte{@literal >}</code>
	 */
	public ArrayList<Byte> encodeResponse(KMIPContainer c);

}
