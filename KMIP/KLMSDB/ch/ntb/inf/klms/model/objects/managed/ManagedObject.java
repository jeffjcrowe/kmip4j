/**
// * ManagedObject.java
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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.ApplicationSpecificInformation;
import ch.ntb.inf.klms.model.attributes.ArchiveDate;
import ch.ntb.inf.klms.model.attributes.ContactInformation;
import ch.ntb.inf.klms.model.attributes.CustomAttribute;
import ch.ntb.inf.klms.model.attributes.InitialDate;
import ch.ntb.inf.klms.model.attributes.LastChangeDate;
import ch.ntb.inf.klms.model.attributes.Name;
import ch.ntb.inf.klms.model.attributes.ObjectGroup;
import ch.ntb.inf.klms.model.attributes.ObjectType;
import ch.ntb.inf.klms.model.attributes.OperationPolicyName;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.attributes.XPurpose;
import ch.ntb.inf.klms.model.objects.base.Attribute;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "ManagedObjectType", discriminatorType = DiscriminatorType.STRING)
public abstract class ManagedObject {
	
	@Transient
	static int objectCount = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	
	
	//required
	@ManyToOne ( cascade = CascadeType.ALL)
	protected UniqueIdentifier uniqueIdentifier;
	
	@ManyToOne ( cascade = {CascadeType.PERSIST,  CascadeType.MERGE, CascadeType.REFRESH})
	protected ObjectType objectType;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	protected LastChangeDate lastChangeDate;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	protected InitialDate initialDate;

	
	// optional
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="MANAGEDOBJECT_NAMES",
		    joinColumns= @JoinColumn(name="MANAGEDOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="NAME_ID", referencedColumnName="ID")
	)
	private Set<Name> names;
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="MANAGEDOBJECT_APPLICATIONSPECIFICINFORMATION",
		    joinColumns= @JoinColumn(name="MANAGEDOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="APPLICATIONSPECIFICINFORMATION_ID", referencedColumnName="ID")
	)
	private Set<ApplicationSpecificInformation> applicationSpecificInformation;
	
	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="MANAGEDOBJECT_OBJECTGROUPS",
		    joinColumns= @JoinColumn(name="MANAGEDOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="OBJECTGROUP_ID", referencedColumnName="ID")
	)
	private Set<ObjectGroup> objectGroups;

	@ManyToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
			name="MANAGEDOBJECT_CUSTOMATTRIBUTES",
		    joinColumns= @JoinColumn(name="MANAGEDOBJECT_ID", referencedColumnName="ID"),
		    inverseJoinColumns=@JoinColumn(name="CUSTOMATTRIBUTE_ID", referencedColumnName="ID")
	)
	private Set<CustomAttribute> customAttributes;
	
	@ManyToOne ( cascade = CascadeType.ALL )
	private OperationPolicyName operationPolicyName;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private ContactInformation contactInformation;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private ArchiveDate archiveDate;
	
	
	public ManagedObject() {
		
	}
	
	public ManagedObject(HashMap<String, String> parameters, String objectType){
		
		//default
		this.uniqueIdentifier = new UniqueIdentifier(generateUID(objectType));
		Date dateTime = new Date();
		this.initialDate = new InitialDate(dateTime.toString());
		this.lastChangeDate = new LastChangeDate(dateTime.toString());

		
		// optional
		if(parameters.containsKey("Name")){
			names = new HashSet<Name>();
			Name n = new Name();
			n.setValue(parameters.get("Name Type" + "value"+ "1"), "Name Type");
			n.setValue(parameters.get("Name Value" + "value"+ "1"), "Name Value");
			names.add(n); 

			int count = Integer.parseInt(parameters.get("Name"));
			for(int i = 2; i <= count; i++){
				n = new Name();
				n.setValue(parameters.get("Name Type" + "value"+ i), "Name Type");
				n.setValue(parameters.get("Name Value" + "value"+ i), "Name Value");
				names.add(n); 
			}
		} 
		if(parameters.containsKey("Application Specific Information")){
			applicationSpecificInformation = new HashSet<ApplicationSpecificInformation>();
			ApplicationSpecificInformation a = new ApplicationSpecificInformation();
			a.setValue(parameters.get("Application Namespace" + "value"+ "1"), "Application Namespace");
			a.setValue(parameters.get("Application Data" + "value"+ "1"), "Application Data");
			applicationSpecificInformation.add(a); 

			int count = Integer.parseInt(parameters.get("Application Specific Information"));
			for(int i = 2; i <= count; i++){
				a = new ApplicationSpecificInformation();
				a.setValue(parameters.get("Application Namespace"+ "value" + i), "Application Namespace");
				a.setValue(parameters.get("Application Data" + "value"+ i), "Application Data");
				applicationSpecificInformation.add(a); 
			}
		} 
		if(parameters.containsKey("Object Group")){
			objectGroups = new HashSet<ObjectGroup>();
			ObjectGroup a = new ObjectGroup();
			a.setValue(parameters.get("Object Group" + "value" + "1"), "Object Group");
			objectGroups.add(a); 

			int count = Integer.parseInt(parameters.get("Object Group"));
			for(int i = 2; i <= count; i++){
				a = new ObjectGroup();
				a.setValue(parameters.get("Object Group" + "value"  + i), "Object Group");
				objectGroups.add(a); 
			}
		} 
		if(parameters.containsKey("x-Purpose")){
			customAttributes = new HashSet<CustomAttribute>();
			CustomAttribute a = new XPurpose(parameters.get("x-Purpose"  + "value" + "1"));
			customAttributes.add(a); 

			int count = Integer.parseInt(parameters.get("x-Purpose"));
			for(int i = 2; i <= count; i++){
				a = new XPurpose(parameters.get("x-Purpose"  + "value" + i));
				customAttributes.add(a); 
			}
		}
		if(parameters.containsKey("Operation Policy Name")){
			operationPolicyName = new OperationPolicyName(parameters.get("Operation Policy Name" + "value" + "1"));
		}
		if(parameters.containsKey("Contact Information")){
			contactInformation = new ContactInformation(parameters.get("Contact Information"+ "value" + "1"));
		}
		if(parameters.containsKey("Archive Date")){
			archiveDate = new ArchiveDate(parameters.get("Archive Date"+ "value" + "1"));
		}

	}
	

	public ManagedObject(ArrayList<Attribute> attributes, String objectType) {
		//default
		this.uniqueIdentifier = new UniqueIdentifier(generateUID(objectType));
		Date dateTime = new Date();
		this.initialDate = new InitialDate(dateTime.toString());
		this.lastChangeDate = new LastChangeDate(dateTime.toString());
		
		for(Attribute a : attributes){
			if(a instanceof Name){
				if(names == null){
					names = new HashSet<Name>();
				}
				this.names.add((Name) a);
			}
		}
		
	}

	public abstract void activate() throws KLMSIllegalOperationException, KLMSObjectNotPreActiveException;
	
	public abstract void revoke(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException;
	
	public abstract ArrayList<Attribute> check(ArrayList<Attribute> attributes) throws KLMSIllegalOperationException;
	
	public abstract void getUsageAllocation(UsageLimits usageLimits) throws KLMSPermissionDeniedException;
	
	public abstract ArrayList<Attribute> obtainLease() throws KLMSPermissionDeniedException;

	
	public void archive(){
		Date now = new Date();
		this.archiveDate = new ArchiveDate(Long.toString(now.getTime())); 
	}
	
	public void checkArchiveStatus() throws KLMSObjectArchivedException {
		if(hasArchiveDate()){
			throw new KLMSObjectArchivedException(this.uniqueIdentifier.getValues()[0].getValueString());
		}
	}
	
	public boolean hasArchiveDate(){
		if(this.archiveDate != null){
			return true;
		}
		return false;
	}
	
	public void recover() {
		this.archiveDate = null;
	}

	public ArrayList<Attribute> getAttributesForCreate(){
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		
		if(customAttributes != null){
			attributes.addAll(customAttributes);
		}
		if(contactInformation != null){
			attributes.add(contactInformation);
		}
		if(applicationSpecificInformation != null){
			attributes.addAll(applicationSpecificInformation);
		}
		if(objectGroups != null){
			attributes.addAll(objectGroups);
		}
		if(operationPolicyName != null){
			attributes.add(operationPolicyName);
		}
		return attributes;
	}
	
	public ArrayList<Attribute> getAttributes(){
		ArrayList<Attribute> attributes = getAttributesForCreate();
		
		if(uniqueIdentifier != null){
			attributes.add(uniqueIdentifier);
		}
		if(objectType != null){
			attributes.add(objectType);
		}
		if(lastChangeDate != null){
			attributes.add(lastChangeDate);
		}
		if(initialDate != null){
			attributes.add(initialDate);
		}
		if(names != null){
			attributes.addAll(names);
		}
		if(archiveDate != null){
			attributes.add(archiveDate);
		}
		return attributes;
	}
	
	public boolean addAttribute(Attribute attrib) {
		if(attrib instanceof Name){
			if(names == null){
				names = new HashSet<Name>();
			}
			names.add((Name) attrib);
			return true;
		}
		else if(attrib instanceof ApplicationSpecificInformation){
			if(applicationSpecificInformation == null){
				applicationSpecificInformation = new HashSet<ApplicationSpecificInformation>();
			}
			applicationSpecificInformation.add((ApplicationSpecificInformation) attrib);
			return true;
		}
		else if(attrib instanceof ObjectGroup){
			if(objectGroups == null){
				objectGroups = new HashSet<ObjectGroup>();
			}
			objectGroups.add((ObjectGroup) attrib);
			return true;
		}
		else if(attrib instanceof CustomAttribute){
			if(customAttributes == null){
				customAttributes = new HashSet<CustomAttribute>();
			}
			customAttributes.add((CustomAttribute) attrib);
			return true;
		}
		else if(attrib instanceof ContactInformation){
			this.contactInformation = (ContactInformation) attrib;
			return true;
		}
		else if(attrib instanceof InitialDate){
			this.initialDate = (InitialDate) attrib;
			return true;
		}	
		else if(attrib instanceof LastChangeDate){
			this.lastChangeDate = (LastChangeDate) attrib;
			return true;
		}
		else if(attrib instanceof UniqueIdentifier){
			this.uniqueIdentifier = (UniqueIdentifier) attrib;
			return true;
		}
		else if(attrib instanceof ObjectType){
			this.objectType = (ObjectType) attrib;
			return true;
		}
		return false;
	}
	
	public Attribute removeAttribute(Attribute attrib) {	
		Attribute returnAttribute = null;
		
		if(attrib instanceof Name){
			for(Name n : names){
				if(n.equals(attrib)){
					names.remove(n);
					return n;
				}
			}
		}
		else if(attrib instanceof ApplicationSpecificInformation){
			for(ApplicationSpecificInformation a : applicationSpecificInformation){
				if(a.equals(attrib)){
					applicationSpecificInformation.remove(a);
					return a;
				}
			}
		}
		else if(attrib instanceof ObjectGroup){
			for(ObjectGroup a : objectGroups){
				if(a.equals(attrib)){
					objectGroups.remove(a);
					return a;
				}
			}
		}
		if(attrib instanceof CustomAttribute){
			for(CustomAttribute a : customAttributes){
				if(a.getAttributeName().equals(attrib.getAttributeName())){
					customAttributes.remove(a);
					return a;
				}
			}
		}
		else if(attrib instanceof ContactInformation){
			returnAttribute = contactInformation;
			this.contactInformation = null;
		}		
		return returnAttribute;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public UniqueIdentifier getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	
	public String getUniqueIdentifierValue() {
		return uniqueIdentifier.getValues()[0].getValueString();
	}
	
	
	protected String generateUID(String objectType) {
		StringBuilder uid = new StringBuilder(objectType + objectCount);
		objectCount++;
		uid.append("-");
		
		for(int i = uid.length(); i < 36; i++){		// Padding to Length 36
			uid.append(0);
		}
		return uid.toString();
	}
	
}
