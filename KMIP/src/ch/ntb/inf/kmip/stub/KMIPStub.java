/**
 * KMIPStub.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description:
 * The Stub encapsulates the whole KMIP functionality of the
 * client side. To process a request, it encodes the request, 
 * sends it to the server over the transport layer, and finally 
 * decodes and returns the response.  
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

package ch.ntb.inf.kmip.stub;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.config.ContextProperties;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.process.decoder.KMIPDecoderInterface;
import ch.ntb.inf.kmip.process.encoder.KMIPEncoderInterface;
import ch.ntb.inf.kmip.stub.transport.KMIPStubTransportLayerInterface;
import ch.ntb.inf.kmip.test.UCStringCompare;
import ch.ntb.inf.kmip.utils.KMIPUtils;

/**
 * The KMIPStubInterface is the interface for all stubs. It 
 * provides the needful flexibility for the interchangeability of 
 * the stub.
 * The Stub encapsulates the whole KMIP functionality of the
 * server side. To process a request, it offers two superimposed 
 * methods:
 * <ul>
 * 	<li><code>processRequest(KMIPContainer c)</code> for common use</li>
 * 	<li><code>processRequest(KMIPContainer c, String expectedTTLVRequest, String expectedTTLVResponse)</code> for test cases</li>
 * </ul>
 */
public class KMIPStub implements KMIPStubInterface {

	private static final Logger logger = Logger.getLogger(KMIPStub.class);
	
	private KMIPEncoderInterface encoder;
	private KMIPDecoderInterface decoder;
	private KMIPStubTransportLayerInterface transportLayer;
	
	
	public KMIPStub() {
		super();
		
		try {
		    String xmlPath =  this.getClass().getResource("config/").getPath();
		    if(xmlPath.contains("kmip4j.jar!")){
		    	xmlPath = xmlPath.substring(5);
		    	xmlPath = xmlPath.replace("kmip4j.jar!/ch/ntb/inf/kmip/stub/", "");
		    	xmlPath = xmlPath.replace("/", "\\");
		    }
		    ContextProperties props = new ContextProperties(xmlPath, "StubConfig.xml");

		    this.encoder = (KMIPEncoderInterface) getClass(props.getProperty("Encoder"), "ch.ntb.inf.kmip.process.encoder.KMIPEncoder").newInstance();
		    this.decoder = (KMIPDecoderInterface) getClass(props.getProperty("Decoder"), "ch.ntb.inf.kmip.process.decoder.KMIPDecoder").newInstance();
		    this.transportLayer = (KMIPStubTransportLayerInterface) getClass(props.getProperty("TransportLayer"), "ch.ntb.inf.kmip.stub.transport.KMIPStubTransportLayerHTTP").newInstance();
		    this.transportLayer.setTargetHostname(props.getProperty("TargetHostname"));
	    	this.transportLayer.setKeyStoreLocation(props.getProperty("KeyStoreLocation"));
	    	this.transportLayer.setKeyStorePW(props.getProperty("KeyStorePW"));

	    	UCStringCompare.testingOption = props.getIntProperty("Testing");
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private Class<?> getClass(String path, String defaultPath) throws ClassNotFoundException {
		return Class.forName(KMIPUtils.getClassPath(path, defaultPath));
	}
	
	/**
	 * Processes a KMIP-Request-Message stored in a <code>KMIPContainer</code> and returns a corresponding KMIP-Response-Message.
	 * 
	 * @param c :      	the <code>KMIPContainer</code> to be encoded and sent. 
	 * @return			<code>KMIPContainer</code> with the response objects.
	 */
	public KMIPContainer processRequest(KMIPContainer c){
		ArrayList<Byte> ttlv = encoder.encodeRequest(c);
		ArrayList<Byte> responseFromServer = transportLayer.send(ttlv);
		return decodeResponse(responseFromServer);
	}
	
	/**
	 * Processes a KMIP-Request-Message stored in a <code>KMIPContainer</code> and returns a corresponding KMIP-Response-Message.
	 * For test cases, there are two additional parameters that may be set by the caller. The idea is, that the generated TTLV-Strings 
	 * can be compared to the expected TTLV-Strings. 
	 * 
	 * @param c :      	the <code>KMIPContainer</code> to be encoded and sent. 
	 * @param expectedTTLVRequest :      	the <code>String</code> to be compared to the encoded request message. 
	 * @param expectedTTLVResponse :      	the <code>String</code> to be compared to the decoded response message. 
	 * @return			<code>KMIPContainer</code> with the response objects.
	 */
	public KMIPContainer processRequest(KMIPContainer c, String expectedTTLVRequest, String expectedTTLVResponse){
		// encode Request
		ArrayList<Byte> ttlv = encoder.encodeRequest(c);
		logger.info("Encoded Request from Client: (actual/expected)");
		KMIPUtils.printArrayListAsHexString(ttlv);
		logger.debug(expectedTTLVRequest);
		UCStringCompare.checkRequest(ttlv, expectedTTLVRequest);
		
		// send Request and check Response
		ArrayList<Byte> responseFromServer = transportLayer.send(ttlv);
		logger.info("Encoded Response from Server: (actual/expected)");
		KMIPUtils.printArrayListAsHexString(responseFromServer);
		logger.debug(expectedTTLVResponse);
		UCStringCompare.checkResponse(responseFromServer,expectedTTLVResponse);
		return decodeResponse(responseFromServer);
	}
	
	private KMIPContainer decodeResponse(ArrayList<Byte> responseFromServer){
		try {
			return decoder.decodeResponse(responseFromServer);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

		
}
