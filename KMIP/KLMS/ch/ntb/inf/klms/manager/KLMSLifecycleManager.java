/**
 * KLMSLifecycleManager.java
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

import ch.ntb.inf.klms.db.KLMSDatabaseInterface;

public class KLMSLifecycleManager {

	KLMSDatabaseInterface database;
	
	public KLMSLifecycleManager(KLMSDatabaseInterface database) {
		super();
		this.database = database;
	}


	
	/** The Lifecycle Manager should iterate over all Managed Objects
	*	in the Database and change the State, if one of the Date Attributes
	*	is in the past.
	*/
	
	
	
	// the following Methods only ensure, that the Use Case 9.2 can be tested without a proper Lifecycle Manager
	
	
	public boolean megaHack(HashMap<String, String> requestParameters) {
		if(requestParameters.containsKey("Deactivation Datevalue1")){
			if(requestParameters.get("Deactivation Datevalue1").equals("Thu Feb 11 14:03:59 CET 2010")){
				try {
					database.activate(requestParameters.get("Unique Identifiervalue1"));
					return true;
				} catch (Exception e) {
				} 
			}
		}
		return false;
	}



	public void continueMegaHack(HashMap<String, String> requestParameters) {
		try {
			database.deactivate(requestParameters.get("Unique Identifiervalue1"));
		} catch (Exception e) {
		}
	}



	public void endMegaHack(HashMap<String, String> responseParameters) {
		try {
			database.forceActivate(responseParameters.get("Unique Identifiervalue1"));
		} catch (Exception e) {
		}
	}


}
