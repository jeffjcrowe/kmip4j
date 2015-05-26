/**
 * SymmetricKey.java
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
import java.util.Date;
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
import ch.ntb.inf.klms.model.attributes.ActivationDate;
import ch.ntb.inf.klms.model.attributes.CompromiseOccurrenceDate;
import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.CryptographicParameters;
import ch.ntb.inf.klms.model.attributes.CryptographicUsageMask;
import ch.ntb.inf.klms.model.attributes.DeactivationDate;
import ch.ntb.inf.klms.model.attributes.DestroyDate;
import ch.ntb.inf.klms.model.attributes.Digest;
import ch.ntb.inf.klms.model.attributes.InitialDate;
import ch.ntb.inf.klms.model.attributes.LastChangeDate;
import ch.ntb.inf.klms.model.attributes.LeaseTime;
import ch.ntb.inf.klms.model.attributes.Link;
import ch.ntb.inf.klms.model.attributes.Name;
import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.ProcessStartDate;
import ch.ntb.inf.klms.model.attributes.ProtectStopDate;
import ch.ntb.inf.klms.model.attributes.RevocationReason;
import ch.ntb.inf.klms.model.attributes.State;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.klmsenum.EnumLinkType;
import ch.ntb.inf.klms.model.klmsenum.EnumObjectType;
import ch.ntb.inf.klms.model.klmsenum.EnumState;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.base.KeyBlock;


@Entity
//@DiscriminatorValue("Symmetric Key")
public class SymmetricKey extends CryptographicObject {
	

	
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
			name="SYMMETRICKEY_CRYPTOGRAPHICPARAMETERS",
		    joinColumns= @JoinColumn(name="SYMMETRICKEY_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="CRYPTOGRAPHICPARAMETERS_ID", referencedColumnName="ID")
	)
	private Set<CryptographicParameters> cryptographicParameters;

	
	
	public SymmetricKey(){
		super();
	}
	
	
	public SymmetricKey(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		super(parameters, "SymmetricKey");
		this.objectType = new ObjectType(Integer.toString(EnumObjectType.SymmetricKey));
		this.digests = new HashSet<Digest>();
		Digest digest = new Digest();
		this.digests.add(digest);
		this.keyBlock = new KeyBlock(parameters, digest);
	}
	
	
	public KeyBlock getKeyBlock(){
		return keyBlock;
	}
	
	private void setKeyBlock(KeyBlock keyBlock) {
		this.keyBlock = keyBlock;
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
		else if(attrib instanceof CryptographicAlgorithm){
			this.keyBlock.setAttribute(attrib);
		}
		else if(attrib instanceof CryptographicLength){
			this.keyBlock.setAttribute(attrib);
		}
		else if(attrib instanceof ProcessStartDate){
			this.processStartDate = (ProcessStartDate) attrib;
			return true;
		}
		else if(attrib instanceof ProtectStopDate){
			this.protectStopDate = (ProtectStopDate) attrib;
			return true;
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

	public SymmetricKey reKey(String offsetValue) throws NoSuchAlgorithmException {

		SymmetricKey newKey  = new SymmetricKey();
		Attribute[] lifeCycleDates = new Attribute[5];
	
		Date currentTime = new Date();
		long it2 = currentTime.getTime();
		InitialDate initialDate = new InitialDate(currentTime.toString());
		newKey.addAttribute(initialDate);
		lifeCycleDates[0] = initialDate;
		
		newKey.addAttribute(new LastChangeDate(currentTime.toString()));
		newKey.addAttribute(new UniqueIdentifier(generateUID("SymmetricKey")));

		// Offset
		long offset = 0;
		if(offsetValue != null){
			offset = Long.parseLong(offsetValue);
		}
		
		CryptographicAlgorithm cryptographicAlgorithm = null;
		CryptographicLength cryptographicLength = null;

		long at1 = 0, at2 = 0, ct1 = 0, ct2, tt1 = 0, tt2, dt1 = 0, dt2;
		ArrayList<Attribute> attributes = this.getAttributes();
		for(Attribute a : attributes){
			if(a instanceof State || a instanceof InitialDate || a instanceof DestroyDate || a instanceof CompromiseOccurrenceDate || a instanceof RevocationReason
					|| a instanceof Digest || a instanceof Link || a instanceof UniqueIdentifier || a instanceof LastChangeDate){
				// do nothing
			} 
			else if(a instanceof ActivationDate){
				at1 = Long.parseLong(a.getValues()[0].getValueString());
				at2 = currentTime.getTime()+offset;
				ActivationDate ad = new ActivationDate(at2);
				newKey.addAttribute(ad);
				lifeCycleDates[1] = ad;
			}
			else if(a instanceof ProcessStartDate){
				ct1 = Long.parseLong(a.getValues()[0].getValueString());
				ProcessStartDate psd = new ProcessStartDate();
				newKey.addAttribute(psd);
				lifeCycleDates[2] = psd;
			}
			else if(a instanceof ProtectStopDate){
				tt1 = Long.parseLong(a.getValues()[0].getValueString());
				ProtectStopDate psd = new ProtectStopDate();
				newKey.addAttribute(psd);
				lifeCycleDates[3] = psd;
			}
			else if(a instanceof DeactivationDate){
				dt1 = Long.parseLong(a.getValues()[0].getValueString());
				DeactivationDate dd = new DeactivationDate();
				newKey.addAttribute(dd);
				lifeCycleDates[4] = dd;
			}
			else if(a instanceof UsageLimits){
				newKey.addAttribute(((UsageLimits) a).getUsageLimitsForReKey());
			}
			else if(a instanceof Name){
				newKey.addAttribute(a);
				this.removeAttribute(a);
			}
			else if(a instanceof CryptographicAlgorithm){
				cryptographicAlgorithm = (CryptographicAlgorithm) a;
			}
			else if(a instanceof CryptographicLength){
				cryptographicLength = (CryptographicLength) a;
			}
			else{
				newKey.addAttribute(a);
			}
		}
		
		// set Dates, if Attribute not set --> MAX_Value, so State is set correctly
		if(ct1 == 0){
			ct2 = Long.MAX_VALUE; 
		} else{
			ct2 = ct1 + (at2 - at1);
		}
		
		if(tt1 == 0){
			tt2 = Long.MAX_VALUE; 
		} else{
			tt2 = tt1 + (at2 - at1);
		}
		
		if(dt1 == 0){
			dt2 = Long.MAX_VALUE; 
		} else{
			dt2 = dt1 + (at2 - at1);
		}


		if(lifeCycleDates[2] != null){
			lifeCycleDates[2].setValue(Long.toString(ct2), null);
		}
		if(lifeCycleDates[3] != null){
			lifeCycleDates[3].setValue(Long.toString(tt2), null);
		}
		if(lifeCycleDates[4] != null){
			lifeCycleDates[4].setValue(Long.toString(dt2), null);
		}
		
		HashMap<String, Long> lifeCycleDateList = new HashMap<String, Long>();
		lifeCycleDateList.put("it2", it2);	// current date
		lifeCycleDateList.put("at2", at2);	
		lifeCycleDateList.put("dt2", dt2);	
		
		setState(lifeCycleDateList, newKey);
		
		setDigest(newKey, cryptographicAlgorithm, cryptographicLength);
		
		setLinks(newKey);
		
		// set Lease Time = 0x00, because of use-case 9.5 (8) Obtain Lease
		newKey.addAttribute(new LeaseTime("0"));
		
		return newKey;
	}
	
	private void setState(HashMap<String, Long> lifeCycleDateList, SymmetricKey newKey) {
		if(lifeCycleDateList.get("dt2") <= lifeCycleDateList.get("it2")){
			newKey.addAttribute(new State(Integer.toString(EnumState.Deactivated)));	
		}
		else if(lifeCycleDateList.get("at2") <= lifeCycleDateList.get("it2")){
			newKey.addAttribute(new State(Integer.toString(EnumState.Active)));	
		}
		else{
			newKey.addAttribute(new State(Integer.toString(EnumState.PreActive)));	
		}
	}


	private void setDigest(SymmetricKey newKey, CryptographicAlgorithm cryptographicAlgorithm, CryptographicLength cryptographicLength) throws NoSuchAlgorithmException{
		Digest digest = new Digest();
		newKey.addAttribute(digest);
		newKey.setKeyBlock(this.getKeyBlock().renew(cryptographicAlgorithm, cryptographicLength, digest));
	}
	
	private void setLinks(SymmetricKey newKey){
		Link linkToNewKey = new Link();
		linkToNewKey.setValue(Integer.toString(EnumLinkType.ReplacementObjectLink), "Link Type");
		linkToNewKey.setValue(newKey.getUniqueIdentifierValue(), "Linked Object Identifier");
		this.addAttribute(linkToNewKey);

		Link linkToOldKey = new Link();
		linkToOldKey.setValue(Integer.toString(EnumLinkType.ReplacedObjectLink), "Link Type");
		linkToOldKey.setValue(this.getUniqueIdentifierValue(), "Linked Object Identifier");
		this.addAttribute(linkToOldKey);
	}
	
}
