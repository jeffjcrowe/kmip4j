/**
 * MessageExtension.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Message Extension is an OPTIONAL structure that MAY be 
 * appended to any Batch Item. It is used to extend protocol 
 * messages for the purpose of adding vendor-specified extensions. 
 * The Message Extension is a structure that SHALL contain the 
 * Vendor Identification, Criticality Indicator, and Vendor 
 * Extension fields.
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
package ch.ntb.inf.kmip.objects;

import ch.ntb.inf.kmip.types.KMIPBoolean;
import ch.ntb.inf.kmip.types.KMIPTextString;


public class MessageExtension {

	private KMIPTextString vendorIdentification;
	private KMIPBoolean criticalityIndicator;
	private VendorExtension vendorExtension;

	public MessageExtension(){
		
	}
	
	public MessageExtension(KMIPTextString vendorIdentification, KMIPBoolean criticalityIndicator, VendorExtension vendorExtension) {
		this.vendorIdentification = vendorIdentification;
		this.criticalityIndicator = criticalityIndicator;
		this.vendorExtension = vendorExtension;
	}


	public void setValue(String valueName, String value) {
		if(valueName.equals("Vendor Identification")){
			this.vendorIdentification = new KMIPTextString(value);
		} else if(valueName.equals("Criticality Indicator")){
			this.criticalityIndicator = new KMIPBoolean(value);
		}
	}

	public KMIPTextString getVendorIdentification() {
		return vendorIdentification;
	}

	public void setVendorIdentification(KMIPTextString vendorIdentification) {
		this.vendorIdentification = vendorIdentification;
	}
	
	public void setVendorIdentification(String vendorIdentification) {
		this.vendorIdentification = new KMIPTextString(vendorIdentification);
	}

	public KMIPBoolean getCriticalityIndicator() {
		return criticalityIndicator;
	}

	public void setCriticalityIndicator(KMIPBoolean criticalityIndicator) {
		this.criticalityIndicator = criticalityIndicator;
	}
	
	public void setCriticalityIndicator(String criticalityIndicator) {
		this.criticalityIndicator = new KMIPBoolean(criticalityIndicator);
	}

	public VendorExtension getVendorExtension() {
		return vendorExtension;
	}

	public void setVendorExtension(VendorExtension vendorExtension) {
		this.vendorExtension = vendorExtension;
	}
	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message Extension");
		sb.append("\nVendorIdentification = "+ vendorIdentification.getValue());
		sb.append("\nCriticalityIndicator = "+ criticalityIndicator.getValue());
		sb.append("\nVendorExtension = "+ vendorExtension.getValue());
		return sb.toString();
	}
	
}
