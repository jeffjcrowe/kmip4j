/**
 * KMIPClientGUISignal.java
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

import java.awt.Color;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class KMIPClientGUISignal extends JPanel {

	private static final long serialVersionUID = 7728992405923373790L;
	private static final Logger logger = Logger.getLogger(KMIPClientGUISignal.class);
	
	private ImageIcon green;
	private ImageIcon yellow;
	private ImageIcon red;
	private JLabel signal;
	
	public final static int GREEN = 1;
	public final static int YELLOW = 2;
	public final static int RED = 3;

	public KMIPClientGUISignal() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBackground(Color.WHITE);
		green = createImageIcon("img/CircleGreen.png","Green Circle");
		yellow = createImageIcon("img/CircleYellow.png","Yellow Circle");
		red = createImageIcon("img/CircleRed.png","Red Circle");
		signal = new JLabel(green);
		this.add(Box.createHorizontalGlue());
		this.add(signal);
		this.add(Box.createHorizontalGlue());
	}
	
	protected ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			ImageIcon i = new ImageIcon(imgURL, description);
			i.setImage(i.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));;
			return i;
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}


	public void setColor(int color){
		switch(color){
		case GREEN:
			signal.setIcon(green);
			break;
		case YELLOW:
			signal.setIcon(yellow);
			break;
		case RED:
			signal.setIcon(red);
			break;
		default:
			logger.warn("KMIPClientGUISignal:: setColor():: Color not defined!");
			break;
		}
		revalidate();
		repaint();
	}
}