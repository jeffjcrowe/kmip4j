/**
 * KMIPDecoderPoolOverflowException.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * Concrete Exception in conjunction with the decoder: 
 * Decoder Pool Overflow Exception
 * Appears if the decoder pool is empty and a new instance of a 
 * decoder should be generated, but the maximum number of decoders,
 * defined in the KMIPDecoderPool, has reached. 
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


public class KMIPDecoderPoolOverflowException extends Exception {

	private static final long serialVersionUID = -3493942653446466119L;

	KMIPDecoderPoolOverflowException(int max){
		super("DecoderPoolOverflowException:: Max number ("+max+") reached! Wait until some get returned");
	}
	
}
