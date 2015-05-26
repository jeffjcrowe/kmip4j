/**
 * KMIPDecoderPool.java
 * ------------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * ------------------------------------------------------------------
 * Description:
 * The Decoder Pool contains earlier generated instances of decoders, 
 * that are no longer in use. During a request, the skeleton gets a 
 * decoder from the pool. If there are no decoders available, the 
 * decoder pool generates and returns a new instance of the decoder.
 * after the request has processed, the skeleton returns the decoder
 * to the pool. 
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
package ch.ntb.inf.kmip.process.decoder;

import java.util.Enumeration;
import java.util.Hashtable;

public class KMIPDecoderPool {

	private Hashtable<KMIPDecoderInterface, Boolean> decoders = new Hashtable<KMIPDecoderInterface, Boolean>();
	private String decoderPath;
	private String defaultPath;
	
	private int numberOfcreatedDecoders;
	private final int maxDecoders = 1000;

	public KMIPDecoderPool(String decoderPath, String defaultPath) {
		this.decoderPath = decoderPath;
		this.defaultPath = defaultPath;
		this.numberOfcreatedDecoders = 0;
	}

	public synchronized KMIPDecoderInterface getDecoder() throws KMIPDecoderPoolOverflowException {
		Enumeration<KMIPDecoderInterface> e = decoders.keys();
		while (e.hasMoreElements()) {
			KMIPDecoderInterface decoder = (KMIPDecoderInterface) e.nextElement();
			Boolean b = (Boolean) decoders.get(decoder);
			if (b.equals(Boolean.FALSE)) {
				decoders.put(decoder, Boolean.TRUE);
				return decoder;
			} 
		}
		
		if(numberOfcreatedDecoders < maxDecoders){
			KMIPDecoderInterface decoder = null;
			try {
				if(decoderPath != null){
					decoder = (KMIPDecoderInterface) Class.forName(decoderPath).newInstance();
				} else{
					decoder = (KMIPDecoderInterface) Class.forName(defaultPath).newInstance();
				}
				numberOfcreatedDecoders++;
				decoders.put(decoder, Boolean.TRUE);
				return decoder;
			} catch (Exception e1) {
				e1.printStackTrace();
				return null;
			}
		}
		else{
			throw new KMIPDecoderPoolOverflowException(maxDecoders);
		}
	} 

	public synchronized void returnDecoder(KMIPDecoderInterface decoder) {
		if (decoders.containsKey(decoder))
			decoders.put(decoder, Boolean.FALSE);
	}

	public String getLoadedDecoder() {
		if(decoderPath != null){
			return decoderPath;
		} else{
			return defaultPath;
		}
	}

}
