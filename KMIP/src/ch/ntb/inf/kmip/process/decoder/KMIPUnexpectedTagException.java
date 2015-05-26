/**
 * KMIPUnexpectedTagException.java
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
 * Unexpected Tag Exception
 * Appears if the expected tag doesn't match to the actual tag in 
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


public class KMIPUnexpectedTagException extends Exception{

	private static final long serialVersionUID = -140899381069466822L;

	public KMIPUnexpectedTagException(String msg){
        super("Unexpected Tag, " + msg + " expected");
    } 
	
	public KMIPUnexpectedTagException(int msg){
        super("Unexpected Tag: " + msg);
    } 
    
    public KMIPUnexpectedTagException(){
        super("Unexpected Tag");
    }
}
