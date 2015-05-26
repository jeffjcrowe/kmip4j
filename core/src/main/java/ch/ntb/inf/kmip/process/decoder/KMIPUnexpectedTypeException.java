/**
 * KMIPUnexpectedTypeException.java
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
 * Unexpected Type Exception
 * Appears if the expected type doesn't match to the actual type in 
 * the KMIP-Message. 
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


public class KMIPUnexpectedTypeException extends Exception{

	private static final long serialVersionUID = -5181012948977547485L;

	public KMIPUnexpectedTypeException(String obj, String type){
        super("Unexpected Type of " + obj + " , " + type + " expected");
      } 
    
	public KMIPUnexpectedTypeException(String type){
        super("Unexpected Type, " + type + " expected");
      } 
}
