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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import ch.ntb.inf.kmip.container.KMIPContainer;
import ch.ntb.inf.kmip.test.UCStringCompare;


public class KMIPClientGUIUseCaseViewer extends JPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = -6061716849716527804L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUIUseCaseViewer.class);
	
	private KMIPClientGUI gui;
	private KMIPContainer responseFromServer;
	
	private int actualUC;
	private boolean processAllSelected = false;
	
	private static final int SLIDER_MIN = 0;
	private static final int SLIDER_MAX = 6000;
	private static final int SLIDER_INIT = 0;
	private static int delay = SLIDER_INIT;
	
	private JTextArea temp = new JTextArea("--select a Use Case--");	
	private JEditorPane ucView = new JEditorPane("text/html", "");	
	private String sendParameters;
	private JPanel buttonPanel;
	private JSlider delaySlider;
	
	

	KMIPClientGUIUseCaseViewer(KMIPClientGUI gui){
		this.gui = gui;
		
		setWindowParameters();
		setScrollableView();
		setButtons();
		setDelaySlider();		
	}
	
	
	private void setWindowParameters(){
		super.setLayout(new BorderLayout());
		super.setBackground(Color.white);
		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Selected Use-Case Specification"),BorderFactory.createEmptyBorder(5,5,5,5)));
	}
	
	private void setScrollableView(){
		JPanel scrollableView = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(scrollableView);
		this.add(scrollPane,"Center");
		ucView.setBackground(Color.white);
		ucView.setBorder(new EmptyBorder(10,10,10,10));
		ucView.setEditable(false);
		scrollableView.add(ucView,"Center");
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);        
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setPreferredSize(new Dimension(500, 300));         
		scrollPane.setMinimumSize(new Dimension(20, 20)); 
	}
	
	private void setButtons(){
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBackground(Color.WHITE);
		String[] sButton = new String[]{"Process selected", "Process all"};
		JButton button;
		for(String s: sButton){
			button = new JButton(s);
			button.addActionListener(this);
			buttonPanel.add(button);
		}
		this.add(buttonPanel,"South");
	}
	
	
	private void setDelaySlider(){
		JLabel sliderTitel = new JLabel("          Delay between UseCases: ");
		buttonPanel.add(sliderTitel);
		
		delaySlider = new JSlider(JSlider.HORIZONTAL, SLIDER_MIN, SLIDER_MAX, SLIDER_INIT);
		delaySlider.addChangeListener(this);
		
		setTickMarks();
		setLabels();

		buttonPanel.add(delaySlider);
	}
	
	
	private void setTickMarks(){
		delaySlider.setMajorTickSpacing(2000);
		delaySlider.setMinorTickSpacing(1000);
		delaySlider.setPaintTicks(true);
	}
	
	private void setLabels(){
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( 0   ), new JLabel("0s") );
		labelTable.put( new Integer( 2000 ), new JLabel("2s") );
		labelTable.put( new Integer( 4000 ), new JLabel("4s") );
		labelTable.put( new Integer( 6000 ), new JLabel("6s") );
		delaySlider.setLabelTable( labelTable );		
		delaySlider.setPaintLabels(true);
	}
	
	
	
	public void showSelectedUseCase(){
		ucView.setText("");	// clear TextArea
		try {
			temp.setText("");
			temp.append("<h2 style=\"background-color:#81BEF7;\">"+gui.ucxml.getUseCaseName()+"</h2>");
			temp.append("<h3 style=\"background-color:#D8D8D8;\">Description:</h3>");
			temp.append(gui.ucxml.getUseCaseDescription());
			temp.append("<h3 style=\"background-color:#D8D8D8;\">Details:</h3>");
			sendParameters = gui.ucxml.getUseCaseDetails();
			temp.append(sendParameters);
			ucView.setText(temp.getText());		
		} catch (Exception e) {
			ucView.setText("<h2 style=\"background-color:#81BEF7;\">Use-Case not found!</h2>");
			e.printStackTrace();
		}	
	}


	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Process selected")) {
			processRequest();
			gui.ucc.getChooseUC().grabFocus();
		}
		else if (ae.getActionCommand().equals("Process all")) {
			processAllSelected = true;
			runAllUseCases();
		}
	}
	
	private boolean processRequest(){
		
		setGUIState();
		
		String expectedTTLVRequest = gui.ucxml.getExpectedTTLVRequest();
		String expectedTTLVResponse = gui.ucxml.getExpectedTTLVResponse();
		
		// get KMIPContainer from XML-Node-UseCase
		KMIPContainer container = gui.ucxml.getKMIPContainer();
		
		responseFromServer = gui.getKmipStub().processRequest(container,expectedTTLVRequest,expectedTTLVResponse);
		logger.info("Decoded Response from Server:");
		logger.debug("\n-----------------------------\n"+responseFromServer+"\n-----------------------------");
		gui.statusBar.setStatus(KMIPClientGUIStatusBar.Done);
		
		writeToResponseArea();
		return compareStrings();
	}
	
	private void setGUIState(){
		gui.statusBar.setStatus(KMIPClientGUIStatusBar.RequestProcess);
		gui.responseArea.setSignals(KMIPClientGUISignal.YELLOW);
	}
	
	
	private void writeToResponseArea(){
		gui.responseArea.appendTextSendArea("<h3 style=\"color:blue;\">KMIP Request Sent with Following Parameters:</h3> \n"+sendParameters);
		gui.responseArea.appendTextReceiveArea("Response from Server: \n--------------------\n"+responseFromServer.toString());
		gui.ucc.updateComboBoxes(responseFromServer.toString());
	}
	
	private boolean compareStrings(){
		boolean[] b = UCStringCompare.getStringComparison();
		int[] i = new int[b.length];
		boolean isSuccessful = true;
		for(int j = 0; j < b.length; j++){
			if(b[j]){
				i[j] = KMIPClientGUISignal.GREEN;
			}else{
				i[j] = KMIPClientGUISignal.RED;
				isSuccessful = false;
			}
		}
		gui.responseArea.setSignals(i);
		
		return isSuccessful;
	}
	

	public void runAllUseCases(){
		actualUC = 0;
		
		ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
    			gui.ucc.setIndexOfUseCase(actualUC); 
    			if(!processRequest() || actualUC >= gui.ucxml.getNumberOfUseCases() - 1){
    				Timer t = (Timer) evt.getSource();
    				t.stop();
    				processAllSelected = false;
    			}
    			else{
    				actualUC++;
    			}
            }
        };
        
        new Timer(delay, taskPerformer).start();
	}
	
	public JEditorPane getUcView() {
		return ucView;
	}
	
	public static void setDelay(int delay) {
		KMIPClientGUIUseCaseViewer.delay = delay;
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	        setDelay((int)source.getValue());
	    }
	}

	public boolean isProcessAllSelected() {
		return processAllSelected;
	}
	
	
}
