package compiler;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JApplet;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;

public class InterfacePrincipale extends JApplet {

	private JPanel jContentPane = null;
	private JPanel jMenuPanel = null;
	private JButton jOpenButton = null;
	private JButton jCompileButton = null;
	private JButton jRunButton1 = null;
	private JButton jButton1 = null;
	private JPanel jProgramPanel = null;
	private JScrollPane jProgramScrollPane = null;
	private JTextArea jProgramEditorPane = null;
	private JPanel jResultPanel = null;
	private JPanel jConsolePanel = null;
	private JScrollPane jScrollPane = null;
	private JEditorPane jResultEditorPane = null;
	private JScrollPane jConsolScrollPane1 = null;
	private JEditorPane jConsolEditorPane = null;
	private JMyFileChooser fileChosser=null;
	private String message;
	private JButton jNewButton = null; 



	/**
	 * This is the xxx default constructor
	 */
	public InterfacePrincipale() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		this.setSize(900, 700);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJMenuPanel(), null);
			jContentPane.add(getJProgramPanel(), null);
			jContentPane.add(getJResultPanel(), null);
			jContentPane.add(getJConsolePanel(), null);
		}
		return jContentPane;
	}

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
			jMenuPanel.setBounds(new Rectangle(9, 7, 878, 70));
			jMenuPanel.add(getJNewButton(), null);
			jMenuPanel.add(getJOpenButton(), null);
			jMenuPanel.add(getJCompileButton(), null);
			jMenuPanel.add(getJRunButton1(), null);
			jMenuPanel.add(getJButton1(), null);
		
		}
		return jMenuPanel;
	}

	/**
	 * This method initializes jOpenButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJOpenButton() {
		
	
		if (jOpenButton == null) {
			
			jOpenButton = new JButton();
			jOpenButton.setIcon(new ImageIcon(getClass().getResource("/charger24.png")));
			jOpenButton.setText("Ouvrir");
			jOpenButton.addActionListener(new ActionListener(){
				int value;
				String check;
	            public void actionPerformed(ActionEvent e){
	                value = getFileChosser().showOpenDialog(null);
	                if (value == 0){
	                	try{ 
	                   check = getFileChosser().getSelectedFile().getPath();
	                   
	                   actionLire(check); 
	                   }
	                    catch(IOException exc){ System.out.println("...problème avec lecture fichier...");} 
	                }
	            }
	        });
			
		}
		return jOpenButton;
	}

	/**
	 * This method initializes jCompileButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJCompileButton() {
		if (jCompileButton == null) {
			jCompileButton = new JButton();
			jCompileButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			jCompileButton.setText("Compiler");
			jCompileButton.setIcon(new ImageIcon(getClass().getResource("/compile24.png")));
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
			jRunButton1.setIcon(new ImageIcon(getClass().getResource("/Execute24.gif")));
			jRunButton1.setText("Executer");
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
			jButton1.setIcon(new ImageIcon(getClass().getResource("/Save24.png")));
			jButton1.setText("Enregister");
		}
		return jButton1;
	}
	
	

	/**
	 * This method initializes jProgramPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJProgramPanel() {
		if (jProgramPanel == null) {
			jProgramPanel = new JPanel();
			jProgramPanel.setLayout( null);
			jProgramPanel.setBounds(new Rectangle(11, 92, 479, 398));
			jProgramPanel.setBorder(BorderFactory.createTitledBorder(null, "Programme", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jProgramPanel.add(getJProgramScrollPane(), null);
		}
		return jProgramPanel;
	}

	/**
	 * This method initializes jProgramScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJProgramScrollPane() {
		if (jProgramScrollPane == null) {
			jProgramScrollPane = new JScrollPane();
			jProgramScrollPane.setBounds(new Rectangle(8, 18, 459, 364));
			jProgramScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jProgramScrollPane.setViewportView(getJProgramEditorPane());
			jProgramScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jProgramScrollPane;
	}

	/**
	 * This method initializes jProgramEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JTextArea getJProgramEditorPane() {
		if (jProgramEditorPane == null) {
			jProgramEditorPane = new JTextArea();
		}
		return jProgramEditorPane;
	}

	/**
	 * This method initializes jResultPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJResultPanel() {
		if (jResultPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jResultPanel = new JPanel();
			jResultPanel.setLayout(new GridBagLayout());
			jResultPanel.setBounds(new Rectangle(498, 92, 394, 399));
			jResultPanel.setBorder(BorderFactory.createTitledBorder(null, "Résultat", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jResultPanel.add(getJScrollPane(), gridBagConstraints);
		}
		return jResultPanel;
	}

	/**
	 * This method initializes jConsolePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJConsolePanel() {
		if (jConsolePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.weighty = 1.0;
			gridBagConstraints1.gridx = 0;
			jConsolePanel = new JPanel();
			jConsolePanel.setLayout(new GridBagLayout());
			jConsolePanel.setBounds(new Rectangle(15, 509, 873, 176));
			jConsolePanel.setBorder(BorderFactory.createTitledBorder(null, "Console", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
			jConsolePanel.add(getJConsolScrollPane1(), gridBagConstraints1);
		}
		return jConsolePanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			jScrollPane.setViewportView(getJResultEditorPane());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JEditorPane getJResultEditorPane() {
		if (jResultEditorPane == null) {
			jResultEditorPane = new JEditorPane();
			jResultEditorPane.setEditable(false);
		}
		return jResultEditorPane;
	}

	/**
	 * This method initializes jConsolScrollPane1	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJConsolScrollPane1() {
		if (jConsolScrollPane1 == null) {
			jConsolScrollPane1 = new JScrollPane();
			jConsolScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			jConsolScrollPane1.setViewportView(getJConsolEditorPane());
			jConsolScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		}
		return jConsolScrollPane1;
	}

	/**
	 * This method initializes jConsolEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JEditorPane getJConsolEditorPane() {
		if (jConsolEditorPane == null) {
			jConsolEditorPane = new JEditorPane();
			jConsolEditorPane.setEditable(false);
		}
		return jConsolEditorPane;
	}
	
	private JMyFileChooser getFileChosser() {
		if (fileChosser== null) {
			fileChosser= new JMyFileChooser();
			
		}
		return fileChosser;
	}
	
	private  void actionLire(String pFile)throws IOException{
		 int index = 0;
		 LireFichier lecture = new LireFichier(pFile);
		 getJProgramEditorPane().setText("");
		 while (( message = lecture.lire())!= null){
		 index++;
		  getJProgramEditorPane().append(message+ "\n" );
		 }
		 lecture.Fermer(); 
		
	}

	/**
	 * This method initializes jNewButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJNewButton() {
		if (jNewButton == null) {
			jNewButton = new JButton();
			jNewButton.setIcon(new ImageIcon(getClass().getResource("/nouveau2.png")));
			jNewButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			jNewButton.setText("Nouveau");
		}
		return jNewButton;
	}
	

}
