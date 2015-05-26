package ch.ntb.inf.klms.db;
/**
 * KLMSDatabase.java
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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ch.ntb.inf.klms.accesscontrol.UserPermissionEntry;
import ch.ntb.inf.klms.model.attributes.KLMSAttributeValue;
import ch.ntb.inf.klms.model.attributes.Name;
import ch.ntb.inf.klms.model.attributes.State;
import ch.ntb.inf.klms.model.attributes.StorageStatusMask;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.klmsenum.EnumState;
import ch.ntb.inf.klms.model.klmsenum.EnumTypeKLMS;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.base.Credential;
import ch.ntb.inf.klms.model.objects.managed.CryptographicObject;
import ch.ntb.inf.klms.model.objects.managed.ManagedObject;
import ch.ntb.inf.klms.model.objects.managed.SymmetricKey;
import ch.ntb.inf.klms.model.objects.managed.Template;

public class KLMSDatabase implements KLMSDatabaseInterface{

	private EntityManagerFactory emf;

	public KLMSDatabase(){
		emf = Persistence.createEntityManagerFactory("klmsdb");
	}

	public void clean(){
		emf = Persistence.createEntityManagerFactory("klmsdb");
	}
	
	
	public void activate(String uniqueIdentifier) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		getObjectByUID(uniqueIdentifier, em).activate();
		
		em.getTransaction().commit();
		em.clear();
	}
	
	public ArrayList<Attribute> addAttribute(String uniqueIdentifier, ArrayList<Attribute> requestAttributes, HashMap<String, String> parameters) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		for(Attribute attrib : requestAttributes){
			object.addAttribute(attrib);
		}
		
		em.getTransaction().commit();
		em.clear();
		return requestAttributes;
	}

	public void archive(String uniqueIdentifier) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		getObjectByUID(uniqueIdentifier, em).archive();
		
		em.getTransaction().commit();
		em.clear();
	}

	public ArrayList<Attribute> check(String uniqueIdentifier, ArrayList<Attribute> attributes) throws KLMSItemNotFoundException, KLMSIllegalOperationException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		ArrayList<Attribute> returnAttributes = object.check(attributes);
		
		em.getTransaction().commit();
		em.clear();
		
		return returnAttributes;
	}
	
	public Attribute deleteAttribute(String uniqueIdentifier, Attribute attribute, HashMap<String, String> parameters) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		Attribute a = object.removeAttribute(attribute);
		
		em.getTransaction().commit();
		em.clear();
		
		return a;
	}
	
	public String destroy(String uniqueIdentifier) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		
		if(object instanceof CryptographicObject){
			((CryptographicObject) object).destroy();
		} else{
			em.remove(object);
		}

		em.getTransaction().commit();
		em.clear();
		
		return object.getUniqueIdentifierValue();
	}
	
	public ManagedObject get(String uniqueIdentifier, HashMap<String, String> parameters)  throws KLMSItemNotFoundException, KLMSObjectArchivedException{
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		object.checkArchiveStatus();
		
		em.getTransaction().commit();
		em.clear();
		
		if(object instanceof CryptographicObject && ((CryptographicObject) object).isDestroyed()){
			throw new KLMSItemNotFoundException(uniqueIdentifier);
		} else{
			return object;
		}
	}
	
	public ArrayList<String> getAttributeList(String uniqueIdentifier, HashMap<String, String> parameters) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		ArrayList<String> attributeNames = new ArrayList<String>();
		for(Attribute a : object.getAttributes()){
			attributeNames.add(a.getAttributeName());
		}
		
		em.getTransaction().commit();
		em.clear();
		
		return attributeNames;
	}
	
	public ArrayList<Attribute> getAttributes(String uniqueIdentifier, HashMap<String, String> parameters) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);	

		em.getTransaction().commit();
		em.clear();
		
		return selectAttributes(object, parameters);
	}
	
	public Template getTemplate(Name name) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Template template = null;
		try{
			String id = getObjectIDByName(name.getValues()[0].getValueString(), em);
			ManagedObject object = getObjectByIdentifier(id, em);
			if(object instanceof Template){
				template = (Template) object;
			}
		} catch(Exception e){
			// do nothing, Object Manager will try next name 
		}

		em.getTransaction().commit();
		em.clear();
		
		return template;
	}

	public void getUsageAllocation(String uniqueIdentifier, UsageLimits usageLimits) throws KLMSItemNotFoundException, KLMSPermissionDeniedException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		object.getUsageAllocation(usageLimits);
		
		em.getTransaction().commit();
		em.clear();
	}
	
	public ArrayList<Attribute> locate(ArrayList<Attribute> locateAttributes) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		List<ManagedObject> objects = loadAllObjects(em, locateAttributes);
		ArrayList<Attribute> uids = new ArrayList<Attribute>();

		for(ManagedObject o : objects){
			
			if(!(o instanceof CryptographicObject && ((CryptographicObject)o).isDestroyed())){
				boolean objectLocated = true;
				ArrayList<Attribute> objectAttributes = o.getAttributes();
				for(Attribute locateAttribute : locateAttributes){
					boolean attributeLocated = false;
					for(Attribute objectAttribute : objectAttributes){
						if(locateAttribute.equals(objectAttribute)){
							attributeLocated = true; 
							break;
						}
					}
					if(!attributeLocated){
						objectLocated = false;
						break;
					}
				}
				if(objectLocated){
					uids.add(new UniqueIdentifier(o.getUniqueIdentifierValue()));
				}
			}
		}
		
		em.getTransaction().commit();
		em.clear();

		return uids;		
	}
	
	public void modifyAttribute(String uniqueIdentifier, Attribute attribute, HashMap<String, String> parameters) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		ArrayList<Attribute> objectAttributes = object.getAttributes();
		
		for(Attribute attrib : objectAttributes){
			if(attrib.getAttributeName().equals(attribute.getAttributeName())){
				KLMSAttributeValue[] values = attrib.getValues();
				KLMSAttributeValue[] newValues = attribute.getValues();
				if(attrib.getAttributeType() == EnumTypeKLMS.Structure){
					for(int i = 0; i < values.length; i++){
						attrib.setValue(newValues[i].getValueString(), newValues[i].getName());
					}
				} else{
					attrib.setValue(newValues[0].getValueString(), null);
				}
			}
		}
		
		em.getTransaction().commit();
		em.clear();
	}
	
	public ArrayList<Attribute> obtainLease(String uniqueIdentifier) throws KLMSPermissionDeniedException, KLMSObjectArchivedException, KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		ArrayList<Attribute> attributes = object.obtainLease();
		
		em.getTransaction().commit();
		em.clear();
		
		return attributes;
	}
	
	public void recover(String uniqueIdentifier) throws KLMSItemNotFoundException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		getObjectByUID(uniqueIdentifier, em).recover();
	
		em.getTransaction().commit();
		em.clear();
	}
	
	public UniqueIdentifier reKey(String uniqueIdentifier, ArrayList<Attribute> requestAttributes, String offset) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, NoSuchAlgorithmException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		
		if(object instanceof SymmetricKey){
			SymmetricKey newKey = ((SymmetricKey) object).reKey(offset);
			
			for(Attribute a : requestAttributes){
				newKey.addAttribute(a);
			}

			em.persist(newKey);
			em.getTransaction().commit();
			em.clear();
			
			return newKey.getUniqueIdentifier();
		} else{
			throw new KLMSPermissionDeniedException("Re-Key only applies to Symmetric Keys");
		}
	}
	
	public void revoke(String uniqueIdentifier, ArrayList<Attribute> attributes) throws KLMSItemNotFoundException, KLMSIllegalOperationException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		getObjectByUID(uniqueIdentifier, em).revoke(attributes);
		
		em.getTransaction().commit();
		em.clear();
	}
	

	
	
	///////////////////////////////////////////////////////// Support Functions
	
	
	public void add(ManagedObject o) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(o);
		em.getTransaction().commit();
		em.clear();
	}
	
	public void addManagedObjects(ArrayList<ManagedObject> moList) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		for(ManagedObject mo : moList){
			em.persist(mo);
		}
		em.getTransaction().commit();
		em.clear();
	}
	
	private ArrayList<Attribute> selectAttributes(ManagedObject managedObject, HashMap<String, String> parameters) {
		ArrayList<Attribute> attributes = managedObject.getAttributes();
		ArrayList<Attribute> requestedAttributes = new ArrayList<Attribute>();
		for(Attribute a : attributes){
			if(parameters.containsKey(a.getAttributeName())){
				requestedAttributes.add(a);
			}
		}
		return requestedAttributes;
	}
	
	private ManagedObject getObjectByUID(String uniqueIdentifier, EntityManager em) throws KLMSItemNotFoundException {
		String id = getObjectIDByUniqueIdentifier(uniqueIdentifier, em);
		return getObjectByIdentifier(id, em);
	}
	
	private String getObjectIDByUniqueIdentifier(String uniqueIdentifier, EntityManager em) throws KLMSItemNotFoundException {

		Query q = em.createNativeQuery(
				"select ID from MANAGEDOBJECT where UNIQUEIDENTIFIER_ID in (" +
						"select ATTRIBUTE_ID from ATTRIBUTE_KLMSATTRIBUTEVALUE where VALUES_ID in (" +
							"select ID from KLMSATTRIBUTEVALUE where VALUE = '"+uniqueIdentifier+"'" +
							")" +
						")"
		);
		try{
			String id = q.getSingleResult().toString();
			return id;
		} catch(NoResultException e){
			throw new KLMSItemNotFoundException("Object: " + uniqueIdentifier + " not found");
		} catch(NonUniqueResultException e){
			throw new KLMSItemNotFoundException("Multiple Instances of Object: " + uniqueIdentifier + " found");
		}
	}
	
	private String getObjectIDByName(String name, EntityManager em) throws KLMSItemNotFoundException{

		Query q = em.createNativeQuery(
				"SELECT ID FROM TEMPLATE WHERE ID = " +
						"( SELECT MANAGEDOBJECT_ID FROM MANAGEDOBJECT_NAMES WHERE NAME_ID =" +
							"(SELECT ID FROM ATTRIBUTE WHERE ID = " +
								"(SELECT ATTRIBUTE_ID FROM ATTRIBUTE_KLMSATTRIBUTEVALUE WHERE VALUES_ID = " +
									"(SELECT ID FROM KLMSATTRIBUTEVALUE WHERE VALUE = '"+name+"'" +
									")" +
								")" +
							")" +
						")"
		);
		try{
			String id = q.getSingleResult().toString();
			return id;
		} catch(NoResultException e){
			throw new KLMSItemNotFoundException("Object: " + name + " not found");
		} catch(NonUniqueResultException e){
			throw new KLMSItemNotFoundException("Multiple Instances of Object: " + name + " found");
		}
	}
	
	private ManagedObject getObjectByIdentifier(String id, EntityManager em) throws KLMSItemNotFoundException{
	
		try{
			String query = "select k from ManagedObject k where k.id = '" + id + "'"; 
			TypedQuery<ManagedObject> tq = em.createQuery(query, ManagedObject.class);
			ManagedObject object = tq.getSingleResult();
			return object;
		}catch(NoResultException e){
			throw new KLMSItemNotFoundException("Object: " + id + " not found");
		} catch(NonUniqueResultException e){
			throw new KLMSItemNotFoundException("Multiple Instances of Object: " + id + " found");
		}
	}
	
	private List<ManagedObject> loadAllObjects(EntityManager em, ArrayList<Attribute> locateAttributes) {
		boolean locateArchivedObjects = checkStorageStatusMask(locateAttributes);

		String query = "select k from ManagedObject k"; 
		TypedQuery<ManagedObject> tq = em.createQuery(query, ManagedObject.class);
		List<ManagedObject> objects = tq.getResultList();
		
		for(int i = 0; i < objects.size(); i++){
			if(!locateArchivedObjects){
				try {
					objects.get(i).checkArchiveStatus();
				} catch (KLMSObjectArchivedException e) {
					objects.remove(objects.get(i));
				}
			}
		}
		
		return objects;
	}
	
	private boolean checkStorageStatusMask(ArrayList<Attribute> locateAttributes) {
		for(Attribute a : locateAttributes){
			if(a instanceof StorageStatusMask){
				StorageStatusMask storageStatusMask = (StorageStatusMask) a;
				locateAttributes.remove(a);
				if(storageStatusMask.getStorageStatusMask() > 1){
					return true;
				}
			}
		}
		return false;
	}

	public Hashtable<String, String> getNumberOfObjects() {
		Hashtable<String, String> objects = new Hashtable<String, String>();
		
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Query q = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM MANAGEDOBJECT");
		objects.put("Managed Objects", q.getSingleResult().toString());
		
		Query q2 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM SYMMETRICKEY");
		objects.put("Symmetric Keys", q2.getSingleResult().toString());
		
		Query q3 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM TEMPLATE");
		objects.put("Templates", q3.getSingleResult().toString());
		
		Query q4 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM PRIVATEKEY");
		objects.put("Private Keys", q4.getSingleResult().toString());
		
		Query q5 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM PUBLICKEY");
		objects.put("Public Keys", q5.getSingleResult().toString());
		
		Query q6 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM SECRETDATA");
		objects.put("Secret Data", q6.getSingleResult().toString());
		
//		Query q7 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM CERTIFICATE");
//		objects.put("Certificates", q7.getSingleResult().toString());
		
//		Query q8 = em.createNativeQuery("SELECT count(ID) AS Anzahl FROM OPAQUEOBJECT");
//		objects.put("Opaque Objects", q8.getSingleResult().toString());
		
		em.getTransaction().commit();
		em.clear();
	
		return objects;
	}

	public void createUserPermissionEntry(String uid, Credential credential) {		
		UserPermissionEntry userPermissionEntry = new UserPermissionEntry(uid, credential);
		persistUserPermissionEntry(userPermissionEntry);
	}

	public void createUserPermissionEntry(String uid) {
		UserPermissionEntry userPermissionEntry = new UserPermissionEntry(uid);
		persistUserPermissionEntry(userPermissionEntry);
	}
	
	public void persistUserPermissionEntry(UserPermissionEntry userPermissionEntry) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		em.persist(userPermissionEntry);
		
		em.getTransaction().commit();
		em.clear();
	}

	public void verifyUserPermission(UserPermissionEntry requestUserPermissionEntry) throws KLMSPermissionDeniedException{
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		String query = "select k from UserPermissionEntry k where k.uid = '" + requestUserPermissionEntry.getUniqueIdentifier() + "'"; 
		TypedQuery<UserPermissionEntry> tq = em.createQuery(query, UserPermissionEntry.class);
		UserPermissionEntry userPermissionEntry = tq.getSingleResult();
		
		if(userPermissionEntry.hasCredential() && !requestUserPermissionEntry.hasCredential()){
			throw new KLMSPermissionDeniedException("Request Credential is missing");

		}
		if(userPermissionEntry.hasCredential() && requestUserPermissionEntry.hasCredential()){
			if(!userPermissionEntry.equals(requestUserPermissionEntry)){
				throw new KLMSPermissionDeniedException("Request Credential does not match the Credential of the requested Object");
			}
		}
		
		em.getTransaction().commit();
		em.clear();
	}
	
	
	
	// Only for Use Case 9.2
	public void forceActivate(String uniqueIdentifier) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		try{
			ManagedObject object = getObjectByUID(uniqueIdentifier, em);
			object.addAttribute(new State(Integer.toString(EnumState.Active)));
		} catch (Exception e){
			
		}
		
		em.getTransaction().commit();
		em.clear();
	}
	
	
	// Used only for Use Case 9.2, not an official KMIP operation
	public void deactivate(String uniqueIdentifier) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		ManagedObject object = getObjectByUID(uniqueIdentifier, em);
		((CryptographicObject)object).deactivate();
		
		em.getTransaction().commit();
		em.clear();
	}
}
