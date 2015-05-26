/**
 * KMIPClientGUIStatusBar.java
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


import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class KMIPClientGUIStatusBar extends JPanel{

	private static final long serialVersionUID = 1694350508510923646L;
	
	private JLabel statusLabel;
	
	public static final String Ready 			= "Status: Ready";
	public static final String RequestProcess 	= "Status: Request in Process...";
	public static final String Done 			= "Status: Request received from server -> Ready for new Tasks";
	
	public KMIPClientGUIStatusBar(KMIPClientGUI gui){
		super.setPreferredSize(new Dimension(gui.getWidth(),20));
		super.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		statusLabel = new JLabel(Ready);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(statusLabel);		
	}
	
	public void setStatus(String status){
		statusLabel.setText(" "+status);
	}
		
}
