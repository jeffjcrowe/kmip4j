/**
 * KMIPUnexpectedAttributeNameException.java
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
 * Unexpected Attribute Name Exception
 * Appears if the dynamically loaded attribute, over the fully 
 * qualified name, is no valid attribute, respectively was loaded 
 * with an invalid name. 
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

public class KMIPUnexpectedAttributeNameException extends Exception{

	private static final long serialVersionUID = 8181697119724843906L;

	public KMIPUnexpectedAttributeNameException(){
        super("Unexpected Attribute Name");
    } 
	
	public KMIPUnexpectedAttributeNameException(String name){
        super("Unexpected Attribute Name " + name);
    } 
}
