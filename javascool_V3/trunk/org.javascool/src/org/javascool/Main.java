/********************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010. All gnu rights reserved. *
 ********************************************************************************/

package org.javascool;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

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
    JLabel l = new JLabel(); l.setText("           Hi ! V3 is comming :-)");
    l.setBounds(20,10,100,100);
    setContentPane(l);
    JButton jDemoButton = null;
    jDemoButton = new JButton();
    jDemoButton.setIcon(Utils.getIcon("org/javascool/doc-files/Play_32x32.png"));
    jDemoButton.setText("Demo");
    setContentPane(jDemoButton);
    JButton jStopButton = null;
    jStopButton = new JButton();
    jStopButton.setIcon(Utils.getIcon("org/javascool/doc-files/Stop_32x32.png"));
    jStopButton.setText("Stop");
    setContentPane(jStopButton);
    JPanel p = new JPanel();
    p.add(jDemoButton);
    p.add(jStopButton);
    setContentPane(p);
    JPanel pane = new JPanel();
    pane.setLayout(new BorderLayout());
    JToolBar bar = new JToolBar();
    pane.add(bar, BorderLayout.EAST);
    bar.add(jDemoButton);
    bar.add(jStopButton);
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
