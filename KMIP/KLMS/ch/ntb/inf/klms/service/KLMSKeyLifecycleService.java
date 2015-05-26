/**
 * KLMSKeyLifecycleService.java
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
package ch.ntb.inf.klms.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Hashtable;

import ch.ntb.inf.klms.db.KLMSDatabaseInterface;
import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSItemNotFoundException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.manager.KLMSEndpointManager;
import ch.ntb.inf.klms.manager.KLMSLifecycleManager;
import ch.ntb.inf.klms.manager.KLMSObjectManager;
import ch.ntb.inf.klms.manager.KLMSPolicyManager;
import ch.ntb.inf.klms.manager.KLMSUniqueIdentifierMissingException;
import ch.ntb.inf.klms.utils.KLMSUtils;



public class KLMSKeyLifecycleService{

	private KLMSEndpointManager endpointManager;
	private KLMSLifecycleManager lifecycleManager;
	private KLMSObjectManager objectManager;
	
	@SuppressWarnings("unused")
	private KLMSPolicyManager policyManager;	
	
	
	/**
	 * ID Placeholder is loaded if no UID is provided in one the following Operations:
	 * (Derive Key), Get, Get Attributes/List/Modify/Add/Delete, Activate, Revoke, Destroy
	 *  Archive/Recover, Certify, Re-Certify, Re-Key, Obtain Lease, Get Usage Allocation, Check
	 * 
	 * 
	 * ID Placeholder is set after the following Operations: 
	 * Create, Create Key Pair (ID of Private Key), Register, Derive Key, Locate
	 *  Certify, Re-Certify, Re-Key
	 */
	private static String idPlaceholder;
	
	
	// Support for Testing of UC 9.2 without a proper Lifecycle Manager
	private boolean megaHack = false;
	private boolean continueMegaHack = false;
	
	
	
	
	public KLMSKeyLifecycleService(KLMSDatabaseInterface database) {
		super();
		this.endpointManager = new KLMSEndpointManager(database);
		this.lifecycleManager = new KLMSLifecycleManager(database);
		this.objectManager = new KLMSObjectManager(database);
		this.policyManager = new KLMSPolicyManager();
	}

	
	public static String getIdPlaceholder() {
		return idPlaceholder;
	}

	public static void setIdPlaceholder(HashMap<String, String> responseParameters) {
		if(responseParameters.containsKey("Unique Identifier")){
			KLMSKeyLifecycleService.idPlaceholder = responseParameters.get("Unique Identifier" + "value" + "1");
		}	
	}




	public HashMap<String, String> createSymmetricKey(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException {
		HashMap<String, String> responseParameters = objectManager.createSymmetricKey(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> createSymmetricKeyUsingTemplate(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException, KLMSItemNotFoundException {
		HashMap<String, String> responseParameters = objectManager.createSymmetricKeyUsingTemplate(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> createKeyPair(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException {
		HashMap<String, String> responseParameters = objectManager.createKeyPair(requestParameters);
		endpointManager.createAccessEntriesForKeyPair(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}

	public HashMap<String, String> destroy(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.destroy(requestParameters);
	}
	
	public HashMap<String, String> registerTemplate(HashMap<String, String> requestParameters) {
		HashMap<String, String> responseParameters =  objectManager.registerTemplate(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> registerSecretData(HashMap<String, String> requestParameters) {
		HashMap<String, String> responseParameters =  objectManager.registerSecretData(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> registerSymmetricKey(HashMap<String, String> requestParameters){
		HashMap<String, String> responseParameters =  objectManager.registerSymmetricKey(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> registerPrivateKey(HashMap<String, String> requestParameters){
		HashMap<String, String> responseParameters =  objectManager.registerPrivateKey(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> registerPublicKey(HashMap<String, String> requestParameters){
		HashMap<String, String> responseParameters =  objectManager.registerPublicKey(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> getAttributes(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		HashMap<String, String> response = objectManager.getAttributes(requestParameters);
		
		// Support for Testing of UC 9.2 without a proper Lifecycle Manager
		if(megaHack){
			lifecycleManager.continueMegaHack(response);
			megaHack = false;
			continueMegaHack = true;
		}
		
		return response;
	}
	
	public HashMap<String, String> locate(HashMap<String, String> requestParameters) {
		HashMap<String, String> responseParameters = objectManager.locate(requestParameters);
		setIdPlaceholder(responseParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> get(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException, KLMSObjectArchivedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.get(requestParameters);
	}
	
	public HashMap<String, String> getAttributeList(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException , KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.getAttributeList(requestParameters);
	}
	
	public HashMap<String, String> addAttribute(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		megaHack = lifecycleManager.megaHack(requestParameters);
		return objectManager.addAttribute(requestParameters);
	}
	
	public HashMap<String, String> modifyAttribute(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.modifyAttribute(requestParameters);
	}
	
	public HashMap<String, String> deleteAttribute(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.deleteAttribute(requestParameters);
	}

	public HashMap<String, String> activate(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.activate(requestParameters);
	}

	public HashMap<String, String> revoke(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException , KLMSIllegalOperationException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.revoke(requestParameters);
	}
	
	public HashMap<String, String> check(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException,KLMSItemNotFoundException , KLMSIllegalOperationException, KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.check(requestParameters);
	}
	
	public HashMap<String, String> getUsageAllocation(HashMap<String, String> requestParameters) throws KLMSIllegalOperationException, KLMSUniqueIdentifierMissingException,KLMSItemNotFoundException , KLMSPermissionDeniedException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.getUsageAllocation(requestParameters);
	}
	
	public HashMap<String, String> reKey(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		HashMap<String, String> responseParameters = objectManager.reKey(requestParameters);
		endpointManager.createAccessEntry(requestParameters, responseParameters);
		setIdPlaceholder(responseParameters);
		
		// Support for Testing of UC 9.2 without a proper Lifecycle Manager
		if(continueMegaHack){
			lifecycleManager.endMegaHack(responseParameters);
			continueMegaHack = false;
		}
		return responseParameters;
	}
	
	public HashMap<String, String> obtainLease(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSObjectArchivedException, KLMSItemNotFoundException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		HashMap<String, String> responseParameters = objectManager.obtainLease(requestParameters);
		return responseParameters;
	}
	
	public HashMap<String, String> archive(HashMap<String, String> requestParameters) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSUniqueIdentifierMissingException {
		KLMSUtils.addIDPlaceholderIfNecessary(requestParameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(requestParameters);
		return objectManager.archive(requestParameters);
	}

	public HashMap<String, String> recover(HashMap<String, String> parameters) throws KLMSPermissionDeniedException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException {
		KLMSUtils.addIDPlaceholderIfNecessary(parameters, getIdPlaceholder());
		endpointManager.verifyUserPermission(parameters);
		return objectManager.recover(parameters);
	}
	

	
	
	
	public Hashtable<String, String> getNumberOfObjects() {
		return objectManager.getNumberOfObjects();
	}
}
