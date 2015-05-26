package ch.ntb.inf.kmip.stub.transport;
/**
 * KMIPStubTransportLayerHTTP.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * The KMIPStubTransportLayerHTTP provides the communication between a server and a client via HTTP,
 * using a HttpUrlConnection. 
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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.utils.KMIPUtils;

/**
 * The KMIPStubTransportLayerHTTP provides the communication between a server and a client via HTTP,
 * using a HttpUrlConnection. 
 */
public class KMIPStubTransportLayerHTTP implements KMIPStubTransportLayerInterface{

	private static final Logger logger = Logger.getLogger(KMIPStubTransportLayerHTTP.class);
	private String url;
	
	public KMIPStubTransportLayerHTTP() {
		logger.info("KMIPStubTransportLayerHTTP initialized...");
	}
	
	public ArrayList<Byte> send(ArrayList<Byte> al) {
		String kmipRequest = KMIPUtils.convertArrayListToHexString(al);
		try {
			String parameter = "KMIPRequest="+URLEncoder.encode(kmipRequest,"UTF-8");	
			String responseString = executePost(url,parameter);
			return KMIPUtils.convertHexStringToArrayList(responseString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String executePost(String targetURL, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try { // Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","./.");

			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setDoInput(true);
			connection.setDoOutput(true); 
			
			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);		
			wr.flush();
			wr.close(); 
			
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public void setTargetHostname(String value) {
		this.url = value;
		logger.info("Connection to: "+value);
	}


	public void setKeyStoreLocation(String property) {
		// Nothing to do. only for HTTPS
	}

	public void setKeyStorePW(String property) {
		// Nothing to do only for HTTPS
	}
	
}
