/**
 * UsageLimits.java
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
@DiscriminatorValue("Usage Limits")
public class UsageLimits extends Attribute {

	public UsageLimits(){
		super("Usage Limits", "UsageLimits", "Structure");
		this.values = new ArrayList<KLMSAttributeValue>();
		
		this.values.add(new KLMSAttributeValue("Enumeration", "UsageLimitsUnit"));
		this.values.get(0).setName("Usage Limits Unit");
		
		this.values.add(new KLMSAttributeValue("Integer", "UsageLimitsCount"));
		this.values.get(1).setName("Usage Limits Count");
		
		this.values.add(new KLMSAttributeValue("Integer", "UsageLimitsTotal"));
		this.values.get(2).setName("Usage Limits Total");
	}


	public void initUsageLimitsCount() {
		this.values.get(1).setValue(this.values.get(2).getValueString());
	}
	
	public int getUsageLimitsCount(){
		return Integer.parseInt(this.values.get(1).getValueString());
	}



	public UsageLimits getUsageLimitsForReKey() {
		UsageLimits newUsageLimits = new UsageLimits();
		newUsageLimits.setValue(this.values.get(0).getValueString(), "Usage Limits Unit");
		
		// set Usage Limits Count to Total Value
		newUsageLimits.setValue(this.values.get(2).getValueString(), "Usage Limits Count");
		newUsageLimits.setValue(this.values.get(2).getValueString(), "Usage Limits Total");
		return newUsageLimits;
	}

	
}
