/**
 * KLMSAdapterInterface.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KLMSAdapterInterface is the interface for all Adapters. It 
 * provides the needful flexibility for the interchangeability of 
 * the adapter. It offers three methods:
 * - doProcess(..) to execute individual Batch Items
 * - setKLMS(..) to set the KLMS from the Skeleton
 * - getStatus() to get status informations from the KLMS
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

package ch.ntb.inf.kmip.skeleton;

import java.util.HashMap;

import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.objects.base.Credential;

/**
 * The KLMSAdapterInterface is the interface for all Adapters. It 
 * provides the needful flexibility for the interchangeability of 
 * the adapter. It offers three methods:
 * <ul>
 * 	<li>doProcess(..) to execute individual Batch Items</li>
 * 	<li>setKLMS(..) to set the KLMS from the Skeleton</li>
 * 	<li>getStatus() to get status informations from the KLMS</li>
 * </ul>
 */
public interface KLMSAdapterInterface {

	/**
	 * Executes an individual Batch Item and returns the corresponding response batch item to the caller, 
	 * which assembles the KMIP-Response-Message. The decoded Batch Item is encapsulated in a <code>KMIPBatch</code>. 
	 * 
	 * @param requestBatch :          	the Batch Item to be executed
	 * @param credential :            	the optional Credential for the client authentication
	 * @param asynchronousIndicator : 	indicates if operation is executed asynchronous. <code>True</code> if asynchronous.
	 * @return							<code>KMIPBatch</code> 
	 */
	public KMIPBatch doProcess(KMIPBatch requestBatch, Credential credential, boolean asynchronousIndicator); 

	/**
	 * Allows to set the KLMS from the Skeleton via dynamic class loading. 
	 * 
	 * @param klmsPath :             	the fully qualified name of the KLMS, which should be loaded
	 * @param defaultPath :            	the fully qualified name of a default KLMS, which should be loaded if the other path doesn't work.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public void setKLMS(String klmsPath, String defaultPath)  throws  InstantiationException, IllegalAccessException, ClassNotFoundException;

	
	/**
	 * Returns the status of the KLMS structured as Key-Value-Pairs in a <code>HashMap{@literal <}String, String{@literal >}</code>. 
	 * Example of an entry: [Loaded KLMSAdapter ; ch.ntb.inf.klms.KLMSAdapter]
	 * 
	 * @return <code>HashMap{@literal <}String, String{@literal >}</code>
	 */
	public HashMap<String, String> getStatus();

}
