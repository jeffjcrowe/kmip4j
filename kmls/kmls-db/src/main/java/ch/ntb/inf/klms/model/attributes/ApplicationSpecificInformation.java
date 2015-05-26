/**
 * ApplicationSpecificInformation.java
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


package ch.ntb.inf.klms.model.attributes;

import java.util.ArrayList;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ch.ntb.inf.klms.model.objects.base.Attribute;

@Entity
@DiscriminatorValue("Application Specific Information")
public class ApplicationSpecificInformation extends Attribute {
	
	
	public ApplicationSpecificInformation(){
		super("Application Specific Information", "ApplicationSpecificInformation", "Structure");
		this.values = new ArrayList<KLMSAttributeValue>();
		
		this.values.add(new KLMSAttributeValue("TextString", "ApplicationNamespace"));
		this.values.get(0).setName("Application Namespace");
		
		this.values.add(new KLMSAttributeValue("TextString", "ApplicationData"));
		this.values.get(1).setName("Application Data");
	}

}
