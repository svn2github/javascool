/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2004.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

import org.javascool.Utils;

// Used to build the gui
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

/** Defines a tool bar corresponding to a makefile usage.
 *
 * @see <a href="MakeToolBar.java.html">source code</a>
 * @serial exclude
 */
/**/public class MakeToolBar extends JToolBar { /**/public MakeToolBar() { }
  private static final long serialVersionUID = 1L;
  private static final int WIDTH = 500, LENGTH = 256;
  {
    setOrientation(VERTICAL);
    add(new JLabel(Utils.getIcon("http://javascool.gforge.inria.fr/v3/images/logo.gif")));
    String pattern = "\\s*make\\s*([\\S]*)\\s*([:,])\\s*(.*)";
    for(String line : Utils.exec("make usage").split("\n")) if (line.matches(pattern))
      new Button(line.replaceFirst(pattern, "$1"), line.replaceFirst(pattern, "$2").equals(","), line.replaceFirst(pattern, "$3"));
  }	  
  private class Button extends JButton {
    private static final long serialVersionUID = 1L;
    public Button(String action, boolean quit, String title) {
      super(toLength(title));
      addActionListener(new AbstractAction() {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) { 
	    new Thread(new Runnable() { public void run() {
	      System.err.println(Utils.exec(xterm ? "xterm\t-geometry\t128x40-10-10\t-e\tmake "+what+" done" : "make "+what, 0));
	    }}).start();
	  }});
      what = action; xterm = !quit;
      MakeToolBar.this.add(this);
    }
    private String what; private boolean xterm;
  }
  private static String toLength(String title) { while(title.length() < LENGTH) title += " "; return title; }
  /** Shows a toolbar corresponding to a makefile usage.
   * @param usage <tt>java org.javascool.MakeToolBar</tt>
   */
  public static void main(String[] usage) { Utils.show(new MakeToolBar(), "make", WIDTH, 700); }
}


