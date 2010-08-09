/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 558 du SVN                          |
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

import javax.swing.SwingUtilities;
import javax.swing.filechooser.*;

// Used for URL read
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.net.URLEncoder;

// Used for URL write
import java.net.URL;
import java.io.IOException;
import java.lang.System; // .out.println
import java.net.URLConnection;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileWriter;

/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="Main.java">source code</a>
 */
public class Main extends JApplet implements ActionListener {
  private static final long serialVersionUID = 1L;
    JButton jOpenButton = new JButton();
    JButton jNewButton = new JButton();
    JButton jSaveButton = new JButton();
    JButton jPlayButton = new JButton();
    JButton jCompilButton = new JButton();
    JButton jHelpButton = new JButton();
    JButton jJvsButton = new JButton();
    JButton jPmlButton = new JButton();
    String[] activities = {"Pml","Jvs","Algo"};
    String help2 = Utils.loadString("http://www.google.fr");
    JComboBox actlist = new JComboBox(activities);
    JTextPane editorPane=new JTextPane();
    JPanel panetop=new JPanel();
    JPanel helpane=new JPanel();
    JPanel pane=new JPanel();
    JToolBar tools=new JToolBar();
    JToolBar mods=new JToolBar();
    JTabbedPane tabbedPane = new JTabbedPane();
    JFileChooser fc;
    SourceEditor se=new SourceEditor();
    AlgoTree at=new AlgoTree();
    JEditorPane help=new JEditorPane();
    AlgoTree ae=new AlgoTree();
    JavaPanel jpc=new JavaPanel();
    File file;
  /* This is the way to build the applet.*/
  public void init() {
  // Panes
    pane.setLayout(new BorderLayout());
    panetop.setLayout(new BorderLayout());
  // Panes positions
    pane.add(panetop,BorderLayout.NORTH);
    panetop.add(tools);
    panetop.add(actlist,BorderLayout.EAST);
    actlist.addActionListener(this);
    tabbedPane.addTab("Nouveau.jvs - JVS Editor",at);
    help.setText(Utils.loadString("http://www.google.fr"));
    help.setContentType("text/html");
    helpane.add(help);
    pane.add(tabbedPane);
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
    jCompilButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/compil.png"));
    jCompilButton.addActionListener(this);
    tools.add(jCompilButton);
    tools.addSeparator();
    jHelpButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/help.png"));
    jHelpButton.addActionListener(this);
    tools.add(jHelpButton);
    jJvsButton.setText("Java's cool");
    jJvsButton.addActionListener(this);
    mods.add(jJvsButton);
    jPmlButton.setText("    Pml    ");
    jPmlButton.addActionListener(this);
    mods.add(jPmlButton);
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
    else if(e.getSource()==jHelpButton){
    showHelp();
    }
    else if(e.getSource()==jPmlButton){
    startPml();
    }
    else if(e.getSource()==actlist){
        JComboBox cb = (JComboBox)e.getSource();
        String actName = (String)cb.getSelectedItem();
        System.out.println(actName);
        if(actName.equals("Jvs")){startJvs();}
        else if(actName.equals("Pml")){startPml();}
        else if(actName.equals("Algo")){startAlgo();}
        else{System.out.println("No activities which is named "+actName);}
    }
    else{System.out.println("Button hasn't got action !!!");}
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
    Main m = new Main();
    Utils.show(m, "Java'Scool v3.0");
  }
  /**Open a file.
  */
  public String openFile(){
    fc.showOpenDialog(Main.this);
    file = fc.getSelectedFile();
    String r= Utils.loadString("file:"+file.getPath());
    return r;
  }
  /**Save a file with a basic dialog.
  */
  public void saveFile(){
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
  /**Save a file.
  * @param title Title of the save dialog
  */
    public void saveFile(String title){
    if(file==null){
        fc.showDialog(Main.this, title);
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
  /**Create a new file.
  */
  public void newFile(){
    file=null;
    se.setText("");
  }
  /**Method deseignd to show wiki help.
  *It's create a new tab with a web navigator which go to http://wiki.inria.fr/sciencinfolycee/
  */
  public void showHelp(){
    tabbedPane.addTab("Aide",new HtmlDisplay().reset("<html><head><title>Ma page</title></head><body><h1>Titre</h1>Bienvenu dans l'aide de Java's cool :-)</body></html>")); 
    tabbedPane.revalidate();
  }
  /** This method start the PML Editor.
  *When PML is selected, it's close all tabs and create a new tab with the PML editor.
  *Actually it's the same that the JVS Editor 
  */
  public void startPml(){
    tabbedPane.removeAll();
    tabbedPane.addTab("Nouveau.pml - Pml Editor",se);
    if(se.getText().length()>0){
    this.saveFile("Enregistrer votre fichier en JVS avant de passer à Pml");
    file=null;
    newFile();}
    tabbedPane.revalidate();
  }
  /** This method start the JVS Editor.
  *When JavaScool is selected, it's close all tabs and create a new tab with the java editor.  
  */
  public void startJvs(){
    tabbedPane.removeAll();
    tabbedPane.addTab("Nouveau.jsc - JVS Editor",se);
    tabbedPane.addTab("Console",jpc.panel);
    tabbedPane.revalidate();
  }
  /** This method start the Algo Editor.
  *When Algo is selected, it's close all tabs and create a new tab with the algo editor.  
  */
   public void startAlgo(){
    tabbedPane.removeAll();
    tabbedPane.addTab("Nouveau.jsc - Algo Editor",ae);
    tabbedPane.revalidate();
  }

}
