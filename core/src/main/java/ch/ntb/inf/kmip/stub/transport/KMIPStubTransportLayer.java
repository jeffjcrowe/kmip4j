/**
 * KMIPStubTransportLayer.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The KMIPStubTransportLayer provides a Thread, which handles the 
 * client requests to the server via TCP-Sockets. The whole read and
 * write functionality is encapsulated in the KMIPClientHandler. 
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

package ch.ntb.inf.kmip.stub.transport;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

/**
 * The KMIPStubTransportLayer provides the communication between a server and a client via TCP-Sockets. 
 */
public class KMIPStubTransportLayer implements KMIPStubTransportLayerInterface {
	
	private static final Logger logger = Logger.getLogger(KMIPStubTransportLayer.class);
	
	private int PORT = 5555;						// default values
	private String targetHostname = "localhost";	// default values

	private KMIPClientHandler clientHandler;
	
	public KMIPStubTransportLayer(){
		logger.info("KMIPTransportLayer initialized...");
	}
	
	/**
	 * Sends a KMIP-Request-Message as a TTLV-encoded hexadecimal string stored in an 
	 * <code>ArrayList{@literal <}Byte{@literal >}</code> to a defined target and returns 
	 * a corresponding KMIP-Response-Message.
	 * 
	 * @param al :     	the <code>ArrayList{@literal <}Byte{@literal >}</code> to be sent.
	 * @return			<code>ArrayList{@literal <}Byte{@literal >}</code>: the response message.
	 */
	public ArrayList<Byte> send(ArrayList<Byte> al){
		logger.info("KLMSClient Request Thread: " + Thread.currentThread());
		clientHandler = new KMIPClientHandler(targetHostname,PORT,al);
		/* Process the call-Method from the clientHandler asynchronous with FutureTask
		 * A Future represents the result of an asynchronous computation
		 */
		FutureTask<ArrayList<Byte>> ft = new FutureTask<ArrayList<Byte>>(clientHandler);
		Thread tft = new Thread(ft);
		tft.start();
 
		// While the clientHandler is in process, other Threads can run
		while (!ft.isDone()) {	// while FutureTask is busy
			Thread.yield();  	
		} 						// FutureTask isDone
		
		logger.info("KLMSClient Request terminated.");
		try {
			return ft.get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets the target host name to the defined value
	 * 
	 * @param value :     	the target host name defined as <code>String</code> to be set. 
	 */
	public void setTargetHostname(String value) {
		int split = value.indexOf(":");
		this.targetHostname = value.substring(0,split);
		this.PORT = Integer.parseInt(value.substring(split+1, value.length()));
		logger.info("Connection to: "+targetHostname+":"+PORT);
	}

	/**
	 * Only for HTTPS support. HTTP: nothing to do here-> empty implementation.
	 * 
	 * @param property :     the key store location defined as <code>String</code> to be set. 
	 */
	public void setKeyStoreLocation(String property) {}

	/**
	 * Only for HTTPS support. HTTP: nothing to do here-> empty implementation.
	 * 
	 * @param property :     the key store password defined as <code>String</code> to be set.
	 */
	public void setKeyStorePW(String property) {}
	
}
