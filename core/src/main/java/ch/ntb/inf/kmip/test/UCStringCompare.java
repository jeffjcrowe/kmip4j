/**
 * UCStringCompare.java
 * -----------------------------------------------------------------
 *     __ __ __  ___________ 
 *    / //_//  |/  /  _/ __ \	  .--.
 *   / ,<  / /|_/ // // /_/ /	 /.-. '----------.
 *  / /| |/ /  / // // ____/ 	 \'-' .--"--""-"-'
 * /_/ |_/_/  /_/___/_/      	  '--'
 * 
 * -----------------------------------------------------------------
 * Description for class
 * This class contains the string comparison of two TTLV encoded 
 * KMIP messages, depending on the defined testing option in the 
 * "StubConfix.xml"-file. 
 *
 * @author     Stefanie Meile <stefaniemeile@gmail.com>
 * @author     Michael Guster <michael.guster@gmail.com>
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @org.       NTB - University of Applied Sciences Buchs, (CH)
 * @copyright  Copyright © 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */
package ch.ntb.inf.kmip.test;

import java.util.ArrayList;

import org.apache.log4j.Logger;


import ch.ntb.inf.kmip.utils.KMIPUtils;

/**
 * This class contains the string comparison of two TTLV encoded 
 * KMIP messages, depending on the defined testing option in the 
 * "StubConfix.xml"-file. 
 */
public class UCStringCompare {

	private static final Logger logger = Logger.getLogger(UCStringCompare.class);
	
	public static int testingOption;
	public static final int NONE = 0;
	public static final int NTB = 1;
	public static final int IBM = 2;
	

	public static boolean comparisonResultRequest;
	public static boolean comparisonResultResponse;
	
	private static final String UID_ATTRIBUTE_STRUCTURE = "420008010000005042000A0700000011556E69717565204964656E7469666965720000000000000042000B0700000024";
	private static final int UID_ATTRIBUTE_STRUCTURE_LENGTH = 176;
	private static final String ATTRIBUTENAME_DESTROYDATE = "44657374726F79204461746500000000";
	private static final String ATTRIBUTENAME_ARCHIVEDATE = "41726368697665204461746500000000";
	private static final String ATTRIBUTENAME_ACTIVATIONDATE = "420008010000002842000A070000000F41637469766174696F6E204461746500";
	private static final String ATTRIBUTENAME_DEACTIVATIONDATE = "420008010000003042000A0700000011446561637469766174696F6E204461746500000000000000";
	

	// get() for the KMIPClientGui to get the comparison results
	public static boolean[] getStringComparison(){
		return new boolean[]{comparisonResultRequest,comparisonResultResponse};
	}

	public static void checkRequest(ArrayList<Byte> ttlv, String expectedTTLVRequest) {
		comparisonResultRequest = true;
		
		switch(testingOption){
		
			case NONE: 
				return;
			case NTB: 
				checkRequestNTB(ttlv, expectedTTLVRequest);
				break;
			default: 
				break;
		}
	}
	
	public static void checkResponse(ArrayList<Byte> ttlv, String expectedTTLVRequest) {
		comparisonResultResponse = true;
		
		switch(testingOption){
		
			case NONE: 
				return;
			case NTB: 
				checkResponseNTB(ttlv, expectedTTLVRequest);
				break;
			default: 
				break;
		}
	}

	
	//////////////////////////////////////////////////////////////////////// String Compare NTB
	
	
	public static void checkRequestNTB(ArrayList<Byte> request, String expected){
		String requestString = KMIPUtils.convertArrayListToHexString(request);
		String expectedWithoutUID = expected;
		
		if(!checkLength(requestString, expected)){
			comparisonResultRequest = false;
			logger.warn("Request TTLV-Strings are NOT the same!");
			return;
		}
		// Check if String contains UID as Attribute Structure 
		if(expected.contains(UID_ATTRIBUTE_STRUCTURE) && requestString.contains(UID_ATTRIBUTE_STRUCTURE)){
			int pos = requestString.indexOf(UID_ATTRIBUTE_STRUCTURE);
			requestString = requestString.substring(0, pos) + requestString.substring(pos + UID_ATTRIBUTE_STRUCTURE_LENGTH);	
			expectedWithoutUID = expected.substring(0, pos) + expected.substring(pos + UID_ATTRIBUTE_STRUCTURE_LENGTH);
		}
		// Check Batches (Order of Attributes may vary)
		if(checkBatchItemsNTB(requestString,expectedWithoutUID)){
			comparisonResultRequest = true;
			logger.info("Request TTLV-Strings are the same!");
		}	
		else{
			comparisonResultRequest = false;
			logger.warn("Request TTLV-Strings are NOT the same!");
		}
	}
	
	public static void checkResponseNTB(ArrayList<Byte> response, String expected){
		comparisonResultRequest = true;
		comparisonResultResponse = true;
		
		String responseString = KMIPUtils.convertArrayListToHexString(response);		
		
		if(!checkLength(responseString, expected)){
			comparisonResultResponse = false;
			logger.warn("Response TTLV-Strings are NOT the same!");
			return;
		}
		// Check Response Header (without TimeStamp)
		int timeStampValueIndex = expected.indexOf("4200920900000008")+16;
		if(!responseString.substring(0, timeStampValueIndex).equals(expected.substring(0, timeStampValueIndex))){
			comparisonResultResponse = false;
			logger.warn("Response TTLV-Strings are NOT the same!");
			return;
		}		
		// Check Response BatchItems
		if(checkBatchItemsNTB(responseString,expected)){
			comparisonResultResponse = true;
			logger.info("Response TTLV-Strings are the same!");
		}	
		else{
			comparisonResultResponse = false;
			logger.warn("Response TTLV-Strings are NOT the same!");
		}
	}
	

	
	private static boolean checkBatchItemsNTB(String actual, String expected){
		int batchCountStartIndex = actual.indexOf("42000D")+16;
		int batchCount = Integer.parseInt(actual.substring(batchCountStartIndex, batchCountStartIndex+8));
			
		int batchIndexEx = expected.indexOf("42000F");				
		int nextBatchIndexEx;
		
		int batchIndex = actual.indexOf("42000F");	
		int nextBatchIndex;		
		
		if(batchCount <= 1){
			nextBatchIndex = actual.length();
			nextBatchIndexEx = expected.length();
		}
		else{
			nextBatchIndex = actual.indexOf("42000F", batchIndex+6);
			nextBatchIndexEx = expected.indexOf("42000F", batchIndexEx+6);
		}
		
		int attributeIndex;
		int nextAttributeIndex;
		StringBuilder expectedBatch, requestBatch; 
		
		for(int i = 0; i < batchCount; i++){
			expectedBatch = new StringBuilder(expected.substring(batchIndexEx, nextBatchIndexEx));			
			requestBatch =  new StringBuilder(actual.substring(batchIndex, nextBatchIndex));
			
			removeDateIfExists(expectedBatch, requestBatch, ATTRIBUTENAME_DESTROYDATE);
			removeDateIfExists(expectedBatch, requestBatch, ATTRIBUTENAME_ARCHIVEDATE);
			removeDateIfExists(expectedBatch, requestBatch, ATTRIBUTENAME_ACTIVATIONDATE);
			removeDateIfExists(expectedBatch, requestBatch, ATTRIBUTENAME_DEACTIVATIONDATE);
			
			
			// check Attributes
			attributeIndex = expectedBatch.indexOf("4200");
			nextAttributeIndex = expectedBatch.indexOf("4200", attributeIndex+1);
			while(attributeIndex != -1){
				if(nextAttributeIndex != -1){						
					if(!removeNotEqualObject(expectedBatch, attributeIndex, requestBatch)){
						return false;
					}
				} else {
					if(!removeNotEqualObject(expectedBatch, attributeIndex, requestBatch)){
						return false;
					}
					nextAttributeIndex = expectedBatch.length();
				}
				attributeIndex = expectedBatch.indexOf("4200", nextAttributeIndex);
				nextAttributeIndex = expectedBatch.indexOf("4200", attributeIndex+1);
			}
			
			if(i < (batchCount - 2)){
				batchIndexEx = nextBatchIndexEx;
				nextBatchIndexEx = expected.indexOf("42000F", batchIndexEx + 6);
				batchIndex = nextBatchIndex;
				nextBatchIndex = actual.indexOf("42000F", batchIndex + 6);
			}
			else{
				batchIndexEx = nextBatchIndexEx;
				nextBatchIndexEx = expected.length();
				batchIndex = nextBatchIndex;
				nextBatchIndex = actual.length();
			}	
		}
		return true;
	}
	

	//////////////////////////////////////////////////////////////////////// Support Methods
	
	
	
	private static boolean checkLength(String requestString, String expected){
		if(requestString.length() != expected.length()){
			return false;
		}
		return true;
	}
	
	
	
	private static void removeDateIfExists(StringBuilder expectedBatch, StringBuilder requestBatch, String toRemove) {
		int startIndexDDex = expectedBatch.indexOf(toRemove);
		if( startIndexDDex != -1){
			expectedBatch.delete(startIndexDDex + toRemove.length(), startIndexDDex + toRemove.length() + 32);
			int startIndexDDact = requestBatch.indexOf(toRemove);
			if( startIndexDDact != -1){
				requestBatch.delete(startIndexDDex + toRemove.length(), startIndexDDex + toRemove.length() + 32);
			}
		}	
	}
	
	
	private static boolean removeNotEqualObject(StringBuilder expectedBatch, int attributeIndex, StringBuilder requestBatch){
		String attribute = expectedBatch.substring(attributeIndex);
		String tag = attribute.substring(0, 6);
		if(tag.equals("420094")){					// Unique Identifier
			if(requestBatch.indexOf(tag) == -1){	// Check if UID is included
				return false;
			}
		}
		else if(tag.equals("420043")){				// Key Material
			if(requestBatch.indexOf(tag) == -1){	// Check if Key Material is included
				return false;
			}
		}
		else if(tag.equals("42004C")){					// Linked Object Identifier
			if(requestBatch.indexOf(tag) == -1){	// Check if Linked Object Identifier is included
				return false;
			}
		}
		else if(tag.equals("420048")){					// Last Change Date
			if(requestBatch.indexOf(tag) == -1){	// Check if Last Change Date is included
				return false;
			}
		}
		else if(tag.equals("420006")){					// Asynchronous Correlation Value
			if(requestBatch.indexOf(tag) == -1){	// Check if Asynchronous Correlation Value is included
				return false;
			}
		}
		else if(expectedBatch.indexOf(attribute) == -1){	// check all other attributes
			return false;
		}
		
		return true;
	}
	


}
