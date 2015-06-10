package ch.ntb.inf.klms.model.objects.base;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ch.ntb.inf.klms.model.objects.CredentialValue;

@Entity
public class Credential  {
	
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String credentialType;
	
	@ManyToOne ( cascade = CascadeType.ALL)
	private CredentialValue credentialValue;
	
	
	public Credential(){
	}

	public Credential(CredentialValue credentialValue) {
		this.credentialValue = credentialValue;
	}
	
	public Credential(CredentialValue credentialValue, String credentialType) {
		this.credentialValue = credentialValue;
		this.credentialType = credentialType;
	}
	
	

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	public CredentialValue getCredentialValue() {
		return credentialValue;
	}
	
	public String getCredentialType(){
		return this.credentialType;	
	}

	public void setCredentialValue(CredentialValue credentialValue) {
		this.credentialValue = credentialValue;
	}

	public boolean equals(Credential otherCredential){
		if(this.credentialType.equals(otherCredential.getCredentialType())){
			if(this.credentialValue.equals(otherCredential.getCredentialValue())){
				return true;
			}
		}
		return false;
	}

}