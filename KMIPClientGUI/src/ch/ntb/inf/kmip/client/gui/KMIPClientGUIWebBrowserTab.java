/**
 * KMIPClientGUIWebParserTab.java
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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import org.apache.log4j.Logger;

public class KMIPClientGUIWebBrowserTab extends JPanel implements HyperlinkListener, ActionListener{

	private static final long serialVersionUID = -175072687855389580L;

	private static final Logger logger = Logger.getLogger(KMIPClientGUIWebBrowserTab.class);
	
	private JEditorPane jep;
	private JComboBox<String> urlBox;
	private JButton backButton, forwardButton;
	private ArrayList<String> pageList = new ArrayList<String>();
	private int pageIndex = 0;
	 
	private String[] urls = new String[]{
			"http://de.wikipedia.org/wiki/Key_Management_Interoperability_Protocol",
			"http://en.wikipedia.org/wiki/Key_Management_Interoperability_Protocol",
			"http://docs.oasis-open.org/kmip/spec/v1.0/os/kmip-spec-1.0-os.html",
			"http://cryptsoft.com/kmip/parse/"
	};
	private String[] urlDescriptions = new String[]{
			"German KMIP Wiki",
			"English KMIP Wiki",
			"KMIP-Specification 1.0",
			"KMIP Packet Parser"
	};

	public KMIPClientGUIWebBrowserTab(){
		this.setLayout(new BorderLayout());
		this.add(makeBrowserHeader(), "North");
		this.add(makeBrowser(), "Center");
	}
		
	private JPanel makeBrowserHeader(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5)));
		panel.add(makeButtonPanel(),"West");
		panel.add(makeURLSelectionBox(),"Center");
		return panel;
	}
	
	private JPanel makeURLSelectionBox(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(urlDescriptions);
		urlBox = new JComboBox<String>(model);
		urlBox.setSelectedIndex(0);
		urlBox.setEditable(false);
		urlBox.addActionListener(this);
		panel.add(urlBox);
		return panel;
	}
	
	private JPanel makeButtonPanel(){
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.white);
        backButton = new JButton(createImageIcon("img/ArrowBack.png","Back Arrow"));
        backButton.setToolTipText("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionBack();
            }
        });
        backButton.setEnabled(false);
        buttonPanel.add(backButton,"West");
        
        forwardButton = new JButton(createImageIcon("img/ArrowForward.png","Forward Arrow"));
        forwardButton.setToolTipText("Forward");
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionForward();
            }
        });
        forwardButton.setEnabled(false);
        buttonPanel.add(forwardButton,"East");
        return buttonPanel;
	}
	
	private JPanel makeBrowser(){
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),BorderFactory.createEmptyBorder(5,5,5,5))); 
		
		jep = new JEditorPane();
		jep.setEditable(false);
		try {
			showPage(new URL(urls[urlBox.getSelectedIndex()]),true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		jep.addHyperlinkListener(this);
				
		JScrollPane jsp = new JScrollPane(jep);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setPreferredSize(new Dimension(200,200));
		jsp.setMaximumSize(new Dimension(200,200));

		panel.add(jsp, BorderLayout.CENTER);
		return panel;
	}
	
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon i = new ImageIcon(imgURL, description);
			i.setImage(i.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));;
			return i;
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}
	
	// Listener Support
	
    private void actionBack() {
        pageIndex--;
        try {
            showPage(
                    new URL((String) pageList.get(pageIndex)), false);
        } catch (Exception e) {}
    }
    
    private void actionForward() {
        pageIndex++;
        try {
            showPage(new URL((String) pageList.get(pageIndex)), false);
        } catch (Exception e) {}
    }
			
	public void actionPerformed(ActionEvent ae) {
		try {
			showPage(new URL(urls[urlBox.getSelectedIndex()]),true);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
		
	 private void showPage(URL pageUrl, boolean addToList) {
         // Show hour glass cursor while crawling is under way.
         setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         try {
        	 // clear the stream description property of the document to supply reload
        	 Document doc = jep.getDocument();
        	 doc.putProperty(Document.StreamDescriptionProperty, null);
        	 // set the requested Page
             jep.setPage(pageUrl);  
             logger.info("Load page \"" + pageUrl + "\"");
             updateHistory(pageUrl, addToList);
         } catch (Exception e) {
             logger.warn("Error while loading page \"" + pageUrl + "\"!");
         } finally {
             setCursor(Cursor.getDefaultCursor());
         }
     }
	 
	 private void updateHistory(URL pageUrl, boolean addToList){
         int listTail = pageList.size()-1;
         if (addToList) {
        	 if(pageIndex < listTail){
        		 for(int i = listTail; i > pageIndex; i--){
        			 pageList.remove(i);
        		 }
        		 pageIndex = pageList.size()-1;
        	 }
             pageList.add(pageUrl.toString());
             pageIndex = pageList.size()-1;
         }
         else{
        	 urlBox.removeActionListener(this);
        	 urlBox.setSelectedIndex(pageIndex);  
        	 urlBox.addActionListener(this);
         }
         updateButtons();
	 }
	 
	 private void updateButtons() {
         if (pageIndex >= pageList.size()-1) {
             forwardButton.setEnabled(false);  
         }
         else{
        	 forwardButton.setEnabled(true);
         }
         
         if(pageIndex <= 0){
        	 backButton.setEnabled(false);
         }
         else {
             backButton.setEnabled(true);
         }
     } 
    
     // Handle hyperlink's
     public void hyperlinkUpdate(HyperlinkEvent event) {
         HyperlinkEvent.EventType eventType = event.getEventType();
         if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
             if (event instanceof HTMLFrameHyperlinkEvent) {
                 HTMLFrameHyperlinkEvent linkEvent =
                         (HTMLFrameHyperlinkEvent) event;
                 HTMLDocument document =
                         (HTMLDocument) jep.getDocument();
                 document.processHTMLFrameHyperlinkEvent(linkEvent);
             } else {
                 showPage(event.getURL(), true);
             }
         }
     }
	
}
