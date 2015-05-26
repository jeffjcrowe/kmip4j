/**
 * SimpleFile.java
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

package ch.ntb.inf.kmip.client.gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class SimpleFile {
	
	private String filename;
	
	public SimpleFile(String fname) {
		this.filename = fname;
	}
	
	public String read() {
		String content = "";
		try {
			FileReader fi = new FileReader(filename);
			BufferedReader bufRead = new BufferedReader(fi);
			String line;
			line = bufRead.readLine();
			while (line != null){
				content=content+line+'\n';
				line = bufRead.readLine();
			}
			fi.close();
			bufRead.close();
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
		return content;
	}
	
	public void write(String content) {
		try {
			FileWriter outFile = new FileWriter(filename);
			PrintWriter out = new PrintWriter(outFile);
			out.write(content);
			outFile.close();
			out.close();
		}
		catch (Exception ex) {
			System.out.print(ex.getMessage());
		}
	}
}
