/**
 * KeyMaterial.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Key Material, is either a Byte String or a Transparent Key 
 * Structure. 
 * 
 * Byte String: for Raw, Opaque, PKCS1, PKCS8, ECPrivateKey, or 
 * 				Extension Key Format types
 * 
 * Structure: 	for Transparent, or Extension Key Format Types
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

import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.objects.base.TransparentKeyStructure;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.utils.KMIPUtils;

public class KeyMaterial {	
	

	private TransparentKeyStructure keyMaterialStructure;
	private KMIPByteString keyMaterialByteString;
	
	private EnumTag tag = new EnumTag(EnumTag.KeyMaterial);


	public KeyMaterial(KMIPByteString keyMaterial) {
		this.keyMaterialByteString = keyMaterial;
	}
	
	public KeyMaterial(String keyMaterial) {
		this.keyMaterialByteString = new KMIPByteString(keyMaterial);
	}

	public KeyMaterial(TransparentKeyStructure keyMaterialStructure) {
		this.keyMaterialStructure = keyMaterialStructure;
	}

	public KeyMaterial() {}




	public EnumTag getTag() {
		return tag;
	}
	
	public void setKeyMaterialByteString(String keyMaterial) {
		keyMaterialByteString = new KMIPByteString(KMIPUtils.convertHexStringToByteArray(keyMaterial));
	}

	public void setKeyMaterialStructure(TransparentKeyStructure keyMaterialStructure) {
		this.keyMaterialStructure = keyMaterialStructure;
	}

	public TransparentKeyStructure getTransparentKeyStructure() {
		return keyMaterialStructure;
	}

	public KMIPByteString getKeyMaterialByteString() {
		return keyMaterialByteString;
	}
	
	
	public boolean hasKeyMaterialStructure(){
		if(keyMaterialStructure != null){
			return true;
		}
		return false;
	}
	
	public boolean hasKeyMaterialByteString(){
		if(keyMaterialByteString != null){
			return true;
		}
		return false;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\tKey Material: [");
		
		if(hasKeyMaterialByteString()){
			sb.append(keyMaterialByteString.getValueString() + "]");
		}		
		if(hasKeyMaterialStructure()){
			sb.append(keyMaterialStructure.toString() + "]");
		}	
		return sb.toString();
	}

}
