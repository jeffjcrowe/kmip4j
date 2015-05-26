/**
 * DerivationParameters.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Derivation Parameters is a Structure object containing the 
 * parameters needed by the specified derivation method. It is only
 * used for the operation Derive Key. 
 *
 * The message payload is determined by the specific operation being 
 * requested or to which is being replied. There are additional 
 * parameters depending on the operation, which are placed in the 
 * package ch.ntb.inf.kmip.operationparameters. These parameters are 
 * not defined as Attributes according to the KMIP 1.0 specification,
 * but they are handled like these. That's why they all extend the
 * superclass Attribute. 
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
package ch.ntb.inf.kmip.operationparameters;

import ch.ntb.inf.kmip.attributes.CryptographicParameters;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPInteger;


public class DerivationParameters {

	
	private CryptographicParameters cryptographicParameters;
	private KMIPByteString initializationVector;
	private KMIPByteString derivationData;
	
	// only for PBKDF2
	private KMIPByteString salt;
	private KMIPInteger iterationCount;


	public DerivationParameters() {
		super();
	}
	

	public DerivationParameters(CryptographicParameters cryptographicParameters, KMIPByteString initializationVector, KMIPByteString derivationData) {
		super();
		this.cryptographicParameters = cryptographicParameters;
		this.initializationVector = initializationVector;
		this.derivationData = derivationData;
	}


	

	public CryptographicParameters getCryptographicParameters() {
		return cryptographicParameters;
	}

	public void setCryptographicParameters(CryptographicParameters cryptographicParameters) {
		this.cryptographicParameters = cryptographicParameters;
	}

	public KMIPByteString getInitializationVector() {
		return initializationVector;
	}

	public void setInitializationVector(KMIPByteString initializationVector) {
		this.initializationVector = initializationVector;
	}

	public KMIPByteString getDerivationData() {
		return derivationData;
	}

	public void setDerivationData(KMIPByteString derivationData) {
		this.derivationData = derivationData;
	}
	
	public KMIPByteString getSalt() {
		return salt;
	}

	public void setSalt(KMIPByteString salt) {
		this.salt = salt;
	}

	public KMIPInteger getIterationCount() {
		return iterationCount;
	}

	public void setIterationCount(KMIPInteger iterationCount) {
		this.iterationCount = iterationCount;
	}


	
	
	public boolean hasCryptographicParameters(){
		if(this.cryptographicParameters != null){
			return true;
		}
		return false;
	}
	
	public boolean hasInitializationVector(){
		if(this.initializationVector != null){
			return true;
		}
		return false;
	}
	
	public boolean hasDerivationData(){
		if(this.derivationData != null){
			return true;
		}
		return false;
	}
	
	public boolean hasSalt(){
		if(this.salt != null){
			return true;
		}
		return false;
	}
	
	public boolean hasIterationCount(){
		if(this.iterationCount != null){
			return true;
		}
		return false;
	}
	
	
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Derivation Parameters");
		
		if(hasCryptographicParameters()){
			sb.append("\n"+ this.cryptographicParameters.toString());
		}
		
		if(hasInitializationVector()){
			sb.append("\nInitializationVector: "+ this.initializationVector.getValueString());
		}
		
		if(hasDerivationData()){
			sb.append("\nDerivationData: "+ this.derivationData.getValueString());
		}
		
		if(hasSalt()){
			sb.append("\nSalt: "+ this.salt.getValueString());
		}
		
		if(hasIterationCount()){
			sb.append("\nIterationCount: "+ this.iterationCount.getValueString());
		}
	
		return sb.toString();
	}
	
	
}
