/**
 * KLMSServerNetworkService.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * A KLMSServerNetworkService provides a Thread, which waits until a 
 * connection is made to the ServerSocket. An incoming request is 
 * going to be executed in a KLMSServerHandler and via the
 * ExecutorService.
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

package ch.ntb.inf.kmip.skeleton.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.skeleton.KMIPSkeleton;

class KLMSServerNetworkService implements Runnable {

	private static final Logger logger = Logger.getLogger(KLMSServerNetworkService.class);
	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private KMIPSkeleton skeleton;
	  
	public KLMSServerNetworkService(ExecutorService pool, ServerSocket serverSocket, KMIPSkeleton skeleton) {
		this.serverSocket = serverSocket;
		this.pool = pool;
		this.skeleton = skeleton;
	}
	  
	public void run() { 
		try {
			while ( true ) {
				// Wait until a connection is made
				Socket clientSocket = serverSocket.accept(); 
				// Start the client-thread via the ExecutorService
				pool.execute(new KLMSServerHandler(clientSocket, skeleton));
			} 
		} catch (IOException e) {
			e.printStackTrace();
	    }
	    finally {
	    	logger.info("Finish KLMSServerNetworkService and disable new tasks from being submitted. ExecutorService.shutdown()...");
	    	pool.shutdown();
	    	try {
	    		// Wait until all tasks have completed execution after a shutdown request, or the timeout occurs
	    		if (!pool.awaitTermination(5L, TimeUnit.SECONDS)) {
	    			// Cancel currently executing tasks
	    			pool.shutdownNow(); 
	    			// Wait a while for tasks to respond to being cancelled
	    			if (!pool.awaitTermination(5L, TimeUnit.SECONDS)){
	    				logger.error("Pool / ExecutorService did not terminate");
	    			}
	    		}
	    		// Close ServerSocket if it is not closed already
	    		if (!serverSocket.isClosed()) {
	    			logger.info("End of KLMSServerNetworkService. ServerSocket.close()...");
	    			serverSocket.close();
	    		}
	    	} catch (Exception e) { 
	    		e.printStackTrace();
	    	} 
	    } 
	} 
}
