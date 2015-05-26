/**
 * Template.java
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.ActivationDate;
import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicDomainParameters;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.CryptographicParameters;
import ch.ntb.inf.klms.model.attributes.CryptographicUsageMask;
import ch.ntb.inf.klms.model.attributes.DeactivationDate;
import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.OperationPolicyName;
import ch.ntb.inf.klms.model.attributes.ProcessStartDate;
import ch.ntb.inf.klms.model.attributes.ProtectStopDate;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.objects.base.Attribute;


@Entity
public class Template extends ManagedObject {

	
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="TEMPLATE_ATTRIBUTES",
		    joinColumns= @JoinColumn(name="TEMPLATE_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="ATTRIBUTE_ID", referencedColumnName="ID")
	)
	private List<Attribute> attributes;
	
	
	
	public Template(){}
	
	
	public Template(HashMap<String, String> parameters){
		super(parameters, "Template");
		attributes = new ArrayList<Attribute>();
		
		
		//default
		this.objectType = new ObjectType("Template");
		
		
		//optional
		if(parameters.containsKey("Cryptographic Algorithm")){
			attributes.add(new CryptographicAlgorithm(parameters.get("Cryptographic Algorithm"+ "value" + "1")));
		}
		if(parameters.containsKey("Cryptographic Length")){
			attributes.add(new CryptographicLength(parameters.get("Cryptographic Length"+ "value" + "1")));
		}
		if(parameters.containsKey("Cryptographic Domain Parameters")){
			CryptographicDomainParameters a = new CryptographicDomainParameters();
			a.setValue(parameters.get("Recommended Curve" + "value" + "1"), "Recommended Curve");
			a.setValue(parameters.get("QLength" + "value"+ "1"), "QLength");
			attributes.add(a);
		}
		if(parameters.containsKey("Cryptographic Parameters")){
			CryptographicParameters n = new CryptographicParameters();
			n.setValue(parameters.get("Key Role Type" + "value"+ "1"), "Key Role Type");
			n.setValue(parameters.get("Padding Method" + "value"+ "1"), "Padding Method");
			n.setValue(parameters.get("Block Cipher Mode"+ "value" + "1"), "Block Cipher Mode");
			attributes.add(n); 

			int count = Integer.parseInt(parameters.get("Cryptographic Parameters"));
			for(int i = 2; i <= count; i++){
				n = new CryptographicParameters();
				n.setValue(parameters.get("Key Role Type" + "value"+ i), "Key Role Type");
				n.setValue(parameters.get("Padding Method" + "value"+ i), "Padding Method");
				n.setValue(parameters.get("Block Cipher Mode" + "value"+ i), "Block Cipher Mode");
				attributes.add(n); 
			}
		} 
		if(parameters.containsKey("Operation Policy Name")){
			attributes.add(new OperationPolicyName(parameters.get("Operation Policy Name"+ "value" + "1")));
		}
		if(parameters.containsKey("Cryptographic Usage Mask")){
			attributes.add(new CryptographicUsageMask(parameters.get("Cryptographic Usage Mask"+ "value" + "1")));
		}
		if(parameters.containsKey("Usage Limits")){
			UsageLimits a = new UsageLimits();
			a.setValue(parameters.get("Usage Limits Unit" + "value"+ "1"), "Usage Limits Unit");
			a.setValue(parameters.get("Usage Limits Count" + "value"+ "1"), "Usage Limits Count");
			a.setValue(parameters.get("Usage Limits Total" + "value"+ "1"), "Usage Limits Total");
			attributes.add(a);
		}
		if(parameters.containsKey("Activation Date")){
			attributes.add(new ActivationDate(parameters.get("Activation Date"+ "value" + "1")));
		}
		if(parameters.containsKey("Process Start Date")){
			attributes.add(new ProcessStartDate(parameters.get("Process Start Date"+ "value" + "1")));
		}
		if(parameters.containsKey("Protect Stop Date")){
			attributes.add(new ProtectStopDate(parameters.get("Protect Stop Date"+ "value" + "1")));
		}
		if(parameters.containsKey("Deactivation Date")){
			attributes.add(new DeactivationDate(parameters.get("Deactivation Date"+ "value" + "1")));
		}
	
	}
	
	
	public ArrayList<Attribute> getAttributesForCreate(){
		ArrayList<Attribute> attributes = super.getAttributesForCreate();
		if(this.attributes != null){
			attributes.addAll(this.attributes);
		}
		return attributes;
	}
	
	public ArrayList<Attribute> getAttributes(){
		ArrayList<Attribute> attributes = super.getAttributes();
		if(this.attributes != null){
			attributes.addAll(this.attributes);
		}
		return attributes;
	}


	public void activate() throws KLMSIllegalOperationException {
		throw new KLMSIllegalOperationException("Activate", "Template");
	}

	public void revoke(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException {
		throw new KLMSIllegalOperationException("Revoke", "Template");
	}
	
	public ArrayList<Attribute> obtainLease() throws KLMSPermissionDeniedException{
		throw new KLMSPermissionDeniedException("Obtain Lease not allowed for Objects of type Template");
	}

	public ArrayList<Attribute> check(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException {
		throw new KLMSIllegalOperationException("Check", "Template");
	}

	public void getUsageAllocation(UsageLimits usageLimits) throws KLMSPermissionDeniedException {
		throw new KLMSPermissionDeniedException("Get Usage Allocation not allowed for Objects of type Template");
	}
	
}
