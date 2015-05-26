/**
 * KMIPClientGUIFreestyleTabList.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.ntb.inf.kmip.objects.base.Attribute;

public class KMIPClientGUIFreestyleTabList extends JPanel implements ActionListener{

	private static final long serialVersionUID = -4299902013291057012L;

    private static final String addString = "Add";
    private static final String removeString = "Remove";
    private static final String clearString = "Clear";
    private JButton removeButton;
    private AddListener addListener;
    private JTextField tag;
    private JTextField value;
    private Color backgroundColor = Color.WHITE;    
    private JTable table;
    private String tagEntry = "Chuck";
    private String valueEntry = "Norris";
    
    private String[] columnNames = new String[]{"Tag","Value"};
	private String[][] listData = new String[100][2];
    
	private int tableTail = 0;
    
    public KMIPClientGUIFreestyleTabList() {
        super(new BorderLayout());
        this.setBackground(backgroundColor);
 
        table = new JTable(listData, columnNames);
        setVisibleRowCount(table,3);
       
        JScrollPane tableScrollPane = new JScrollPane(table);  
        
        JPanel buttonPane = makeButtonPane();        
        JPanel inputPane = makeInputPane();
        
        add(inputPane, BorderLayout.PAGE_START);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }
    
    public JPanel makeButtonPane(){
        JButton addButton = new JButton(addString);
        addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);
 
        removeButton = new JButton(removeString);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(new RemoveListener());
        removeButton.setEnabled(false);
        
        JButton clearButton = new JButton(clearString);
        clearButton.setActionCommand(clearString);
        clearButton.addActionListener(this);
 
        JPanel buttonPane = new JPanel();
        buttonPane.setBackground(backgroundColor);
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(addButton);
      	buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(clearButton);
        
        return buttonPane;
    }
    
    public JPanel makeInputPane(){
        tag = new JTextField(10);
        tag.addActionListener(addListener);
        tag.getDocument().addDocumentListener(addListener);
        
        value = new JTextField(10);
        value.addActionListener(addListener);
        value.getDocument().addDocumentListener(addListener);
 
        JPanel inputPane = new JPanel();
        inputPane.setBackground(backgroundColor);
        inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.LINE_AXIS));
        inputPane.add(tag);
        inputPane.add(Box.createHorizontalStrut(3));
        inputPane.add(new JSeparator(SwingConstants.VERTICAL));
        inputPane.add(Box.createHorizontalStrut(3));
        inputPane.add(value);
        
        return inputPane;
    }
    
    
    
	public static void setVisibleRowCount(JTable table, int rows) {
		int height = 0;
		for (int row = 0; row < rows; row++)
			height += table.getRowHeight(row);
		table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredScrollableViewportSize().width, height));
	}
    
    public void addEntry(){
    	listData[tableTail][0] = tagEntry;
    	listData[tableTail][1] = valueEntry;
        table.updateUI();
        tableTail++;
    }
    
    public void removeEntry(){
    	listData[tableTail-1][0] = null;	
    	listData[tableTail-1][1] = null;
        table.updateUI();
        tableTail --;
    }
    
    public void removeEntry(int index){
    	listData[index][0] = null;	
    	listData[index][1] = null;
        table.updateUI();
        cleanupTable(index);
    }
    
    public void cleanupTable(int index){
    	for(int i = index; i<= tableTail; i++){
        	listData[i][0] = listData[i+1][0];	
        	listData[i][1] = listData[i+1][1];
    	}
    	tableTail--;
        table.updateUI();
    }
    
    public void clearTable(){
    	for(int i = 0; i< tableTail; i++){
        	listData[i][0] = null; 	
        	listData[i][1] = null;
    	}
    	tableTail = 0;
        table.updateUI();
        tag.setText("");
        value.setText("");
        removeButton.setEnabled(false);
    }
    
 
	class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int index = table.getSelectedRow();
			if (index != -1) {
				removeEntry(index);
			} else if (tableTail != 0) {
				removeEntry();
			}

			if (tableTail <= 0) { // Nobody's left, disable removing
				removeButton.setEnabled(false);
			}
			table.getSelectionModel().clearSelection();
		}
	}
 
    //This listener is shared by the text fields and the Add button.
    class AddListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;
 
        public AddListener(JButton button) {
            this.button = button;
        }
 
        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            tagEntry = tag.getText();
            valueEntry = value.getText();
            addEntry();
            
            //Reset the text field.
            tag.requestFocusInWindow();
            tag.setText("");
            value.requestFocusInWindow();
            value.setText("");
            tag.requestFocusInWindow();
            
            if (tableTail > 0) { // enable removing
                removeButton.setEnabled(true);
            }
        }
  
        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }
 
        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }
 
        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }
 
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }
 
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }


	public ArrayList<Attribute> getList() {
		ArrayList<Attribute> al = new ArrayList<Attribute>();
		Attribute a;
		for(int i = 0; i < tableTail; i++){
			a = createAttributeInstance(listData[i][0]);
			if(a.getValues().length > 1){
				JOptionPane.showMessageDialog(this, "Error! Attributes with multiple values not supported! (\"" + a.getAttributeName()+"\")",
	        			"Attribute Error",
	        			JOptionPane.ERROR_MESSAGE);
			}		
			a.setValue(listData[i][1].replaceAll("\\s", ""), null);
			al.add(a);
		}
		return al;
	}
	
	private Attribute createAttributeInstance(String className){
		try{
			return (Attribute) Class.forName("ch.ntb.inf.kmip.attributes." + className.replaceAll("\\s", "")).newInstance();
		} catch(Exception e){
			try{
				return (Attribute) Class.forName("ch.ntb.inf.kmip.operationparameters." + className.replaceAll("\\s", "")).newInstance();
			} catch(Exception e2){
				JOptionPane.showMessageDialog(this, "Error! Invalid Attribute \"" + className+"\"!",
	        			"Attribute List Error",
	        			JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
			}
		}
		return null;
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals(clearString)) {
			clearTable();
		}
	}
 
  
}


	
	

