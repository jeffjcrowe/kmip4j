/**
 * DefaultValues.java
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
package ch.ntb.inf.klms.defaultvalues;

import java.util.ArrayList;

import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.QueryOperation;
import ch.ntb.inf.klms.model.klmsenum.EnumKeyFormatType;
import ch.ntb.inf.klms.model.objects.base.Attribute;



public class DefaultValues {


	public static final int KEY_FORMAT_TYPE = EnumKeyFormatType.Raw;
	
	public static final int LEASE_TIME = 0x10;
	
	public static final ArrayList<Attribute> KLMS_CAPABILITIES_OPERATIONS = new ArrayList<Attribute>();
	
	public static final ArrayList<Attribute> KLMS_CAPABILITIES_OBJECTS = new ArrayList<Attribute>();
	
	static{
		// supported Operations
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Create"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("CreateKeyPair"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Register"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("ReKey"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Locate"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Check"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Get"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("GetAttributes"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("GetAttributeList"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("AddAttribute"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("ModifyAttribute"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("DeleteAttribute"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("ObtainLease"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("GetUsageAllocation"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Activate"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Revoke"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Destroy"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Archive"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Recover"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Query"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Cancel"));
		KLMS_CAPABILITIES_OPERATIONS.add(new QueryOperation("Poll"));
		
		// supported Types
		KLMS_CAPABILITIES_OBJECTS.add(new ObjectType("Certificate"));
		KLMS_CAPABILITIES_OBJECTS.add(new ObjectType("SymmetricKey"));
		KLMS_CAPABILITIES_OBJECTS.add(new ObjectType("PublicKey"));
		KLMS_CAPABILITIES_OBJECTS.add(new ObjectType("PrivateKey"));
		KLMS_CAPABILITIES_OBJECTS.add(new ObjectType("Template"));
		
	}

}
