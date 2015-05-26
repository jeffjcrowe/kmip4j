/**
 * Digest.java
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

import ch.ntb.inf.klms.model.objects.base.Attribute;


@Entity
@DiscriminatorValue("Digest")
public class Digest extends Attribute {
	
	public Digest(){
		super("Digest", "Digest", "Structure");
		this.values = new ArrayList<KLMSAttributeValue>();
		
		this.values.add(new KLMSAttributeValue("ByteString", "DigestValue"));
		this.values.get(0).setName("Digest Value");
		
		this.values.add(new KLMSAttributeValue("TextString", "HashingAlgorithm"));
		this.values.get(1).setName("Hashing Algorithm");
	}
	
}
