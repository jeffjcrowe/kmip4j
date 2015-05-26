/**
 * Certificate.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * This class is a structure for a Managed Cryptographic Object that 
 * is a digital certificate. The Certificate structure contains a 
 * Certificate-Type and -Value. For X.509 certificates, the value is 
 * a DER-encoded X.509 public key certificate. For PGP certificates, 
 * it is a transferable public key in the OpenPGP message format. 
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

package ch.ntb.inf.kmip.objects.managed;


import ch.ntb.inf.kmip.kmipenum.EnumCertificateType;
import ch.ntb.inf.kmip.kmipenum.EnumTag;
import ch.ntb.inf.kmip.types.KMIPByteString;

public class Certificate extends CryptographicObject {

	private EnumCertificateType certificateType;
	private KMIPByteString certificateValue;
	
	public Certificate() {
		super(new EnumTag(EnumTag.Certificate));
	}

	public Certificate(KMIPByteString certificateValue, EnumCertificateType certificateType) {
		this();
		this.certificateValue = certificateValue;
		this.certificateType = certificateType;
	}
	
	public EnumCertificateType getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(EnumCertificateType certificateType) {
		this.certificateType = certificateType;
	}

	public KMIPByteString getCertificateValue() {
		return certificateValue;
	}

	public void setCertificateValue(KMIPByteString certificateValue) {
		this.certificateValue = certificateValue;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Certificate");
		sb.append("\nCertificateType: "+certificateType.getValueString());
		sb.append("\nCertificateValue: "+certificateValue.getValueString());	
		return sb.toString();
	}
	
	
}
