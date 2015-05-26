/**
 * PrivateKey.java
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.CryptographicParameters;
import ch.ntb.inf.klms.model.attributes.CryptographicUsageMask;
import ch.ntb.inf.klms.model.attributes.Digest;
import ch.ntb.inf.klms.model.attributes.LeaseTime;
import ch.ntb.inf.klms.model.attributes.Link;
import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.ProcessStartDate;
import ch.ntb.inf.klms.model.attributes.ProtectStopDate;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.klmsenum.EnumLinkType;
import ch.ntb.inf.klms.model.klmsenum.EnumObjectType;
import ch.ntb.inf.klms.model.klmsenum.EnumState;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.base.KeyBlock;


@Entity
public class PrivateKey extends CryptographicObject{
	

	// required
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private KeyBlock keyBlock;
	
	
	
	// optional

	@ManyToOne ( cascade = CascadeType.ALL)
	private ProcessStartDate processStartDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private ProtectStopDate protectStopDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private UsageLimits usageLimits;
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="PRIVATEKEY_CRYPTOGRAPHICPARAMETERS",
		    joinColumns= @JoinColumn(name="PRIVATEKEY_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="CRYPTOGRAPHICPARAMETERS_ID", referencedColumnName="ID")
	)
	private Set<CryptographicParameters> cryptographicParameters;

	
	
	public PrivateKey(){
		super();
	}
	
	public PrivateKey(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		super(parameters, "PrivateKey");
		this.objectType = new ObjectType(Integer.toString(EnumObjectType.PrivateKey));
		this.digests = new HashSet<Digest>();
		Digest digest = new Digest();
		this.digests.add(digest);
		this.keyBlock = new KeyBlock(parameters, digest);
	}
	
	public PrivateKey(ArrayList<Attribute> attributes, CryptographicAlgorithm ca, CryptographicLength len, byte[] keyMaterial) throws NoSuchAlgorithmException {
		super(attributes, "PrivateKey");
		this.objectType = new ObjectType(Integer.toString(EnumObjectType.PrivateKey));
		this.digests = new HashSet<Digest>();
		Digest digest = new Digest();
		this.digests.add(digest);
		this.keyBlock = new KeyBlock(ca, len, keyMaterial, digest);
	}
	
	
	public KeyBlock getKeyBlock(){
		return keyBlock;
	}
	
	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = super.getAttributes();
		if (processStartDate != null) {
			attributes.add(processStartDate);
		}
		if (protectStopDate != null) {
			attributes.add(protectStopDate);
		}
		if (usageLimits != null) {
			attributes.add(usageLimits);
		}
		if (cryptographicParameters != null) {
			attributes.addAll(cryptographicParameters);
		}
		if (keyBlock != null) {
			attributes.addAll(keyBlock.getAttributes());
		}
		return attributes;

	}

	public boolean addAttribute(Attribute attrib) {
		if(super.addAttribute(attrib)) {
			return true;
		}
		
		if(attrib instanceof CryptographicParameters){
			if(cryptographicParameters == null){
				cryptographicParameters = new HashSet<CryptographicParameters>();
			}
			cryptographicParameters.add((CryptographicParameters) attrib);
			return true;
		} 
		else if(attrib instanceof UsageLimits){
			((UsageLimits) attrib).initUsageLimitsCount();
			this.usageLimits = (UsageLimits) attrib;
		}
		else if(this.state.getValues()[0].getValueString().equals(Integer.toString(EnumState.PreActive))){
			if(attrib instanceof ProcessStartDate){
				this.processStartDate = (ProcessStartDate) attrib;
				return true;
			}
			else if(attrib instanceof ProtectStopDate){
				this.protectStopDate = (ProtectStopDate) attrib;
				return true;
			}
		}
		return false;
	}

	public ArrayList<Attribute> check(ArrayList<Attribute> attributes){
		ArrayList<Attribute> returnAttributes = new ArrayList<Attribute>();
		for(Attribute a : attributes){
			if(a instanceof UsageLimits){
				if(((UsageLimits) a).getUsageLimitsCount() > this.usageLimits.getUsageLimitsCount()){
					returnAttributes.add(a);
				}
			} else if(a instanceof CryptographicUsageMask){
				int usage = ((CryptographicUsageMask) a).getUsageMask();
				int usageMask = this.cryptographicUsageMask.getUsageMask();
				if( (usage & usageMask) != usage){
					returnAttributes.add(a);
				}
			} else if(a instanceof LeaseTime){
				if(((LeaseTime) a).getLeaseTime() > this.leaseTime.getLeaseTime()){
					returnAttributes.add(a);
				}
			}
		}
		return returnAttributes;
	}
	
	public void getUsageAllocation(UsageLimits usageLimits) throws KLMSPermissionDeniedException{
		int usageAllocation = usageLimits.getUsageLimitsCount();
		int usageLimitsCount = this.usageLimits.getUsageLimitsCount();
		if(usageAllocation <= usageLimitsCount){
			this.usageLimits.setValue(Integer.toString(usageLimitsCount - usageAllocation), "Usage Limits Count");
		} else{
			throw new KLMSPermissionDeniedException("Requested Usage Allocation exceeds Usage Limits Count");
		}
	}

	public void setLink(String uniqueIdentifierValue) {
		if(this.links == null){
			links = new HashSet<Link>();
		}
		Link l = new Link();
		l.setValue(uniqueIdentifierValue, "Linked Object Identifier");
		l.setValue(Integer.toString(EnumLinkType.PublicKeyLink), "Link Type");
		links.add(l);
	}
}
