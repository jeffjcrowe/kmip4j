/**
 * KMIPClientHandler.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * The KMIPClientHandler provides a Thread, which handles the client-
 * requests to the server, as well the read and write service to the 
 * server via TCP-Sockets. 
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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

class KMIPClientHandler implements Callable<ArrayList<Byte>> {

	private static final Logger logger = Logger.getLogger(KMIPClientHandler.class);
	
	private int port;
	private String targetHostname;
	private ArrayList<Byte> al;
	private Socket clientSocket;
	
	public KMIPClientHandler(String targetHostname, int port, ArrayList<Byte> al){
		this.port = port;
		this.targetHostname = targetHostname;
		this.al = al;
	} 

	// Call method for the FutureTask (similar to run() of a Thread)
	public ArrayList<Byte> call() { 
		logger.info("ClientHandler:" + Thread.currentThread());
		// Start a server-request
		// Create a Socket for the TCP Client and build up the communication to the corresponding server.
		try {
			clientSocket = new Socket(targetHostname, port);
			// Send to server
			logger.info("Write Data to Server...");  
			writeData(clientSocket);
			logger.info("Data transmitted!");
			
			// Close output signalize EOF
			clientSocket.shutdownOutput();
			
			// Read from server
			ArrayList<Byte> responseFromServer = readData();
			
			// Close connection
			clientSocket.close();
			
			return responseFromServer;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;	
		} 
	} 


	private void writeData(Socket clientSocket){
		try {
			// Get OutputStream from Socket
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			// Prepare data to send
			byte[] b = new byte[al.size()];
			for(int i=0; i<al.size();i++){
				b[i]=al.get(i);
			}
			// Send data
			outToServer.write(b);
			outToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 

	private ArrayList<Byte> readData(){
    	byte[] resultBuff = new byte[0];
        byte[] buff = new byte[1024];
        int k = -1;
        
        try {
    		InputStream is = clientSocket.getInputStream();
			while((k = is.read(buff, 0, buff.length)) > -1) {
			    byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
			    System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
			    System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
			    resultBuff = tbuff; // call the temp buffer as your result buff
			}
		} catch (IOException e) {
			e.printStackTrace();
		} // try
       
        logger.debug(resultBuff.length + " bytes read.");
        ArrayList<Byte> response = new ArrayList<Byte>();
        
        for(byte b:resultBuff){
        	response.add(b);
        }
        
        logger.info("");  
        return response;
	} 
} 
