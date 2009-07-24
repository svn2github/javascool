package compiler;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JApplet;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.ComponentOrientation;

public class PrincipalInterface extends JApplet {

	private JPanel jMenuPanel = null;
	private JButton jCompileButton = null;
	private JButton jRunButton1 = null;
	private JButton jButton1 = null;
	private JButton jOpenButton = null;
	private JMyFileChooser fc=null;

	/**
	 * This is the xxx default constructor
	 */
	public PrincipalInterface() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {        this.setSize(new Dimension(879, 480));
        this.setContentPane(getJMenuPanel());

		
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	

	/**
	 * This method initializes jPrincipalPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */


	/**
	 * This method initializes jMenuPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJMenuPanel() {
		if (jMenuPanel == null) {
			jMenuPanel = new JPanel();
			
			jMenuPanel.setLayout(new FlowLayout());
			jMenuPanel.setBorder(BorderFactory.createTitledBorder(null, "Commande", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jMenuPanel.add(getJOpenButton(), null);
			jMenuPanel.add(getJCompileButton(), null);
			jMenuPanel.add(getJRunButton1(), null);
			jMenuPanel.add(getJButton1(), null);
			jMenuPanel.add(getFc(), null);
		
			
		}
		return jMenuPanel;
	}

	/**
	 * This method initializes jCompileButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCompileButton() {
		if (jCompileButton == null) {
			jCompileButton = new JButton();
			jCompileButton.setText("Compiler");
			jCompileButton.setIcon(new ImageIcon(getClass().getResource("/compile24.png")));
			jCompileButton.setMnemonic(KeyEvent.VK_UNDEFINED);
		}
		return jCompileButton;
	}

	/**
	 * This method initializes jRunButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJRunButton1() {
		if (jRunButton1 == null) {
			jRunButton1 = new JButton();
			jRunButton1.setText("Executer");
			jRunButton1.setIcon(new ImageIcon(getClass().getResource("/Execute24.gif")));
		}
		return jRunButton1;
	}

	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setText("Enregister");
			jButton1.setIcon(new ImageIcon(getClass().getResource("/Save24.png")));
		}
		return jButton1;
	}

	private JMyFileChooser getFc() {
		if (fc == null) {
			fc = new JMyFileChooser();
			
		}
		return fc;
	}

	/**
	 * This method initializes jOpenButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJOpenButton() {
		if (jOpenButton == null) {
			jOpenButton = new JButton();
			jOpenButton.setText("Ouvrir");
			jOpenButton.setIcon(new ImageIcon(getClass().getResource("/charger24.png")));
		}
		return jOpenButton;
	}

}  //  @jve:decl-index=0:visual-constraint="447,0"
