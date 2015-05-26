/**
 * KMIPDecoder.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPDecoder decodes the received KMIP-Message and returns a 
 * KMIPContainer with KMIPObjects. The KMIP-Message is a TTLV- 
 * encoded hexadecimal string stored in an ArrayList<Byte>.
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

package ch.ntb.inf.kmip.process.decoder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import ch.ntb.inf.kmip.attributes.CompromiseOccurrenceDate;
import ch.ntb.inf.kmip.attributes.CryptographicAlgorithm;
import ch.ntb.inf.kmip.attributes.CryptographicLength;
import ch.ntb.inf.kmip.attributes.CryptographicParameters;
import ch.ntb.inf.kmip.attributes.CryptographicUsageMask;
import ch.ntb.inf.kmip.attributes.LastChangeDate;
import ch.ntb.inf.kmip.attributes.LeaseTime;
import ch.ntb.inf.kmip.attributes.Name;
import ch.ntb.inf.kmip.attributes.ObjectType;
import ch.ntb.inf.kmip.attributes.RevocationReason;
import ch.ntb.inf.kmip.attributes.UniqueIdentifier;
import ch.ntb.inf.kmip.attributes.UsageLimits;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.kmipenum.EnumBatchError;
import ch.ntb.inf.kmip.kmipenum.EnumBlockCipherMode;
import ch.ntb.inf.kmip.kmipenum.EnumCancellationResult;
import ch.ntb.inf.kmip.kmipenum.EnumCertificateRequestType;
import ch.ntb.inf.kmip.kmipenum.EnumCertificateType;
import ch.ntb.inf.kmip.kmipenum.EnumCredentialType;
import ch.ntb.inf.kmip.kmipenum.EnumCryptographicAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumDerivationMethod;
import ch.ntb.inf.kmip.kmipenum.EnumHashingAlgorithm;
import ch.ntb.inf.kmip.kmipenum.EnumKeyCompressionType;
import ch.ntb.inf.kmip.kmipenum.EnumKeyFormatType;
import ch.ntb.inf.kmip.kmipenum.EnumKeyRoleType;
import ch.ntb.inf.kmip.kmipenum.EnumObjectType;
import ch.ntb.inf.kmip.kmipenum.EnumOpaqueDataType;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.kmipenum.EnumPaddingMethod;
import ch.ntb.inf.kmip.kmipenum.EnumPutFunction;
import ch.ntb.inf.kmip.kmipenum.EnumQueryFunction;
import ch.ntb.inf.kmip.kmipenum.EnumRecommendedCurve;
import ch.ntb.inf.kmip.kmipenum.EnumResultReason;
import ch.ntb.inf.kmip.kmipenum.EnumResultStatus;
import ch.ntb.inf.kmip.kmipenum.EnumSecretDataType;
import ch.ntb.inf.kmip.kmipenum.EnumSplitKeyMethod;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.kmipenum.EnumValidityIndicator;
import ch.ntb.inf.kmip.kmipenum.EnumWrappingMethod;
import ch.ntb.inf.kmip.objects.Authentication;
import ch.ntb.inf.kmip.objects.EncryptionKeyInformation;
import ch.ntb.inf.kmip.objects.KeyMaterial;
import ch.ntb.inf.kmip.objects.MACorSignatureKeyInformation;
import ch.ntb.inf.kmip.objects.MessageExtension;
import ch.ntb.inf.kmip.objects.VendorExtension;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.CommonTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.KeyBlock;
import ch.ntb.inf.kmip.objects.base.KeyValue;
import ch.ntb.inf.kmip.objects.base.KeyWrappingData;
import ch.ntb.inf.kmip.objects.base.KeyWrappingSpecification;
import ch.ntb.inf.kmip.objects.base.PrivateKeyTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.PublicKeyTemplateAttribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.base.TransparentKeyStructure;
import ch.ntb.inf.kmip.objects.managed.Certificate;
import ch.ntb.inf.kmip.objects.managed.OpaqueObject;
import ch.ntb.inf.kmip.objects.managed.PrivateKey;
import ch.ntb.inf.kmip.objects.managed.PublicKey;
import ch.ntb.inf.kmip.objects.managed.SecretData;
import ch.ntb.inf.kmip.objects.managed.SplitKey;
import ch.ntb.inf.kmip.objects.managed.SymmetricKey;
import ch.ntb.inf.kmip.objects.managed.Template;
import ch.ntb.inf.kmip.operationparameters.AsynchronousCorrelationValue;
import ch.ntb.inf.kmip.operationparameters.CertificateRequest;
import ch.ntb.inf.kmip.operationparameters.DerivationParameters;
import ch.ntb.inf.kmip.operationparameters.MaximumItems;
import ch.ntb.inf.kmip.operationparameters.Offset;
import ch.ntb.inf.kmip.operationparameters.QueryFunction;
import ch.ntb.inf.kmip.operationparameters.QueryOperation;
import ch.ntb.inf.kmip.operationparameters.ReplacedUniqueIdentifier;
import ch.ntb.inf.kmip.operationparameters.StorageStatusMask;
import ch.ntb.inf.kmip.operationparameters.ValidityDate;
import ch.ntb.inf.kmip.process.EnumStaticValues;
import ch.ntb.inf.kmip.types.KMIPBigInteger;
import ch.ntb.inf.kmip.types.KMIPBoolean;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPDateTime;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPInterval;
import ch.ntb.inf.kmip.types.KMIPLongInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;
import ch.ntb.inf.kmip.utils.KMIPUtils;


public class KMIPDecoder implements KMIPDecoderInterface{

	private final int TAG_SIZE = 3;
	private final int TYPE_SIZE = 1;
	private final int LENGTH_SIZE = 4;
	private final int TTL_SIZE = TAG_SIZE + TYPE_SIZE + LENGTH_SIZE;
	private final int PADDING_SIZE_INT = 4;
	private final int PADDING_SIZE_ENUM = 4;
	private final String ATTRIBUTE_LOCATION = "ch.ntb.inf.kmip.attributes.";
	private final String ENUM_LOCATION = "ch.ntb.inf.kmip.kmipenum.";
	private final String OPERATION_PARAMETER_LOCATION = "ch.ntb.inf.kmip.operationparameters.";

	private int subItemLength = 0;
	
	private void resetDecoder(){
		subItemLength = 0;
	}
	
	public KMIPContainer decodeRequest(ArrayList<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException {
		KMIPContainer container = new KMIPContainer();
		resetDecoder();
		checkTagAndType(EnumTag.RequestMessage, EnumType.Structure, al);
		
		// Request Header
		int length = decodeLength(al);
		decodeRequestHeader(al.subList(TTL_SIZE, TTL_SIZE + length), container);
		
		// Batch Items
		boolean hasMultipleBatchItems = container.getBatchCount() > 1 ? true : false;
		int offset = subItemLength;
		for(int i = 0; i < container.getBatchCount(); i++){
			decodeRequestBatchItem(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container.getBatch(i), hasMultipleBatchItems);
			offset += subItemLength;
		}
		
		return container;
	}
	
	public KMIPContainer decodeResponse(ArrayList<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException {
		KMIPContainer container = new KMIPContainer();
		resetDecoder();
		checkTagAndType(EnumTag.ResponseMessage, EnumType.Structure, al);
		
		// Response Header
		int length = decodeLength(al);
		decodeResponseHeader(al.subList(TTL_SIZE, TTL_SIZE + length), container);
		
		// Batch Items
		boolean hasMultipleBatchItems = container.getBatchCount() > 1 ? true : false;
		int offset = subItemLength;
		for(int i = 0; i < container.getBatchCount(); i++){
			decodeResponseBatchItem(al.subList(TTL_SIZE + offset, TTL_SIZE + length ), container.getBatch(i), hasMultipleBatchItems);
			offset += subItemLength;
		}
		
		return container;
	}
	
	private void decodeRequestHeader(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException, UnsupportedEncodingException{
		checkTagAndType(EnumTag.RequestHeader, EnumType.Structure, al);
		
		// Protocol Version 		(Required)
		int length = decodeLength(al);
		decodeProtocolVersion(al.subList(TTL_SIZE, TTL_SIZE + length));
		
		// Evaluate Optional Objects
		int tag = decodeTag(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + subItemLength + 3));
		int offset = subItemLength;
		int i = 0, numberOfOptions = 6;
		while(tag != EnumTag.BatchCount && i < numberOfOptions){
			switch(tag){
				case EnumTag.MaximumResponseSize: 
					// Maximum Response Size 	(Optional)
					decodeMaximumResponseSize(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				case EnumTag.AsynchronousIndicator:	
					// Asynchronous Indicator	(Optional)
					decodeAsynchronousIndicator(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				case EnumTag.Authentication:	
					// Authentication			(Optional)
					decodeAuthentication(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				case EnumTag.BatchErrorContinuationOption:	
					// Batch Error Continuation	(Optional)
					docodeBatchErrorContinuation(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				case EnumTag.BatchOrderOption:	
					// Batch Order Option		(Optional)
					decodeBatchOrderOption(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				case EnumTag.TimeStamp:	
					// Time Stamp				(Optional)
					decodeTimeStamp(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
					break;
				default:
					// Unexpected Tag
					throw new KMIPUnexpectedTagException(tag);
			}
			offset += subItemLength;
			tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
			i++;
		}
		
		// Batch Count 				(Required)
		decodeBatchCount(al.subList(TTL_SIZE + offset, TTL_SIZE + length), container);
		
		// set subItemLength of decodeRequestHeader
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeResponseHeader(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException{
		checkTagAndType(EnumTag.ResponseHeader, EnumType.Structure, al);
		
		// Protocol Version (Required)
		int length = decodeLength(al);
		decodeProtocolVersion(al.subList(TTL_SIZE, TTL_SIZE + length));
		
		// Time Stamp (Required)
		int protocolVersionLength = subItemLength;
		decodeTimeStamp(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), container);
		
		// Batch Count (Required)
		decodeBatchCount(al.subList(TTL_SIZE + subItemLength + protocolVersionLength , TTL_SIZE + length), container);
		subItemLength = length + TTL_SIZE; 
	}
	
	private void decodeProtocolVersion(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException{
		checkTagAndType(EnumTag.ProtocolVersion, EnumType.Structure, al);
		int length = decodeLength(al);
		decodeProtocolVersionMajor(al.subList(TTL_SIZE, TTL_SIZE + length));
		decodeProtocolVersionMinor(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length));
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeProtocolVersionMajor(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPProtocolVersionException{
		checkTagAndType(EnumTag.ProtocolVersionMajor, EnumType.Integer, al);
		KMIPInteger value = decodeKMIPInteger(al);
		if(value.getValue() != EnumStaticValues.ProtocolVersionMajor.getValue()){
			throw new KMIPProtocolVersionException("Protocol Version Major inconsistent, " + EnumStaticValues.ProtocolVersionMajor.getValue() + " expected");
		}
	}
	
	private void decodeProtocolVersionMinor(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPProtocolVersionException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.ProtocolVersionMinor, EnumType.Integer, al);
		KMIPInteger value = decodeKMIPInteger(al);
		if(value.getValue() != EnumStaticValues.ProtocolVersionMinor.getValue()){
			throw new KMIPProtocolVersionException("Protocol Version Minor inconsistent, " + EnumStaticValues.ProtocolVersionMinor.getValue() + " expected");
		}
	}
	
	private void decodeMaximumResponseSize(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.MaximumResponseSize, EnumType.Integer, al);
		container.setMaximumResponseSize(decodeKMIPInteger(al));
	}
	
	private void decodeAsynchronousIndicator(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.AsynchronousIndicator, EnumType.Boolean, al);
		container.setAsynchronousIndicator(decodeKMIPBoolean(al));
	}
		
	private void decodeAuthentication(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException{
		checkTagAndType(EnumTag.Authentication, EnumType.Structure, al);
		int length = decodeLength(al);
		container.setAuthentication(new Authentication(decodeCredential(al.subList(TTL_SIZE, TTL_SIZE + length))));		
		subItemLength = length + TTL_SIZE;
	}
	
	private Credential decodeCredential(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException{
		checkTagAndType(EnumTag.Credential, EnumType.Structure, al);
		Credential credential = new Credential();
		int length = decodeLength(al);
		decodeCredentialType(al.subList(TTL_SIZE, TTL_SIZE + length), credential);
		decodeCredentialValue(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), credential);		
		subItemLength = length + TTL_SIZE;
		return credential;
	}
	
	private void decodeCredentialType(List<Byte> al, Credential credential) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.CredentialType, EnumType.Enumeration, al);
		credential.setCredentialType(new EnumCredentialType(decodeKMIPEnumeration(al)));
	}
		
	private void decodeCredentialValue(List<Byte> al, Credential credential) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException{
		if(credential.getCredentialType().getValue() == EnumCredentialType.UsernameAndPassword){
			// Structure
			checkTagAndType(EnumTag.CredentialValue, EnumType.Structure, al);
			int length = decodeLength(al);
			
			// Username (Required)
			decodeUsername(al.subList(TTL_SIZE, TTL_SIZE + length), credential);	
			
			// Password (Optional)
			if(decodeTag(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + subItemLength + 3)) == EnumTag.Password){
				decodePassword(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), credential);	
			}
			subItemLength = length + TTL_SIZE;
		}
		// Extensions come here
		else{
			// Unknown CredentialType
			throw new KMIPUnexpectedTypeException("CredentialType","UsernameAndPassowrd");
		}
	}
	
	private void decodeUsername(List<Byte> al, Credential credential) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.Username, EnumType.TextString, al);
		credential.getCredentialValue().setUsername(decodeKMIPTextString(al));
	}
		
	private void decodePassword(List<Byte> al, Credential credential) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.Password, EnumType.TextString, al);
		credential.getCredentialValue().setPassword(decodeKMIPTextString(al));
	}
	
	private void docodeBatchErrorContinuation(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.BatchErrorContinuationOption, EnumType.Enumeration, al);
		container.setBatchErrorContinuationOption(new EnumBatchError(decodeKMIPEnumeration(al)));
	}
	
	private void decodeBatchOrderOption(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.BatchOrderOption, EnumType.Boolean, al);
		container.setBatchOrderOption(decodeKMIPBoolean(al));
	}

	private void decodeTimeStamp(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.TimeStamp, EnumType.DateTime, al);
		container.setTimeStamp(decodeKMIPDateTime(al));
	}
	
	private void decodeBatchCount(List<Byte> al, KMIPContainer container) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.BatchCount, EnumType.Integer, al);
		container.createBatches(decodeKMIPInteger(al).getValue());
	}

	private void decodeRequestBatchItem(List<Byte> al, KMIPBatch batch, boolean hasMultipleBatchItems) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.BatchItem, EnumType.Structure, al);
		int length = decodeLength(al);
		decodeOperation(al.subList(TTL_SIZE, TTL_SIZE + length), batch);
		int offset = subItemLength;
		
		// Unique Batch Item ID (Optional, Reqired if Batch Count > 1)
		if(hasMultipleBatchItems){	
			decodeUniqueBatchItemID(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
			offset += subItemLength;
		}
		
		// Request Payload (Required)
		decodePayload(EnumTag.RequestPayload, al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
		offset += subItemLength;
		
		// Message Extension (Optional)
		if(offset < length){	// Unique Batch Item ID (Optional)
			decodeMessageExtension(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
		}
		
		subItemLength = length + TTL_SIZE; 
	}
	
	private void decodeResponseBatchItem(List<Byte> al, KMIPBatch batch, boolean hasMultipleBatchItems) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.BatchItem, EnumType.Structure, al);
		int length = decodeLength(al);
		int offset = 0;
		
		// Operation 			(Required)
		if(decodeTag(al.subList(TTL_SIZE, TTL_SIZE + TAG_SIZE)) == EnumTag.Operation){
			decodeOperation(al.subList(TTL_SIZE, TTL_SIZE + length), batch);
			offset = subItemLength;
		}
		
		// Unique Batch Item ID	(Optional, Reqired if Batch Count > 1)
		if(hasMultipleBatchItems){	
			decodeUniqueBatchItemID(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
			offset += subItemLength;
		}
		
		// Result Status 		(Required)
		decodeResultStatus(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
		offset += subItemLength;
		int resultStatus = batch.getResultStatus().getValue();
		
		// Result Reason		(Optional -> Required if ResultSatus is "Failure")
		int tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
		if(resultStatus == EnumResultStatus.OperationFailed && tag == EnumTag.ResultReason){
			decodeResultReason(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
			}
		}
		
		// Result Message		(Optional if ResultStatus is not "Pending" or "Success") 
		if(tag == EnumTag.ResultMessage && (resultStatus != EnumResultStatus.OperationPending || resultStatus != EnumResultStatus.Success)){
			decodeResultMessage(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
			}
		}
		
		// Asynchronous Correlation Value	(Required if ResultStatus is "Pending")
		if(resultStatus == EnumResultStatus.OperationPending){
			KMIPByteString asynchronousCorrelationValue = decodeAsynchronousCorrelationValue(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
			batch.setAsynchronousCorrelationValue(asynchronousCorrelationValue);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
				offset += TAG_SIZE;
			}
		}		
		
		// Response Payload 	(Required if not failure)
		if(batch.getResultStatus().getValue() == EnumResultStatus.Success){
			decodePayload(EnumTag.ResponsePayload, al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
			offset += subItemLength;
		}
		
		// Message Extension	(Optional)
		if(offset < length){	// Unique Batch Item ID (Optional)
			decodeMessageExtension(al.subList(TTL_SIZE + offset, TTL_SIZE + length), batch);
		}
		
		subItemLength = length + TTL_SIZE; 
	}
	

	private void decodeOperation(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.Operation, EnumType.Enumeration, al);
		batch.setOperation(new EnumOperation(decodeKMIPEnumeration(al)));
	}
	
	private void decodeUniqueBatchItemID(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.UniqueBatchItemID, EnumType.ByteString, al);
		batch.setUniqueBatchItemID(decodeKMIPByteString(al));
	}
		
	private void decodeResultStatus(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.ResultStatus, EnumType.Enumeration, al);
		batch.setResultStatus(new EnumResultStatus(decodeKMIPEnumeration(al)));
	}
	
	private void decodeResultReason(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.ResultReason, EnumType.Enumeration, al);
		batch.setResultReason(new EnumResultReason(decodeKMIPEnumeration(al)));	
	}
	
	private void decodeResultMessage(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.ResultMessage, EnumType.TextString, al);
		batch.setResultMessage(decodeKMIPTextString(al));
	}
	
	private KMIPByteString decodeAsynchronousCorrelationValue(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.AsynchronousCorrelationValue, EnumType.ByteString, al);
		return decodeKMIPByteString(al);
	}
	
	private void decodePayload(int tag, List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(tag, EnumType.Structure, al);
		int length = decodeLength(al);
		for(int i = TTL_SIZE; i < length; i += subItemLength){
			decodePayloadElement(al.subList(i, al.size()), batch);
		}
		subItemLength = length + TTL_SIZE;
	}

	private void decodePayloadElement(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException, KMIPUnexpectedAttributeNameException{
		int tag = decodeTag(al.subList(0, 3));
		
		switch(tag){
			case EnumTag.ApplicationNamespace:
				decodeApplicationNamespace(al, batch);
				break;
		
			case EnumTag.AsynchronousCorrelationValue:
				batch.addAttribute(new AsynchronousCorrelationValue(decodeAsynchronousCorrelationValue(al)));
				break;
		
			case EnumTag.Attribute:
				batch.addAttribute(decodeAttribute(al));
				break;
				
			case EnumTag.AttributeIndex:
				decodeAttributeIndex(al, batch.getAttributes().get(batch.getAttributes().size()-1));
				break;
				
			case EnumTag.AttributeName:
				try{
					KMIPTextString attributeName = decodeAttributeName(al);
	                String className = attributeName.getValue().replaceAll("-", "");
	                String classNameFinal = Character.toUpperCase(className.charAt(0)) + className.substring(1);
	                Attribute  a = loadAttributeInstance(new KMIPTextString(classNameFinal));
	                batch.addAttribute(a);
				} catch(Exception e){
					e.printStackTrace();
				}
				break;
				
			case EnumTag.Certificate:
				decodeCertificate(al, batch);
				break;
				
			case EnumTag.CertificateRequestType:
				decodeCertificateRequestType(al, batch);
				break;
				
			case EnumTag.CertificateRequest:
				decodeCertificateRequest(al, batch);
				break;
				
			case EnumTag.CancellationResult:
				decodeCancellationResult(al, batch);
				break;
				
			case EnumTag.CompromiseOccurrenceDate:
				decodeCompromiseOccurrenceDate(al, batch);
				break;
				
			case EnumTag.CommonTemplateAttribute:
				decodeCommonTemplateAttribute(al, batch);
				break;
				
			case EnumTag.CryptographicUsageMask:
				decodeCryptographicUsageMask(al, batch);
				break;
				
			case EnumTag.DerivationMethod:
				decodeDerivationMethod(al, batch);
				break;
				
			case EnumTag.DerivationParameters:
				decodeDerivationParameters(al, batch);
				break;
				
			case EnumTag.KeyFormatType:
				decodeKeyFormatType(al, batch);
				break;
				
			case EnumTag.KeyCompressionType:
				batch.addKMIPType(decodeKeyCompressionType(al));
				break;
				
			case EnumTag.KeyWrappingSpecification:
				decodeKeyWrappingSpecification(al, batch);
				break;
				
			case EnumTag.LastChangeDate:
				decodeLastChangeDate(al, batch);
				break;
				
			case EnumTag.LeaseTime:
				decodeLeaseTime(al, batch);
				break;
				
			case EnumTag.MaximumItems:
				decodeMaximumItems(al, batch);
				break;
				
			case EnumTag.ObjectType:
				if(batch.getOperation().getValue() == EnumOperation.Query){
					decodeQueryObjectType(al, batch);
				}else{
					decodeObjectType(al, batch);
				}
				break;
				
			case EnumTag.Offset:
				decodeOffset(al, batch);
				break;
				
			case EnumTag.OpaqueObject:
				decodeOpaqueObject(al, batch);
				break;
				
			case EnumTag.Operation:
				decodeQueryOperation(al, batch);
				break;
				
			case EnumTag.PrivateKey:
				decodePrivateKey(al, batch);
				break;
				
			case EnumTag.PrivateKeyTemplateAttribute:
				decodePrivateKeyTemplateAttribute(al, batch);
				break;
				
			case EnumTag.PublicKey:
				decodePublicKey(al, batch);
				break;
				
			case EnumTag.PublicKeyTemplateAttribute:
				decodePublicKeyTemplateAttribute(al, batch);
				break;
				
			case EnumTag.PutFunction:
				decodePutFunction(al, batch);
				break;
				
			case EnumTag.QueryFunction:
				decodeQueryFunction(al, batch);
				break;
				
			case EnumTag.ReplacedUniqueIdentifier:
				decodeReplacedUniqueIdentifier(al, batch);
				break;
				
			case EnumTag.RevocationReason:
				decodeRevocationReason(al, batch);
				break;
				
			case EnumTag.SecretData:
				decodeSecretData(al, batch);
				break;
				
			case EnumTag.ServerInformation:
				decodeServerInformation(al, batch);	
				break;
				
			case EnumTag.SplitKey:
				decodeSplitKey(al, batch);
				break;
				
			case EnumTag.StorageStatusMask:
				decodeStorageStatusMask(al, batch);
				break;
				
			case EnumTag.SymmetricKey:
				decodeSymmetricKey(al, batch);
				break;
				
			case EnumTag.Template:
				decodeTemplate(al, batch);
				break;
				
			case EnumTag.TemplateAttribute:
				decodeTemplateAttribute(al, batch);
				break;
				
			case EnumTag.UniqueIdentifier:
			case EnumTag.PrivateKeyUniqueIdentifier:
			case EnumTag.PublicKeyUniqueIdentifier:
				UniqueIdentifier uid = decodeUniqueIdentifier(al);	
				batch.addAttribute(uid);
				break;
				
			case EnumTag.UsageLimitsCount:
				decodeUsageLimitsCount(al, batch);	
				break;
				
			case EnumTag.ValidityDate:
				decodeValidityDate(al, batch);	
				break;
				
			case EnumTag.ValidityIndicator:
				decodeValidityIndicator(al, batch);	
				break;
				
			case EnumTag.VendorIdentification:
				KMIPTextString vendorIdentification = decodeVendorIdentification(al);	
				batch.addKMIPType(vendorIdentification);
				break;
																						
			default:
				throw new KMIPUnexpectedTagException(tag);
		}
	}

	private void decodeApplicationNamespace(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkType(EnumTag.ApplicationNamespace, EnumType.TextString, al);
		batch.addKMIPType(decodeKMIPTextString(al));
	}
	
	private void decodeAttributeIndex(List<Byte> al, Attribute a) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.AttributeIndex, EnumType.Integer, al);
		a.setAttributeIndex(decodeKMIPInteger(al));
	}
	
	private void decodeQueryOperation(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.Operation, EnumType.Enumeration, al);
		batch.addAttribute(new QueryOperation(new EnumOperation(decodeKMIPEnumeration(al))));
	}
	
	private void decodeQueryObjectType(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException{
		checkType(EnumTag.ObjectType, EnumType.Enumeration, al);
		batch.addAttribute(new ObjectType(new EnumObjectType(decodeKMIPEnumeration(al))));
	}
	
	private void decodeOffset(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkType(EnumTag.Offset, EnumType.Interval, al);
		batch.addAttribute(new Offset(decodeKMIPInterval(al)));
	}
		
	private void decodeLeaseTime(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkType(EnumTag.LeaseTime, EnumType.Interval, al);
		batch.addAttribute(new LeaseTime(decodeKMIPInterval(al)));
	}
	
	private void decodeMaximumItems(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.MaximumItems, EnumType.Integer, al);
		batch.addAttribute(new MaximumItems(decodeKMIPInteger(al)));
	}
	
	private void decodeKeyFormatType(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.KeyFormatType, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumKeyFormatType(decodeKMIPEnumeration(al)));
	}
	
	private EnumKeyCompressionType decodeKeyCompressionType(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.KeyCompressionType, EnumType.Enumeration, al);
		return new EnumKeyCompressionType(decodeKMIPEnumeration(al));
	}
	
	private void decodeKeyWrappingSpecification(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.KeyWrappingSpecification, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyWrappingSpecification kws = new KeyWrappingSpecification();
		kws.setWrappingMethod(decodeWrappingMethod(al.subList(TTL_SIZE, TTL_SIZE + length)));
		int offset = subItemLength;
		int tag = 0;
		if(offset < length){
			tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
		}
		// Encryption Key Information
		if(tag == EnumTag.EncryptionKeyInformation){
			kws.setEncryptionKeyInformation(decodeEncryptionKeyInformation(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}

		// Mac Signature Key Information
		if(tag == EnumTag.MACSignatureKeyInformation){
			kws.setMacSignatureKeyInformation(decodeMACSignatureKeyInformation(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Attribute Name
		KMIPTextString attributeName;
		while(tag == EnumTag.AttributeName){
			attributeName = decodeAttributeName(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
			kws.setName(attributeName);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		batch.setKeyWrappingSpecification(kws);
		subItemLength = length + TTL_SIZE;
	}
	
	private EnumWrappingMethod decodeWrappingMethod(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.WrappingMethod, EnumType.Enumeration, al);
		return new EnumWrappingMethod(decodeKMIPEnumeration(al));
	}
	
	private EncryptionKeyInformation decodeEncryptionKeyInformation(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.EncryptionKeyInformation, EnumType.Structure, al);
		int length = decodeLength(al);
		EncryptionKeyInformation eki = new EncryptionKeyInformation();

		UniqueIdentifier uid = decodeUniqueIdentifier(al.subList(TTL_SIZE, TTL_SIZE + length));
		eki.setUniqueIdentifier(uid);
		int offset = subItemLength;
		if(offset < length){
			CryptographicParameters cp = decodeCryptographicParameters(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
			eki.setCryptographicParameters(cp);
		}
		
		subItemLength = length + TTL_SIZE;
		return eki;
	}
	
	private MACorSignatureKeyInformation decodeMACSignatureKeyInformation(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.MACSignatureKeyInformation, EnumType.Structure, al);
		int length = decodeLength(al);
		MACorSignatureKeyInformation macSigKeyInfo = new MACorSignatureKeyInformation();

		UniqueIdentifier uid = decodeUniqueIdentifier(al.subList(TTL_SIZE, TTL_SIZE + length));
		macSigKeyInfo.setUniqueIdentifier(uid);
		int offset = subItemLength;
		if(offset < length){
			CryptographicParameters cp = decodeCryptographicParameters(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
			macSigKeyInfo.setCryptographicParameters(cp);
		}
		
		subItemLength = length + TTL_SIZE;
		return macSigKeyInfo;
	}
		
	private void decodeLastChangeDate(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException {
		checkTagAndType(EnumTag.LastChangeDate, EnumType.DateTime, al);
		batch.addAttribute(new LastChangeDate(decodeKMIPDateTime(al)));
	}
	
	private void decodeQueryFunction(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.QueryFunction, EnumType.Enumeration, al);
		batch.addAttribute(new QueryFunction(new EnumQueryFunction(decodeKMIPEnumeration(al))));	
	}

	private void decodeCompromiseOccurrenceDate(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException {
		checkTagAndType(EnumTag.CompromiseOccurrenceDate, EnumType.DateTime, al);
		batch.addAttribute(new CompromiseOccurrenceDate(decodeKMIPDateTime(al)));
	}
	
	private void decodeCommonTemplateAttribute(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.CommonTemplateAttribute, EnumType.Structure, al);
		int length = decodeLength(al);
		CommonTemplateAttribute cta = new CommonTemplateAttribute();
		decodeTemplateAttributeStructure(al, cta, length);
		batch.addTemplateAttributeStructure(cta);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeCryptographicUsageMask(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.CryptographicUsageMask, EnumType.Integer, al);
		batch.addAttribute(new CryptographicUsageMask(decodeKMIPInteger(al)));
	}
	
	private void decodeDerivationMethod(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.DerivationMethod, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumDerivationMethod(decodeKMIPEnumeration(al)));
	}
	
	private void decodeDerivationParameters(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.DerivationParameters, EnumType.Structure, al);
		int length = decodeLength(al);
		DerivationParameters dp = new DerivationParameters();
		int offset = 0;
		int tag = 0;
		if(offset < length){
			tag = decodeTag(al.subList(TTL_SIZE, TTL_SIZE + 3));	
		}
		// Cryptographic Parameters
		if(tag == EnumTag.CryptographicParameters){
			CryptographicParameters cp = decodeCryptographicParameters(al.subList(TTL_SIZE, TTL_SIZE + length));
			dp.setCryptographicParameters(cp);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Initialization Vector
		if(tag == EnumTag.InitializationVector){
			decodeInitializationVector(al.subList(TTL_SIZE + offset, TTL_SIZE + length), dp);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Derivation Data
		if(tag == EnumTag.DerivationData){
			decodeDerivationData(al.subList(TTL_SIZE + offset, TTL_SIZE + length), dp);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Salt
		if(tag == EnumTag.Salt){
			decodeSalt(al.subList(TTL_SIZE + offset, TTL_SIZE + length), dp);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Iteration Count
		if(tag == EnumTag.IterationCount){
			decodeIterationCount(al.subList(TTL_SIZE + offset, TTL_SIZE + length), dp);
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}

		batch.setDerivationParameters(dp);
		subItemLength = length + TTL_SIZE;
	}
	
	private CryptographicParameters decodeCryptographicParameters(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.CryptographicParameters, EnumType.Structure, al);
		int length = decodeLength(al);
		CryptographicParameters cp = new CryptographicParameters();
		int tag = decodeTag(al.subList(TTL_SIZE, TTL_SIZE + 3));
		int offset = 0;
		while(tag == EnumTag.BlockCipherMode || tag == EnumTag.PaddingMethod ||  tag == EnumTag.HashingAlgorithm || tag == EnumTag.KeyRoleType){
			switch(tag){
				case EnumTag.BlockCipherMode:
					decodeBlockCipherMode(al.subList(TTL_SIZE + offset, TTL_SIZE + length), cp);
					break;
				case EnumTag.PaddingMethod:
					decodePaddingMethod(al.subList(TTL_SIZE + offset, TTL_SIZE + length), cp);
					break;
				case EnumTag.HashingAlgorithm:
					decodeHashingAlgorithm(al.subList(TTL_SIZE + offset, TTL_SIZE + length), cp);
					break;
				case EnumTag.KeyRoleType:
					decodeKeyRoleType(al.subList(TTL_SIZE + offset, TTL_SIZE + length), cp);
					break;
				default:
					// No Parameters in Structure -> Nothing to do
					break;
			}
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));
			}
		}
		subItemLength = length + TTL_SIZE;
		return cp;
	}
	
	private void decodeBlockCipherMode(List<Byte> al, CryptographicParameters cp) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.BlockCipherMode, EnumType.Enumeration, al);
		cp.setBlockCipherMode(new EnumBlockCipherMode(decodeKMIPEnumeration(al)));
	}
	
	private void decodePaddingMethod(List<Byte> al, CryptographicParameters cp) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.PaddingMethod, EnumType.Enumeration, al);
		cp.setPaddingMethod(new EnumPaddingMethod(decodeKMIPEnumeration(al))); 
	}
	
	private void decodeHashingAlgorithm(List<Byte> al, CryptographicParameters cp) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.HashingAlgorithm, EnumType.Enumeration, al);
		cp.setHashingAlgorithm(new EnumHashingAlgorithm(decodeKMIPEnumeration(al)));
	}
	
	private void decodeKeyRoleType(List<Byte> al, CryptographicParameters cp) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.KeyRoleType, EnumType.Enumeration, al);
		cp.setKeyRoleType(new EnumKeyRoleType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeInitializationVector(List<Byte> al, DerivationParameters dp) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.InitializationVector, EnumType.ByteString, al);
		dp.setInitializationVector(decodeKMIPByteString(al));
	}
	
	private void decodeDerivationData(List<Byte> al, DerivationParameters dp) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.DerivationData, EnumType.ByteString, al);
		dp.setDerivationData(decodeKMIPByteString(al));		
	}
	
	private void decodeSalt(List<Byte> al, DerivationParameters dp) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.Salt, EnumType.ByteString, al);
		dp.setSalt(decodeKMIPByteString(al));		
	}
	
	private void decodeIterationCount(List<Byte> al, DerivationParameters dp) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.IterationCount, EnumType.Integer, al);
		dp.setIterationCount(decodeKMIPInteger(al));
	}
		
	private void decodePrivateKeyTemplateAttribute(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.PrivateKeyTemplateAttribute, EnumType.Structure, al);
		int length = decodeLength(al);
		PrivateKeyTemplateAttribute pkta = new PrivateKeyTemplateAttribute();
		decodeTemplateAttributeStructure(al, pkta, length);
		batch.addTemplateAttributeStructure(pkta);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodePublicKeyTemplateAttribute(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.PublicKeyTemplateAttribute, EnumType.Structure, al);
		int length = decodeLength(al);
		PublicKeyTemplateAttribute pkta = new PublicKeyTemplateAttribute();
		decodeTemplateAttributeStructure(al, pkta, length);
		batch.addTemplateAttributeStructure(pkta);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodePutFunction(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.PutFunction, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumPutFunction(decodeKMIPEnumeration(al)));
	}
	
	private void decodeTemplateAttribute(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.TemplateAttribute, EnumType.Structure, al);
		int length = decodeLength(al);
		TemplateAttribute ta = new TemplateAttribute();
		decodeTemplateAttributeStructure(al, ta, length);
		batch.addTemplateAttributeStructure(ta);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeTemplateAttributeStructure(List<Byte> al, TemplateAttributeStructure tas, int length) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		for(int i=TTL_SIZE; i<=length; i+=subItemLength){	
			if(decodeTag(al.subList(i, i + TAG_SIZE)) == EnumTag.Name){
				decodeName(al.subList(i, TTL_SIZE + length), tas);
			}
			else{
				Attribute a = decodeAttribute(al.subList(i, TTL_SIZE+length));
				tas.addAttribute(a); 
			}
		}
	}
	
	private void decodeObjectType(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException{
		checkType(EnumTag.ObjectType, EnumType.Enumeration, al);
		batch.addAttribute(new ObjectType((KMIPEnumeration) new EnumObjectType(decodeKMIPEnumeration(al))));
	}
	
	private void decodeRevocationReason(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException, UnsupportedEncodingException {
		checkTagAndType(EnumTag.RevocationReason, EnumType.Structure, al);
		int length = decodeLength(al);
		RevocationReason rr = new RevocationReason();
		decodeRevocationReasonCode(al.subList(TTL_SIZE, TTL_SIZE + length), rr);
		
		if(al.subList(0, 3) != null && decodeTag(al.subList(0, 3)) == EnumTag.RevocationMessage){
			decodeRevocationMessage(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), rr);
		}
		
		batch.addAttribute(rr);
		subItemLength = length + TTL_SIZE; 
	}
	
	private void decodeRevocationReasonCode(List<Byte> al, RevocationReason rr) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkType(EnumTag.RevocationReasonCode, EnumType.Enumeration, al);
		rr.setValue(Integer.toString(decodeKMIPEnumeration(al)), "RevocationReasonCode");	
	}

	private void decodeRevocationMessage(List<Byte> al, RevocationReason rr) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkType(EnumTag.RevocationMessage, EnumType.TextString, al);
		rr.setValue(decodeKMIPTextString(al).getValue(), "RevocationMessage");
	}
	
	private void decodeReplacedUniqueIdentifier(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, UnsupportedEncodingException{
		checkType(EnumTag.ReplacedUniqueIdentifier, EnumType.TextString, al);
		batch.addAttribute(new ReplacedUniqueIdentifier(decodeKMIPTextString(al)));
	}
	
	private UniqueIdentifier decodeUniqueIdentifier(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, UnsupportedEncodingException{
		checkType(EnumTag.UniqueIdentifier, EnumType.TextString, al);
		return new UniqueIdentifier(decodeKMIPTextString(al));
	}
		
	private void decodeUsageLimitsCount(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException {
		checkTagAndType(EnumTag.UsageLimitsCount, EnumType.LongInteger, al);
		UsageLimits usageLimits = new UsageLimits();
		usageLimits.setUsageLimitsCount(decodeKMIPLongInteger(al));
		batch.addAttribute(usageLimits);
	}
		
	private void decodeName(List<Byte> al, TemplateAttributeStructure tas) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPPaddingExpectedException, UnsupportedEncodingException{
		checkTagAndType(EnumTag.Name, EnumType.Structure, al);
		int length = decodeLength(al);
		Name name = new Name();
		decodeNameValue(al.subList(TTL_SIZE, TTL_SIZE + length), name);
		decodeNameType(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), name);
		tas.addName(name);
		subItemLength = length + TTL_SIZE; 
	}
	
	private void decodeNameValue(List<Byte> al, Name name) throws UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.NameValue, EnumType.TextString, al);
		name.setValue(decodeKMIPTextString(al).getValue(), "NameValue");
	}
	
	private void decodeNameType(List<Byte> al, Name name) throws UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.NameType, EnumType.Enumeration, al); 		
		name.setValue(Integer.toString(decodeKMIPEnumeration(al)), "NameType");
	}
	
	private void decodeTemplate(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.Template, EnumType.Structure, al);
		int length = decodeLength(al);
		Template template = new Template();
		batch.setManagedObject(template);
		for(int i=TTL_SIZE; i<=length; i+=subItemLength){	
			template.addAttribute(decodeAttribute(al.subList(i, TTL_SIZE+length))); 	
		}
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeSecretData(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.SecretData, EnumType.Structure, al);
		int length = decodeLength(al);
		SecretData sd = new SecretData();
		
		// Secret Data Type
		decodeSecretDataType(al.subList(TTL_SIZE, TTL_SIZE + length), sd);
		int offset = subItemLength;
		
		// KeyBlock
		KeyBlock keyBlock = decodeKeyBlock(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
		sd.setKeyBlock(keyBlock);
		batch.setManagedObject(sd);
		subItemLength = length + TTL_SIZE;	
	}
	
	private void decodeSecretDataType(List<Byte> al, SecretData sd) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.SecretDataType, EnumType.Enumeration, al);
		sd.setSecretDataType(new EnumSecretDataType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeServerInformation(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.VendorIdentification, EnumType.TextString, al);
		batch.addKMIPType(decodeKMIPTextString(al));
	}
	
	private void decodeCertificate(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.Certificate, EnumType.Structure, al);
		int length = decodeLength(al);
		Certificate cert = new Certificate();
		decodeCertificateType(al.subList(TTL_SIZE, TTL_SIZE + length), cert);
		int offset = subItemLength;
		decodeCertificateValue(al.subList(TTL_SIZE + offset, TTL_SIZE + length), cert);
		batch.setManagedObject(cert);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeCertificateType(List<Byte> al, Certificate cert) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CertificateType, EnumType.Enumeration, al);
		cert.setCertificateType(new EnumCertificateType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeCertificateValue(List<Byte> al, Certificate cert) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CertificateValue, EnumType.ByteString, al);
		cert.setCertificateValue(decodeKMIPByteString(al));		
	}
	
	private void decodeCertificateRequestType(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.CertificateRequestType, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumCertificateRequestType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeCertificateRequest(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CertificateRequest, EnumType.ByteString, al);
		batch.addAttribute(new CertificateRequest(decodeKMIPByteString(al)));		
	}
	
	private void decodeCancellationResult(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.CancellationResult, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumCancellationResult(decodeKMIPEnumeration(al)));
	}
	
	private void decodeOpaqueObject(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.OpaqueObject, EnumType.Structure, al);
		int length = decodeLength(al);
		OpaqueObject opaque = new OpaqueObject();
		decodeOpaqueDataType(al.subList(TTL_SIZE, TTL_SIZE + length), opaque);
		int offset = subItemLength;
		decodeOpaqueDataValue(al.subList(TTL_SIZE + offset, TTL_SIZE + length), opaque);
		batch.setManagedObject(opaque);
		subItemLength = length + TTL_SIZE;
	}

	private void decodeOpaqueDataType(List<Byte> al, OpaqueObject opaque) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CertificateType, EnumType.Enumeration, al);
		opaque.setOpaqueDataType(new EnumOpaqueDataType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeOpaqueDataValue(List<Byte> al, OpaqueObject opaque) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CertificateValue, EnumType.ByteString, al);
		opaque.setOpaqueDataValue(decodeKMIPByteString(al));	
	}
	
	private void decodeSplitKey(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.SplitKey, EnumType.Structure, al);
		int length = decodeLength(al);
		SplitKey splitKey = new SplitKey();
		// Split Key Parts
		decodeSplitKeyParts(al, splitKey);
		int offset = subItemLength;
		// Key Part Identifier
		decodeKeyPartIdentifier(al.subList(TTL_SIZE + offset, TTL_SIZE + length), splitKey);
		offset += subItemLength;
		// Split Key Threshold
		decodeSplitKeyThreshold(al.subList(TTL_SIZE + offset, TTL_SIZE + length), splitKey);
		offset += subItemLength;
		// Split Key Method
		decodeSplitKeyMethod(al.subList(TTL_SIZE + offset, TTL_SIZE + length), splitKey);
		offset += subItemLength;
		// Prime Field Size (Required if SplitKeyMethod is Polynomial Sharing Prime Field)
		if(splitKey.getSplitKeyMethod().getValue() == EnumSplitKeyMethod.PolynomialSharingPrimeField){
			decodePrimeFieldSize(al.subList(TTL_SIZE + offset, TTL_SIZE + length), splitKey);
			offset += subItemLength;
		}
		// Key Block
		KeyBlock keyBlock = decodeKeyBlock(al.subList(TTL_SIZE + offset, TTL_SIZE + length));
		splitKey.setKeyBlock(keyBlock);
		
		batch.setManagedObject(splitKey);
		subItemLength = length + TTL_SIZE;
	}

	private void decodeSplitKeyParts(List<Byte> al, SplitKey splitKey) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.SplitKeyParts, EnumType.Integer, al);   
        splitKey.setSplitKeyParts(decodeKMIPInteger(al));
	}
	
	private void decodeKeyPartIdentifier(List<Byte> al, SplitKey splitKey) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.KeyPartIdentifier, EnumType.Integer, al);
        splitKey.setKeyPartIdentifier(decodeKMIPInteger(al));
	}
	
	private void decodeSplitKeyThreshold(List<Byte> al, SplitKey splitKey) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.SplitKeyThreshold, EnumType.Integer, al);
        splitKey.setSplitKeyThreshhosd(decodeKMIPInteger(al));
	}
	
	private void decodeSplitKeyMethod(List<Byte> al, SplitKey splitKey) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.SplitKeyMethod, EnumType.Enumeration, al);
        splitKey.setSplitKeyMethod(new EnumSplitKeyMethod(decodeKMIPEnumeration(al)));
	}
	
	private void decodePrimeFieldSize(List<Byte> al, SplitKey splitKey) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.PrimeFieldSize, EnumType.BigInteger, al);
        splitKey.setPrimeFieldSize(decodeKMIPBigInteger(al));
	}
	
	private void decodeStorageStatusMask(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.StorageStatusMask, EnumType.Integer, al);
		batch.addAttribute(new StorageStatusMask(decodeKMIPInteger(al)));
	}
	
	private void decodeSymmetricKey(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.SymmetricKey, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyBlock keyBlock = decodeKeyBlock(al.subList(TTL_SIZE, TTL_SIZE + length));
		batch.setManagedObject(new SymmetricKey(keyBlock));
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodePrivateKey(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.PrivateKey, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyBlock keyBlock = decodeKeyBlock(al.subList(TTL_SIZE, TTL_SIZE + length));
		batch.setManagedObject(new PrivateKey(keyBlock));
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodePublicKey(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.PublicKey, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyBlock keyBlock = decodeKeyBlock(al.subList(TTL_SIZE, TTL_SIZE + length));
		batch.setManagedObject(new PublicKey(keyBlock));
		subItemLength = length + TTL_SIZE;
	}
	
	private KeyBlock decodeKeyBlock(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.KeyBlock, EnumType.Structure, al);
		int length = decodeLength(al);

		KeyBlock keyBlock = new KeyBlock();
		
		for(int i=TTL_SIZE; i<=length; i+=subItemLength){	
			int tag = decodeTag(al.subList(i, i + TAG_SIZE));
			if(tag == EnumTag.KeyFormatType){
				decodeKeyFormatType(al.subList(i, TTL_SIZE + length), keyBlock);
			} else if(tag == EnumTag.KeyCompressionType){
				keyBlock.setKeyCompressionType(decodeKeyCompressionType(al.subList(i, TTL_SIZE + length)).getKey());
			} else if(tag == EnumTag.KeyValue){
				decodeKeyValue(al.subList(i, TTL_SIZE + length), keyBlock);
			} else if(tag == EnumTag.CryptographicAlgorithm){
				decodeCryptographicAlgorithm(al.subList(i, TTL_SIZE + length), keyBlock);
			} else if(tag == EnumTag.CryptographicLength){
				decodeCryptographicLength(al.subList(i, TTL_SIZE + length), keyBlock); 
			} else if(tag == EnumTag.KeyWrappingData){
				decodeKeyWrappingData(al.subList(i, TTL_SIZE + length), keyBlock);
			} 
		}
		return keyBlock;
	}
	
	private void decodeKeyFormatType(List<Byte> al, KeyBlock keyBlock) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.KeyFormatType, EnumType.Enumeration, al);
        keyBlock.setKeyFormatType(new EnumKeyFormatType(decodeKMIPEnumeration(al)));
	}
	
	private void decodeKeyWrappingData(List<Byte> al, KeyBlock keyBlock) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException {
		checkTagAndType(EnumTag.KeyWrappingData, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyWrappingData kwd = new KeyWrappingData();
		kwd.setWrappingMethod(decodeWrappingMethod(al.subList(TTL_SIZE, TTL_SIZE + length)));
		int offset = subItemLength;
		int tag = 0;
		if(offset < length){
			tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
		}
		// Encryption Key Information
		if(tag == EnumTag.EncryptionKeyInformation){
			kwd.setEncryptionKeyInformation(decodeEncryptionKeyInformation(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}

		// Mac Signature Key Information
		if(tag == EnumTag.MACSignatureKeyInformation){
			kwd.setMacSignatureKeyInformation(decodeMACSignatureKeyInformation(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// Mac Signature
		if(tag == EnumTag.MACSignature){
			kwd.setMacSignature(decodeMACSignature(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}
		
		// IV/Counter/Nounce
		if(tag == EnumTag.IVCounterNonce){
			kwd.setMacSignature(decodeIVCounterNonce(al.subList(TTL_SIZE + offset, TTL_SIZE + length)));
			offset += subItemLength;
			if(offset < length){
				tag = decodeTag(al.subList(TTL_SIZE + offset, TTL_SIZE + offset + 3));	
			}
		}

		keyBlock.setKeyWrappingData(kwd);
		subItemLength = length + TTL_SIZE;
	}
	
	private KMIPByteString decodeMACSignature(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.MACSignature, EnumType.ByteString, al);
		return decodeKMIPByteString(al);
	}
	
	private KMIPByteString decodeIVCounterNonce(List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.IVCounterNonce, EnumType.ByteString, al);
		return decodeKMIPByteString(al);
	}
	
	private void decodeKeyValue(List<Byte> al, KeyBlock keyBlock) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException, KMIPUnexpectedAttributeNameException, UnsupportedEncodingException {
		checkTagAndType(EnumTag.KeyValue, EnumType.Structure, al);
		int length = decodeLength(al);
		KeyValue keyValue = new KeyValue();
				
		for(int i=TTL_SIZE; i<=length; i+=subItemLength){	
			int tag = decodeTag(al.subList(i, TTL_SIZE + length));
			if(tag == EnumTag.KeyMaterial){
				decodeKeyMaterial(al.subList(i, TTL_SIZE + length), keyValue, keyBlock.getKeyFormatType());
			} else{
				keyValue.addAttribute(decodeAttribute(al.subList(i, TTL_SIZE + length)));
			} 
		}
		keyBlock.setKeyValue(keyValue);
		subItemLength = length + TTL_SIZE;
	}
	
	private void decodeKeyMaterial(List<Byte> al, KeyValue keyValue, EnumKeyFormatType keyFormatType) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException {
		checkTag(EnumTag.KeyMaterial, al);
		int type = decodeType(al.get(3));
	
		// for Raw, Opaque, PKCS1, PKCS8, ECPrivateKey, or Extension Key Format Types
		if(type == EnumType.ByteString){
			int length = decodeLength(al);
			int paddingLength = getPaddingLength(length);
			KMIPByteString keyMaterial = decodeByteString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength);
			keyValue.setKeyMaterial(new KeyMaterial(keyMaterial));
			subItemLength = length + TTL_SIZE;
		}	
		// for Transparent Key Structures, Extension Key Format Types
		else if(type == EnumType.Structure){
			int length = decodeLength(al);
			TransparentKeyStructure tks = decodeKeyMaterialStructure(al.subList(TTL_SIZE, TTL_SIZE + length), keyFormatType, length);
			keyValue.setKeyMaterial(new KeyMaterial(tks));
			subItemLength = length + TTL_SIZE;	
		}
		else{
			throw new KMIPUnexpectedTypeException("Key Material", "ByteString");
		}
	}
	
	// Not tested yet
	private TransparentKeyStructure decodeKeyMaterialStructure(List<Byte> al, EnumKeyFormatType keyFormatType, int length) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTagException{
		TransparentKeyStructure tks = new TransparentKeyStructure(keyFormatType);
		decodeTransparentKeyStructureParameters(al, tks, length);		
		return tks;
	}
	
	// Not tested yet
	private void decodeTransparentKeyStructureParameters(List<Byte> al, TransparentKeyStructure tks, int length) throws KMIPUnexpectedTypeException, KMIPPaddingExpectedException, KMIPUnexpectedTagException, UnsupportedEncodingException {
		List<Byte> subAl;
		int tag; 
		for(int i=TTL_SIZE; i<=length; i+=subItemLength){	
			subAl = al.subList(i, TTL_SIZE + length);
			tag = decodeTag(subAl);
			
			switch(tag){
				case EnumTag.Modulus:
					tks.setModulus(decodeModulus(subAl));
					break;
					
				case EnumTag.PrivateExponent:
					tks.setPrivateExponent(decodePrivateExponent(subAl));
					break;
					
				case EnumTag.PublicExponent:
					tks.setPublicExponent(decodePublicExponent(subAl));
					break;
					
				case EnumTag.P:
					tks.setP(decodeP(subAl));
					break;
					
				case EnumTag.Q:
					tks.setQ(decodeQ(subAl));
					break;
					
				case EnumTag.G:
					tks.setG(decodeG(subAl));
					break;
					
				case EnumTag.J:
					tks.setJ(decodeJ(subAl));
					break;
					
				case EnumTag.X:
					tks.setX(decodeX(subAl));
					break;
					
				case EnumTag.Y:
					tks.setY(decodeY(subAl));
					break;
					
				case EnumTag.PrimeExponentP:
					tks.setPrimeExponentP(decodePrimeExponentP(subAl));
					break;
					
				case EnumTag.PrimeExponentQ:
					tks.setPrimeExponentQ(decodePrimeExponentQ(subAl));
					break;
					
				case EnumTag.CRTCoefficient:
					tks.setCrtCoefficient(decodeCRTCoefficient(subAl));
					break;
					
				case EnumTag.RecommendedCurve:
					tks.setRecommendedCurve(decodeRecommendedCurve(subAl));
					break;
					
				case EnumTag.D:
					tks.setD(decodeD(subAl));
					break;
					
				case EnumTag.QString:
					tks.setQString(decodeQString(subAl));
					break;
									
				default:
					throw new KMIPUnexpectedTagException(new EnumTag(tag).getKey());
			}
		}
	}
		
	private KMIPBigInteger decodeModulus(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.Modulus, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodePrivateExponent(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.PrivateExponent, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodePublicExponent(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.PublicExponent, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeP(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.P, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeQ(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.Q, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeG(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.G, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeJ(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.J, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeX(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.X, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodeY(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.Y, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodePrimeExponentP(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.PrimeExponentP, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPBigInteger decodePrimeExponentQ(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.PrimeExponentQ, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
		
	private KMIPBigInteger decodeCRTCoefficient(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.CRTCoefficient, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private EnumRecommendedCurve decodeRecommendedCurve(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.RecommendedCurve, EnumType.Enumeration, al);
		return new EnumRecommendedCurve(decodeKMIPEnumeration(al));	
	}
	
	private KMIPBigInteger decodeD(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException {
		checkType(EnumTag.D, EnumType.BigInteger, al);
		return decodeKMIPBigInteger(al);
	}
	
	private KMIPByteString decodeQString(List<Byte> al) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException {
		checkType(EnumTag.QString, EnumType.ByteString, al);
		return decodeKMIPByteString(al);
	}
	
	private void decodeCryptographicAlgorithm(List<Byte> al, KeyBlock keyBlock) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.CryptographicAlgorithm, EnumType.Enumeration, al);
        CryptographicAlgorithm cryptographicAlgorithm = new CryptographicAlgorithm(new EnumCryptographicAlgorithm(decodeKMIPEnumeration(al)));
        keyBlock.setCryptographicAlgorithm(cryptographicAlgorithm);
	}
	
	private void decodeCryptographicLength(List<Byte> al, KeyBlock keyBlock) throws KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedTagException{
		checkTagAndType(EnumTag.CryptographicLength, EnumType.Integer, al);
        CryptographicLength cryptographicLength = new CryptographicLength(decodeKMIPInteger(al));
        keyBlock.setCryptographicLength(cryptographicLength);
	}


	private Attribute decodeAttribute(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedAttributeNameException{
		checkTagAndType(EnumTag.Attribute, EnumType.Structure, al);
		int length = decodeLength(al);
		KMIPTextString attributeName = decodeAttributeName(al.subList(TTL_SIZE, TTL_SIZE+length));
		Attribute a = decodeAttributeValue(al.subList(TTL_SIZE + subItemLength, TTL_SIZE + length), attributeName);
		subItemLength = length + TTL_SIZE;
		return a;
	}
	
	private KMIPTextString decodeAttributeName(List<Byte> al) throws KMIPUnexpectedTypeException, KMIPUnexpectedTagException, UnsupportedEncodingException, KMIPPaddingExpectedException{
		checkTagAndType(EnumTag.AttributeName, EnumType.TextString, al);
		return decodeKMIPTextString(al);
	}
	
    private Attribute decodeAttributeValue(List<Byte> al, KMIPTextString attributeName) throws KMIPUnexpectedTagException, KMIPPaddingExpectedException, KMIPUnexpectedTypeException, KMIPUnexpectedAttributeNameException, UnsupportedEncodingException{
    	checkTag(EnumTag.AttributeValue, al);
 
		int type = decodeType(al.get(3));
		int length = decodeLength(al);
		subItemLength = length + TTL_SIZE;

		if (type == EnumType.Integer) {
			int value = decodeInteger(al, length); 
			Attribute a = loadAttributeInstance(attributeName);
			a.setValue(Integer.toString(value), null);
			if (a.getAttributeType() != type) {
				throw new KMIPUnexpectedTypeException(attributeName.getValue(),a.getValues()[0].getTypeAsEnumType().getKey());
			}
			return a;
		} else if (type == EnumType.DateTime || type == EnumType.LongInteger ) {
			long value = decodeLong(al.subList(TTL_SIZE, TTL_SIZE + 8));
			Attribute a = loadAttributeInstance(attributeName);
			a.setValue(Long.toString(value), null);
			if (a.getAttributeType() != type) {
				throw new KMIPUnexpectedTypeException(attributeName.getValue(), a.getValues()[0].getTypeAsEnumType().getKey());
			}
			return a;	
		} else if (type == EnumType.Interval) {
			long value = decodeInterval(al, length);
			Attribute a = loadAttributeInstance(attributeName);
			a.setValue(Long.toString(value), null);
			if (a.getAttributeType() != type) {
				throw new KMIPUnexpectedTypeException(attributeName.getValue(), a.getValues()[0].getTypeAsEnumType().getKey());
			}
			return a;		
		} else if (type == EnumType.Enumeration) {

			int value = decodeEnumeration(al, length);
			String enumName = "Enum" + attributeName.getValue().replaceAll("\\s", "");

			try {
				Class<?> enumClass = Class.forName(ENUM_LOCATION + enumName);
				Constructor<?> enumClassConstructor = enumClass.getConstructor(Integer.TYPE);
				KMIPEnumeration enumeration = (KMIPEnumeration) enumClassConstructor.newInstance(value);

				Class<?> retClass = Class.forName(ATTRIBUTE_LOCATION + attributeName.getValue().replaceAll("\\s", ""));
				Constructor<?> attributeConstructor = retClass.getConstructor(KMIPType.class);
				Attribute retAttrib = (Attribute) attributeConstructor.newInstance(enumeration);

				return retAttrib;
			} catch (Exception e) {
				throw new KMIPUnexpectedAttributeNameException("Unexpected Attribute: " + attributeName.getValue());
			}

		} else if (type == EnumType.TextString) {
			int paddingLength = getPaddingLength(length);
			String value = decodeTextString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength),paddingLength).toString();

			try {
				String className = attributeName.getValue().replaceAll("-", "");
				String classNameFinal = Character.toUpperCase(className.charAt(0)) + className.substring(1);
				Attribute a = loadAttributeInstance(new KMIPTextString(classNameFinal));
				a.setValue(value, null);
				if (a.getAttributeType() != type) {
					throw new KMIPUnexpectedTypeException(className, a.getValues()[0].getTypeAsEnumType().getKey());
				}
				return a;
			} catch (Exception e) {
				throw new KMIPUnexpectedAttributeNameException("Unexpected Attribute: " + attributeName.getValue());
			}

		} else if (type == EnumType.Structure) {
			Attribute a = loadAttributeInstance(attributeName);

			for (int i = TTL_SIZE; i <= length; i += subItemLength) {
				decodeValueStructure(al.subList(i, TTL_SIZE + length), a);
			}

			if (a.getAttributeType() != type) {
				throw new KMIPUnexpectedTypeException(attributeName.getValue(), a.getValues()[0].getTypeAsEnumType().getKey());
			}
			return a;
			
		} 
		// Other KMIPTypes for Attributes come here
		return null;
    }
    

  
    private void decodeValueStructure(List<Byte> al, Attribute a) throws KMIPPaddingExpectedException, UnsupportedEncodingException{
    	EnumTag tag = new EnumTag(decodeTag(al.subList(0, 3)));
    	EnumType type = new EnumType(decodeType(al.get(3)));
    	int length = decodeLength(al);
    	
    	if(type.getValue() == EnumType.Integer){
    		int value = decodeInteger(al, length);
    		a.setValue(Integer.toString(value), tag.getKey());
    		subItemLength = length + PADDING_SIZE_INT + TTL_SIZE;
    	} else if(type.getValue() == EnumType.Enumeration){
    		int value = decodeEnumeration(al, length); 		
    		a.setValue(Integer.toString(value), tag.getKey());
    		subItemLength = length + PADDING_SIZE_INT + TTL_SIZE;
    	} else if(type.getValue() == EnumType.LongInteger){
    		long value = decodeLong(al.subList(TTL_SIZE, TTL_SIZE + length)); 		
    		a.setValue(Long.toString(value), tag.getKey());
    		subItemLength = length + TTL_SIZE;
    	} else if(type.getValue() == EnumType.TextString){
			int paddingLength = getPaddingLength(length);
			String value = decodeTextString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength).getValue();
			a.setValue(value, tag.getKey());
			subItemLength = length + TTL_SIZE + paddingLength;    
		} else if(type.getValue() == EnumType.ByteString){
			int paddingLength = getPaddingLength(length);
			byte[] value = decodeByteString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength).getValue();
			a.setValue(KMIPUtils.convertByteStringToHexString(value), tag.getKey());
			subItemLength = length + TTL_SIZE + paddingLength;    
		}
    	// Other KMIPTypes for ValueStructure come here
    }
    
	private void decodeMessageExtension(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.MessageExtension, EnumType.Structure, al);
		int length = decodeLength(al);
		MessageExtension me = new MessageExtension();
		decodeCriticalityIndicator(al.subList(TTL_SIZE, TTL_SIZE + length), me);
		int offset = subItemLength;
		KMIPTextString vendorIdentification = decodeVendorIdentification(al.subList(TTL_SIZE  + offset, TTL_SIZE + length));
		me.setVendorIdentification(vendorIdentification);
		offset += subItemLength;
		decodeVendorExtension(al.subList(TTL_SIZE + offset, TTL_SIZE + length), me);
		batch.setMessageExtension(me);
		subItemLength = length + TTL_SIZE;
	}
    
	private KMIPTextString decodeVendorIdentification(List<Byte> al) throws KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException, KMIPUnexpectedTagException {
		checkTagAndType(EnumTag.VendorIdentification, EnumType.TextString, al);
		return decodeKMIPTextString(al);
	}
	
	private void decodeCriticalityIndicator(List<Byte> al, MessageExtension me) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.CriticalityIndicator, EnumType.Boolean, al);
		me.setCriticalityIndicator(decodeKMIPBoolean(al));
	}
	
	private void decodeVendorExtension(List<Byte> al, MessageExtension me) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, UnsupportedEncodingException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.VendorExtension, EnumType.Structure, al);
		int length = decodeLength(al);
		decodeExtension(al, me);
		subItemLength = length + TTL_SIZE;
	}
	

	private void decodeExtension(List<Byte> al, MessageExtension me) throws UnsupportedEncodingException, KMIPPaddingExpectedException {
		int tag = decodeTag(al);
		EnumType type =  new EnumType(decodeType(al.get(3)));
		int length = decodeLength(al);
		KMIPType value = null;
		
		switch(type.getValue()){
			case EnumType.TextString:
				int paddingLength = getPaddingLength(length);
				value = new KMIPTextString(decodeTextString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength).getValue());
				break;
			// Other KMIPTypes for Extension come here
			default:
				break;
		}
		
		VendorExtension ve = new VendorExtension(tag, type, value);
		me.setVendorExtension(ve);
	}
	
	private void decodeValidityDate(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException {
		checkTagAndType(EnumTag.ValidityDate, EnumType.DateTime, al);
		batch.addAttribute(new ValidityDate(decodeKMIPDateTime(al)));
	}
	
	private void decodeValidityIndicator(List<Byte> al, KMIPBatch batch) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException, KMIPPaddingExpectedException {
		checkTagAndType(EnumTag.ValidityIndicator, EnumType.Enumeration, al);
		batch.addKMIPType(new EnumValidityIndicator(decodeKMIPEnumeration(al)));
	}
	

///////////////////////////////////////////////////////////// Supporting Methods
    
	private void checkTag(int tag, List<Byte> al) throws KMIPUnexpectedTagException{
		if(!(decodeTag(al.subList(0, 3)) == tag)){
			throw new KMIPUnexpectedTagException(new EnumTag(tag).getKey());
		}
	}
	
	private void checkType(int tag, int type, List<Byte> al) throws KMIPUnexpectedTypeException{
		if(!(decodeType(al.get(3)) == type)){
			throw new KMIPUnexpectedTypeException(new EnumTag(tag).getKey(), new EnumType(type).getKey());
		} 
	}
    
	private void checkTagAndType(int tag, int type, List<Byte> al) throws KMIPUnexpectedTagException, KMIPUnexpectedTypeException{
		if(!(decodeTag(al.subList(0, 3)) == tag)){
			throw new KMIPUnexpectedTagException(new EnumTag(tag).getKey());
		}
		if(!(decodeType(al.get(3)) == type)){
			throw new KMIPUnexpectedTypeException(new EnumTag(tag).getKey(), new EnumType(type).getKey());
		} 
	}
    
	private int decodeTag(List<Byte> al) {
		int tag = al.get(2) >= 0 ? al.get(2) : al.get(2) + 256;
		tag += al.get(1) >= 0 ? al.get(1) << 8 : (al.get(1) + 256) << 8;
		tag += al.get(0) >= 0 ? al.get(0) << 16 : (al.get(0) + 256) << 16;
		return tag;
	}
	
	private int decodeType(Byte b) {		
		return b >= 0 ? b : b + 256;
	}
	
	private int decodeLength(List<Byte> al) {
		int len = al.get(7) >= 0 ? al.get(7) : al.get(7) + 256;
		len += al.get(6) >= 0 ? al.get(6) << 8 : (al.get(6) + 256) << 8;
		len += al.get(5) >= 0 ? al.get(5) << 16 : (al.get(5) + 256) << 16;
		len += al.get(4) >= 0 ? al.get(4) << 24 : (al.get(4) + 256) << 24;
		return len;
	}
	
	private KMIPInteger decodeKMIPInteger(List<Byte> al) throws KMIPPaddingExpectedException{
		int length = decodeLength(al);
		subItemLength = length + PADDING_SIZE_INT + TTL_SIZE; 
		return new KMIPInteger(decodeInteger(al, length));
	}
	
	private int decodeInteger(List<Byte> al, int length) throws KMIPPaddingExpectedException { 	
		List<Byte> subal = al.subList(TTL_SIZE,	TTL_SIZE + PADDING_SIZE_INT + length);
		int val = subal.get(3) >= 0 ? subal.get(3) : subal.get(3) + 256;
		val += subal.get(2) >= 0 ? subal.get(2) << 8 : (subal.get(2) + 256) << 8;
		val += subal.get(1) >= 0 ? subal.get(1) << 16 : (subal.get(1) + 256) << 16;
		val += subal.get(0) >= 0 ? subal.get(0) << 24 : (subal.get(0) + 256) << 24;
		
		if(subal.get(4) != 0 || subal.get(5) != 0 || subal.get(6) != 0 || subal.get(7) != 0){
			throw new KMIPPaddingExpectedException();
		}
		return val;
	}
	
	private KMIPBigInteger decodeKMIPBigInteger(List<Byte> al){
		int length = decodeLength(al);
		subItemLength = length + TTL_SIZE;
        return decodeBigInteger(al, length);
	}
	
	private KMIPDateTime decodeKMIPDateTime(List<Byte> al){
		int length = decodeLength(al);
		subItemLength = length + TTL_SIZE; 
		return new KMIPDateTime(decodeLong(al.subList(TTL_SIZE, TTL_SIZE + length)));
	}
	
	private KMIPLongInteger decodeKMIPLongInteger(List<Byte> al){
		int length = decodeLength(al);
		subItemLength = length + TTL_SIZE; 
		return new KMIPLongInteger(decodeLong(al.subList(TTL_SIZE, TTL_SIZE + length)));
	}
	
	private long decodeLong(List<Byte> al) { 			
		long val = al.get(7) >= 0 ? (long) al.get(7) : (long)al.get(7) + 256;
		val += al.get(6) >= 0 ? (long)al.get(6) << 8 : (long)(al.get(6) + 256) << 8;
		val += al.get(5) >= 0 ? (long)al.get(5) << 16 : (long)(al.get(5) + 256) << 16;
		val += al.get(4) >= 0 ? (long)al.get(4) << 24 : (long)(al.get(4) + 256) << 24;
		val += al.get(3) >= 0 ? (long)al.get(3) << 32 : (long)(al.get(3) + 256) << 32;
		val += al.get(2) >= 0 ? (long)al.get(2) << 40 : (long)(al.get(2) + 256) << 40;
		val += al.get(1) >= 0 ? (long)al.get(1) << 48 : (long)(al.get(1) + 256) << 48;
		val += al.get(0) >= 0 ? (long)al.get(0) << 56 : (long)(al.get(0) + 256) << 56;
		return val;
	}
	
	private KMIPInterval decodeKMIPInterval(List<Byte> al) throws KMIPPaddingExpectedException{
		int length = decodeLength(al);
		long value = decodeInterval(al, length);
		subItemLength = length + PADDING_SIZE_INT + TTL_SIZE; 
		return new KMIPInterval(value);
	}
	
	private long decodeInterval(List<Byte> al, int length) throws KMIPPaddingExpectedException { 	
		return decodeInteger(al, length);
	}
	
	private int decodeKMIPEnumeration(List<Byte> al) throws KMIPPaddingExpectedException{
		int length = decodeLength(al);
		subItemLength = length + PADDING_SIZE_ENUM + TTL_SIZE;
		return decodeEnumeration(al, length);
	}
	
	private int decodeEnumeration(List<Byte> al, int length) throws KMIPPaddingExpectedException{    
		return decodeInteger(al, length);
	}
	
	private KMIPBoolean decodeKMIPBoolean(List<Byte> al){
		int length = decodeLength(al);
		subItemLength = length + TTL_SIZE; 
		return new KMIPBoolean(decodeBoolean(al.subList(TTL_SIZE, TTL_SIZE + length)));
	}
	
	private long decodeBoolean(List<Byte> al){
		return decodeLong(al);
	}
	
	private KMIPTextString decodeKMIPTextString(List<Byte> al) throws UnsupportedEncodingException, KMIPPaddingExpectedException{
		int length = decodeLength(al);
		int paddingLength = getPaddingLength(length);
		subItemLength = length + TTL_SIZE + paddingLength; 
		return decodeTextString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength);
	}
	
	private KMIPTextString decodeTextString(List<Byte> al, int paddingLength) throws KMIPPaddingExpectedException, UnsupportedEncodingException{		
		byte[] b = new byte[al.size()-paddingLength];
		for(int i = 0; i<b.length; i++){
			b[i] = al.get(i);
		}
		for(int i = (al.size()-paddingLength); i < b.length; i++){
			if(al.get(i) != 0){
				throw new KMIPPaddingExpectedException();
			}
		}
		return new KMIPTextString(new String(b , "UTF-8"));
	}
	

	private int getPaddingLength(int length){ 
		int pLen = 8 - (length % 8);
		if((pLen>0) && (pLen<8)){
			return pLen;
		}
		return 0;
	}	
	
	private KMIPByteString decodeKMIPByteString(List<Byte> al) throws UnsupportedEncodingException, KMIPPaddingExpectedException{
		int length = decodeLength(al);
		int paddingLength = getPaddingLength(length);
		subItemLength = length + TTL_SIZE;
		return decodeByteString(al.subList(TTL_SIZE, TTL_SIZE + length + paddingLength), paddingLength);
	}
	
	private KMIPByteString decodeByteString(List<Byte> al, int paddingLength) throws KMIPPaddingExpectedException, UnsupportedEncodingException{		
		byte[] b = new byte[al.size()-paddingLength];
		for(int i = 0; i<b.length; i++){
			b[i] = al.get(i);
		}
		for(int i = (al.size()-paddingLength); i < b.length; i++){
			if(al.get(i) != 0){
				throw new KMIPPaddingExpectedException();
			}
		}
		return new KMIPByteString(b);
	}
	
	private KMIPBigInteger decodeBigInteger(List<Byte> al, int length){		
		return new KMIPBigInteger((ArrayList<Byte>) al);
	}
	
	
    private Attribute loadAttributeInstance(KMIPTextString attributeName) throws KMIPUnexpectedAttributeNameException{
		String className = attributeName.getValue().replaceAll("\\s","");
		try{
			return (Attribute) Class.forName(ATTRIBUTE_LOCATION + className).newInstance();
		}
		catch(Exception e1){
			try{
				return (Attribute) Class.forName(OPERATION_PARAMETER_LOCATION + className).newInstance();
			}
			catch(Exception e2){
				throw new KMIPUnexpectedAttributeNameException("Unexpected Attribute: " + attributeName.getValue());
			}
		}
    }
    

}
