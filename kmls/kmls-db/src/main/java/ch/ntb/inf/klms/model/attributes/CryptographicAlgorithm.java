/**
 * CryptographicAlgorithm.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 *
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
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

package ch.ntb.inf.klms.model.attributes;

import java.util.ArrayList;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ch.ntb.inf.klms.model.klmsenum.EnumCryptographicAlgorithm;
import ch.ntb.inf.klms.model.objects.base.Attribute;


@Entity
@DiscriminatorValue("Cryptographic Algorithm")
public class CryptographicAlgorithm extends Attribute {

	
	public CryptographicAlgorithm(){
		super("Cryptographic Algorithm", "CryptographicAlgorithm", "Enumeration");
		this.values = new ArrayList<KLMSAttributeValue>();
		this.values.add(new KLMSAttributeValue("Enumeration", "CryptographicAlgorithm"));
		this.values.get(0).setName(this.getAttributeName());
	}
	
	public CryptographicAlgorithm(String value){
		this();
		this.values.get(0).setValue(value);
	}
	
	public String getAlgorithm(){
		int value = Integer.parseInt(this.values.get(0).getValueString());
		EnumCryptographicAlgorithm ca = new EnumCryptographicAlgorithm(value);
		return ca.getKey();
	}
	
}
