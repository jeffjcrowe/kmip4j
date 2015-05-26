/**
 * KMIPStubInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Stub encapsulates the whole KMIP functionality of the
 * client side. To process a request, it encodes the request, 
 * sends it to the server over the transport layer, and finally 
 * decodes and returns the response.  
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

package ch.ntb.inf.kmip.stub;

import ch.ntb.inf.kmip.container.KMIPContainer;

/**
 * The KMIPStubInterface is the interface for all stubs. It 
 * provides the needful flexibility for the interchangeability of 
 * the stub.
 * The Stub encapsulates the whole KMIP functionality of the
 * server side. To process a request, it offers two superimposed 
 * methods:
 * <ul>
 * 	<li><code>processRequest(KMIPContainer c)</code> for common use</li>
 * 	<li><code>processRequest(KMIPContainer c, String expectedTTLVRequest, String expectedTTLVResponse)</code> for test cases</li>
 * </ul>
 */
public interface KMIPStubInterface {

	/**
	 * Processes a KMIP-Request-Message stored in a <code>KMIPContainer</code> and returns a corresponding KMIP-Response-Message.
	 * 
	 * @param c :      	the <code>KMIPContainer</code> to be encoded and sent. 
	 * @return			<code>KMIPContainer</code> with the response objects.
	 */
	public KMIPContainer processRequest(KMIPContainer c);

	/**
	 * Processes a KMIP-Request-Message stored in a <code>KMIPContainer</code> and returns a corresponding KMIP-Response-Message.
	 * For test cases, there are two additional parameters that may be set by the caller. The idea is, that the generated TTLV-Strings 
	 * can be compared to the expected TTLV-Strings. 
	 * 
	 * @param c :      	the <code>KMIPContainer</code> to be encoded and sent. 
	 * @param expectedTTLVRequest :      	the <code>String</code> to be compared to the encoded request message. 
	 * @param expectedTTLVResponse :      	the <code>String</code> to be compared to the decoded response message. 
	 * @return			<code>KMIPContainer</code> with the response objects.
	 */
	public KMIPContainer processRequest(KMIPContainer c, String expectedTTLVRequest, String expectedTTLVResponse);
}
