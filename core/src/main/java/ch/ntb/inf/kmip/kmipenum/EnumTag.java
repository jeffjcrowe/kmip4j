/**
 * EnumTag.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * Concrete KMIPEnumeration: Tags
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
package ch.ntb.inf.kmip.kmipenum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import ch.ntb.inf.kmip.types.KMIPEnumeration;

public class EnumTag extends KMIPEnumeration{
	
	private static HashMap<String, Integer> values;
	
	public static final int Default		 	= -1;
	public static final int ActivationDate = 0x420001;
	public static final int ApplicationData = 0x420002;
	public static final int ApplicationNamespace = 0x420003;
	public static final int ApplicationSpecificInformation = 0x420004;
	public static final int ArchiveDate = 0x420005;
	public static final int AsynchronousCorrelationValue = 0x420006;
	public static final int AsynchronousIndicator = 0x420007;
	public static final int Attribute = 0x420008;
	public static final int AttributeIndex = 0x420009;
	public static final int AttributeName = 0x42000A;
	public static final int AttributeValue = 0x42000B;
	public static final int Authentication = 0x42000C;
	public static final int BatchCount = 0x42000D;
	public static final int BatchErrorContinuationOption = 0x42000E;
	public static final int BatchItem = 0x42000F;
	public static final int BatchOrderOption = 0x420010;
	public static final int BlockCipherMode = 0x420011;
	public static final int CancellationResult = 0x420012;
	public static final int Certificate = 0x420013;
	public static final int CertificateIdentifier = 0x420014;
	public static final int CertificateIssuer = 0x420015;
	public static final int CertificateIssuerAlternativeName = 0x420016;
	public static final int CertificateIssuerDistinguishedName = 0x420017;
	public static final int CertificateRequest = 0x420018;
	public static final int CertificateRequestType = 0x420019;
	public static final int CertificateSubject = 0x42001A;
	public static final int CertificateSubjectAlternativeName = 0x42001B;
	public static final int CertificateSubjectDistinguishedName = 0x42001C;
	public static final int CertificateType = 0x42001D;
	public static final int CertificateValue = 0x42001E;
	public static final int CommonTemplateAttribute = 0x42001F;
	public static final int CompromiseDate = 0x420020;
	public static final int CompromiseOccurrenceDate = 0x420021;
	public static final int ContactInformation = 0x420022;
	public static final int Credential = 0x420023;
	public static final int CredentialType = 0x420024;
	public static final int CredentialValue = 0x420025;
	public static final int CriticalityIndicator = 0x420026;
	public static final int CRTCoefficient = 0x420027;
	public static final int CryptographicAlgorithm = 0x420028;
	public static final int CryptographicDomainParameters = 0x420029;
	public static final int CryptographicLength = 0x42002A;
	public static final int CryptographicParameters = 0x42002B;
	public static final int CryptographicUsageMask = 0x42002C;
	public static final int CustomAttribute = 0x42002D;
	public static final int D = 0x42002E;
	public static final int DeactivationDate = 0x42002F;
	public static final int DerivationData = 0x420030;
	public static final int DerivationMethod = 0x420031;
	public static final int DerivationParameters = 0x420032;
	public static final int DestroyDate = 0x420033;
	public static final int Digest = 0x420034;
	public static final int DigestValue = 0x420035;
	public static final int EncryptionKeyInformation = 0x420036;
	public static final int G = 0x420037;
	public static final int HashingAlgorithm = 0x420038;
	public static final int InitialDate = 0x420039;
	public static final int InitializationVector = 0x42003A;
	public static final int Issuer = 0x42003B;
	public static final int IterationCount = 0x42003C;
	public static final int IVCounterNonce = 0x42003D;
	public static final int J = 0x42003E;
	public static final int Key = 0x42003F;
	public static final int KeyBlock = 0x420040;
	public static final int KeyCompressionType = 0x420041;
	public static final int KeyFormatType = 0x420042;
	public static final int KeyMaterial = 0x420043;
	public static final int KeyPartIdentifier = 0x420044;
	public static final int KeyValue = 0x420045;
	public static final int KeyWrappingData = 0x420046;
	public static final int KeyWrappingSpecification = 0x420047;
	public static final int LastChangeDate = 0x420048;
	public static final int LeaseTime = 0x420049;
	public static final int Link = 0x42004A;
	public static final int LinkType = 0x42004B;
	public static final int LinkedObjectIdentifier = 0x42004C;
	public static final int MACSignature = 0x42004D;
	public static final int MACSignatureKeyInformation = 0x42004E;
	public static final int MaximumItems = 0x42004F;
	public static final int MaximumResponseSize = 0x420050;
	public static final int MessageExtension = 0x420051;
	public static final int Modulus = 0x420052;
	public static final int Name = 0x420053;
	public static final int NameType = 0x420054;
	public static final int NameValue = 0x420055;
	public static final int ObjectGroup = 0x420056;
	public static final int ObjectType = 0x420057;
	public static final int Offset = 0x420058;
	public static final int OpaqueDataType = 0x420059;
	public static final int OpaqueDataValue = 0x42005A;
	public static final int OpaqueObject = 0x42005B;
	public static final int Operation = 0x42005C;
	public static final int OperationPolicyName = 0x42005D;
	public static final int P = 0x42005E;
	public static final int PaddingMethod = 0x42005F;
	public static final int PrimeExponentP = 0x420060;
	public static final int PrimeExponentQ = 0x420061;
	public static final int PrimeFieldSize = 0x420062;
	public static final int PrivateExponent = 0x420063;
	public static final int PrivateKey = 0x420064;
	public static final int PrivateKeyTemplateAttribute = 0x420065;
	public static final int PrivateKeyUniqueIdentifier = 0x420066;
	public static final int ProcessStartDate = 0x420067;
	public static final int ProtectStopDate = 0x420068;
	public static final int ProtocolVersion = 0x420069;
	public static final int ProtocolVersionMajor = 0x42006A;
	public static final int ProtocolVersionMinor = 0x42006B;
	public static final int PublicExponent = 0x42006C;
	public static final int PublicKey = 0x42006D;
	public static final int PublicKeyTemplateAttribute = 0x42006E;
	public static final int PublicKeyUniqueIdentifier = 0x42006F;
	public static final int PutFunction = 0x420070;
	public static final int Q = 0x420071;
	public static final int QString = 0x420072;
	public static final int Qlength = 0x420073;
	public static final int QueryFunction = 0x420074;
	public static final int RecommendedCurve = 0x420075;
	public static final int ReplacedUniqueIdentifier = 0x420076;
	public static final int RequestHeader = 0x420077;
	public static final int RequestMessage = 0x420078;
	public static final int RequestPayload = 0x420079;
	public static final int ResponseHeader = 0x42007A;
	public static final int ResponseMessage = 0x42007B;
	public static final int ResponsePayload = 0x42007C;
	public static final int ResultMessage = 0x42007D;
	public static final int ResultReason = 0x42007E;
	public static final int ResultStatus = 0x42007F;
	public static final int RevocationMessage = 0x420080;
	public static final int RevocationReason = 0x420081;
	public static final int RevocationReasonCode = 0x420082;
	public static final int KeyRoleType = 0x420083;
	public static final int Salt = 0x420084;
	public static final int SecretData = 0x420085;
	public static final int SecretDataType = 0x420086;
	public static final int SerialNumber = 0x420087;
	public static final int ServerInformation = 0x420088;
	public static final int SplitKey = 0x420089;
	public static final int SplitKeyMethod = 0x42008A;
	public static final int SplitKeyParts = 0x42008B;
	public static final int SplitKeyThreshold = 0x42008C;
	public static final int State = 0x42008D;
	public static final int StorageStatusMask = 0x42008E;
	public static final int SymmetricKey = 0x42008F;
	public static final int Template = 0x420090;
	public static final int TemplateAttribute = 0x420091;
	public static final int TimeStamp = 0x420092;
	public static final int UniqueBatchItemID = 0x420093;
	public static final int UniqueIdentifier = 0x420094;
	public static final int UsageLimits = 0x420095;
	public static final int UsageLimitsCount = 0x420096;
	public static final int UsageLimitsTotal = 0x420097;
	public static final int UsageLimitsUnit = 0x420098;
	public static final int Username = 0x420099;
	public static final int ValidityDate = 0x42009A;
	public static final int ValidityIndicator = 0x42009B;
	public static final int VendorExtension = 0x42009C;
	public static final int VendorIdentification = 0x42009D;
	public static final int WrappingMethod = 0x42009E;
	public static final int X = 0x42009F;
	public static final int Y = 0x4200A0;
	public static final int Password = 0x4200A1;
	
	static{
		values = new HashMap<String, Integer>();
		Field[] fields = EnumTag.class.getDeclaredFields();
		for (Field f : fields) {
		    if (Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())){
		    	try {
					values.put(f.getName(),f.getInt(EnumTag.class));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    } 
		}
	}
	
	public EnumTag(){
		try {
			this.value = getEntry(EnumTag.Default, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumTag(int value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedValueException e) {
			e.printStackTrace();
		}
	}
	
	public EnumTag(String key){
		setValue(key);
	}
	
	public void setValue(String value){
		try {
			this.value = getEntry(value, values);
		} catch (KMIPEnumUndefinedKeyException e) {
			try{
				int intValue;
				if(value.length() > 1 && value.substring(0,2).equals("0x")){
					intValue = java.lang.Integer.parseInt(value.substring(2), 16);
				}
				else{
					intValue = java.lang.Integer.parseInt(value);
				}
				this.value = getEntry(intValue, values);
			}
			catch(KMIPEnumUndefinedValueException e1){
				e1.printStackTrace();
			}	
		}
	}

}
