/**
 * KMIPClientGUIMenuBar.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class KMIPClientGUIMenuBar extends JMenuBar implements ActionListener{

	private static final long serialVersionUID = 1323060708257016313L;
	
	private KMIPClientGUI gui;
	
	KMIPClientGUIMenuBar(KMIPClientGUI gui){
		this.gui = gui;
		
		setFileMenu();
		setAbout();
	}
	


	private void setFileMenu(){
		JMenu mlist = new JMenu("File");			
		this.add(mlist);
		
		addJMenuItem("Reset", mlist);
		addJMenuItem("Exit", mlist);
	}

	private void setAbout() {
		JMenu mlist = new JMenu("About");			
		this.add(mlist);
		addJMenuItem("About", mlist);
	}
	
	
	private void addJMenuItem(String name, JMenu menu){
		JMenuItem mi = new JMenuItem(name);	
		menu.add(mi);
		mi.addActionListener(this);		
	}

	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("Reset")){	
			gui.reset();
		}	
		
		if(e.getActionCommand().equals("Exit")){	
			System.exit(0);
		}	
		
		if(e.getActionCommand().equals("About")){	
			new KMIPClientGUIAbout();
		}
	}
	
	
}
