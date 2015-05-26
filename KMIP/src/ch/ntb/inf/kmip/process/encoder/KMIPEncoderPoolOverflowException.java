/**
 * KMIPEncoderPoolOverflowException.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete Exception in conjunction with the encoder: 
 * Encoder Pool Overflow Exception
 * Appears if the encoder pool is empty and a new instance of an 
 * encoder should be generated, but the maximum number of encoders,
 * defined in the KMIPEncoderPool, has reached. 
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

public class KMIPEncoderPoolOverflowException extends Exception {

	private static final long serialVersionUID = -8958423205345013137L;

	KMIPEncoderPoolOverflowException(int max){
		super("PoolOverflowException:: Max number ("+max+") reached! Wait until some get returned");
	}
	
}
