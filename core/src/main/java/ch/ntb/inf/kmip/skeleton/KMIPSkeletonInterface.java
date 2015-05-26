/**
 * KMIPSkeletonInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Skeleton encapsulates the whole KMIP functionality of the
 * server side. It has an encoder pool, a decoder pool and an 
 * adapter to the KLMS. To process a request, it decodes the 
 * request, processes the separated batches of the request via the
 * adapter to the KLMS, encodes and returns the response.  
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright © 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 */

package ch.ntb.inf.kmip.skeleton;

import java.util.ArrayList;

/**
 * The KMIPSkeletonInterface is the interface for all Skeletons. It 
 * provides the needful flexibility for the interchangeability of 
 * the skeleton.
 * The Skeleton encapsulates the whole KMIP functionality of the
 * server side. To process a request, it offers a method 
 * processRequest(..).
 */
public interface KMIPSkeletonInterface {
	/**
	 * Processes a KMIP-Request-Message as a TTLV-encoded hexadecimal string stored in an 
	 * <code>ArrayList{@literal <}Byte{@literal >}</code> and returns a corresponding KMIP-Response-Message.
	 * 
	 * @param request :       	the KMIP-Message to be processed
	 * @return					<code>ArrayList{@literal <}Byte{@literal >}</code>
	 */
	public ArrayList<Byte> processRequest(ArrayList<Byte> request);
}
