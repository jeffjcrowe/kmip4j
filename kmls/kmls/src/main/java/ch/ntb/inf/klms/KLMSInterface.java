/**
 * KLMSInterface.java
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
 * @copyright  Copyright � 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */
package ch.ntb.inf.klms;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import ch.ntb.inf.klms.db.KLMSIllegalOperationException;
import ch.ntb.inf.klms.db.KLMSItemNotFoundException;
import ch.ntb.inf.klms.db.KLMSObjectArchivedException;
import ch.ntb.inf.klms.db.KLMSObjectNotPreActiveException;
import ch.ntb.inf.klms.db.KLMSPermissionDeniedException;
import ch.ntb.inf.klms.manager.KLMSUniqueIdentifierMissingException;


public interface KLMSInterface {

	boolean supportsAsynchronousOperations();
	
	
	
	HashMap<String, String> createSymmetricKey(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException;
	
	HashMap<String, String> createKeyPair(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException;
	
	HashMap<String, String> destroy(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException;
	
	HashMap<String, String> getStatus();

	HashMap<String, String> registerTemplate(HashMap<String, String> requestParameters);
	
	HashMap<String, String> registerSecretData(HashMap<String, String> requestParameters);
	
	HashMap<String, String> registerSymmetricKey(HashMap<String, String> requestParameters);
	
	HashMap<String, String> registerPrivateKey(HashMap<String, String> requestParameters);
	
	HashMap<String, String> registerPublicKey(HashMap<String, String> requestParameters);

	HashMap<String, String> createSymmetricKeyUsingTemplate(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException, KLMSItemNotFoundException;

	HashMap<String, String> getAttributes(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException;

	HashMap<String, String> locate(HashMap<String, String> requestParameters);

	HashMap<String, String> get(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException, KLMSObjectArchivedException;

	HashMap<String, String> getAttributeList(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException;

	HashMap<String, String> addAttribute(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException;

	HashMap<String, String> modifyAttribute(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException;

	HashMap<String, String> deleteAttribute(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException;

	HashMap<String, String> activate(HashMap<String, String> requestParameters) throws KLMSItemNotFoundException, KLMSUniqueIdentifierMissingException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSPermissionDeniedException;

	HashMap<String, String> revoke(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException;

	HashMap<String, String> check(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSPermissionDeniedException;

	HashMap<String, String> getUsageAllocation(HashMap<String, String> requestParameters) throws KLMSIllegalOperationException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException, KLMSPermissionDeniedException;

	HashMap<String, String> reKey(HashMap<String, String> requestParameters) throws NoSuchAlgorithmException, KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException;

	HashMap<String, String> obtainLease(HashMap<String, String> requestParameters) throws KLMSUniqueIdentifierMissingException, KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSObjectArchivedException;

	HashMap<String, String> query(HashMap<String, String> requestParameters);

	HashMap<String, String> archive(HashMap<String, String> requestParameters) throws KLMSPermissionDeniedException, KLMSItemNotFoundException, KLMSIllegalOperationException, KLMSObjectNotPreActiveException, KLMSUniqueIdentifierMissingException;

	HashMap<String, String> recover(HashMap<String, String> requestParameters) throws KLMSPermissionDeniedException, KLMSUniqueIdentifierMissingException, KLMSItemNotFoundException;




}
