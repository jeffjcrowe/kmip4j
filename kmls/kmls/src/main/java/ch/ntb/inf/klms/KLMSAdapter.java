/**
 * KLMSAdapter.java
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
import java.util.Random;

import org.apache.log4j.Logger;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSItemNotFoundException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.manager.KLMSUniqueIdentifierMissingException;
import ch.ntb.inf.klms.utils.KLMSAdapterUtils;
import ch.ntb.inf.kmip.attributes.ObjectType;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.kmipenum.EnumCancellationResult;
import ch.ntb.inf.kmip.kmipenum.EnumObjectType;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.kmipenum.EnumResultReason;
import ch.ntb.inf.kmip.kmipenum.EnumResultStatus;
import ch.ntb.inf.kmip.objects.MessageExtension;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.managed.ManagedObject;
import ch.ntb.inf.kmip.operationparameters.AsynchronousCorrelationValue;
import ch.ntb.inf.kmip.skeleton.KLMSAdapterInterface;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.utils.KMIPUtils;


public class KLMSAdapter implements KLMSAdapterInterface{

	private static final Logger logger = Logger.getLogger(KLMSAdapter.class);
	private KLMSInterface klms;
	
	private HashMap<String, KMIPBatch> asynchronousPuffer;

	
	public KLMSAdapter(){
		super();
	}
	
	public KLMSAdapter(KLMSInterface klms) {
		super();
		this.klms = klms;
		asynchronousPuffer = new HashMap<String, KMIPBatch>(); 
	}

	public void setKLMS(String klmsPath, String defaultPath) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		this.klms = (KLMSInterface) Class.forName(KMIPUtils.getClassPath(klmsPath, defaultPath)).newInstance();
	}
	
	
	public HashMap<String, String> getStatus(){
		HashMap<String, String> status = klms.getStatus();
		status.put("Loaded KLMSAdapter", this.getClass().getName());
		return status;
	}
	
	
	private KMIPByteString generateAsynchronousCorrelationValue() {
		Random r = new Random();
		byte[] randomByteArray = new byte[8];
		r.nextBytes(randomByteArray);
		return new KMIPByteString(randomByteArray);
	}
	
	


	public KMIPBatch doProcess(KMIPBatch requestBatch, Credential credential, boolean asynchronousIndicator) {
		KMIPBatch responseBatch = null;
		KMIPBatch asynchronousResponseBatch = null;
		
		if(requestBatch.hasAsynchronousCorrelationValueAttribute()){
			return createAsynchronousResponseBatch(requestBatch);
		}
		
		if(asynchronousIndicator){
			asynchronousResponseBatch = prepareAsynchronousOperation(requestBatch);
		}
	
		MessageExtension messageExtension = requestBatch.getMessageExtension();
		if(messageExtension != null && messageExtension.getCriticalityIndicator().getValue()){
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.FeatureNotSupported);
			responseBatch.setResultMessage("Critical Message Extension not recognized");
		} else{
			responseBatch = executeOperation(requestBatch, credential);
		}
		
		if(requestBatch.getUniqueBatchItemID() != null){
			responseBatch.setUniqueBatchItemID(requestBatch.getUniqueBatchItemID());
		}
		
		if(asynchronousIndicator){
			if(klms.supportsAsynchronousOperations()){
				String acv = asynchronousResponseBatch.getAsynchronousCorrelationValue().getValueString();
				this.asynchronousPuffer.put(acv, responseBatch);
			} 
			return asynchronousResponseBatch;
		}
		
		return responseBatch;
	}


	private KMIPBatch createAsynchronousResponseBatch(KMIPBatch requestBatch){
		if(requestBatch.getOperation().getValue() == EnumOperation.Cancel){
			this.asynchronousPuffer.remove(requestBatch.getAsynchronousCorrelationValueAttributeString());
			KMIPBatch responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.Success);
			responseBatch.addKMIPType(new EnumCancellationResult(EnumCancellationResult.Cancelled));
			AsynchronousCorrelationValue acv = new AsynchronousCorrelationValue();
			acv.setValue(requestBatch.getAsynchronousCorrelationValueAttributeString(), null);
			responseBatch.addAttribute(acv);
			return responseBatch;
		}else{
			return asynchronousPuffer.get(requestBatch.getAsynchronousCorrelationValueAttributeString());
		}
	}
	

	private KMIPBatch prepareAsynchronousOperation(KMIPBatch requestBatch) {
		KMIPBatch asynchronousResponseBatch = new KMIPBatch();
		asynchronousResponseBatch.setOperation(requestBatch.getOperation());
		if(klms.supportsAsynchronousOperations()){
			asynchronousResponseBatch.setResultStatus(new EnumResultStatus(EnumResultStatus.OperationPending));
			KMIPByteString asynchronousCorrelationValue = generateAsynchronousCorrelationValue();
			asynchronousResponseBatch.setAsynchronousCorrelationValue(asynchronousCorrelationValue);
		} else{
			asynchronousResponseBatch.setResultStatus(new EnumResultStatus(EnumResultStatus.OperationFailed));
			asynchronousResponseBatch.setResultReason(new EnumResultReason(EnumResultReason.OperationNotSupported));
			asynchronousResponseBatch.setResultMessage("Server does not support asynchronous Operations");
		}
		return asynchronousResponseBatch;
	}


	private KMIPBatch executeOperation(KMIPBatch requestBatch, Credential credential) {
		KMIPBatch responseBatch;
		ArrayList<Attribute> attributes = requestBatch.getAttributes();
		ArrayList<TemplateAttributeStructure> templateAttributeStructures = requestBatch.getTemplateAttributeStructures();
		ManagedObject managedObject = requestBatch.getManagedObject();
		logger.info("Operation = " + requestBatch.getOperation().getValueString());
		try{
			
			switch(requestBatch.getOperation().getValue()){
				case EnumOperation.Activate:
					responseBatch = activate(attributes, credential);
					break;
				case EnumOperation.AddAttribute:
					responseBatch = addAttribute(attributes, credential);
					break;
				case EnumOperation.Archive:
					responseBatch = archive(attributes, credential);
					break;
				case EnumOperation.Check:
					responseBatch = check(attributes, credential);
					break;
				case EnumOperation.Create:
					responseBatch = create(attributes, templateAttributeStructures, credential);
					break;	
				case EnumOperation.CreateKeyPair:
					responseBatch = createKeyPair(attributes, templateAttributeStructures, credential);
					break;
				case EnumOperation.DeleteAttribute:
					responseBatch = deleteAttribute(attributes, credential);
					break;
				case EnumOperation.Destroy:
					responseBatch = destroy(attributes, credential);
					break;
				case EnumOperation.Get:
					responseBatch = get(attributes, credential);
					break;
				case EnumOperation.GetAttributeList:
					responseBatch = getAttributeList(attributes, credential);
					break;
				case EnumOperation.GetAttributes:
					responseBatch = getAttributes(attributes, credential);
					break;
				case EnumOperation.GetUsageAllocation:
					responseBatch = getUsageAllocation(attributes, credential);
					break;
				case EnumOperation.Locate:
					responseBatch = locate(attributes, credential);
					break;
				case EnumOperation.ModifyAttribute:
					responseBatch = modifyAttribute(attributes, credential);
					break;
				case EnumOperation.ObtainLease:
					responseBatch = obtainLease(attributes, credential);
					break;
				case EnumOperation.Query:
					responseBatch = query(attributes, credential);
					break;
				case EnumOperation.Recover:
					responseBatch = recover(attributes, credential);
					break;
				case EnumOperation.Register:
					responseBatch = register(attributes, templateAttributeStructures, managedObject, credential);
					break;
				case EnumOperation.ReKey:
					responseBatch = reKey(attributes, templateAttributeStructures, credential);
					break;
				case EnumOperation.Revoke:
					responseBatch = revoke(attributes, credential);
					break;	
				default:
					responseBatch = new KMIPBatch();
					responseBatch.setResultStatus(new EnumResultStatus(EnumResultStatus.OperationFailed));
					responseBatch.setResultReason(new EnumResultReason(EnumResultReason.OperationNotSupported));	
			}
		}
		catch (KLMSIllegalOperationException e) {
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.IllegalOperation);
		} 
		catch (KLMSObjectNotPreActiveException e) {
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.PermissionDenied);
		} 
		catch (KLMSPermissionDeniedException e) {
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.PermissionDenied);
			responseBatch.setResultMessage("Access denied");
		} 
		catch (KLMSItemNotFoundException e) {
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.ItemNotFound);
		}
		catch (NoSuchAlgorithmException e) {
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.CryptographicFailure);
		}
		catch (KLMSObjectArchivedException e) {
			responseBatch = createNewKMIPBatch(EnumOperation.Get, EnumResultStatus.OperationFailed, EnumResultReason.ObjectArchived);
			responseBatch.setResultMessage("Object is archived");
		} 
		catch (Exception e){
			responseBatch = createNewKMIPBatch(requestBatch.getOperation().getValue(), EnumResultStatus.OperationFailed, EnumResultReason.GeneralFailure);
		}
		return responseBatch;
	}
	
	
	
	

	private KMIPBatch activate(ArrayList<Attribute> attributes, Credential credential) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.activate(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Activate);
	}
	
	private KMIPBatch addAttribute(ArrayList<Attribute> attributes, Credential credential) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.addAttribute(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.AddAttribute);
	}
	
	private KMIPBatch archive(ArrayList<Attribute> attributes, Credential credential) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSUniqueIdentifierMissingException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.archive(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Archive);
	}
	
	private KMIPBatch check(ArrayList<Attribute> attributes, Credential credential) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.check(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Check);
	}
	
	private KMIPBatch create(ArrayList<Attribute> attributes, ArrayList<TemplateAttributeStructure> templateAttributeStructures, Credential credential) throws NoSuchAlgorithmException, KLMSItemNotFoundException{
		EnumObjectType objectType = getAndRemoveObjectType(attributes);
		
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, templateAttributeStructures, null, credential);
		HashMap<String, String> responseParameters = null;
 		
		int ot = objectType.getValue();
		if(ot == EnumObjectType.SymmetricKey){
			logger.info("ObjectType = SymmetricKey");
			if(templateAttributeStructures.size() > 0){
				if(templateAttributeStructures.get(0).getNames() != null && templateAttributeStructures.get(0).getNames().size() > 0){
					responseParameters = klms.createSymmetricKeyUsingTemplate(requestParameters);
				} else{
					responseParameters = klms.createSymmetricKey(requestParameters);
				}
				return createResponseBatch(responseParameters, EnumOperation.Create);
			}
		}
		return createNewKMIPBatch(EnumOperation.Create, EnumResultStatus.OperationFailed, EnumResultReason.InvalidField);
	}

	private KMIPBatch createKeyPair(ArrayList<Attribute> attributes, ArrayList<TemplateAttributeStructure> templateAttributeStructures, Credential credential) throws NoSuchAlgorithmException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, templateAttributeStructures, null, credential);
		HashMap<String, String> responseParameters = klms.createKeyPair(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.CreateKeyPair);
	}
	
	private KMIPBatch deleteAttribute(ArrayList<Attribute> attributes, Credential credential) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.deleteAttribute(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.DeleteAttribute);
	}
	
	private KMIPBatch destroy(ArrayList<Attribute> attributes, Credential credential) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.destroy(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Destroy);
	}
	
	private KMIPBatch get(ArrayList<Attribute> attributes, Credential credential) throws KLMSPermissionDeniedException, KLMSObjectArchivedException, KLMSUniqueIdentifierMissingException {
		KMIPBatch responseBatch = new KMIPBatch();
		
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters;
		try{
			responseParameters = klms.get(requestParameters);
			KLMSAdapterUtils.addCertainAttributeToBatch(responseParameters, responseBatch, "Object Type");
			KLMSAdapterUtils.addCertainAttributeToBatch(responseParameters, responseBatch, "Unique Identifier");
			KLMSAdapterUtils.addObjectToBatch(responseParameters, responseBatch);
			addToKMIPBatch(responseBatch, EnumOperation.Get, EnumResultStatus.Success);
		}
		catch (KLMSItemNotFoundException e) {
			responseBatch = createNewKMIPBatch(EnumOperation.Get, EnumResultStatus.OperationFailed, EnumResultReason.ItemNotFound);
			responseBatch.setResultMessage("Object does not exist");
		}
		return responseBatch;
	}
	
	private KMIPBatch getAttributeList(ArrayList<Attribute> attributes, Credential credential) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.getAttributeList(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.GetAttributeList);
	}
	
	private KMIPBatch getAttributes(ArrayList<Attribute> attributes, Credential credential) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.getAttributes(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.GetAttributes);
	}
	
	private KMIPBatch getUsageAllocation(ArrayList<Attribute> attributes, Credential credential) throws KLMSIllegalOperationException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException{
		KMIPBatch responseBatch = new KMIPBatch();
		
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters;
	
		try {
			responseParameters = klms.getUsageAllocation(requestParameters);
			KLMSAdapterUtils.addAttributesToBatch(responseParameters, responseBatch);	
			addToKMIPBatch(responseBatch, EnumOperation.GetUsageAllocation, EnumResultStatus.Success);
		} catch (KLMSPermissionDeniedException e) {
			responseBatch = createNewKMIPBatch(EnumOperation.GetUsageAllocation, EnumResultStatus.OperationFailed, EnumResultReason.PermissionDenied);
			responseBatch.setResultMessage("Unable to allocate requested amount");
		}

		return responseBatch;
	}
	
	private KMIPBatch locate(ArrayList<Attribute> attributes, Credential credential) {
		logger.info("Operation = Locate");
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.locate(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Locate);
	}
	
	private KMIPBatch modifyAttribute(ArrayList<Attribute> attributes, Credential credential) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException {
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.modifyAttribute(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.ModifyAttribute);
	}
	
	private KMIPBatch obtainLease(ArrayList<Attribute> attributes, Credential credential) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSObjectArchivedException{
		KMIPBatch responseBatch = new KMIPBatch();
		
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters;
		
		try {
			responseParameters = klms.obtainLease(requestParameters);
			KLMSAdapterUtils.addAttributesToBatch(responseParameters, responseBatch);	
			addToKMIPBatch(responseBatch, EnumOperation.ObtainLease, EnumResultStatus.Success);
		} catch (KLMSPermissionDeniedException e) {
			addToKMIPBatch(responseBatch, EnumOperation.ObtainLease, EnumResultStatus.OperationFailed, EnumResultReason.PermissionDenied);
			responseBatch.setResultMessage("CO is in state Compromised, no lease given");
		}
		return responseBatch;
	}
	
	private KMIPBatch query(ArrayList<Attribute> attributes, Credential credential){
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.query(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Query);
	}
	
	private KMIPBatch recover(ArrayList<Attribute> attributes, Credential credential) throws KLMSPermissionDeniedException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.recover(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Recover);
	}
	
	private KMIPBatch register(ArrayList<Attribute> attributes, ArrayList<TemplateAttributeStructure> templateAttributeStructures, ManagedObject managedObject, Credential credential){
		KMIPBatch responseBatch = new KMIPBatch();
		responseBatch.setOperation(new EnumOperation(EnumOperation.Register));
	
		EnumObjectType objectType = getAndRemoveObjectType(attributes);
		logger.info("ObjectType = " + objectType.getValueString());		
		
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, templateAttributeStructures, managedObject, credential);
		HashMap<String, String> responseParameters = null;
		
		int ot = objectType.getValue();
		if(ot == EnumObjectType.PrivateKey){
			responseParameters = klms.registerPrivateKey(requestParameters);
		}
		else if(ot == EnumObjectType.PublicKey){
			responseParameters = klms.registerPublicKey(requestParameters);
		}
		else if(ot == EnumObjectType.SecretData){	
			responseParameters = klms.registerSecretData(requestParameters);
		}
		else if(ot == EnumObjectType.SymmetricKey){	
			responseParameters = klms.registerSymmetricKey(requestParameters);
		}
		else if(ot == EnumObjectType.Template){	
			responseParameters = klms.registerTemplate(requestParameters);
		}

		if(responseParameters != null){
			KLMSAdapterUtils.addAttributesToBatch(responseParameters, responseBatch);
			addToKMIPBatch(responseBatch, EnumOperation.Register, EnumResultStatus.Success);
		} else{
			responseBatch = createNewKMIPBatch(EnumOperation.GetAttributes, EnumResultStatus.OperationFailed, EnumResultReason.InvalidField);
		}
		
		return responseBatch;
	}
	
	private KMIPBatch reKey(ArrayList<Attribute> attributes, ArrayList<TemplateAttributeStructure> templateAttributeStructures, Credential credential) throws NoSuchAlgorithmException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, templateAttributeStructures, null, credential);
		HashMap<String, String> responseParameters = klms.reKey(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.ReKey);
	}

	private KMIPBatch revoke(ArrayList<Attribute> attributes, Credential credential) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException{
		HashMap<String, String> requestParameters  = KLMSAdapterUtils.createRequestParameters(attributes, null, null, credential);
		HashMap<String, String> responseParameters = klms.revoke(requestParameters);
		return createResponseBatch(responseParameters, EnumOperation.Revoke);
	}
	

	

	
	
	private EnumObjectType getAndRemoveObjectType(ArrayList<Attribute> attributes) {
		EnumObjectType objectType = null;
 		for(Attribute a : attributes){
			if(a instanceof ObjectType){
				objectType =  (EnumObjectType) a.getValues()[0].getValueAsKMIPType();
				attributes.remove(a);
				break;
			}
 		}
 		return objectType;
	}
	
	
	private KMIPBatch createResponseBatch(HashMap<String, String> responseParameters, int operation) {
		KMIPBatch responseBatch = new KMIPBatch();
		KLMSAdapterUtils.addAttributesToBatch(responseParameters, responseBatch);	
		addToKMIPBatch(responseBatch, operation, EnumResultStatus.Success);
		return responseBatch;
	}
	
	private KMIPBatch createNewKMIPBatch(int operation, int resultStatus){
		KMIPBatch batch = new KMIPBatch();
		addToKMIPBatch(batch, operation, resultStatus);
		return batch;
	}
	
	private KMIPBatch createNewKMIPBatch(int operation, int resultStatus, int resultReason){
		KMIPBatch batch = new KMIPBatch();
		addToKMIPBatch(batch, operation, resultStatus, resultReason);
		return batch;
	}
	
	private void addToKMIPBatch(KMIPBatch batch, int operation, int resultStatus, int resultReason){
		addToKMIPBatch(batch, operation, resultStatus);
		batch.setResultReason(new EnumResultReason(resultReason));
		if(resultReason == EnumResultReason.ItemNotFound){
			batch.setResultMessage("Object does not exist");
		}
	}
	
	private void addToKMIPBatch(KMIPBatch batch, int operation, int resultStatus){
		batch.setOperation(new EnumOperation(operation));
		batch.setResultStatus(new EnumResultStatus(resultStatus));
	}
	


		
}
