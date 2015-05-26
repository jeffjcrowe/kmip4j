/**
 * KLMSDatabaseInterface.java
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
package ch.ntb.inf.klms.db;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import ch.ntb.inf.klms.accesscontrol.UserPermissionEntry;
import ch.ntb.inf.klms.model.attributes.Name;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.managed.ManagedObject;
import ch.ntb.inf.klms.model.objects.managed.Template;


public interface KLMSDatabaseInterface {

	public void activate(String valueString) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException;
	
	public ArrayList<Attribute> addAttribute(String valueString, ArrayList<Attribute> requestAttributes, HashMap<String, String> parameters) throws KLMSItemNotFoundException;

	public void archive(String valueString) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException;

	public ArrayList<Attribute> check(String uniqueIdentifier, ArrayList<Attribute> attributes) throws KLMSItemNotFoundException, KLMSIllegalOperationException;

	public Attribute deleteAttribute(String valueString, Attribute attribute, HashMap<String, String> parameters) throws KLMSItemNotFoundException;

	public String destroy(String uid) throws KLMSItemNotFoundException;

	public ManagedObject get(String uid, HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSObjectArchivedException;

	public ArrayList<String> getAttributeList(String valueString, HashMap<String, String> parameters) throws KLMSItemNotFoundException;

	public ArrayList<Attribute> getAttributes(String valueString, HashMap<String, String> parameters) throws KLMSItemNotFoundException;

	public Template getTemplate(Name n) throws KLMSItemNotFoundException;

	public void getUsageAllocation(String uniqueIdentifier, UsageLimits usageLimits) throws KLMSItemNotFoundException, KLMSPermissionDeniedException;

	public ArrayList<Attribute> locate(ArrayList<Attribute> attributes);

	public void modifyAttribute(String valueString, Attribute attribute, HashMap<String, String> parameters) throws KLMSItemNotFoundException;

	public ArrayList<Attribute> obtainLease(String valueString) throws KLMSPermissionDeniedException, KLMSObjectArchivedException, KLMSItemNotFoundException;

	public void recover(String valueString) throws KLMSItemNotFoundException;

	public UniqueIdentifier reKey(String uniqueIdentifier, ArrayList<Attribute> requestAttributes, String offset) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, NoSuchAlgorithmException;

	public void revoke(String valueString, ArrayList<Attribute> attributes) throws KLMSItemNotFoundException, KLMSIllegalOperationException;

	
	
	
	
	public void addManagedObjects(ArrayList<ManagedObject> moList);
	
	public void add(ManagedObject o);
	
	public void deactivate(String string) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException;

	public void forceActivate(String string);
	
	public Hashtable<String, String> getNumberOfObjects();

	public void persistUserPermissionEntry(UserPermissionEntry userPermissionEntry);

	public void verifyUserPermission(UserPermissionEntry userPermissionEntry) throws KLMSPermissionDeniedException;

}
