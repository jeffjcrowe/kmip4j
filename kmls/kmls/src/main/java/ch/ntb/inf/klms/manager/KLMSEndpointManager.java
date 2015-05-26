/**
 * KLMSEndpointManager.java
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

package ch.ntb.inf.klms.manager;

import java.util.HashMap;

import ch.ntb.inf.klms.accesscontrol.UserPermissionEntry;
import ch.ntb.inf.klms.db.KLMSDatabaseInterface;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.model.objects.base.Credential;
import ch.ntb.inf.klms.utils.KLMSUtils;

public class KLMSEndpointManager{

	KLMSDatabaseInterface database;


	public KLMSEndpointManager(KLMSDatabaseInterface database) {
		this.database = database;
	}

	public void createAccessEntry(HashMap<String, String> requestParameters, HashMap<String, String> responseParameters) {
		UserPermissionEntry userPermissionEntry = createUserPermissionEntry(requestParameters, responseParameters, 1);
		database.persistUserPermissionEntry(userPermissionEntry);
	}
	
	public void createAccessEntriesForKeyPair(HashMap<String, String> requestParameters, HashMap<String, String> responseParameters) {
		UserPermissionEntry userPermissionEntry = createUserPermissionEntry(requestParameters, responseParameters, 1);
		database.persistUserPermissionEntry(userPermissionEntry);
		userPermissionEntry = createUserPermissionEntry(requestParameters, responseParameters, 2);
		database.persistUserPermissionEntry(userPermissionEntry);
	}
	
	private UserPermissionEntry createUserPermissionEntry(HashMap<String, String> requestParameters, HashMap<String, String> responseParameters, int numberOfUID){
		String uid = responseParameters.get("Unique Identifier" + "value" + Integer.toString(numberOfUID));
		UserPermissionEntry userPermissionEntry = new UserPermissionEntry(uid);
		if(requestParameters.containsKey("Credential")){
			Credential credential = KLMSUtils.getCredentialFromParameters(requestParameters);
			userPermissionEntry.setCredential(credential);
		} 
		return userPermissionEntry;
	}

	
	public void verifyUserPermission(HashMap<String, String> requestParameters) throws KLMSPermissionDeniedException {
		UserPermissionEntry userPermissionEntry = createUserPermissionEntry(requestParameters, 1);
		database.verifyUserPermission(userPermissionEntry);
	}
	
	private UserPermissionEntry createUserPermissionEntry(HashMap<String, String> requestParameters, int numberOfUID){
		String uid = requestParameters.get("Unique Identifier" + "value" + Integer.toString(numberOfUID));
		UserPermissionEntry userPermissionEntry = new UserPermissionEntry(uid);
		if(requestParameters.containsKey("Credential")){
			Credential credential = KLMSUtils.getCredentialFromParameters(requestParameters);
			userPermissionEntry.setCredential(credential);
		} 
		return userPermissionEntry;
	}
	
}
