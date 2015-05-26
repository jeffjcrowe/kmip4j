/**
 * SplitKey.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Split Key object is a Managed Cryptographic Object that is a 
 * secret, usually a symmetric key or a private key that has been 
 * split into a number of parts, each of which MAY then be 
 * distributed to several key holders, for additional security.
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
package ch.ntb.inf.kmip.objects.managed;

import ch.ntb.inf.kmip.kmipenum.EnumSplitKeyMethod;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.base.KeyBlock;
import ch.ntb.inf.kmip.types.KMIPBigInteger;
import ch.ntb.inf.kmip.types.KMIPInteger;

public class SplitKey extends CryptographicObject{
	
	private KMIPInteger splitKeyParts;
	private KMIPInteger keyPartIdentifier;
	private KMIPInteger splitKeyThreshhosd;
	private EnumSplitKeyMethod splitKeyMethod;
	private KMIPBigInteger primeFieldSize;
	
	private KeyBlock keyBlock;
	
	public SplitKey() {
		super(new EnumTag(EnumTag.SplitKey));
	}
	
	public SplitKey(KeyBlock keyBlock) {
		this();
		this.keyBlock = keyBlock;
	}
	
	
	// Getters & Setters	
	public KMIPInteger getSplitKeyParts() {
		return splitKeyParts;
	}
	
	public void setSplitKeyParts(KMIPInteger splitKeyParts) {
		this.splitKeyParts = splitKeyParts;
	}

	public KMIPInteger getKeyPartIdentifier() {
		return keyPartIdentifier;
	}

	public void setKeyPartIdentifier(KMIPInteger keyPartIdentifier) {
		this.keyPartIdentifier = keyPartIdentifier;
	}

	public KMIPInteger getSplitKeyThreshhosd() {
		return splitKeyThreshhosd;
	}

	public void setSplitKeyThreshhosd(KMIPInteger splitKeyThreshhosd) {
		this.splitKeyThreshhosd = splitKeyThreshhosd;
	}

	public EnumSplitKeyMethod getSplitKeyMethod() {
		return splitKeyMethod;
	}

	public void setSplitKeyMethod(EnumSplitKeyMethod splitKeyMethod) {
		this.splitKeyMethod = splitKeyMethod;
	}

	public KMIPBigInteger getPrimeFieldSize() {
		return primeFieldSize;
	}

	public void setPrimeFieldSize(KMIPBigInteger primeFieldSize) {
		this.primeFieldSize = primeFieldSize;
	}
	
	public KeyBlock getKeyBlock() {
		return keyBlock;
	}

	public void setKeyBlock(KeyBlock keyBlock) {
		this.keyBlock = keyBlock;
	}
	
	
	public boolean hasPrimeFieldSize(){
		if(this.primeFieldSize != null){
			return true;
		}
		return false;
	}

	
	// to String
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Split Key");
		sb.append("\nSplitKeyParts: "+ splitKeyParts.getValueString());
		sb.append("\nKeyPartID: "+ keyPartIdentifier.getValueString());
		sb.append("\nSplitKeyTreshold: "+ splitKeyThreshhosd.getValueString());
		sb.append("\nSplitKeyMethod: "+ splitKeyMethod.getValueString());
		if(hasPrimeFieldSize()){
			sb.append("\nPrimeFieldSize: "+ primeFieldSize.getValueString());
		}
		sb.append("\n" + keyBlock.toString());	
		return sb.toString();
	}

}
