/**
 * KMIPEncoder.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPEncoder encodes the KMIPContainer and returns a KMIP-
 * Message. This message is a TTLV- encoded hexadecimal string 
 * stored in an ArrayList<Byte>.
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

package ch.ntb.inf.kmip.process.encoder;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ntb.inf.kmip.attributes.ApplicationSpecificInformation;
import ch.ntb.inf.kmip.attributes.KMIPAttributeValue;
import ch.ntb.inf.kmip.attributes.Name;
import ch.ntb.inf.kmip.attributes.UniqueIdentifier;
import ch.ntb.inf.kmip.attributes.UsageLimits;
import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.kmipenum.EnumCancellationResult;
import ch.ntb.inf.kmip.kmipenum.EnumCertificateRequestType;
import ch.ntb.inf.kmip.kmipenum.EnumDerivationMethod;
import ch.ntb.inf.kmip.kmipenum.EnumKeyCompressionType;
import ch.ntb.inf.kmip.kmipenum.EnumKeyFormatType;
import ch.ntb.inf.kmip.kmipenum.EnumOperation;
import ch.ntb.inf.kmip.kmipenum.EnumPutFunction;
import ch.ntb.inf.kmip.kmipenum.EnumResultStatus;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.kmipenum.EnumType;
import ch.ntb.inf.kmip.kmipenum.EnumValidityIndicator;
import ch.ntb.inf.kmip.objects.Authentication;
import ch.ntb.inf.kmip.objects.CredentialValue;
import ch.ntb.inf.kmip.objects.EncryptionKeyInformation;
import ch.ntb.inf.kmip.objects.KeyMaterial;
import ch.ntb.inf.kmip.objects.MACorSignatureKeyInformation;
import ch.ntb.inf.kmip.objects.MessageExtension;
import ch.ntb.inf.kmip.objects.VendorExtension;
import ch.ntb.inf.kmip.objects.base.Attribute;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.KeyBlock;
import ch.ntb.inf.kmip.objects.base.KeyValue;
import ch.ntb.inf.kmip.objects.base.KeyWrappingData;
import ch.ntb.inf.kmip.objects.base.KeyWrappingSpecification;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.objects.base.TransparentKeyStructure;
import ch.ntb.inf.kmip.objects.managed.Certificate;
import ch.ntb.inf.kmip.objects.managed.ManagedObject;
import ch.ntb.inf.kmip.objects.managed.OpaqueObject;
import ch.ntb.inf.kmip.objects.managed.PrivateKey;
import ch.ntb.inf.kmip.objects.managed.PublicKey;
import ch.ntb.inf.kmip.objects.managed.SecretData;
import ch.ntb.inf.kmip.objects.managed.SplitKey;
import ch.ntb.inf.kmip.objects.managed.SymmetricKey;
import ch.ntb.inf.kmip.objects.managed.Template;
import ch.ntb.inf.kmip.operationparameters.DerivationParameters;
import ch.ntb.inf.kmip.operationparameters.StorageStatusMask;
import ch.ntb.inf.kmip.process.EnumStaticValues;
import ch.ntb.inf.kmip.types.KMIPBigInteger;
import ch.ntb.inf.kmip.types.KMIPBoolean;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPDateTime;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPInteger;
import ch.ntb.inf.kmip.types.KMIPTextString;
import ch.ntb.inf.kmip.types.KMIPType;

public class KMIPEncoder implements KMIPEncoderInterface {
	
	private final int INTEGER_LENGTH = 4;
	private final int ENUMERATION_LENGTH = 4;
	private final int BOOLEAN_LENGTH = 8;
	private final int DATETIME_LENGTH = 8;
	@SuppressWarnings("unused")
	private final int LONGINTEGER_LENGTH = 8;
	@SuppressWarnings("unused")
	private final int INTERVAL_LENGTH = 4;

	public ArrayList<Byte> encodeRequest(KMIPContainer container) {
		 ArrayList<Byte> al = new ArrayList<Byte>();
		 encodeRequestMessage(container, al);
		 return al;
	}
		
	public ArrayList<Byte> encodeResponse(KMIPContainer container) {
		ArrayList<Byte> al = new ArrayList<Byte>();
		encodeResponseMessage(container, al);
		return al;
	}

	private void encodeRequestMessage(KMIPContainer container, ArrayList<Byte> al) {	
		encodeTagAndType(EnumTag.RequestMessage, EnumType.Structure, al);
		int pos = al.size();
		encodeRequestHeader(container, al);
		for(int i = 0; i < container.getBatchCount(); i++){
			encodeRequestBatchItem(container.getBatch(i), al);
		}
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeResponseMessage(KMIPContainer container, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.ResponseMessage, EnumType.Structure, al);
		int pos = al.size();
		encodeResponseHeader(container, al);
		for(int i = 0; i < container.getBatchCount(); i++){
			encodeResponseBatchItem(container.getBatch(i), al);
		}
		createLength(al.size() - pos, pos, al);
	}

	private void encodeRequestHeader(KMIPContainer container, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.RequestHeader, EnumType.Structure, al);
		int pos = al.size();
		
		// Protocol Version (required)
		encodeProtocolVersion(al);	
		
		// Request Header Options
		if(container.hasMaximumResponseSize()){
			encodeInteger(EnumTag.MaximumResponseSize, container.getMaximumResponseSize(), al);
		}
		if(container.hasAsynchronousIndicator()){
			encodeBoolean(EnumTag.AsynchronousIndicator, container.getAsynchronousIndicator(), al);
		}
		if(container.hasAuthentication()){
			encodeAuthentication(container.getAuthentication(), al);
		}
		if(container.hasBatchErrorContinuationOption()){
			encodeEnumeration(EnumTag.BatchErrorContinuationOption, container.getBatchErrorContinuationOption(), al);
		}
		if(container.hasBatchOrderOption()){
			encodeBoolean(EnumTag.BatchOrderOption, container.getBatchOrderOption(), al);
		}
		if(container.hasTimeStamp()){
			encodeTimeStamp(container.getTimeStamp(), al);
		}
		
		// Batch Count  (required)
		encodeInteger(EnumTag.BatchCount, container.getBatchCountAsKMIPInteger(), al);

		createLength(al.size() - pos, pos, al);
	}
	

	private void encodeResponseHeader(KMIPContainer container, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.ResponseHeader, EnumType.Structure, al);
		int pos = al.size();
		encodeProtocolVersion(al);
		encodeTimeStamp(null, al);
		encodeInteger(EnumTag.BatchCount, container.getBatchCountAsKMIPInteger(), al);
		createLength(al.size() - pos, pos, al);
	}

	private void encodeProtocolVersion(ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.ProtocolVersion, EnumType.Structure, al);
		int pos = al.size();
		encodeInteger(EnumTag.ProtocolVersionMajor, EnumStaticValues.ProtocolVersionMajor.getValue(), al);
		encodeInteger(EnumTag.ProtocolVersionMinor, EnumStaticValues.ProtocolVersionMinor.getValue(), al);
		createLength(al.size() - pos, pos, al);
	}

	private void encodeAuthentication(Authentication authentication, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.Authentication, EnumType.Structure, al);
		int pos = al.size();
		encodeCredential(authentication.getCredential(), al);
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeCredential(Credential credential, ArrayList<Byte> al){
		encodeTagAndType(EnumTag.Credential, EnumType.Structure, al);
		int pos = al.size();
		encodeEnumeration(EnumTag.CredentialType, credential.getCredentialType(), al);
		encodeCredentialValue(credential.getCredentialValue(), al);
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeCredentialValue(CredentialValue credentialValue, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.CredentialValue, EnumType.Structure, al);
		int pos = al.size();
		encodeTextString(EnumTag.Username, credentialValue.getUsername(), al);
		encodeTextString(EnumTag.Password, credentialValue.getPassword(), al);
		createLength(al.size() - pos, pos, al);
	}

	private void encodeTimeStamp(KMIPDateTime dateTime, ArrayList<Byte> al){
		encodeTagAndType(EnumTag.TimeStamp, EnumType.DateTime, al);
		createLength(DATETIME_LENGTH, al.size(), al);
		if(dateTime == null){
			toArrayList(KMIPDateTime.createCurrentDateTime(), al);
		} else{
			toArrayList(dateTime.getValue(), al);
		}
	}

	private void encodeRequestBatchItem(KMIPBatch batch,  ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.BatchItem, EnumType.Structure, al);
		int pos = al.size();
		
		// Operation (required)
		encodeEnumeration(EnumTag.Operation, batch.getOperation(), al);
		
		// Unique Batch Item ID (required if Batch Count > 1)
		if(batch.hasUniqueBatchItemID()){
			encodeByteString(EnumTag.UniqueBatchItemID, batch.getUniqueBatchItemID(), al);
		}
		
		// Request Payload (required)
		encodeRequestPayload(batch, al);
		
		// Message Extension (optional)
		if(batch.hasMessageExtension()){
			encodeMessageExtension(batch.getMessageExtension(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}
	

	private void encodeResponseBatchItem(KMIPBatch batch, ArrayList<Byte> al ) {
		encodeTagAndType(EnumTag.BatchItem, EnumType.Structure, al);
		int pos = al.size();
		
		// Operation (required, except if Response is too large)
		if(batch.hasOperation()){
			encodeEnumeration(EnumTag.Operation, batch.getOperation(), al);
		}
		
		// Unique Batch Item ID (optional)
		if(batch.hasUniqueBatchItemID()){
			encodeByteString(EnumTag.UniqueBatchItemID, batch.getUniqueBatchItemID(), al);
		}
		
		// Result Status (required)
		encodeEnumeration(EnumTag.ResultStatus, batch.getResultStatus(), al);

		
		// Result Reason (required if Result Status is Failure)
		if(batch.hasResultReason()){
			encodeEnumeration(EnumTag.ResultReason, batch.getResultReason(), al);
		}
		
		// Result Message (optional, if Result Status is not Pending of Success)
		if(batch.hasResultMessage()){
			encodeTextString(EnumTag.ResultMessage, batch.getResultMessage(), al);
		}
		
		// Asynchronous Correlation Value (required, if Result Status is Pending)
		if(batch.hasAsynchronousCorrelationValue()){
			encodeByteString(EnumTag.AsynchronousCorrelationValue, batch.getAsynchronousCorrelationValue(), al);
		}
		
		// Response Payload (required, if Result Status is not OperationFailed)
		if(batch.getResultStatus().getValue() == EnumResultStatus.Success){
			encodeResponsePayload(batch, al);
		}
		
		if(batch.hasMessageExtension()){
			encodeMessageExtension(batch.getMessageExtension(), al);
		}

		createLength(al.size() - pos, pos, al);
	}

	
	private void encodeMessageExtension(MessageExtension messageExtension, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.MessageExtension, EnumType.Structure, al);
		int pos = al.size();
		encodeBoolean(EnumTag.CriticalityIndicator, messageExtension.getCriticalityIndicator(), al);
		encodeTextString(EnumTag.VendorIdentification, messageExtension.getVendorIdentification(), al);
		encodeVendorExtension(messageExtension.getVendorExtension(), al);
		createLength(al.size() - pos, pos, al);
	}

	private void encodeVendorExtension(VendorExtension vendorExtension, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.VendorExtension, EnumType.Structure, al);
		int pos = al.size();
		encodeUnknownTag(vendorExtension, al);
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeUnknownTag(VendorExtension vendorExtension, ArrayList<Byte> al) {
		encodeTagAndType(vendorExtension.getTag(), vendorExtension.getType(), al);
		int pos = al.size();
		al.addAll(vendorExtension.getValue());
		
		if(vendorExtension.getType() == EnumType.TextString){
			createLength(((KMIPTextString)vendorExtension.getKMIPType()).getLength(), pos, al);
		} else{
			createLength(al.size() - pos, pos, al);
		}
	}

	
	private void encodeRequestPayload(KMIPBatch batch, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.RequestPayload, EnumType.Structure, al);
		int pos = al.size();
		Iterator<Attribute> it;			
		it = batch.getAttributes().iterator();

		
		if(batch.getOperation().getValue() == EnumOperation.GetAttributes || batch.getOperation().getValue() == EnumOperation.DeleteAttribute){
			while (it.hasNext()) {
				Attribute attrib = it.next();
				if(attrib instanceof UniqueIdentifier){
					encodeAttribute(attrib, al);
				} else{
					encodeAttributeName(attrib, al);
					if(attrib.hasAttributeIndex()){
						encodeInteger(EnumTag.AttributeIndex, attrib.getAttributeIndex(), al);
					}
				}
			}
		} else if(batch.getOperation().getValue() == EnumOperation.Locate){
			while(it.hasNext()){
				Attribute attrib = it.next();
				if(attrib instanceof StorageStatusMask){
					encodeAttribute(attrib, al);
				}else{
					encodeAttributeStructure(attrib, al);
				}
			}
		} else if(batch.getOperation().getValue() == EnumOperation.Check || batch.getOperation().getValue() == EnumOperation.GetUsageAllocation){
			while(it.hasNext()){
				Attribute attrib = it.next();
				if(attrib instanceof UsageLimits){
					encodeUsageLimits((UsageLimits) attrib, al);
				} else{
					encodeAttribute(attrib, al);
				}
			}
		} else if(batch.getOperation().getValue() == EnumOperation.AddAttribute || batch.getOperation().getValue() == EnumOperation.ModifyAttribute){
			while (it.hasNext()) {
				Attribute attrib = it.next();
				if(attrib instanceof UniqueIdentifier){
					encodeAttribute(attrib, al);
				} else{
					encodeAttributeStructure(attrib, al);
				}
			}
		} else{
			while (it.hasNext()) {
				encodeAttribute(it.next(), al);
			}
		}
		
		Iterator<TemplateAttributeStructure> itTas = batch.getTemplateAttributeStructures().iterator();
		while(itTas.hasNext()){
			encodeTemplateAttributeStructure(itTas.next(), al);
		}
		
		if(batch.hasManagedObject()){
			encodeManagedObject(batch.getManagedObject(), al);
		}

		if(batch.hasKMIPTypes()){
			for(KMIPType kmipType : batch.getKMIPTypes()){
				encodeKMIPType(kmipType, al);
			}
		}
		
		if(batch.getOperation().getValue() == EnumOperation.DeriveKey && batch.hasDerivationParameters()){
			encodeDerivationParameters(batch.getDerivationParameters(), al);
		}
		
		if(batch.getOperation().getValue() == EnumOperation.Get && batch.hasKeyWrappingSpecification()){
			encodeKeyWrappingSpecification(batch.getKeyWrappingSpecification(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}

	
	private void encodeResponsePayload(KMIPBatch batch, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.ResponsePayload, EnumType.Structure, al);
		int pos = al.size();
		Iterator<Attribute> it = batch.getAttributes().iterator();
		
		int op = batch.getOperation().getValue();
		if(op == EnumOperation.GetAttributes || op == EnumOperation.AddAttribute || op == EnumOperation.ModifyAttribute || op == EnumOperation.DeleteAttribute){
			while (it.hasNext()) {
				Attribute attrib = it.next();
				if(attrib instanceof UniqueIdentifier){
					encodeAttribute(attrib, al);
				} else{
					encodeAttributeStructure(attrib, al);
				}
			}
		} else if(op == EnumOperation.GetAttributeList ){
			while (it.hasNext()) {
				Attribute attrib = it.next();
				if(attrib instanceof UniqueIdentifier && attrib.getValues()[0].getValueString() != null){
					encodeAttribute(attrib, al);
				} else{
					encodeAttributeName(attrib, al);
				}
			}
		} else if(op == EnumOperation.Query ){
			while (it.hasNext()) {
				Attribute attrib = it.next();
				if(attrib instanceof ApplicationSpecificInformation){
					encodeTextString(EnumTag.ApplicationNamespace, (KMIPTextString) attrib.getValues()[0].getValueAsKMIPType(), al);
				} else{
					encodeAttribute(attrib, al);
				}
			}
		}else{
			while (it.hasNext()) {
				encodeAttribute(it.next(), al);
			}
		}

		Iterator<TemplateAttributeStructure> itTas = batch.getTemplateAttributeStructures().iterator();
		while(itTas.hasNext()){
			it = itTas.next().getAttributes().iterator();
			while (it.hasNext()) {
				encodeAttributeStructure(it.next(), al);
			}
		}
		
	
		if(batch.hasManagedObject()){
			encodeManagedObject(batch.getManagedObject(), al);
		}
		
		if(batch.hasKMIPTypes()){
			for(KMIPType kmipType : batch.getKMIPTypes()){
				encodeKMIPType(kmipType, al);
			}
		}
		
		createLength(al.size() - pos, pos, al);
	}




	private void encodeTemplateAttributeStructure(TemplateAttributeStructure tas, ArrayList<Byte> al) {
		encodeTagAndType(tas.getTag().getValue(), EnumType.Structure, al);
		int pos = al.size();
		
		Iterator<Name> itName = tas.getNames().iterator();
		while(itName.hasNext()){
			encodeAttribute(itName.next(), al);
		}
		
		Iterator<Attribute> it = tas.getAttributes().iterator();
		while (it.hasNext()) {
			encodeAttributeStructure(it.next(), al);
		}
		createLength(al.size() - pos, pos, al);
	}

	private void encodeAttributeStructure(Attribute attribute, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.Attribute, EnumType.Structure, al);
		int pos = al.size();
		encodeAttributeName(attribute, al);
		encodeAttributeValue(attribute, al);
		createLength(al.size() - pos, pos, al);
	}

	private void encodeAttributeName(Attribute attribute, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.AttributeName, EnumType.TextString, al);
		int pos = al.size();
		al.addAll(attribute.getEncodedAttributeName());
		createLength(attribute.getLength(), pos, al);
	}
	

	private void encodeAttributeValue(Attribute attribute, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.AttributeValue, attribute.getAttributeType(), al);
		int pos = al.size(); 
		if(attribute.getAttributeType() == EnumType.Structure){
			KMIPAttributeValue[] values = attribute.getValues();
			for(int i = 0; i < values.length; i++){
				if(!(values[i].getValueString().equals("-1"))){
					if(! (values[i].getType() == EnumType.LongInteger && values[i].getValueString().equals("0"))){ // Case Usage Limits without Usage Limit Count
						encodeAttributeValueElement(al, values[i]);
					}
				}
			}
			createLength(al.size() - pos, pos, al);
		}else{
			KMIPAttributeValue[] values = attribute.getValues();
			al.addAll(values[0].getValue());
			createLength(values[0].getLength(), pos, al);
		}
		
	}
	

	
	private void encodeManagedObject(ManagedObject managedObject, ArrayList<Byte> al) {
		
		if(managedObject instanceof Template){
			encodeTemplate((Template)managedObject, al);
		}
		else if(managedObject instanceof SymmetricKey){
			encodeSymmetricKey((SymmetricKey)managedObject, al);
		} 
		else if(managedObject instanceof SecretData){
			encodeSecretData((SecretData)managedObject, al);
		} 
		else if(managedObject instanceof PrivateKey){
			encodePrivateKey((PrivateKey)managedObject, al);
		} 
		else if(managedObject instanceof PublicKey){
			encodePublicKey((PublicKey)managedObject, al);
		} 
		else if(managedObject instanceof Certificate){
			encodeCertificate((Certificate)managedObject, al);
		} 
		else if(managedObject instanceof SplitKey){
			encodeSplitKey((SplitKey)managedObject, al);
		} 
		else if(managedObject instanceof OpaqueObject){
			encodeOpaqueObject((OpaqueObject)managedObject, al);
		}
	}
	
	private void encodeTemplate(Template template, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.Template, EnumType.Structure, al);
		int pos = al.size();

		Iterator<Attribute> it = template.getAttributes().iterator();
		while (it.hasNext()) {
			encodeAttributeStructure(it.next(), al);
		}
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeSymmetricKey(SymmetricKey symmetricKey, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.SymmetricKey, EnumType.Structure, al);
		int pos = al.size();
		encodeKeyBlock(symmetricKey.getKeyBlock(), al); 
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeSecretData(SecretData secretData, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.SecretData, EnumType.Structure, al);
		int pos = al.size();
		encodeEnumeration(EnumTag.SecretDataType, secretData.getSecretDataType(), al);
		encodeKeyBlock(secretData.getKeyBlock(), al); 
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodePrivateKey(PrivateKey privateKey, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.PrivateKey, EnumType.Structure, al);
		int pos = al.size();
		encodeKeyBlock(privateKey.getKeyBlock(), al); 
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodePublicKey(PublicKey publicKey, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.PublicKey, EnumType.Structure, al);
		int pos = al.size();
		encodeKeyBlock(publicKey.getKeyBlock(), al); 
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeCertificate(Certificate certificate, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.Certificate, EnumType.Structure, al);
		int pos = al.size();
		encodeEnumeration(EnumTag.CertificateType, certificate.getCertificateType(), al);
		encodeCertificateValue(certificate.getCertificateValue(), al);
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeCertificateValue(KMIPByteString certificateValue, ArrayList<Byte> al){
		encodeTagAndType(EnumTag.CertificateValue, EnumType.ByteString, al);
		int pos = al.size();
		al.addAll(certificateValue.toArrayList());
		createLength(certificateValue.getLength(), pos, al);
	}
	
	private void encodeSplitKey(SplitKey splitKey, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.SplitKey, EnumType.Structure, al);
		int pos = al.size();
		encodeInteger(EnumTag.SplitKeyParts, splitKey.getSplitKeyParts(), al);
		encodeInteger(EnumTag.KeyPartIdentifier, splitKey.getKeyPartIdentifier(), al);
		encodeInteger(EnumTag.SplitKeyThreshold, splitKey.getSplitKeyThreshhosd(), al);
		encodeEnumeration(EnumTag.SplitKeyMethod, splitKey.getSplitKeyMethod(), al);
		
		if(splitKey.hasPrimeFieldSize()){
			encodeBigInteger(EnumTag.PrimeFieldSize, splitKey.getPrimeFieldSize(), al);
		}
		
		encodeKeyBlock(splitKey.getKeyBlock(), al); 

		createLength(al.size() - pos, pos, al);
	}
	
	
	private void encodeOpaqueObject(OpaqueObject opaqueObject, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.OpaqueObject, EnumType.Structure, al);
		int pos = al.size();

		encodeEnumeration(EnumTag.OpaqueDataType, opaqueObject.getOpaqueDataType(), al);
		encodeByteString(EnumTag.OpaqueDataValue, opaqueObject.getOpaqueDataValue(), al);
		
		createLength(al.size() - pos, pos, al);
	}
	

	private void encodeKeyBlock(KeyBlock keyBlock, ArrayList<Byte> al){
		encodeTagAndType(EnumTag.KeyBlock, EnumType.Structure, al);
		int pos = al.size();
		
		encodeEnumeration(EnumTag.KeyFormatType, keyBlock.getKeyFormatType(), al);

		if(keyBlock.hasKeyCompressionType()){
			encodeEnumeration(EnumTag.KeyCompressionType, keyBlock.getKeyCompressionType(), al);
		}
		
		encodeKeyValue(keyBlock.getKeyValue(), al);
		
		if(keyBlock.hasCryptographicAlgorithm()){
			encodeAttribute(keyBlock.getCryptographicAlgorithm(), al);
		}
		if(keyBlock.hasCryptographicLength()){
			encodeAttribute(keyBlock.getCryptographicLength(), al);
		}
	
		if(keyBlock.hasKeyWrappingData()){
			encodeKeyWrappingData(keyBlock.getKeyWrappingData(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}


	private void encodeKeyValue(KeyValue keyValue, ArrayList<Byte> al){
		encodeTagAndType(EnumTag.KeyValue, EnumType.Structure, al);
		int pos = al.size();
		
		encodeKeyMaterial(keyValue.getKeyMaterial(), al);
		
		for(Attribute a : keyValue.getAttributes()){
			encodeAttribute(a, al);
		}
		
		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeKeyMaterial(KeyMaterial keyMaterial, ArrayList<Byte> al){
		createTag(EnumTag.KeyMaterial, al);
		KMIPByteString keyMaterialByteString =  keyMaterial.getKeyMaterialByteString();
		TransparentKeyStructure transparentKeyStructure = keyMaterial.getTransparentKeyStructure();
		
		if(keyMaterialByteString != null){
			al.add((byte)EnumType.ByteString);
			int pos = al.size();
			al.addAll(keyMaterialByteString.toArrayList());
			createLength(al.size() - pos, pos, al);
		} else if(transparentKeyStructure != null){
			al.add((byte)EnumType.Structure);
			int pos = al.size();
			encodeTransparentKeyStructure(keyMaterial.getTransparentKeyStructure(), al);
			createLength(al.size() - pos, pos, al);
		}
	}
	
	// not tested yet
	private void encodeTransparentKeyStructure(TransparentKeyStructure tks, ArrayList<Byte> al) {
		if(tks.hasModulus()){
			encodeBigInteger(EnumTag.Modulus, tks.getModulus(), al);
		}
		if(tks.hasPrivateExponent()){
			encodeBigInteger(EnumTag.PrivateExponent, tks.getPrivateExponent(), al);
		}
		if(tks.hasPublicExponent()){
			encodeBigInteger(EnumTag.PublicExponent, tks.getPublicExponent(), al);
		}
		if(tks.hasP()){
			encodeBigInteger(EnumTag.P, tks.getP(), al);
		}
		if(tks.hasQ()){
			encodeBigInteger(EnumTag.Q, tks.getQ(), al);
		}
		if(tks.hasG()){
			encodeBigInteger(EnumTag.G, tks.getG(), al);
		}
		if(tks.hasJ()){
			encodeBigInteger(EnumTag.J, tks.getJ(), al);
		}
		if(tks.hasX()){
			encodeBigInteger(EnumTag.X, tks.getX(), al);
		}
		if(tks.hasY()){
			encodeBigInteger(EnumTag.Y, tks.getY(), al);
		}
		if(tks.hasPrimeExponentP()){
			encodeBigInteger(EnumTag.PrimeExponentP, tks.getPrimeExponentP(), al);
		}
		if(tks.hasPrimeExponentQ()){
			encodeBigInteger(EnumTag.PrimeExponentQ, tks.getPrimeExponentQ(), al);
		}
		if(tks.hasCrtCoefficient()){
			encodeBigInteger(EnumTag.CRTCoefficient, tks.getCrtCoefficient(), al);
		}
		if(tks.hasRecommendedCurve()){
			encodeEnumeration(EnumTag.RecommendedCurve, tks.getRecommendedCurve(), al);
		}
		if(tks.hasD()){
			encodeBigInteger(EnumTag.D, tks.getD(), al);
		}
		if(tks.hasQString()){
			encodeByteString(EnumTag.QString, tks.getQString(), al);
		}
	}
	
	private void encodeKeyWrappingData(KeyWrappingData keyWrappingData, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.KeyWrappingData, EnumType.Structure, al);
		encodeTagAndType(EnumTag.DerivationParameters, EnumType.Structure, al);
		int pos = al.size();
		
		encodeEnumeration(EnumTag.WrappingMethod, keyWrappingData.getWrappingMethod(), al);
		
		if(keyWrappingData.hasEncryptionKeyInformation()){
			encodeEncryptionKeyInformation(keyWrappingData.getEncryptionKeyInformation(), al);
		}
		if(keyWrappingData.hasMACSignatureKeyInformation()){
			encodeMacSignatureKeyInformation(keyWrappingData.getMacSignatureKeyInformation(), al);
		}
		if(keyWrappingData.hasMACSignature()){
			encodeByteString(EnumTag.MACSignature, keyWrappingData.getMacSignature(), al);
		}
		if(keyWrappingData.hasIVCounterNonce()){
			encodeByteString(EnumTag.IVCounterNonce, keyWrappingData.getIVCounterNonce(), al);
		}

		createLength(al.size() - pos, pos, al);
	}
	
	private void encodeDerivationParameters(DerivationParameters derivationParameters, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.DerivationParameters, EnumType.Structure, al);
		int pos = al.size();
		
		if(derivationParameters.hasCryptographicParameters()){
			encodeAttribute(derivationParameters.getCryptographicParameters(), al);
		}
		
		if(derivationParameters.hasInitializationVector()){
			encodeByteString(EnumTag.InitializationVector, derivationParameters.getInitializationVector(), al);
		}
		
		if(derivationParameters.hasDerivationData()){
			encodeByteString(EnumTag.DerivationData, derivationParameters.getDerivationData(), al);
		}
		
		if(derivationParameters.hasSalt()){
			encodeByteString(EnumTag.Salt, derivationParameters.getSalt(), al);
		}
		
		if(derivationParameters.hasIterationCount()){
			encodeInteger(EnumTag.IterationCount, derivationParameters.getIterationCount(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}

	private void encodeKeyWrappingSpecification(KeyWrappingSpecification keyWrappingSpecification, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.KeyWrappingSpecification, EnumType.Structure, al);
		int pos = al.size();
		
		encodeEnumeration(EnumTag.WrappingMethod, keyWrappingSpecification.getWrappingMethod(), al);
		
		if(keyWrappingSpecification.hasEncryptionKeyInformation()){
			encodeEncryptionKeyInformation(keyWrappingSpecification.getEncryptionKeyInformation(), al);
		}
		if(keyWrappingSpecification.hasMACSignatureKeyInformation()){
			encodeMacSignatureKeyInformation(keyWrappingSpecification.getMacSignatureKeyInformation(), al);
		}
		if(keyWrappingSpecification.hasAttributeNames()){
			for(KMIPTextString attributeName : keyWrappingSpecification.getNames()){
				encodeTextString(EnumTag.AttributeName, attributeName, al);
			}
		}
		
		createLength(al.size() - pos, pos, al);
	}
	
	
	private void encodeEncryptionKeyInformation(EncryptionKeyInformation encryptionKeyInformation, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.EncryptionKeyInformation, EnumType.Structure, al);
		int pos = al.size();
		
		encodeAttribute(encryptionKeyInformation.getUniqueIdentifier(), al);
		if(encryptionKeyInformation.hasCryptographicParameters()){
			encodeAttribute(encryptionKeyInformation.getCryptographicParameters(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}
	
	
	private void encodeMacSignatureKeyInformation(MACorSignatureKeyInformation macSignatureKeyInformation, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.MACSignatureKeyInformation, EnumType.Structure, al);
		int pos = al.size();
		
		encodeAttribute(macSignatureKeyInformation.getUniqueIdentifier(), al);
		if(macSignatureKeyInformation.hasCryptographicParameters()){
			encodeAttribute(macSignatureKeyInformation.getCryptographicParameters(), al);
		}
		
		createLength(al.size() - pos, pos, al);
	}

	private void encodeKMIPType(KMIPType kmipType, ArrayList<Byte> al){

		if(kmipType instanceof EnumDerivationMethod){
			encodeEnumeration(EnumTag.DerivationMethod, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumCertificateRequestType){
			encodeEnumeration(EnumTag.CertificateRequestType, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumKeyFormatType){
			encodeEnumeration(EnumTag.KeyFormatType, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumKeyCompressionType){
			encodeEnumeration(EnumTag.KeyCompressionType, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumValidityIndicator){
			encodeEnumeration(EnumTag.ValidityIndicator, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumCancellationResult){
			encodeEnumeration(EnumTag.CancellationResult, (KMIPEnumeration) kmipType, al);
		}
		else if(kmipType instanceof EnumPutFunction){
			encodeEnumeration(EnumTag.PutFunction, (KMIPEnumeration) kmipType, al);
		} 
	}

	
	private void encodeAttributeValueElement(ArrayList<Byte> al, KMIPAttributeValue attributeValue) {
		encodeTagAndType(attributeValue.getTag(), attributeValue.getType(), al);
		int pos = al.size();
		al.addAll(attributeValue.getValue());
		createLength(attributeValue.getLength(), pos, al);
	}

	private void encodeAttribute(Attribute attribute, ArrayList<Byte> al){
		encodeTagAndType(attribute.getTag().getValue(), attribute.getAttributeType(), al);
		int pos = al.size();	
		if(attribute.getAttributeType() == EnumType.Structure){
			KMIPAttributeValue[] values = attribute.getValues();
			for(int i = 0; i < values.length; i++){
				if(values[i].getValueString() != null && values[i].getValueString().length() > 0){
					encodeTagAndType(values[i].getTag(), values[i].getType(), al);
					int position = al.size();
					al.addAll(values[i].getValue());
					createLength(values[i].getLength(), position, al);
				}

			}
			createLength(al.size() - pos, pos, al);
		}else{
			KMIPAttributeValue[] values = attribute.getValues();
			al.addAll(values[0].getValue());
			createLength(values[0].getLength(), pos, al);
		}
		
	}
	
	private void encodeUsageLimits(UsageLimits usageLimits, ArrayList<Byte> al) {
		encodeTagAndType(EnumTag.UsageLimitsCount, EnumType.LongInteger, al);
		int pos = al.size();
		al.addAll(usageLimits.getValues()[1].getValue());
		createLength(usageLimits.getValues()[1].getLength(), pos, al);
	}
	
	
	private void encodeEnumeration(int tag, KMIPEnumeration kmipEnum, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.Enumeration, al);
		createLength(ENUMERATION_LENGTH, al.size(), al);
		toArrayList(kmipEnum.getValue(), al);
	}
	
	private void encodeInteger(int tag, KMIPInteger kmipInteger, ArrayList<Byte> al) {
		encodeInteger(tag, kmipInteger.getValue(), al);
	}
	
	private void encodeInteger(int tag, int kmipInteger, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.Integer, al);
		createLength(INTEGER_LENGTH, al.size(), al);
		toArrayList(kmipInteger, al);
	}
	
	private void encodeBigInteger(int tag, KMIPBigInteger kmipBigInteger, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.BigInteger, al);
		int pos = al.size();
		al.addAll(kmipBigInteger.toArrayList());
		createLength(kmipBigInteger.getLength(), pos, al);
	}
	
	private void encodeByteString(int tag, KMIPByteString kmipByteString, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.ByteString, al);
		int pos = al.size();
		al.addAll(kmipByteString.toArrayList());
		createLength(kmipByteString.getLength(), pos, al);
	}
	
	private void encodeTextString(int tag, KMIPTextString kmipTextString, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.TextString, al);
		int pos = al.size();
		al.addAll(kmipTextString.toArrayList());
		createLength(kmipTextString.getLength(), pos, al);
	}
	
	private void encodeBoolean(int tag, KMIPBoolean kmipBoolean, ArrayList<Byte> al) {
		encodeTagAndType(tag, EnumType.Boolean, al);
		createLength(BOOLEAN_LENGTH, al.size(), al);
		toArrayList(kmipBoolean.getValue(), al);
	}
	 
	
///////////////////////////////////////////////////////////// Support Methods
	public void encodeTagAndType(int tag, int type, ArrayList<Byte> al){
		createTag(tag, al);
		createType(type, al);
	}
	
	public void createTag(int val, ArrayList<Byte> al) {
		al.add((byte) (val >> 16));
		al.add((byte) (val >> 8));
		al.add((byte) val);
	}
	
	public void createType(int type, ArrayList<Byte> al) {
		al.add((byte)type);
	}
	
	public void createLength(int val, int pos, ArrayList<Byte> al) {
		al.add(pos, (byte) (val >> 24));
		al.add(pos + 1, (byte) (val >> 16));
		al.add(pos + 2, (byte) (val >> 8));
		al.add(pos + 3, (byte) val);
	}
	
	public void toArrayList(int val, ArrayList<Byte> al) {
		al.add((byte) (val >> 24));
		al.add((byte) (val >> 16));
		al.add((byte) (val >> 8));
		al.add((byte) val);
		al.add((byte) 0x00);		// 4 Padding-Bytes
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
	}
	
	public void toArrayList(long val, ArrayList<Byte> al) {
		al.add((byte) (val >> 56));
		al.add((byte) (val >> 48));
		al.add((byte) (val >> 40));
		al.add((byte) (val >> 32));
		al.add((byte) (val >> 24));
		al.add((byte) (val >> 16));
		al.add((byte) (val >> 8));
		al.add((byte) val);
	}
	
	public void toArrayList(boolean val, ArrayList<Byte> al) {
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		al.add((byte) 0x00);
		
		if(val){
			al.add((byte) 0x01);
		} else{
			al.add((byte) 0x00);
		}
	}
	
}