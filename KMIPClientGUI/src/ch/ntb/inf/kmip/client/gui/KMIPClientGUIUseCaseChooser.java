/**
 * KMIPClientGUIUseCaseChooser.java
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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;


public class KMIPClientGUIUseCaseChooser extends JPanel implements ListSelectionListener, ActionListener{

	private static final long serialVersionUID = -6061716849716527804L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUIUseCaseChooser.class);

	private KMIPClientGUI gui;
	private JList<String> chooseUC;
	private JComboBox<String> chooseUID;
	private JComboBox<String> chooseAsynchronousCorrelationValue;
	private JPanel ucSelectionPanel;
	private DefaultListModel<String> listModel;
	private DefaultComboBoxModel<String> chooseUIDModel;
	private DefaultComboBoxModel<String> chooseAsynchronousCorrelationValueModel;
	private String lastUseCase = null;
	private String[] useCases;
	private static HashMap<String, Integer> useCaseUIDs;
		
	static{
		useCaseUIDs = new HashMap<String, Integer>();
		useCaseUIDs.put("Use Case 3.1.2 (4) - Destroy Template", 1);
		useCaseUIDs.put("Use Case 3.1.4 (10) - Client A: Destroy Template", 1);
		useCaseUIDs.put("Use Case 8.1 (2) - Locate (Private Key)", 2);
		useCaseUIDs.put("Use Case 8.1 (4) - Destroy (Public Key)", 1);
		useCaseUIDs.put("Use Case 8.2 (1) - Register (Public Key)", 1);
		useCaseUIDs.put("Use Case 8.2 (2) - Add Attribute", 1);
		useCaseUIDs.put("Use Case 8.2 (2) - Add Attribute2", 2);
		useCaseUIDs.put("Use Case 8.2 (3) - Locate (Public Key)", 1);
		useCaseUIDs.put("Use Case 8.2 (4) - Locate (Private Key)",  2);
		useCaseUIDs.put("Use Case 8.2 (6) - Destroy (Public Key)",  1);
		useCaseUIDs.put("Use Case 9.1 (4) - Get Attributes", 1);
		useCaseUIDs.put("Use Case 9.1 (5) - Get (Symmetric Key)", 1);
		useCaseUIDs.put("Use Case 9.1 (6) - Destroy", 2);
		useCaseUIDs.put("Use Case 9.1 (7) - Destroy", 1);
		useCaseUIDs.put("Use Case 9.2 (7) - Destroy", 1);
		useCaseUIDs.put("Use Case 9.2 (8) - Revoke and Destroy", 2);
		useCaseUIDs.put("Use Case 9.3 (6) - Destroy", 1);
		useCaseUIDs.put("Use Case 9.3 (7) - Revoke and Destroy", 2);
		useCaseUIDs.put("Use Case 9.4 (3) - Get Attribute", 1);
		useCaseUIDs.put("Use Case 9.4 (4) - Get Attribute", 2);
		useCaseUIDs.put("Use Case 9.4 (6) - Destroy", 1);
		useCaseUIDs.put("Use Case 9.4 (7) - Revoke and Destroy", 2);
		useCaseUIDs.put("Use Case 9.5 (5) - Client A: Obtain Lease", 1);
		useCaseUIDs.put("Use Case 9.5 (9) - Client A: Destroy", 1);
		useCaseUIDs.put("Use Case 9.5 (10) - Client A: Revoke and Destroy", 2);
	}
	
	KMIPClientGUIUseCaseChooser(KMIPClientGUI gui){
		this.gui = gui;
		
		// generate String[] for ComboBox from xml-file
		useCases = gui.ucxml.getNamesOfUseCases();
		
		setWindowParameters();
		setLabel();
		setUCSelectionList();
		setSelectionBoxes();
	}
	


	private void setWindowParameters(){
		super.setLayout(new BorderLayout());
		super.setBackground(Color.white);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Use-Case Selection"),BorderFactory.createEmptyBorder(5,5,5,5)));
	}

	private void setLabel(){
		JLabel label = new JLabel("Select a Use-Case:", JLabel.LEFT);
		label.setFont(new Font("SansSerif", Font.BOLD, 14));
		label.setForeground(Color.black);
		this.add(label,"North");
	}
	
	private void setUCSelectionList(){
		setPanelForUCSelectionList();
		setListModel();
		createUCList();

        JScrollPane listScrollPane = new JScrollPane(chooseUC);
        ucSelectionPanel.add(listScrollPane);
	}
	
	private void setPanelForUCSelectionList(){
		ucSelectionPanel = new JPanel();
		ucSelectionPanel.setLayout(new GridLayout(0,1));
		ucSelectionPanel.setBackground(Color.white);
		this.add(ucSelectionPanel,"Center");
	}
	
	private void setListModel(){
		listModel = new DefaultListModel<String>();   
		for(String s : useCases){
	        listModel.addElement(s);         
		} 
	}
	
	private void createUCList(){         
		chooseUC = new JList<String>(listModel);         
		chooseUC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);         
		chooseUC.setSelectedIndex(0); 
		gui.ucv.showSelectedUseCase();
		chooseUC.addListSelectionListener(this);  
		chooseUC.setVisibleRowCount(JList.VERTICAL); 
		addKeyListenerToList();
	}

	private void addKeyListenerToList() {
		chooseUC.addKeyListener(new KeyAdapter(){  
		      public void keyPressed(KeyEvent ke){  
		          if(ke.getKeyCode() == KeyEvent.VK_DOWN && chooseUC.getSelectedIndex() == listModel.getSize()-1){  
		            ke.consume();  
		            chooseUC.setSelectedIndex(0);  
		          }
		      }
		});
	}



	private void setSelectionBoxes() {
		JPanel selectionBoxes = new JPanel(new GridLayout(2, 0));
		
		selectionBoxes.add(setUIDSelectionBox());
		selectionBoxes.add(setAsynchronousCorrelationValueSelectionBox());
		
		this.add(selectionBoxes, "South");
	}
	
	private JPanel setUIDSelectionBox(){
		JPanel uidSelection = new JPanel(new BorderLayout());
		uidSelection.setBackground(Color.white);
		chooseUIDModel = new DefaultComboBoxModel<String>(new String[]{"Default"});
		chooseUID = new JComboBox<String>(chooseUIDModel);
		chooseUID.setSelectedIndex(0);
		chooseUID.setEditable(true);
		chooseUID.addActionListener(this);
		
		JLabel uidSelectionLabel = new JLabel("Select a Unique Identifier", JLabel.LEFT);
		uidSelectionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		uidSelectionLabel.setForeground(Color.black);
		
		uidSelection.add(uidSelectionLabel,"North");
		uidSelection.add(chooseUID,"South");
		
		return uidSelection;
	}
	
	private JPanel setAsynchronousCorrelationValueSelectionBox(){
		JPanel acvSelection = new JPanel(new BorderLayout());
		acvSelection.setBackground(Color.white);
		chooseAsynchronousCorrelationValueModel = new DefaultComboBoxModel<String>(new String[]{"Default"});
		chooseAsynchronousCorrelationValue = new JComboBox<String>(chooseAsynchronousCorrelationValueModel);
		chooseAsynchronousCorrelationValue.setSelectedIndex(0);
		chooseAsynchronousCorrelationValue.setEditable(true);
		chooseAsynchronousCorrelationValue.addActionListener(this);
		
		JLabel acvSelectionLabel = new JLabel("Select an Asynchronous Correlation Value", JLabel.LEFT);
		acvSelectionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		acvSelectionLabel.setForeground(Color.black);
		
		acvSelection.add(acvSelectionLabel,"North");
		acvSelection.add(chooseAsynchronousCorrelationValue,"South");
		
		return acvSelection;
	}
	
	
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			int useCaseId = chooseUC.getSelectedIndex();
			gui.ucxml.setSelectedUseCase(useCaseId);
			gui.ucv.showSelectedUseCase();
		}
	}
	
	public void reset(){
		chooseUC.setSelectedIndex(0);
		chooseUC.ensureIndexIsVisible(0);
		removeAllElementsFromComboBoxes();
	}
	
	public void setIndexOfUseCase(int i){
		try{
			chooseUC.setSelectedIndex(i);
			chooseUC.ensureIndexIsVisible(i);
		} catch (Exception e){
			logger.error("KMIPClientGUIUseCaseChooser:: selectIndex():: invalid index!");
			chooseUC.setSelectedIndex(0);
			chooseUC.ensureIndexIsVisible(0);
		}
	}
	private void addUIDtoComboBox(String uid){
		if(chooseUIDModel.getIndexOf(uid) == -1){
			chooseUIDModel.addElement(uid);
			chooseUID.setSelectedIndex(chooseUIDModel.getSize()-1);
		}
	}
	
	private void addAsynchronousCorrelationValuetoComboBox(String uid){
		chooseAsynchronousCorrelationValueModel.addElement(uid);
		chooseAsynchronousCorrelationValue.setSelectedIndex(chooseAsynchronousCorrelationValueModel.getSize()-1);
	}
	
	
	private void cleanUpComboBoxes(){
		int beginIndex = gui.ucxml.getUseCaseName().indexOf("Use Case");
		int endIndex = gui.ucxml.getUseCaseName().indexOf("(");
		String currentUseCase = gui.ucxml.getUseCaseName().substring(beginIndex, endIndex);
		if(!currentUseCase.equals(lastUseCase)){
			removeAllElementsFromComboBoxes();
		}
		
		lastUseCase = currentUseCase;
	}
	
	private void removeAllElementsFromComboBoxes(){
		chooseUIDModel.removeAllElements();
		addUIDtoComboBox("Default");
		
		chooseAsynchronousCorrelationValueModel.removeAllElements();
		addAsynchronousCorrelationValuetoComboBox("Default");
	}
	
	public void updateComboBoxes(String response) {
		cleanUpComboBoxes();
		updateUIDComboBox(response);
		updateAsynchronousCorrelationValueComboBox(response);
	}
	
	public void updateUIDComboBox(String response) {
		String uid = "overwrite";
		if(response.indexOf("Unique Identifier:") != (-1)){
			uid = response.substring(response.indexOf("Unique Identifier:")+18, response.indexOf("]",response.indexOf("Unique Identifier:")+18));
			addUIDtoComboBox(uid);
		} 
	}
	
	public void updateAsynchronousCorrelationValueComboBox(String response) {
		String acv = "overwrite";
		if(response.indexOf("AsynchronousCorrelationValue") != (-1)){
			acv = response.substring(response.indexOf("AsynchronousCorrelationValue =") + 31, response.indexOf("AsynchronousCorrelationValue =") + 47);
			addAsynchronousCorrelationValuetoComboBox(acv);
		} 
	}

	public String getSelectedAsynchronousCorrelationValue(){
		return (String) chooseAsynchronousCorrelationValueModel.getSelectedItem();
	}
	
	public String getSelectedUID(){
		gui.ucxml.setHasTwoUIDs(false);
		if(gui.ucv.isProcessAllSelected()){
			
			String currentUC = getUCName();
			if(useCaseUIDs.containsKey(currentUC)){
					
				if(currentUC.equals("Use Case 8.2 (2) - Add Attribute")){
					gui.ucxml.setHasTwoUIDs(true);
				}
				return (String) chooseUIDModel.getElementAt(useCaseUIDs.get(currentUC));
			}
		}
		return (String) chooseUIDModel.getElementAt(chooseUID.getSelectedIndex());
	}
	
	public String getSecondUID(){
		return chooseUIDModel.getElementAt(useCaseUIDs.get("Use Case 8.2 (2) - Add Attribute2"));
	}
	
	private String getUCName(){
		String ucWithWhiteSpace = (String) chooseUC.getSelectedValue();
		int begin = ucWithWhiteSpace.indexOf("Use Case");
		String ucWithWhiteSpaceAtTheEnd = ucWithWhiteSpace.substring(begin);
		int startWhiteSpace = ucWithWhiteSpaceAtTheEnd.indexOf("\n");
		return ucWithWhiteSpaceAtTheEnd.substring(0, startWhiteSpace);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("comboBoxEdited")) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) e.getSource();
			String selection = (String) cb.getSelectedItem();
			cb.insertItemAt(selection, 0);
			cb.setSelectedIndex(0);
		}
	}
	
	// Getters & Setters
	public JList<String> getChooseUC() {
		return chooseUC;
	}
	
}
