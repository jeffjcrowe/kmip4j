/**
 * KMIPClientGUIResponseArea.java
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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class KMIPClientGUIResponseArea extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5584205880320898650L;
	
	private JEditorPane send;
	private JTextArea receive;
	private JPanel buttonPanel;
	private JPanel textAreas;
	private JScrollPane sendScrollPane;
	private JScrollPane receiveScrollPane;
	private KMIPClientGUISignal requestCompareSignal;
	private KMIPClientGUISignal responseCompareSignal;
	private KMIPClientGUI gui;
	
	public KMIPClientGUIResponseArea(KMIPClientGUI gui){
		this.gui = gui;
		setWindowParameters();
		setTextAreas();
		createSignals();
		createButtons();
	}
	
	
	private void setWindowParameters(){
		super.setLayout(new BorderLayout());
		super.setBackground(Color.white);
		super.setSize(gui.getWidth(),gui.getHeight()/2);
		super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5)));
	}
	
	private void setTextAreas(){
		this.textAreas = new JPanel(new BorderLayout(5, 5));
		this.add(textAreas,"Center");
		this.textAreas.setBackground(Color.WHITE);
				
		setSendTextArea();
		setReceiveTextArea();
		putTextAreasToSplitPane();
	}
	
	private void setSendTextArea() {
		send = new JEditorPane("text/html", "");      
		send.setEditable(false);
		sendScrollPane = new JScrollPane(send);         
		sendScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);         
		sendScrollPane.setPreferredSize(new Dimension(500, 200));         
		sendScrollPane.setMinimumSize(new Dimension(10, 10));  
		sendScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Sent Request"),BorderFactory.createEmptyBorder(5,5,5,5)));
	}
	
	private void setReceiveTextArea() {
		receive = new JTextArea();       
		receive.setEditable(false);
		receiveScrollPane = new JScrollPane(receive);         
		receiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);         
		receiveScrollPane.setPreferredSize(new Dimension(500, 200));         
		receiveScrollPane.setMinimumSize(new Dimension(10, 10));       
		receiveScrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Received Response"),BorderFactory.createEmptyBorder(5,5,5,5)));
	}

	private void putTextAreasToSplitPane() {    
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sendScrollPane,receiveScrollPane);         
		splitPane.setOneTouchExpandable(true);         
		splitPane.setResizeWeight(0.5);         
		textAreas.add(splitPane,"Center");     
	}

	private void createSignals() {
		responseCompareSignal = new KMIPClientGUISignal();
		responseCompareSignal.setColor(KMIPClientGUISignal.GREEN);
		textAreas.add(responseCompareSignal,"East");
		
		requestCompareSignal = new KMIPClientGUISignal();
		requestCompareSignal.setColor(KMIPClientGUISignal.GREEN);
		textAreas.add(requestCompareSignal,"West");
	}
	
	private void createButtons(){
		buttonPanel = new JPanel();	
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));		
		addButtonToButtonPanel("Clear");
		addButtonToButtonPanel("Search");
		textAreas.add(buttonPanel,"South");		
	}
	
	private void addButtonToButtonPanel(String name){
		JButton button = new JButton(name);	
		button.addActionListener(this);
		buttonPanel.add(button);
	}
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Clear")){
			send.setText(" \n");
			receive.setText(" \n");
		}	
		if(e.getActionCommand().equals("Search")){			
			KMIPClientGUISearchWindow sw = new KMIPClientGUISearchWindow(receive);
			sw.setVisible(true);
		}	
		
	}
	
	public JEditorPane getSendArea() {
		return send;
	}
	public JTextArea getReceiveArea() {
		return receive;
	}
	
	public void appendTextSendArea(String text){
		send.setText("");
		send.setText(text);
	}
	
	public void appendTextReceiveArea(String text){
		receive.setText("");
		receive.append(text);
	}

	public void setSignals(int i) {
		this.requestCompareSignal.setColor(i);
		this.responseCompareSignal.setColor(i);
	}
	
	public void setSignals(int[] i) {
		this.requestCompareSignal.setColor(i[0]);
		this.responseCompareSignal.setColor(i[1]);
	}
	
		
}
