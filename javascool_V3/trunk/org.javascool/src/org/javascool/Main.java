/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 539 du SVN                          |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|


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
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="../../org/javascool/Main.java">source code</a>
 */
public class Main extends JApplet implements ActionListener{
  private static final long serialVersionUID = 1L;
    JButton jOpenButton = new JButton();
    JButton jNewButton = new JButton();
    JButton jSaveButton = new JButton();
    JButton jPlayButton = new JButton();
    JTextPane editorPane=new JTextPane();
    JPanel panetop=new JPanel();
    JPanel pane=new JPanel();
    JToolBar tools=new JToolBar();
    JFileChooser fc;
    SourceEditor se=new SourceEditor();
    File file;
  // This is the way to build the applet
  public void init() {
  // Panes
    pane.setLayout(new BorderLayout());
    panetop.setLayout(new BorderLayout());
  // Panes positions
    pane.add(panetop,BorderLayout.NORTH);
    panetop.add(tools,BorderLayout.CENTER);
    pane.add(se,BorderLayout.CENTER);
  // Butons
    fc = new JFileChooser();
    jNewButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/new.png"));
    tools.add(jNewButton);
    jNewButton.addActionListener(this);
    jOpenButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/open.png"));
    jOpenButton.addActionListener(this);
    tools.add(jOpenButton);
    jSaveButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/save.png"));
    jSaveButton.addActionListener(this);
    tools.add(jSaveButton);
    tools.addSeparator();
    jPlayButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/play.png"));
    jPlayButton.addActionListener(this);
    tools.add(jPlayButton);
  // Set Visible
    setContentPane(pane);
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource()==jOpenButton){
    se.setText(openFile());
    }
    else if(e.getSource()==jNewButton){
    newFile();
    }
    else if(e.getSource()==jSaveButton){
    saveFile();
    }
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
  private String openFile(){
    fc.showOpenDialog(Main.this);
    file = fc.getSelectedFile();
    String r= Utils.loadString("file:"+file.getPath());
    return r;
  }
  private void saveFile(){
    if(file==null){
        fc.showSaveDialog(Main.this);
        file = fc.getSelectedFile();
        String path=file.getPath();
        String text=se.getText();
        Utils.saveString(path,text);
    }
    else{
        String path=file.getPath();
        String text=se.getText();
        Utils.saveString(path,text);
    }
    
  }
  private void newFile(){
    file=null;
    se.setText("");
  }
}
