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
  // Butons declarations
    JButton jNewButton = null;
    jNewButton = new JButton();
    jNewButton.setIcon(Utils.getIcon("org/javascool/doc-files/New_32x32.png"));
    JButton jStopButton = null;
    jStopButton = new JButton();
    jStopButton.setIcon(Utils.getIcon("org/javascool/doc-files/Stop_32x32.png"));
    JTextPane editorPane = new JTextPane();
  // Creation of Panel
    JPanel pane = new JPanel();
    JPanel pane2 = new JPanel();
  // Setting of layouts
    pane.setLayout(new BorderLayout());
  // Creation of toolbar
    JToolBar bar = new JToolBar();
    JPanel panetop = new JPanel();
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pane2,editorPane);
  // Adding of all to the toolbar
    pane.add(split);
    panetop.add(jNewButton);
    bar.add(jStopButton);
    pane.add(bar,BorderLayout.NORTH);
    pane.add(panetop, BorderLayout.APPLET);
  // Show Panel
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
