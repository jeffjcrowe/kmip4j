/**
 * SecretData.java
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
package ch.ntb.inf.klms.model.objects.managed;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.klmsenum.EnumObjectType;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.base.KeyBlock;

@Entity
public class SecretData extends CryptographicObject {
	
	
	// required
	private String secretDataType;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private KeyBlock keyBlock;
	
	

	public SecretData(){
		super();
	}
	
	public SecretData(HashMap<String, String> parameters) {
		super(parameters, "SecretData");
		this.objectType = new ObjectType(Integer.toString(EnumObjectType.SecretData));
		this.keyBlock = new KeyBlock(parameters);
		this.setSecretDataType(parameters.get("Secret Data Type"));

	}

	public String getSecretDataType() {
		return secretDataType;
	}

	public void setSecretDataType(String secretDataType) {
		this.secretDataType = secretDataType;
	}
	

	public ArrayList<Attribute> check(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException {
		return null;
	}

	public void getUsageAllocation(UsageLimits usageLimits) throws KLMSPermissionDeniedException {
		throw new KLMSPermissionDeniedException("Get Usage Allocation not allowed for Objects of type Secret Data");
	}

}
