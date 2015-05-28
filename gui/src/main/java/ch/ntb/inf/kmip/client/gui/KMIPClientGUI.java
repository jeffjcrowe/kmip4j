/**
 * KMIPClientGUI.java
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
 * @copyright  Copyright � 2013, Stefanie Meile, Michael Guster
 * @license    Simplified BSD License (see LICENSE.TXT)
 * @version    1.0, 2013/08/09
 * @since      Class available since Release 1.0
 *
 * 
 */

package ch.ntb.inf.kmip.client.gui;

import ch.ntb.inf.kmip.stub.KMIPStub;
import ch.ntb.inf.kmip.stub.KMIPStubInterface;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;


public class KMIPClientGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUI.class);
	
	private Container cp;
	private JPanel centerContent;
	private JPanel useCaseTab;
	private JPanel freeStyleTab;
	private JPanel customXmlTab;
	private JPanel webBrowserTab;
	private JSplitPane splitPane;
	private JTabbedPane tabbedPane;
	private KMIPStubInterface kmipStub;
	
	public KMIPClientGUIUseCaseChooser ucc;
	public KMIPClientGUIUseCaseViewer ucv;
	public KMIPClientGUIxml ucxml;
	
	public KMIPClientGUIResponseArea responseArea;
	public KMIPClientGUIStatusBar statusBar;
	

	private KMIPStub createStub() {
		try {
			File configFile = new File(ClassLoader.getSystemClassLoader().getResource("StubConfig.xml").toURI());
			return new KMIPStub(configFile);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public KMIPClientGUI(){
		// load UseCases from XML
		this.ucxml = new KMIPClientGUIxml(this);
		this.kmipStub = createStub();
		setWindow();
	}
	
	private void setWindow() {
		setLookAndFeel();
		setWindowParameters();
		setProgramIcon();
						
		// get frame container and set Layout
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());
		
		// add JMenuBar
		this.setJMenuBar(new KMIPClientGUIMenuBar(this));

		setTabbedPane();	
		setCenterContent();
		setUseCaseViewer();
		setUseCaseChooser();
		
		responseArea = new KMIPClientGUIResponseArea(this);
		
		setSplitPane();
		setStatusBar();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// pack Frame
		this.pack();	
		// set the location of the window to center of the screen  (has to be after pack)
		this.setLocationRelativeTo(null);
		// show Frame
		this.setVisible(true);
	}

	private void setTabbedPane() {
		this.tabbedPane = new JTabbedPane();
		this.useCaseTab = new JPanel(new BorderLayout());
		this.freeStyleTab = new JPanel(new BorderLayout());
		this.freeStyleTab.add(new KMIPClientGUIFreestyleTab(this));	
		this.customXmlTab = new JPanel(new BorderLayout());
		this.customXmlTab.add(new KMIPClientGUIXmlTab(this));
		this.webBrowserTab = new JPanel(new BorderLayout());
		this.webBrowserTab.add(new KMIPClientGUIWebBrowserTab());
		
		tabbedPane.addTab("Use-Cases", null, useCaseTab, "Use-Cases");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
			
		tabbedPane.addTab("Freestyle", null, freeStyleTab, "Freestyle");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
		tabbedPane.addTab("Custom-XML", null, customXmlTab, "Custom-XML");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        
        tabbedPane.addTab("Web Browser", null, webBrowserTab, "Web Browser");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        
		// enable to use scrolling tabs
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);	
		
		cp.add(tabbedPane, "Center");
	}


	private void setLookAndFeel(){
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
	}
	
	private void setWindowParameters() {
		this.setTitle("KMIP Client GUI");			
		this.setSize(1000,500);		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void setProgramIcon() {
		URL imageURL = ClassLoader.getSystemClassLoader().getResource("img/kmipIconRound.PNG");
		Image image = Toolkit.getDefaultToolkit().getImage(imageURL);
		this.setIconImage(image);
	}
	
	private void setCenterContent(){
		this.centerContent = new JPanel();
		this.centerContent.setLayout(new BorderLayout());
		this.centerContent.setBackground(Color.WHITE);
	}
	
	private void setUseCaseViewer() {
		ucv = new KMIPClientGUIUseCaseViewer(this);
		this.centerContent.add(ucv,"Center");
	}
	
	private void setUseCaseChooser() {
		ucc = new KMIPClientGUIUseCaseChooser(this);
		this.centerContent.add(ucc,"West");
	}
	
	private void setSplitPane() {
		this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerContent, responseArea);         
		this.splitPane.setOneTouchExpandable(true);         
		this.splitPane.setResizeWeight(0.5);
		useCaseTab.add(splitPane,"Center");
	}
	
	private void setStatusBar(){
		statusBar = new KMIPClientGUIStatusBar(this);
		cp.add(statusBar,"South");
	}
	
	public KMIPStubInterface getKmipStub() {
		return kmipStub;
	}	
	
	public void reset(){
		logger.info("Reset GUI...");
		this.kmipStub = createStub();
		this.ucxml = new KMIPClientGUIxml(this);
		ucc.reset();
		ucv.showSelectedUseCase();
		responseArea.appendTextReceiveArea("");
		responseArea.appendTextSendArea("");
		logger.info("Reset Done!");
	}
	
	public static void main(String[] args) {
		// configure Logger
		DOMConfigurator.configureAndWatch( "config/log4j-1.2.17.xml", 60*1000 );
		logger.info("Hello KMIPClient! What a beatiful day;)");
		new KMIPClientGUI();
	}
		
}
