/**
 * EncryptionKeyInformation.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * Encryption Key Information is a structure, which contains the 
 * Unique Identifier value of the encryption key and associated 
 * cryptographic parameters.
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

package ch.ntb.inf.kmip.objects;

import ch.ntb.inf.kmip.attributes.CryptographicParameters;
import ch.ntb.inf.kmip.attributes.UniqueIdentifier;

public class EncryptionKeyInformation {

	private CryptographicParameters cryptographicParameters;
	private UniqueIdentifier uniqueIdentifier;
	
	public EncryptionKeyInformation() {
	}
	
	public EncryptionKeyInformation(CryptographicParameters cryptographicParameters, UniqueIdentifier uniqueIdentifier) {
		this.cryptographicParameters = cryptographicParameters;
		this.uniqueIdentifier = uniqueIdentifier;
	}

	// Getters & Setters
	public CryptographicParameters getCryptographicParameters() {
		return cryptographicParameters;
	}

	public void setCryptographicParameters(CryptographicParameters cryptographicParameters) {
		this.cryptographicParameters = cryptographicParameters;
	}

	public UniqueIdentifier getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	
	public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	
	public boolean hasCryptographicParameters(){
		if(this.cryptographicParameters != null){
			return true;
		}
		return false;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Encryption Key Information");
		sb.append("\n"+ this.uniqueIdentifier.toString());
		
		if(hasCryptographicParameters()){
			sb.append("\nCryptographic Parameters"+ this.cryptographicParameters.toString());
		}

		return sb.toString();
	}

}
