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

import java.lang.String;
import java.awt.BorderLayout;

/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne
 * @see <a href="../../org/javascool/Main.java">source code</a>
 */
public class Main extends JApplet {
  private static final long serialVersionUID = 1L;

  // This is the way to build the applet
  public void init() {
  // Panes
    JPanel pane=new JPanel();
    pane.setLayout(new BorderLayout());
    JPanel panetop=new JPanel();
    panetop.setLayout(new BorderLayout());
    JToolBar tools=new JToolBar();
    JTextPane editorPane=new JTextPane();
  // Panes positions
    pane.add(panetop,BorderLayout.NORTH);
    panetop.add(tools,BorderLayout.WEST);
    pane.add(editorPane,BorderLayout.CENTER);
  // Butons
    JButton jNewButton = null;
    jNewButton = new JButton();
    jNewButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/New_16x16.png"));
    JButton jStopButton = null;
    jStopButton = new JButton();
    jStopButton.setIcon(Utils.getIcon("org/javascool/doc-files/icones16/Stop_16x16.png"));
    tools.add(jNewButton);
    tools.add(jStopButton);
  // Set Visible
    setContentPane(pane);
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
