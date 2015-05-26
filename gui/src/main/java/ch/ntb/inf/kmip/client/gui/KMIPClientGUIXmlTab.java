/**
 * KMIPClientGUIXmlTab.java
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.container.KMIPContainer;

public class KMIPClientGUIXmlTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = -7794911332402570791L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUIXmlTab.class);
	private final JFileChooser fc = new JFileChooser("src/ch/ntb/inf/kmip/client/gui/xml/");
	private final int vGap = 5;
	private final int hGap = 5;
	
	private KMIPClientGUI gui; 
	private JTextArea xmlInputTextArea;
	private JTextArea sendTextArea;
	private JTextArea receiveTextArea;
	private KMIPClientGUIxml xmlConverter;
	private JPanel buttonPanel;
	private GridBagConstraints gbc;
	private JPanel responseArea;

	public KMIPClientGUIXmlTab(KMIPClientGUI gui){
		this.gui = gui;
		this.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
		
		panel.add(makeButtonArea(), "West");	
		this.add(makeResponseArea(), "South");
		panel.add(makeXMLInputTextArea(), "Center");
		this.add(panel, "Center");
	}
	
	private JPanel makeButtonArea() {
		buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();

		gbc.insets = new Insets(vGap, 0, 0, hGap*2);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		
		addButton("Load Specific XML", 0);
		addButton("Load Empty XML", 1);
		addButton("Save", 2);
		addButton("Clear", 3);
		
		// Add empty space
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weighty = 1;
		gbc.gridy++;
		JPanel emptyPanel = new JPanel();
		emptyPanel.setBackground(Color.white);
		buttonPanel.add(emptyPanel, gbc);
		
		return buttonPanel;
	}
	
	private void addButton(String name, int gridy) {
		JButton button = new JButton(name);
		gbc.gridx = 0;
		gbc.gridy = gridy;
		buttonPanel.add(button, gbc);
		button.addActionListener(this);
	}
	

	// XML Input TextArea
	private JScrollPane makeXMLInputTextArea(){
		xmlInputTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(xmlInputTextArea);         
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);         
		scrollPane.setPreferredSize(new Dimension(500, 200));         
		scrollPane.setMinimumSize(new Dimension(10, 10));  
		return scrollPane;
	}
	

	private JPanel makeResponseArea(){
		responseArea = new JPanel(new BorderLayout(5, 5));
		responseArea.setBackground(Color.white);
		this.add(responseArea,"Center");
		responseArea.add(makeSendButton(), "North");
		makeTextAreas();
		return responseArea;
	}
	
	private JPanel makeSendButton(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
		JButton sendButton = new JButton("Send Request");
		sendButton.addActionListener(this);
		panel.add(sendButton, "Center");
		return panel;
	}
	
	private void makeTextAreas(){
		sendTextArea = new JTextArea();
		sendTextArea.setEditable(false);
		JScrollPane send = makeScrollPane(sendTextArea, "Sent Request");
		
		receiveTextArea = new JTextArea();
		receiveTextArea.setEditable(false);
		JScrollPane receive = makeScrollPane(receiveTextArea, "Received Response");
		
		JSplitPane splitPane = putTextAreasToSplitPane(send, receive);
		responseArea.add(splitPane, "Center");
		responseArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
	}
	
	private JScrollPane makeScrollPane(JComponent c, String borderTitel){
		JScrollPane scrollPane = new JScrollPane(c);         
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);         
		scrollPane.setPreferredSize(new Dimension(500, 200));         
		scrollPane.setMinimumSize(new Dimension(10, 10));  
		scrollPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(borderTitel),BorderFactory.createEmptyBorder(5,5,5,5)));
		return scrollPane;
	}
	
	private JSplitPane putTextAreasToSplitPane(JScrollPane jsp1, JScrollPane jsp2) {    
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jsp1,jsp2);         
		splitPane.setOneTouchExpandable(true);         
		splitPane.setResizeWeight(0.5);         
		return splitPane;
	}


	
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Send Request")) {
			sendRequest();
		}
		else if (ae.getActionCommand().equals("Load Specific XML")) {
			loadSpecificFile();
		}
		else if (ae.getActionCommand().equals("Load Empty XML")) {
			loadEmptyFile();
		}
		else if (ae.getActionCommand().equals("Save")) {
			saveFile();
		}
		else if (ae.getActionCommand().equals("Clear")) {
			xmlInputTextArea.setText("");
		}
		
	}
	
	private void sendRequest(){
			// get Request-KMIPContainer from XML specified in TextArea
			xmlConverter = getXMLFileFromTextArea();
			KMIPContainer requestFromClient = xmlConverter.getKMIPContainer();
			
			// Send Request
			KMIPContainer responseFromServer = gui.getKmipStub().processRequest(requestFromClient);
			logger.info("Decoded Response from Server:");
			logger.debug("\n-----------------------------\n"+responseFromServer+"\n-----------------------------");
			gui.statusBar.setStatus(KMIPClientGUIStatusBar.Done);
			
			// Output to Response-TextAreas
			sendTextArea.setText("Request from Client: \n--------------------\n" + requestFromClient.toString());
			receiveTextArea.setText("Response from Server: \n--------------------\n" + responseFromServer.toString());
	}
	
	private KMIPClientGUIxml getXMLFileFromTextArea(){
		try {
			String fileData = xmlInputTextArea.getText();
			File f = new File("src/ch/ntb/inf/kmip/client/gui/xml/CustomXMLRequest.xml");
			FileWriter fw = new FileWriter(f);
			fw.write(fileData);
			fw.flush();
			fw.close();
			return new KMIPClientGUIxml(gui, f);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void loadEmptyFile(){
		File file = new File("src/ch/ntb/inf/kmip/client/gui/xml/EmptyRequest.xml");
		writeFileToTextArea(file);
	}
	
	private void loadSpecificFile(){
		int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File file = fc.getSelectedFile();
           	writeFileToTextArea(file);
        } else {
            logger.info("Load command cancelled by user.");
        }
	}
	
	private void writeFileToTextArea(File file){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(file));
			
	    	String fileData;
	    	xmlInputTextArea.setText("");
	    	while((fileData = br.readLine()) != null){
	    		xmlInputTextArea.append(fileData.replace("\t", "       "));
	    		xmlInputTextArea.append("\n");
	    	}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	private void saveFile(){
		int returnVal = fc.showDialog(this, "Save");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	BufferedWriter bw = null;
        	File file = fc.getSelectedFile();
            try{
            	bw = new BufferedWriter(new FileWriter(file));
            	bw.write(xmlInputTextArea.getText());
            	JOptionPane.showMessageDialog(this, "Your XML-Request has been saved as \"" + file.getName()+"\"",
            			"XML Saved Message",
            			JOptionPane.INFORMATION_MESSAGE);
            }
            catch(Exception e){
            	JOptionPane.showMessageDialog(this, "Error! Access Deied for  \"" + file.getName()+"\"",
            			"XML Error Message",
            			JOptionPane.ERROR_MESSAGE);
            	e.printStackTrace();
            }
            finally{
            	try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }  
        else {
            logger.info("Save command cancelled by user.");
        }
	}
	
	public KMIPClientGUIxml getXmlConverter(){
		return this.xmlConverter;
	}
}
