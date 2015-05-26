package ch.ntb.inf.kmip.utils;
/**
 * KMIPUtils.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * This class is a collection of multiple used functions
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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class KMIPUtils {
	
	private static final Logger logger = Logger.getLogger(KMIPUtils.class);
	
	/** 
	 * @param String which contains a HEX-Number, that needs to be converted to an ArrayList
	 * @return Byte ArrayList of the input String 
	 */
	public static ArrayList<Byte> convertHexStringToArrayList(String s) {
		ArrayList<Byte> al = new ArrayList<Byte>();
	    for (int i = 0; i < s.length()-1; i += 2) {
	        al.add((byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16)));
	    }
	    return al;   
	}
	
	/** 
	 * @param ArrayList that needs to be converted to a HEX-Formated String
	 * @return HEX-formated String
	 */
	public static String convertArrayListToHexString(ArrayList<Byte> al){
		StringBuffer buf = new StringBuffer();	
		for (Byte b : al) {
			buf.append(String.format("%02X", b));
		}
		return buf.toString();
	}
	
	/** 
	 * @param List<Byte> that needs to be converted to a HEX-Formated String
	 * @return HEX-formated String
	 */
	public static String convertArrayListToHexString(List<Byte> al){
		StringBuffer buf = new StringBuffer();	
		for (Byte b : al) {
			buf.append(String.format("%02X", b));
		}
		return buf.toString();
	}
	
	/** 
	 * @param ArrayList that needs to be printed to the console
	 */
	public static void printArrayListAsHexString(ArrayList<Byte> al){
		logger.debug(convertArrayListToHexString(al));
	}
		
	public static String getClassPath(String path, String defaultPath) {
		return path != null ? path : defaultPath;
	}

	public static byte[] convertHexStringToByteArray(String value){
		return toByteArray(convertHexStringToArrayList(value));
	}
	
	public static byte[] toByteArray(List<Byte> in) {
	    final int n = in.size();
	    byte ret[] = new byte[n];
	    for (int i = 0; i < n; i++) {
	        ret[i] = in.get(i);
	    }
	    return ret;
	}
	
	public static String convertByteStringToHexString(byte[] bytes){
		StringBuffer buf = new StringBuffer();	
		for (Byte b : bytes) {
			if(b > 0){
				buf.append(String.format("%02X", b));
			} else{
				buf.append(String.format("%02X", b + 256));
			}
			
		}
		return buf.toString();
	}
	
	
	public static ArrayList<Byte> convertByteArrayToArrayList(byte[] bytes){
		ArrayList<Byte> al =  new ArrayList<Byte>();
		for(int i = 0; i < bytes.length; i++){
			al.add(bytes[i]);
		}
		return al;
	}

}
