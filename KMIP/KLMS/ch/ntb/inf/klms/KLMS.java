/**
 * KLMS.java
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

package ch.ntb.inf.klms;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import ch.ntb.inf.klms.db.KLMSDatabase;
import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSItemNotFoundException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.defaultvalues.DefaultValues;
import ch.ntb.inf.klms.manager.KLMSUniqueIdentifierMissingException;
import ch.ntb.inf.klms.model.attributes.QueryFunction;
import ch.ntb.inf.klms.model.klmsenum.EnumQueryFunction;
import ch.ntb.inf.klms.model.objects.base.Attribute;
import ch.ntb.inf.klms.service.KLMSKeyLifecycleService;
import ch.ntb.inf.klms.utils.KLMSUtils;

/** This Key Lifecycle Management System is only a prototype.
 * 	It shows how the architekture of such a system could look like
 * 	and provides support for the testing of all KMIP Use Cases.
*/
public class KLMS implements KLMSInterface{

	private KLMSKeyLifecycleService service;
	
	private boolean supportsAsynchonousOperations;

	
	public KLMS(){
		service = new KLMSKeyLifecycleService(new KLMSDatabase());
		supportsAsynchonousOperations = true;
	}
	
	public boolean supportsAsynchronousOperations(){
		return supportsAsynchonousOperations;
	}
	
	
	
	public HashMap<String, String> activate(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSPermissionDeniedException{
		return service.activate(parameters);
	}
	
	public HashMap<String, String> addAttribute(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException{
		return service.addAttribute(parameters);
	}
	
	public HashMap<String, String> archive(HashMap<String, String> parameters) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSUniqueIdentifierMissingException {
		return service.archive(parameters);
	}
	
	public HashMap<String, String> check(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException{
		return service.check(parameters);
	}
	
	public HashMap<String, String> createKeyPair(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		return service.createKeyPair(parameters);
	}
	
	public HashMap<String, String> createSymmetricKey(HashMap<String, String> parameters) throws NoSuchAlgorithmException {
		return service.createSymmetricKey(parameters);
	}
	
	public HashMap<String, String> createSymmetricKeyUsingTemplate(HashMap<String, String> parameters) throws NoSuchAlgorithmException, KLMSItemNotFoundException {
		return service.createSymmetricKeyUsingTemplate(parameters);
	}
	
	public HashMap<String, String> deleteAttribute(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException{
		return service.deleteAttribute(parameters);
	}
	
	public HashMap<String, String> destroy(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException{
		return service.destroy(parameters);
	}
	
	public HashMap<String, String> get(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException, KLMSObjectArchivedException{
		return service.get(parameters);
	}
	
	public HashMap<String, String> getAttributeList(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException{
		return service.getAttributeList(parameters);
	}
	
	public HashMap<String, String> getAttributes(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		return service.getAttributes(parameters);
	}
	
	public HashMap<String, String> getUsageAllocation(HashMap<String, String> parameters) throws KLMSIllegalOperationException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		return service.getUsageAllocation(parameters);
	}
	
	public HashMap<String, String> locate(HashMap<String, String> parameters) {
		return service.locate(parameters);
	}
	
	public HashMap<String, String> modifyAttribute(HashMap<String, String> parameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException{
		return service.modifyAttribute(parameters);
	}
	
	public HashMap<String, String> obtainLease(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSObjectArchivedException {
		return service.obtainLease(parameters);
	}
	
	public HashMap<String, String> query(HashMap<String, String> parameters) {
		HashMap<String, String> response = new HashMap<String, String>();

		
		ArrayList<Attribute> attributes = KLMSUtils.createAttributesFromHashMap(parameters);
		
		for(Attribute a : attributes){
			if(a instanceof QueryFunction){
				if(((QueryFunction) a).getQueryFunction() == EnumQueryFunction.QueryObjects){
					KLMSUtils.addAttributeListToParameterMap(DefaultValues.KLMS_CAPABILITIES_OBJECTS, response);
				}
				else if(((QueryFunction) a).getQueryFunction() == EnumQueryFunction.QueryOperations){
					KLMSUtils.addAttributeListToParameterMap(DefaultValues.KLMS_CAPABILITIES_OPERATIONS, response);
				}
			}
		}
		
		return response;
	}
	
	public HashMap<String, String> recover(HashMap<String, String> parameters) throws KLMSPermissionDeniedException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException {
		return service.recover(parameters);
	}
	
	public HashMap<String, String> registerPrivateKey(HashMap<String, String> parameters) {
		return service.registerPrivateKey(parameters);
	}
	
	public HashMap<String, String> registerPublicKey(HashMap<String, String> parameters) {
		return service.registerPublicKey(parameters);
	}
	
	public HashMap<String, String> registerSecretData(HashMap<String, String> parameters){
		return service.registerSecretData(parameters);
	}
	
	public HashMap<String, String> registerSymmetricKey(HashMap<String, String> parameters) {
		return service.registerSymmetricKey(parameters);
	}
	
	public HashMap<String, String> registerTemplate(HashMap<String, String> parameters){
		return service.registerTemplate(parameters);
	}
	
	public HashMap<String, String> reKey(HashMap<String, String> parameters) throws NoSuchAlgorithmException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException{
		return service.reKey(parameters);
	}
	
	public HashMap<String, String> revoke(HashMap<String, String> parameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException{
		return service.revoke(parameters);
	}
	

	
	
	public HashMap<String, String> getStatus(){
		HashMap<String, String> status = new HashMap<String, String>();
	
		status.put("Loaded KLMS", this.getClass().getName());
		
		status.putAll(service.getNumberOfObjects());
		
		return status;
	}

}

