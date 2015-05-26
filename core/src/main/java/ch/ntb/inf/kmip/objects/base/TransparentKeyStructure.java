/**
 * TransparentKeyStructure.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * Transparent Key structures describe the necessary parameters to 
 * obtain the key material. They are used in the Key Value structure.
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

package ch.ntb.inf.kmip.objects.base;

import ch.ntb.inf.kmip.kmipenum.EnumKeyFormatType;
import ch.ntb.inf.kmip.kmipenum.EnumRecommendedCurve;
import ch.ntb.inf.kmip.types.KMIPBigInteger;
import ch.ntb.inf.kmip.types.KMIPByteString;

public class TransparentKeyStructure extends BaseObject{

	private KMIPBigInteger modulus;
	private KMIPBigInteger privateExponent;
	private KMIPBigInteger publicExponent;
	private KMIPBigInteger p;
	private KMIPBigInteger q;
	private KMIPBigInteger g;
	private KMIPBigInteger j;
	private KMIPBigInteger x;
	private KMIPBigInteger y;
	private KMIPBigInteger primeExponentP;
	private KMIPBigInteger primeExponentQ;
	private KMIPBigInteger crtCoefficient;
	private EnumRecommendedCurve recommendedCurve;
	private KMIPBigInteger d;
	private KMIPByteString qString;
	
	private EnumKeyFormatType keyFormatType;
	
	
	public TransparentKeyStructure(EnumKeyFormatType keyFormatType) {
		super(null);
		this.keyFormatType = keyFormatType;
	}
	
	// Getters & Setters
		
	public KMIPBigInteger getModulus() {
		return modulus;
	}

	public void setModulus(KMIPBigInteger modulus) {
		this.modulus = modulus;
	}

	public KMIPBigInteger getPrivateExponent() {
		return privateExponent;
	}

	public void setPrivateExponent(KMIPBigInteger privateExponent) {
		this.privateExponent = privateExponent;
	}

	public KMIPBigInteger getPublicExponent() {
		return publicExponent;
	}

	public void setPublicExponent(KMIPBigInteger publicExponent) {
		this.publicExponent = publicExponent;
	}

	public KMIPBigInteger getP() {
		return p;
	}

	public void setP(KMIPBigInteger p) {
		this.p = p;
	}

	public KMIPBigInteger getQ() {
		return q;
	}

	public void setQ(KMIPBigInteger q) {
		this.q = q;
	}

	public KMIPBigInteger getG() {
		return g;
	}

	public void setG(KMIPBigInteger g) {
		this.g = g;
	}

	public KMIPBigInteger getJ() {
		return j;
	}

	public void setJ(KMIPBigInteger j) {
		this.j = j;
	}

	public KMIPBigInteger getX() {
		return x;
	}

	public void setX(KMIPBigInteger x) {
		this.x = x;
	}

	public KMIPBigInteger getY() {
		return y;
	}

	public void setY(KMIPBigInteger y) {
		this.y = y;
	}

	public KMIPBigInteger getPrimeExponentP() {
		return primeExponentP;
	}

	public void setPrimeExponentP(KMIPBigInteger primeExponentP) {
		this.primeExponentP = primeExponentP;
	}

	public KMIPBigInteger getPrimeExponentQ() {
		return primeExponentQ;
	}

	public void setPrimeExponentQ(KMIPBigInteger primeExponentQ) {
		this.primeExponentQ = primeExponentQ;
	}

	public KMIPBigInteger getCrtCoefficient() {
		return crtCoefficient;
	}

	public void setCrtCoefficient(KMIPBigInteger crtCoefficient) {
		this.crtCoefficient = crtCoefficient;
	}

	public EnumRecommendedCurve getRecommendedCurve() {
		return recommendedCurve;
	}

	public void setRecommendedCurve(EnumRecommendedCurve recommendedCurve) {
		this.recommendedCurve = recommendedCurve;
	}

	public KMIPBigInteger getD() {
		return d;
	}

	public void setD(KMIPBigInteger d) {
		this.d = d;
	}

	public KMIPByteString getQString() {
		return qString;
	}

	public void setQString(KMIPByteString qString) {
		this.qString = qString;
	}
	
	public EnumKeyFormatType getKeyFormatType() {
		return keyFormatType;
	}

	public void setKeyFormatType(EnumKeyFormatType keyFormatType) {
		this.keyFormatType = keyFormatType;
	}
	
	// has Methods
	
	public boolean hasP(){
		if(this.p != null)
			return true;
		return false;
	}

	public boolean hasQ(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasG(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasX(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasY(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasJ(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasModulus(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasPrivateExponent(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasPublicExponent(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasPrimeExponentP(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasPrimeExponentQ(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasCrtCoefficient(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasRecommendedCurve(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasD(){
		if(this.p != null)
			return true;
		return false;
	}
	
	public boolean hasQString(){
		if(this.p != null)
			return true;
		return false;
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nTransparent Key Structure: ");
		
		if(hasModulus()){
			sb.append("\nModulus" + modulus.toString());
		}
		if(hasPrivateExponent()){
			sb.append("\nPrivateExponent" + privateExponent.toString());
		}	
		if(hasPublicExponent()){
			sb.append("\nPublicExponent" + publicExponent.toString());
		}	
		if(hasP()){
			sb.append("\nP" + p.toString());
		}	
		if(hasQ()){
			sb.append("\nQ" + q.toString());
		}	
		if(hasG()){
			sb.append("\nG" + g.toString());
		}	
		if(hasJ()){
			sb.append("\nJ" + j.toString());
		}	
		if(hasX()){
			sb.append("\nX" + x.toString());
		}	
		if(hasY()){
			sb.append("\nY" + y.toString());
		}	
		if(hasPrimeExponentP()){
			sb.append("\nPrimeExponentP" + primeExponentP.toString());
		}	
		if(hasPrimeExponentQ()){
			sb.append("\nPrimeExponentQ" + primeExponentQ.toString());
		}	
		if(hasCrtCoefficient()){
			sb.append("\nCrtCoefficient" + crtCoefficient.toString());
		}	
		if(hasRecommendedCurve()){
			sb.append("\nRecommendedCurve" + recommendedCurve.toString());
		}	
		if(hasD()){
			sb.append("\nD" + d.toString());
		}	
		if(hasQString()){
			sb.append("\nQString" + qString.toString());
		}	
			
		return sb.toString();
	}
	

}
