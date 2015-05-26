/**
 * KMIPClientGUISearchWindow.java
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
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

public class KMIPClientGUISearchWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -7444352894855282327L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUISearchWindow.class);
	
	private JTextField searchField;
	private JTextArea ta;
	private String search;
	private Container cp;
	private JPanel buttonPanel;
	
	KMIPClientGUISearchWindow(JTextArea ta){
		// TextArea to look up
		this.ta=ta;
		
		setWindowParameters();
		setProgramIcon();
		setSearchField();
		setJScrollPane();
		createButtons();

		// pack Frame
		this.pack();	
		// set the location of the window to center of the screen  (has to be after pack)
		this.setLocationRelativeTo(null);
		// show Frame
		this.setVisible(true);
	}
	
	
	private void setWindowParameters(){
		this.setTitle("Search");			
		this.setSize(300,150);		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		// set the location of the window to center   
		this.setLocationRelativeTo(null);
		
		cp = this.getContentPane();	
		cp.setLayout(new BorderLayout());
	}
	
	private void setProgramIcon() {
		URL imageURL = getClass().getResource("img/kmipIconRound.PNG");
		Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.setIconImage(image);
	}
	
	private void setSearchField(){
		searchField = new JTextField();
		searchField.setSize(300, 100);
		cp.add(searchField,"Center");
	}
	
	private void setJScrollPane(){
		JScrollPane scp = new JScrollPane(searchField);
		cp.add(scp,BorderLayout.CENTER);
	}
	
	private void createButtons() {	 
		buttonPanel = new JPanel();		
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout(new FlowLayout());	
		
		final JButton search = new JButton("Search");
		search.addActionListener(this);
		buttonPanel.add(search);
		
        KeyListener tfKeyListener = new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER)
                    search.doClick();
            }
        };
        searchField.addKeyListener(tfKeyListener);
		
		JButton b = new JButton("Cancle");		
		b.addActionListener(this);
		buttonPanel.add(b);
	
		cp.add(buttonPanel,"South");		
	}
	

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Search")){		
			dispose();
			search = searchField.getText();
			StringBuffer buf = new StringBuffer(ta.getText());
			logger.info("Search: " + search);
			if(buf.indexOf(search)<0){
				logger.info("Search String not found!");
			}else{
				ta.select(buf.indexOf(search), buf.indexOf(search)+search.length());		
				ta.requestFocus(); // Focus/Highlight 
			}
		}
		if(e.getActionCommand().equals("Cancle")){		
			dispose();
		}		
	}

}
