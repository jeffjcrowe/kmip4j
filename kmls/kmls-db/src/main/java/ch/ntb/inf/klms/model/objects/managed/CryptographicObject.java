/**
 * CryptographicObject.java
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.ActivationDate;
import ch.ntb.inf.klms.model.attributes.CompromiseDate;
import ch.ntb.inf.klms.model.attributes.CompromiseOccurrenceDate;
import ch.ntb.inf.klms.model.attributes.CryptographicUsageMask;
import ch.ntb.inf.klms.model.attributes.DeactivationDate;
import ch.ntb.inf.klms.model.attributes.DestroyDate;
import ch.ntb.inf.klms.model.attributes.Digest;
import ch.ntb.inf.klms.model.attributes.LeaseTime;
import ch.ntb.inf.klms.model.attributes.Link;
import ch.ntb.inf.klms.model.attributes.RevocationReason;
import ch.ntb.inf.klms.model.attributes.State;
import ch.ntb.inf.klms.model.klmsenum.EnumRevocationReasonCode;
import ch.ntb.inf.klms.model.klmsenum.EnumState;
import ch.ntb.inf.klms.model.objects.base.Attribute;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class CryptographicObject extends ManagedObject {	


	// required
	
	@ManyToOne (  cascade = {CascadeType.PERSIST,  CascadeType.MERGE, CascadeType.REFRESH})
	protected State state;
	
	@ManyToOne (  cascade = {CascadeType.PERSIST,  CascadeType.MERGE, CascadeType.REFRESH})
	protected CryptographicUsageMask cryptographicUsageMask;
	
	
	// optional
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="CRYPTOGRAPHICOBJECT_DIGESTS",
		    joinColumns= @JoinColumn(name="CRYPTOGRAPHICOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="DIGEST_ID", referencedColumnName="ID")
	)
	protected Set<Digest> digests;
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="CRYPTOGRAPHICOBJECT_LINKS",
		    joinColumns= @JoinColumn(name="CRYPTOGRAPHICOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="LINK_ID", referencedColumnName="ID")
	)
	protected Set<Link> links;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private RevocationReason revocationReason;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private CompromiseDate compromiseDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private CompromiseOccurrenceDate compromiseOccurrenceDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private DestroyDate destroyDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private DeactivationDate deactivationDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private ActivationDate activationDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	protected LeaseTime leaseTime;
	
	
	

	public CryptographicObject(HashMap<String, String> parameters, String objectType) {
		super(parameters, objectType);
		
		//default
		this.state = new State(Integer.toString(EnumState.PreActive));
		this.leaseTime = new LeaseTime("16");
		
		if(parameters.containsKey("Cryptographic Usage Mask")){
			this.cryptographicUsageMask = new CryptographicUsageMask(parameters.get("Cryptographic Usage Mask"  + "value" + "1"));
		} else {
			this.cryptographicUsageMask = new CryptographicUsageMask();
		}
		
		if(parameters.containsKey("Activation Date")){
			this.activationDate = new ActivationDate(parameters.get("Activation Date"  + "value" + "1"));
			if(new Date().getTime() >= Long.parseLong(this.activationDate.getValues()[0].getValueString())){
				this.state = new State(Integer.toString(EnumState.Active));
			}
		} 
		
		if(parameters.containsKey("Link")){
			this.links = new HashSet<Link>();
			Link a = new Link();
			a.setValue(parameters.get("Link Type" + "value"+ "1"), "Link Type");
			a.setValue(parameters.get("Linked Object Identifier" + "value"+ "1"), "Linked Object Identifier");
			links.add(a); 

			int count = Integer.parseInt(parameters.get("Link"));
			for(int i = 2; i <= count; i++){
				Link link = new Link();
				link.setValue(parameters.get("Link Type" + "value"+ "1"), "Link Type");
				link.setValue(parameters.get("Linked Object Identifier" + "value"+ "1"), "Linked Object Identifier");
				links.add(link); 
			}
		} 
	}


	public CryptographicObject() {
		super();
	}
	
	
	public CryptographicObject(ArrayList<Attribute> attributes, String objectType) {
		super(attributes, objectType);
		
		//default
		this.state = new State(Integer.toString(EnumState.PreActive));
		this.leaseTime = new LeaseTime("16");
		
		for(Attribute a : attributes){
			if(a instanceof CryptographicUsageMask){
				this.cryptographicUsageMask = (CryptographicUsageMask) a;
			}
		}
	}


	public ArrayList<Attribute> getAttributes() {
		ArrayList<Attribute> attributes = super.getAttributes();
		if (state != null) {
			attributes.add(state);
		}
		if (cryptographicUsageMask != null) {
			attributes.add(cryptographicUsageMask);
		}
		if (digests != null) {
			attributes.addAll(digests);
		}
		if (links != null) {
			attributes.addAll(links);
		}
		if (revocationReason != null) {
			attributes.add(revocationReason);
		}
		if (activationDate != null) {
			attributes.add(activationDate);
		}
		if (compromiseDate != null) {
			attributes.add(compromiseDate);
		}
		if (compromiseOccurrenceDate != null) {
			attributes.add(compromiseOccurrenceDate);
		}
		if (destroyDate != null) {
			attributes.add(destroyDate);
		}
		if (deactivationDate != null) {
			attributes.add(deactivationDate);
		}
		return attributes;

	}
	
	public boolean addAttribute(Attribute attrib) {
		if(super.addAttribute(attrib)) {
			return true;
		}
		
		if(attrib instanceof Link){
			if(links == null){
				links = new HashSet<Link>();
			}
			links.add((Link) attrib);
			return true;
		} 
		else if(attrib instanceof Digest){
			if(digests == null){
				digests = new HashSet<Digest>();
			}
			digests.add((Digest) attrib);
			return true;
		} 
		else if(attrib instanceof State){
			this.state = (State) attrib;
			return true;
		}
		else if(attrib instanceof LeaseTime){
			this.leaseTime = (LeaseTime) attrib;
			return true;
		}
		else if(attrib instanceof CryptographicUsageMask){
			this.cryptographicUsageMask = (CryptographicUsageMask) attrib;
			return true;
		}
		else if(attrib instanceof DeactivationDate){
			this.deactivationDate = (DeactivationDate) attrib;
			return true;
		}
		else if(attrib instanceof ActivationDate){
			this.activationDate = (ActivationDate) attrib;
			return true;
		}
		return false;
	}
	
	
	
	public void activate() throws KLMSIllegalOperationException, KLMSObjectNotPreActiveException {
		if(this.state.getValues()[0].getValueString().equals(Integer.toString(EnumState.PreActive))){
			this.state.setValue(Integer.toString(EnumState.Active), null);
			this.activationDate = new ActivationDate(new Date().toString());
		} else{
			throw new KLMSObjectNotPreActiveException();
		}
	}
	
	public void deactivate() {
		this.state.setValue(Integer.toString(EnumState.Deactivated), null);	
	}

	
	public void revoke(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException {
		for(Attribute a : attributes){
			if(a instanceof RevocationReason){
				this.revocationReason = (RevocationReason) a;
				Date now = new Date();
				if(a.getValues()[1].getValueString().equals(Integer.toString(EnumRevocationReasonCode.KeyCompromise))){
					this.state.setValue(Integer.toString(EnumState.Compromised), null);
					this.compromiseDate = new CompromiseDate(now.toString());
				} else{
					this.state.setValue(Integer.toString(EnumState.Deactivated), null);
					this.deactivationDate = new DeactivationDate(now.toString());
				}
			}
			else if(a instanceof CompromiseOccurrenceDate){
				this.compromiseOccurrenceDate = (CompromiseOccurrenceDate) a;
			}
		}	
	}
	
	public ArrayList<Attribute> obtainLease() throws KLMSPermissionDeniedException{
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		int currentState = Integer.parseInt(this.state.getValues()[0].getValueString());
		if(currentState == EnumState.Active){
			attributes.add(this.leaseTime);
			Date currentDateTime = new Date();
			this.lastChangeDate.setValue(currentDateTime.toString(), null);
			attributes.add(this.lastChangeDate);
		} else{
			throw new KLMSPermissionDeniedException("Cryptographic Object is not Active");
		}
		return attributes;
	}


	public void destroy() {
		this.state.setValue(Integer.toString(EnumState.Destroyed), null);
		this.destroyDate = new DestroyDate(new Date().toString());
	}

	public boolean isDestroyed() {
		int state = Integer.parseInt(this.state.getValues()[0].getValueString());
		if(state == EnumState.Destroyed || state == EnumState.DestroyedCompromised){
			return true;
		}
		return false;
	}

}
