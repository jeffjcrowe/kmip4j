/**
 * KMIPEncoderPool.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * The Encoder Pool contains earlier generated instances of encoders, 
 * that are no longer in use. During a request, the skeleton gets an 
 * encoder from the pool. If there are no encoders available, the 
 * encoder pool generates and returns a new instance of the encoder.
 * after the request has processed, the skeleton returns the encoder
 * to the pool. 
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright ï¿½ 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */
package ch.ntb.inf.kmip.process.encoder;

import java.util.Enumeration;
import java.util.Hashtable;


public class KMIPEncoderPool {

	private Hashtable<KMIPEncoderInterface, Boolean> encoders = new Hashtable<>();
	private String encoderPath;
	private String defaultPath;
	
	private int numberOfcreatedEncoders;
	private final int maxEncoders = 5;

	public KMIPEncoderPool(String encoderPath, String defaultPath) {
		this.encoderPath = encoderPath;
		this.defaultPath = defaultPath;
		this.numberOfcreatedEncoders = 0;
	}

	public synchronized KMIPEncoderInterface getEncoder() throws KMIPEncoderPoolOverflowException {
		Enumeration<KMIPEncoderInterface> e = encoders.keys();
		while (e.hasMoreElements()) {
			KMIPEncoderInterface encoder = e.nextElement();
			Boolean b = encoders.get(encoder);
			if (b.equals(Boolean.FALSE)) {
				encoders.put(encoder, Boolean.TRUE);
				return encoder;
			} 
		}
		
		if(numberOfcreatedEncoders < maxEncoders){
			KMIPEncoderInterface encoder;
			try{
				if(encoderPath != null){
					encoder = (KMIPEncoderInterface) Class.forName(encoderPath).newInstance();
				} else{
					encoder = (KMIPEncoderInterface) Class.forName(defaultPath).newInstance();
				}
				numberOfcreatedEncoders++;
				encoders.put(encoder, Boolean.TRUE);
				return encoder;	
			} catch (Exception e1){
				e1.printStackTrace();
				return null;
			}
		}
		else{
			throw new KMIPEncoderPoolOverflowException(maxEncoders);
		}
	} 

	public synchronized void returnEncoder(KMIPEncoderInterface decoder) {
		if (encoders.containsKey(decoder)){
			encoders.put(decoder, Boolean.FALSE);
		}
	}

	public String getLoadedEncoder() {
		if(encoderPath != null){
			return encoderPath;
		} else{
			return defaultPath;
		}
	}

}
