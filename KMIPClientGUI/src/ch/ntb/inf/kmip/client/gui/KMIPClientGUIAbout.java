/**
 * KMIPClientGUIAbout.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class KMIPClientGUIAbout extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 3015336557140902274L;
	
	private static final String ABOUT = "<html><p>KMIP 1.0 Implementation<br><br>" +
			"Successful Interoperability Test of Use Cases against IBM Server Implementation <br><br>" +
			"Authors:<br>Michael Guster<br>Stefanie Meile<br><br>" +
			"Developed in 2013 as a bachelor thesis at: <br>" +
			"Interstaatliche Hochschule für Technik Buchs NTB <br><br>" +
//			"<a href=\"www.ntb.ch \" > NTB </a>" +
			"Copyright (c) 2013, Stefanie Meile, Michael Guster" +
			"</p></html>";
	
	private Container cp;

	
	KMIPClientGUIAbout(){
		setWindowParameters();
		setContent();
		setExitButton();

		// pack Frame
		this.pack();	
		// set the location of the window to center of the screen  (has to be after pack)
		this.setLocationRelativeTo(null);
		// show Frame
		this.setVisible(true);
	}


	private void setWindowParameters(){
		this.setTitle("About");			
		this.setSize(300,150);		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cp = this.getContentPane();	
		cp.setLayout(new BorderLayout());
		setProgramIcon();
		this.setBackground(Color.white);
	}
	
	private void setProgramIcon() {
		URL imageURL = getClass().getResource("img/kmipIconRound.PNG");
		Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.setIconImage(image);
	}
	

	private void setContent() {
		JPanel aboutPanel = new JPanel();
		aboutPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		aboutPanel.setBackground(Color.white);
		aboutPanel.setLayout(new BorderLayout(10, 10));
		this.add(aboutPanel, "Center");
		
		ImageIcon logo = createImageIcon("img/kmipIconRound.PNG","KMIP Icon");
		JLabel signal = new JLabel(logo);
		aboutPanel.add(Box.createHorizontalGlue());
		aboutPanel.add(signal, "West");
		
		JLabel about = new JLabel(ABOUT);
		aboutPanel.add(about, "Center");
	
//		JEditorPane about;
//		about = new JEditorPane("text/html",ABOUT);
//		about.setEditable(false);  
//		about.setOpaque(false);  
//		about.addHyperlinkListener(new HyperlinkListener() {  
//			public void hyperlinkUpdate(HyperlinkEvent hle) {  
//				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {  
//					System.out.println(hle.getURL());  
//				}  
//			}  
//		});  
//		aboutPanel.add(about);  

	}
	
	private ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon i = new ImageIcon(imgURL, description);
			i.setImage(i.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));;
			return i;
		} else {
			return null;
		}
	}
	
	private void setExitButton() {	 
		JPanel buttonPanel = new JPanel();		
		buttonPanel.setBackground(Color.white);
		
		final JButton exit = new JButton("Exit");
		exit.addActionListener(this);
		buttonPanel.add(exit);
		
		cp.add(buttonPanel,"South");		
	}
	

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Exit")){		
			dispose();
		}
	}

}


