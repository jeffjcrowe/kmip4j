/**
 * KLMSObjectManager.java
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
package ch.ntb.inf.klms.manager;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import ch.ntb.inf.klms.db.KLMSDatabaseInterface;
import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSItemNotFoundException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.attributes.CryptographicAlgorithm;
import ch.ntb.inf.klms.model.attributes.CryptographicLength;
import ch.ntb.inf.klms.model.attributes.Name;
import ch.ntb.inf.klms.model.attributes.UniqueIdentifier;
import ch.ntb.inf.klms.model.attributes.UsageLimits;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.model.objects.managed.ManagedObject;
import ch.ntb.inf.klms.model.objects.managed.PrivateKey;
import ch.ntb.inf.klms.model.objects.managed.PublicKey;
import ch.ntb.inf.klms.model.objects.managed.SecretData;
import ch.ntb.inf.klms.model.objects.managed.SymmetricKey;
import ch.ntb.inf.klms.model.objects.managed.Template;
import ch.ntb.inf.klms.utils.KLMSUtils;



public class KLMSObjectManager {

	KLMSDatabaseInterface database;
	
	public KLMSObjectManager(KLMSDatabaseInterface database) {
		super();
		this.database = database;
	}
	
	
	
	public HashMap<String, String> activate(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException , KLMSIllegalOperationException, KLMSObjectNotPreActiveException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		database.activate(uid.getValues()[0].getValueString());
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		return response;
	}
	
	public HashMap<String, String> addAttribute(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		database.addAttribute(uid.getValues()[0].getValueString(), attributes, parameters);
		attributes.add(uid);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(attributes, response);
		return response;
	}
	
	public HashMap<String, String> archive(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSUniqueIdentifierMissingException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		database.archive(uid.getValues()[0].getValueString());
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		return response;
	}
	
	public HashMap<String, String> check(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		attributes = database.check(uid.getValues()[0].getValueString(), attributes);
		attributes.add(uid);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(attributes, response);
		return response;
	}
	
	public HashMap<String, String> createKeyPair(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		HashMap<String, ArrayList<Attribute>> attributeLists = KLMSUtils.createAttributeListsForKeyPairFromHashMap(parameters);
		ArrayList<Attribute> commonList = attributeLists.get("Common");
		ArrayList<Attribute> privateKeyList = attributeLists.get("PrivateKey");
		ArrayList<Attribute> publicKeyList = attributeLists.get("PublicKey");
		
		CryptographicAlgorithm ca = null;
		CryptographicLength len = null;
		for(Attribute a : commonList){
			if(a instanceof CryptographicAlgorithm){
				ca = (CryptographicAlgorithm) a;
				
			}
			if(a instanceof CryptographicLength){
				len = (CryptographicLength) a;
			}
		}
			
		KeyPair keyPair = generateKeyPair(ca, len);

		PrivateKey privateKey = new PrivateKey(privateKeyList, ca, len, keyPair.getPrivate().getEncoded());
		PublicKey publicKey = new PublicKey(publicKeyList, ca, len, keyPair.getPublic().getEncoded());
		privateKey.setLink(publicKey.getUniqueIdentifierValue());
		publicKey.setLink(privateKey.getUniqueIdentifierValue());
		
		ArrayList<ManagedObject> moList = new ArrayList<ManagedObject>();
		moList.add(privateKey);
		moList.add(publicKey);
		database.addManagedObjects(moList);
		
		HashMap<String, String> response  = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(privateKey.getUniqueIdentifier(), response);
		KLMSUtils.addAttributeToParameterMap(publicKey.getUniqueIdentifier(), response);
		return response;
	}
	
	public HashMap<String, String> createSymmetricKey(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		SymmetricKey s = new SymmetricKey(parameters);
		database.add(s);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(s.getObjectType(), response);
		KLMSUtils.addAttributeToParameterMap(s.getUniqueIdentifier(), response);
		return response;
	}
	
	public HashMap<String, String> createSymmetricKeyUsingTemplate(HashMap<String, String> parameters) throws NoSuchAlgorithmException, KLMSItemNotFoundException {
		ArrayList<Name> names = getNames(parameters);
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		getAttributesFromTemplate(attributes, names);
		KLMSUtils.addAttributeListToParameterMap(attributes, parameters);
		return createSymmetricKey(parameters);
	}

	public HashMap<String, String> deleteAttribute(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException   {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		ArrayList<Attribute> responseAttributes = new ArrayList<Attribute>();
		responseAttributes.add(database.deleteAttribute(uid.getValues()[0].getValueString(), attributes.get(0), parameters));
		responseAttributes.add(uid);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(responseAttributes, response);
		return response;
	}
	
	public HashMap<String, String> destroy(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException  {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		database.destroy(uid.getValues()[0].getValueString());
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		return response;
	}
	
	public HashMap<String, String> get(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSObjectArchivedException  {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ManagedObject object = database.get(uid.getValues()[0].getValueString(), parameters);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(object.getObjectType(), response);
		KLMSUtils.addAttributeToParameterMap(uid, response);
		KLMSUtils.addManagedObjectToParameterMap(object, response);
		return response;
	}

	public HashMap<String, String> getAttributes(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = database.getAttributes(uid.getValues()[0].getValueString(), parameters);
		attributes.add(uid);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(attributes, response);
		return response;
	}
	
	public HashMap<String, String> getAttributeList(HashMap<String, String> parameters) throws KLMSItemNotFoundException , KLMSUniqueIdentifierMissingException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<String> attributeNames = database.getAttributeList(uid.getValues()[0].getValueString(), parameters);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		KLMSUtils.addAttributeNamesToParameterMap(attributeNames, response);
		return response;
	}

	public HashMap<String, String> getUsageAllocation(HashMap<String, String> parameters) throws KLMSIllegalOperationException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException , KLMSPermissionDeniedException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		if(attributes.size() == 1 && attributes.get(0) instanceof UsageLimits){
			database.getUsageAllocation(uid.getValues()[0].getValueString(), (UsageLimits) attributes.get(0));
			HashMap<String, String> response = new HashMap<String, String>();
			KLMSUtils.addAttributeToParameterMap(uid, response);
			return response;
		} else{
			throw new KLMSIllegalOperationException("Get Usage Allocation", "Usage Limits missing");
		}
	}
	
	public HashMap<String, String> locate(HashMap<String, String> parameters) {
		ArrayList<Attribute> uids = database.locate(KLMSUtils.createAttributesFromHashMap(parameters));
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(uids, response);
		return response;
	}
	
	public HashMap<String, String> modifyAttribute(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException  {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		database.modifyAttribute(uid.getValues()[0].getValueString(), attributes.get(0), parameters);
		attributes.add(uid);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeListToParameterMap(attributes, response);
		return response;
	}
	
	public HashMap<String, String> obtainLease(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSObjectArchivedException, KLMSItemNotFoundException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = database.obtainLease(uid.getValues()[0].getValueString());
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		KLMSUtils.addAttributeListToParameterMap(attributes, response);
		return response;
	}
	
	public HashMap<String, String> recover(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		database.recover(uid.getValues()[0].getValueString());
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		return response;
	}

	public HashMap<String, String> registerPrivateKey(HashMap<String, String> parameters) {
		HashMap<String, String> response = new HashMap<String, String>();	
		try {
			PrivateKey privateKey = new PrivateKey(parameters);
			database.add(privateKey);		
			KLMSUtils.addAttributeToParameterMap(privateKey.getUniqueIdentifier(), response);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public HashMap<String, String> registerPublicKey(HashMap<String, String> parameters) {
		HashMap<String, String> response = new HashMap<String, String>();
		
		try {
			PublicKey publicKey = new PublicKey(parameters);
			database.add(publicKey);		
			KLMSUtils.addAttributeToParameterMap(publicKey.getUniqueIdentifier(), response);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public HashMap<String, String> registerSecretData(HashMap<String, String> parameters) {
		SecretData secretData = new SecretData(parameters);
		database.add(secretData);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(secretData.getUniqueIdentifier(), response);
		return response;
	}
	
	public HashMap<String, String> registerSymmetricKey(HashMap<String, String> parameters) {
		HashMap<String, String> response = new HashMap<String, String>();
		
		try {
			SymmetricKey symmetricKey = new SymmetricKey(parameters);
			database.add(symmetricKey);		
			KLMSUtils.addAttributeToParameterMap(symmetricKey.getUniqueIdentifier(), response);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	public HashMap<String, String> registerTemplate(HashMap<String, String> parameters) {
		Template t = new Template(parameters);
		database.add(t);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(t.getUniqueIdentifier(), response);
		return response;
	}

	public HashMap<String, String> reKey(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, NoSuchAlgorithmException, KLMSPermissionDeniedException, KLMSItemNotFoundException  {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		String offset = KLMSUtils.getAttributeValue("Offset", 1, parameters);
		ArrayList<Attribute> requestAttributes = KLMSUtils.createAttributesFromHashMap(parameters);
		UniqueIdentifier uidNewKey = database.reKey(uid.getValues()[0].getValueString(), requestAttributes, offset);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uidNewKey, response);
		return response;
	}
	
	public HashMap<String, String> revoke(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException , KLMSIllegalOperationException {
		UniqueIdentifier uid = KLMSUtils.getUniqueIdentifierFromParameters(parameters);
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		database.revoke(uid.getValues()[0].getValueString(), attributes);
		HashMap<String, String> response = new HashMap<String, String>();
		KLMSUtils.addAttributeToParameterMap(uid, response);
		return response;
	}
	

	

	
	//////////////////////////////////////////// Support Functions
	
	private ArrayList<Name> getNames(HashMap<String, String> parameters){
		ArrayList<Name> names =  new ArrayList<Name>();
		if(parameters.containsKey("Name")){
			names.add(getNameFromParameters(1, parameters));
	 
			int count = Integer.parseInt(parameters.get("Name"));
			for(int i = 2; i <= count; i++){
				names.add(getNameFromParameters(i, parameters));
			}
			parameters.remove("Name");
		}
		return names;
	}
	
	private Name getNameFromParameters(int count, HashMap<String, String> parameters) {
		Name n = new Name();	
		n.setValue(KLMSUtils.getAttributeValue("Name Type", count, parameters), "Name Type");
		n.setValue(KLMSUtils.getAttributeValue("Name Value", count, parameters), "Name Value");
		return n;
	}
	
	private void getAttributesFromTemplate(ArrayList<Attribute> attributes, ArrayList<Name> names){
		for(Name n : names){
			try{
				Template t = database.getTemplate(n);
				attributes.addAll(t.getAttributesForCreate());
				names.remove(n);
				attributes.addAll(names);
				break;
			}catch(Exception e){
			}
		}
	}
	

	private KeyPair generateKeyPair(CryptographicAlgorithm ca, CryptographicLength len) throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ca.getAlgorithm());
		kpg.initialize(len.getCryptographicLength());
		return  kpg.genKeyPair();
	}

	public Hashtable<String, String> getNumberOfObjects() {
		return database.getNumberOfObjects();
	}

}
