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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.container.KMIPBatch;
import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.kmipenum.*;
import ch.ntb.inf.kmip.objects.Authentication;
import ch.ntb.inf.kmip.objects.CredentialValue;
import ch.ntb.inf.kmip.objects.base.Credential;
import ch.ntb.inf.kmip.objects.base.TemplateAttribute;
import ch.ntb.inf.kmip.objects.base.TemplateAttributeStructure;
import ch.ntb.inf.kmip.types.KMIPBoolean;
import ch.ntb.inf.kmip.types.KMIPByteString;
import ch.ntb.inf.kmip.types.KMIPEnumeration;
import ch.ntb.inf.kmip.types.KMIPInteger;

public class KMIPClientGUIFreestyleTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1694350508510923646L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUIFreestyleTab.class);
	private static final String[] string_trueFalse = new String[]{"true", "false"};
		
	private static final String[] string_Operation = new String[]{"Create",	"Create Key Pair","Register","Re-key","Derive Key","Certify","Re-certify",
		"Locate","Check","Get","Get Attributes","Get Attribute List","Add Attribute","Modify Attribute","Delete Attribute","Obtain Lease","Get Usage Allocation",
		"Activate","Revoke","Destroy","Archive","Recover","Validate","Query","Cancel","Poll","Notify","Put"};
		
	private KMIPClientGUI gui; 
	private KMIPClientGUIFreestyleTabList attributes;
	private KMIPClientGUIFreestyleTabList templateAttributes;
	
	private JTextArea sendTextArea;
	private JTextArea receiveTextArea;
	private JTextField maxResponseSize;
	private JComboBox<String> asynchronousIndicator;
	private JComboBox<String> operation;
	private JTextField username;
	private JTextField password;
	private JTextField uniqueBatchItemID;
	private JTextField asynchronousCorrelationValue;
	private JButton sendButton;
	private Color backgroundColor = Color.WHITE;
	
	public KMIPClientGUIFreestyleTab(KMIPClientGUI gui){		
		this.gui = gui;
		this.setBackground(backgroundColor);
		this.setLayout(new BorderLayout());
		this.add(makeRequestHeader(), "West");	
		this.add(makeResponseArea(), "South");
		this.add(makeBatch(), "Center");
	}

	private JPanel makeRequestHeader() {
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Request Header"),BorderFactory.createEmptyBorder(5,5,5,5)));

		panel.add(makeHeaderOptions(),0);
		panel.add(makeAuthentication(),1);
		return panel;
	}

	private JPanel makeHeaderOptions(){
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Options"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		maxResponseSize = makeTextField("", "Maximum Response Size");
		asynchronousIndicator = makeComboBox(string_trueFalse, 1);
		JLabel maxResponseSizeLabel = makeTextLabel("Maximum Response Size");
		JLabel asynchronousIndicatorLabel = makeTextLabel("Asynchronous Indicator");

		// Layout the Components in this panel
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(maxResponseSizeLabel,150,160,180)
							.addComponent(asynchronousIndicatorLabel,150,160,180)
					)
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.LEADING)
							.addComponent(maxResponseSize)
							.addComponent(asynchronousIndicator)
					)
			);
			layout.setVerticalGroup(
				layout.createSequentialGroup()
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(maxResponseSizeLabel)
							.addComponent(maxResponseSize,20,25,30)
					)
					.addGroup(
						layout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(asynchronousIndicatorLabel)
							.addComponent(asynchronousIndicator,20,25,30)
					)
			);
		
		return panel;
	}
	
	private JPanel makeAuthentication(){
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Authentication"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		username = makeTextField("", "Username");
		password = makeTextField("", "Password");
		JLabel userLabel = makeTextLabel("Username");
		JLabel passwordLabel = makeTextLabel("Password");	
		
		// Layout the Components in this panel
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(userLabel,40,60,80)
						.addComponent(passwordLabel,40,60,80)
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(username)
						.addComponent(password)
				)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(userLabel)
						.addComponent(username,20,25,30)
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(passwordLabel)
						.addComponent(password,20,25,30)
				)
		);
			
		return panel;
	}
		
	private JPanel makeBatch() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Batch"),BorderFactory.createEmptyBorder(5,5,5,5)));
		panel.add(makeBatchWest(), "West");
		panel.add(makeRequestPayload(), "Center");
		return panel;
	}
	
	private JPanel makeBatchWest(){
		JPanel panel = new JPanel(new GridLayout(0,1));
		panel.setBackground(backgroundColor);
		panel.add(makeBatchOptions(), 0);
		panel.add(makeBatchOperation(), 1);
		return panel;
	}
	
	private JPanel makeRequestPayload(){
		JPanel panel = new JPanel(new GridLayout(0,1,0,10));
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Request Payload"),BorderFactory.createEmptyBorder(5,5,5,5)));		
		panel.add(makeAttributes(), 0);
		panel.add(makeTemplateAttributes(), 1);
		return panel;
	}
	
	private JPanel makeAttributes(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.add(makeTextLabel("Add Attributes"), "North");
		attributes = new KMIPClientGUIFreestyleTabList();
		panel.add(attributes, "Center");
		return panel;
	}
	
	private JPanel makeTemplateAttributes(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.add(makeTextLabel("Add Template Attributes"), "North");
		templateAttributes = new KMIPClientGUIFreestyleTabList();
		panel.add(templateAttributes, "Center");
		return panel;
	}
			
	private JPanel makeBatchOptions(){
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Options"),BorderFactory.createEmptyBorder(5,5,5,5)));
		
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		uniqueBatchItemID = makeTextField("", "Unique Batch Item ID");
		asynchronousCorrelationValue = makeTextField("", "Asynchronous Correlation Value");
		JLabel uniqueBatchItemIDLabel = makeTextLabel("Unique Batch Item ID");
		JLabel asynchronousCorrelationValueLabel = makeTextLabel("Async. Correlation Value");
		
		// Layout the Components in this panel
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(uniqueBatchItemIDLabel,120,130,140)
						.addComponent(asynchronousCorrelationValueLabel,120,130,140)
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(uniqueBatchItemID)
						.addComponent(asynchronousCorrelationValue)
				)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(uniqueBatchItemIDLabel)
						.addComponent(uniqueBatchItemID,20,25,30)
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(asynchronousCorrelationValueLabel)
						.addComponent(asynchronousCorrelationValue,20,25,30)
				)
		);
				
		return panel;
	}
		
	private JPanel makeBatchOperation(){
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Operation"),BorderFactory.createEmptyBorder(5,5,5,5)));

		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		
		operation = makeComboBox(string_Operation, 0);
		JLabel operationLabel = makeTextLabel("Operation");
		
		// Layout the Components in this panel
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(operationLabel,60,70,80)
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(operation,140,150,160)
				)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(operationLabel)
						.addComponent(operation,20,25,30)
				)
		);
		
		return panel;
	}
	
	private JLabel makeTextLabel(String text){
		JLabel label = new JLabel(text, JLabel.LEFT);
		label.setFont(new Font("SansSerif", Font.PLAIN, 12));
		label.setForeground(Color.black);
		return label;
	}
	
	private JTextField makeTextField(String text, String name){
		JTextField textField = new JTextField(text, 1);
		textField.setEditable(true);
		textField.setFont(new Font("SansSerif", Font.PLAIN, 12));
		textField.setForeground(Color.black);
		return textField;
	}
	
	private JComboBox<String> makeComboBox(String[] s, int index){
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(s);
		JComboBox<String> box = new JComboBox<String>(model);
		box.setEditable(false);
		box.setSelectedIndex(index);
		box.setPrototypeDisplayValue("123456478910");
		box.setFont(new Font("SansSerif", Font.PLAIN, 12));
		return box;
	}
	
	
	// Response Area
	private JPanel makeResponseArea(){
		JPanel responseArea = new JPanel(new BorderLayout(5, 5));
		responseArea.setBackground(backgroundColor);
		responseArea.add(makeSendButton(), "North");
		
		sendTextArea = new JTextArea();
		sendTextArea.setEditable(false);
		JScrollPane send = makeScrollPane(sendTextArea, "Sent Request");
		
		receiveTextArea = new JTextArea();
		receiveTextArea.setEditable(false);
		JScrollPane receive = makeScrollPane(receiveTextArea, "Received Response");
		
		JSplitPane splitPane = putTextAreasToSplitPane(send, receive);
		responseArea.add(splitPane, "Center");
		responseArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
		return responseArea;
	}
	
	private JPanel makeSendButton(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
		sendButton = new JButton("Send Request");
		sendButton.addActionListener(this);
		panel.add(sendButton, "Center");
		return panel;
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

	// Required by ActionListener
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Send Request")) {
			sendRequest();
		}
	}
	
	private void sendRequest(){
		gui.statusBar.setStatus(KMIPClientGUIStatusBar.RequestProcess);
		KMIPContainer requestFromClient = new KMIPContainer();
		evaluateKMIPContainer(requestFromClient);
		evaluateKMIPBatch(requestFromClient);
		// send Request
		KMIPContainer responseFromServer = gui.getKmipStub().processRequest(requestFromClient,"","");
		logger.info("Decoded Response from Server:");
		logger.debug("\n-----------------------------\n"+responseFromServer+"\n-----------------------------");
		gui.statusBar.setStatus(KMIPClientGUIStatusBar.Done);
		// Update Response-TextAreas
		sendTextArea.setText("Request from Client: \n--------------------\n"+requestFromClient.toString());
		receiveTextArea.setText("Response from Server: \n--------------------\n"+responseFromServer.toString());
	}
	
	private void evaluateKMIPContainer(KMIPContainer container){
		// evaluate Request Header Options
		container.setMaximumResponseSize(evaluateKMIPIntegerFromTextField(maxResponseSize));
		container.setAsynchronousIndicator(evalueateKMIPBooleanFromComboBox(asynchronousIndicator));
		// evaluate Authentication
		String user = evaluateStringFromTextField(username);
		String pw = evaluateStringFromTextField(password);
		if(user != null && pw != null){
			CredentialValue credentialValue = new CredentialValue(user, pw);
			Credential credential = new Credential(new EnumCredentialType(EnumCredentialType.UsernameAndPassword), credentialValue);
			container.setAuthentication(new Authentication(credential));
		}
	}
	
	private void evaluateKMIPBatch(KMIPContainer container){
		KMIPBatch batch = new KMIPBatch();
		container.addBatch(batch);
		// evaluate Batch Options
		batch.setUniqueBatchItemID(evaluateKMIPByteString(uniqueBatchItemID));
		batch.setAsynchronousCorrelationValue(evaluateKMIPByteString(asynchronousCorrelationValue));
		// evaluate Operation
		batch.setOperation((EnumOperation) evaluateKMIPEnumFromComboBox(operation, "EnumOperation"));
		// evaluate Attributes
		batch.setAttributes(attributes.getList());
		// evaluate Template Attribute
		TemplateAttributeStructure tas = new TemplateAttribute();
		tas.setAttributes(templateAttributes.getList());
		batch.addTemplateAttributeStructure(tas);
		// set Batch count
		container.calculateBatchCount();
	}
	
	private KMIPByteString evaluateKMIPByteString(JTextField textField) {
		String value = evaluateStringFromTextField(textField);
		if(value != null){
			return new KMIPByteString(value.getBytes());
		}
		return null;
	}

	private String evaluateStringFromTextField(JTextField textField){
		if(textField.getText().equals("")){
			return null;
		}
		return textField.getText();
	}
	
	private KMIPInteger evaluateKMIPIntegerFromTextField(JTextField textField){
		String s = textField.getText();
		if(s.equals("")){
			return null;
		}
		else{
			try{
				return new KMIPInteger(Integer.parseInt(s));
			}
			catch(Exception e){
				JOptionPane.showMessageDialog(this, "Entry in Text-Field \"" + textField.getName()+"\" is Not an Integer!",
	        			"XML Error Message",
	        			JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
	}
	
	private KMIPBoolean evalueateKMIPBooleanFromComboBox(JComboBox<String> box){
		if(box.getSelectedIndex() == 0){
			return new KMIPBoolean(true);
		}
		return null;
	}
	
	private KMIPEnumeration evaluateKMIPEnumFromComboBox(JComboBox<String> box, String enumClassName){
		try {
			Class<?> enumerationClass = Class.forName("ch.ntb.inf.kmip.kmipenum."+enumClassName.replaceAll("\\s", ""));
			Constructor<?> enumerationConstructor = enumerationClass.getConstructor(Integer.TYPE);
			return (KMIPEnumeration) enumerationConstructor.newInstance(box.getSelectedIndex()+1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
