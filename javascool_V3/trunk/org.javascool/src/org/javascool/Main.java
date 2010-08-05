/********************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010. All gnu rights reserved. *
 ********************************************************************************/

package org.javascool;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.*; 

import java.lang.String;
import java.awt.BorderLayout;
import java.awt.event.*;
//Java
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;


/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne
 * @see <a href="../../org/javascool/Main.java">source code</a>
 */
public class Main extends JApplet implements ActionListener{
  private static final long serialVersionUID = 1L;
    JButton jOpenButton = new JButton();
    JButton jNewButton = new JButton();
    JTextPane editorPane=new JTextPane();
    JFileChooser fc;
  // This is the way to build the applet
  public void init() {
  // Panes
    JPanel pane=new JPanel();
    pane.setLayout(new BorderLayout());
    JPanel panetop=new JPanel();
    panetop.setLayout(new BorderLayout());
    JToolBar tools=new JToolBar();
    
  // Panes positions
    pane.add(panetop,BorderLayout.NORTH);
    panetop.add(tools,BorderLayout.WEST);
    pane.add(editorPane,BorderLayout.CENTER);
  // Butons
    fc = new JFileChooser();
    jNewButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/New_16x16.png"));
    tools.add(jNewButton);
    jNewButton.addActionListener(this);
    jOpenButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/Open_16x16.png"));
    jOpenButton.addActionListener(this);
    tools.add(jOpenButton);
    JButton jStopButton = new JButton();
    jStopButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/Stop_16x16.png"));
    tools.add(jStopButton);
  // Set Visible
    setContentPane(pane);
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource()==jOpenButton){
    fc.showOpenDialog(Main.this);
    File file = fc.getSelectedFile();
    editorPane.setText("Vous avez selectionnez : "+file.getName()+"\nQui est dans : "+file.getPath());
    }
    else if(e.getSource()==jNewButton){System.out.println("Hi ! V4 is comming :-)");}
    else{System.out.println("ERROR");}
  }

  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Using javascool means: doing an "activity" which result is to be stored in a "file-name".</p>
   * @param args usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done.</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] args) {
    System.out.println("Hi ! V3 is comming :-)");
    Main m = new Main(); Utils.show(m, "Java'Scool v3.0", 600, 400);
  }
}
