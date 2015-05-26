/**
 * KMIPPaddingExpectedException.java
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
 * Padding Expected Exception
 * Appears if the padding bytes are missing in one of the following 
 * padded types: Byte String, Text String, Integer
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

public class KMIPPaddingExpectedException extends Exception{

	private static final long serialVersionUID = 850135123043470455L;

	public KMIPPaddingExpectedException(){
        super("Padding Bytes expected");
      } 
}
