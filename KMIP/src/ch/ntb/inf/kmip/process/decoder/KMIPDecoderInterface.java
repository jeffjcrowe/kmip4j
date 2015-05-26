/**
 * KMIPDecoderInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPDecoderInterface provides the needful flexibility for
 * the interchangeability of the Decoder. It offers two methods to 
 * decode a message dependent on whether it is a request or a 
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

package ch.ntb.inf.kmip.process.decoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ch.ntb.inf.kmip.container.KMIPContainer;

/**
 * The KMIPDecoderInterface is the interface for all decoders. It 
 * provides the needful flexibility for the interchangeability of 
 * the decoder. It offers two methods to decode a message 
 * dependent on whether it is a request or a response.
 */
public interface KMIPDecoderInterface {
	
	/**
	 * Decodes a KMIP-Request-Message as a TTLV-encoded hexadecimal string stored in an 
	 * <code>ArrayList{@literal <}Byte{@literal >}</code> and returns a <code>KMIPContainer</code>.
	 * 
	 * @param al 	: the <code>ArrayList{@literal <}Byte{@literal >}</code> to be decoded.
	 * @return		<code>KMIPContainer</code> if the request is well formed and no exception was thrown.
	 * @throws KMIPUnexpectedTypeException
	 * @throws KMIPUnexpectedTagException
	 * @throws KMIPPaddingExpectedException
	 * @throws KMIPProtocolVersionException
	 * @throws UnsupportedEncodingException
	 * @throws KMIPUnexpectedAttributeNameException
	 */
	public KMIPContainer decodeRequest(ArrayList<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException;
	
	/**
	 * Decodes a KMIP-Response-Message as a TTLV-encoded hexadecimal string stored in an 
	 * <code>ArrayList{@literal <}Byte{@literal >}</code> and returns a <code>KMIPContainer</code>.
	 * 
	 * @param al 	: the <code>ArrayList{@literal <}Byte{@literal >}</code> to be decoded.
	 * @return		<code>KMIPContainer</code> if the request is well formed and no exception was thrown.
	 * @throws KMIPUnexpectedTypeException
	 * @throws KMIPUnexpectedTagException
	 * @throws KMIPPaddingExpectedException
	 * @throws KMIPProtocolVersionException
	 * @throws UnsupportedEncodingException
	 * @throws KMIPUnexpectedAttributeNameException
	 */
	public KMIPContainer decodeResponse(ArrayList<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException;


}
