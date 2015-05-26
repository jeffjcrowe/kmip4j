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

package ch.ntb.inf.klms.model.objects;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.Digest;
import ch.ntb.inf.klms.model.klmsenum.EnumCryptographicAlgorithm;

@Entity
public class KeyMaterial {

	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	@Lob
	private byte[] kmByteString;
	
	/**
	 * Transparent Key Structure not supported
	 */
//	@ManyToOne (cascade = CascadeType.ALL)
//	private TransparentKeyStructure transparentKeyStructure;

	
	
	public KeyMaterial(){
		
	}
	
	public KeyMaterial(CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength, Digest digest) throws NoSuchAlgorithmException {
		this.kmByteString = createKeyMaterialByteArray(cryptographicAlgorithm, cryptographicLength);
		createDigest(digest);
	}
	
	public KeyMaterial(HashMap<String, String> parameters) {
		this.kmByteString = parameters.get("Key Material Byte String").getBytes();
	}
	
	public KeyMaterial(HashMap<String, String> parameters, Digest digest) {
		this.kmByteString =  parameters.get("Key Material Byte String").getBytes();
		createDigest(digest);
	}

	public KeyMaterial(byte[] keyMaterial, Digest digest) {
		this.kmByteString = keyMaterial;
		createDigest(digest);
	}
	
	
	
	private byte[] createKeyMaterialByteArray(CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength) throws NoSuchAlgorithmException{
		int alg = Integer.parseInt(cryptographicAlgorithm.getValues()[0].getValueString());
		EnumCryptographicAlgorithm algorithm = new EnumCryptographicAlgorithm(alg);
		KeyGenerator gen = KeyGenerator.getInstance(algorithm.getKey());
		int length = Integer.parseInt(cryptographicLength.getValues()[0].getValueString());
		gen.init(length);
		return gen.generateKey().getEncoded();
	}

	private void createDigest(Digest digest) {
		try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        digest.setValue("SHA256", "Hashing Algorithm");
	        byte[] digestValue = md.digest(kmByteString);
			digest.setValue(new String(digestValue), "Digest Value");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public String getKMString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < kmByteString.length; i++){
			sb.append(String.format("%02X", kmByteString[i]));
		}
		return sb.toString();
	}

//	public TransparentKeyStructure getTransparentKeyStructure() {
//		return transparentKeyStructure;
//	}

	public KeyMaterial renew(CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength, Digest digest) throws NoSuchAlgorithmException {
		return new KeyMaterial(cryptographicAlgorithm, cryptographicLength, digest);
	}
	
}
