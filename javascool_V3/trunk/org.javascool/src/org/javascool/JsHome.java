/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 904 du SVN                          |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JApplet;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.lang.reflect.Array;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

// Processing interface
import java.awt.Container;
import java.applet.Applet;
import java.io.PrintStream;

// Proglets used
import proglet.ingredients.Console;
import proglet.exosdemaths.CurveDisplay;

// Used for the file interface
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Object;

// Used to manage keystroke
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

// Used to register elements
import java.util.HashMap;

// Used to set Win LOOK
import javax.swing.UIManager;

public class JsHome {
  private static final long serialVersionUID = 1L;
  static final String title = "Java'Scool 3.1";
  private JList list;
  private DefaultListModel listModel;
  private Main gui;
  Boolean NoStartApp=true;
  private String Activity;
  private JButton CloseButton;
  private JButton GoButton;

  // [0] Defines the look and field.
  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); } catch(Exception e) { }
    } else {
      try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", title); } catch(Exception e) { }
      try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) { System.err.println("Note: Utilisaton du thème Java (et non du système)"); }
    } 
  }

  // [1] Defines the main panel and defines how to edit the toolbar, activityList and tabbedpane
  private JPanel pane=new JPanel();
  /** Builds the GUI. */
  private void initGUI() {
    //1. Create the frame.
    JFrame frame = new JFrame(title);
    listModel = new DefaultListModel();
    for(Object act : gui.getActivities()){
      listModel.addElement(act);
    }
    list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);
    list.setVisibleRowCount(5);
    JScrollPane listScrollPane = new JScrollPane(list);
    CloseButton=new JButton("Quitter");
    CloseButton.addActionListener(CloseButtonListener);
    GoButton=new JButton("Lancer");
    GoButton.addActionListener(GoButtonListener);
    pane.add(listScrollPane, BorderLayout.CENTER);
    pane.add(GoButton, BorderLayout.EAST);
    pane.add(CloseButton, BorderLayout.EAST);
    //2. Optional: What happens when the frame closes?
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //3. Create components and put them in the frame.
    //...create emptyLabel...
    frame.getContentPane().add(pane, BorderLayout.CENTER);
    frame.addWindowListener(new WindowListen());
    //4. Size the frame.
    frame.pack();
    //5. Show it.
    frame.setVisible(true);
  }

  public void JsHome(Main main){
    gui=main;
    initGUI();
  }
  class WindowListen implements WindowListener {
    public void windowOpened(WindowEvent e){}
    public void windowClosing(WindowEvent e){}
    public void windowClosed(WindowEvent e){
      System.exit(0);
    }
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}
  }
  public String getAct(){return Activity;}
    /** Generic action listener for all actions. */
  private ActionListener CloseButtonListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	  if(false){}
	  else{System.exit(0);}
	  //String button=((JButton) e.getSource()).getText();
	  //if(button.equal(quit)){this.dispose();}
      }
    };
  private ActionListener GoButtonListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	NoStartApp=false;
	Activity=(String)list.getSelectedValue();
	gui.setActivityAs(Activity);
	Utils.show(gui, title, Utils.getIcon("org/javascool/doc-files/icones32/logo_jvs.gif"), true);
      }
    };
}