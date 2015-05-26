/**
 * KMIPTypeInvalidValueException.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete Exception in conjunction with the KMIP-Types: 
 * Invalid Value Exception
 * Appears if the value of a concrete KMIPType is invalid. 
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
package ch.ntb.inf.kmip.types;


public class KMIPTypeInvalidValueException extends Exception{

	private static final long serialVersionUID = -2844330029781466388L;

	public KMIPTypeInvalidValueException(String className, String value){
		super("Invalid value (" + value + ") for KMIPType " + className);
	}
	
	public KMIPTypeInvalidValueException(String className, long value){
		super("Invalid value (" + value + ") for KMIPType " + className);
	}
}
