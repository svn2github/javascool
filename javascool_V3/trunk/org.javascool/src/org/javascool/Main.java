/********************************************************************************
 * Philippe.Vienne@linux-azur.org, Copyright (C) 2010. All gnu rights reserved. *
 ********************************************************************************/

package org.javascool;

import javax.swing.JApplet;

/** This is the javascool v3 interface starter.
 * <p>- It can be used either as standalone application or a certified applet.</p>
 * @author Philippe Vienne
 * @see <a href="../../org/javascool/Main.java">source code</a>
 */
public class Main extends JApplet {
  private static final long serialVersionUID = 1L;

  /** Used to run a javasccol v3 as a standalone program. 
   * <p>- Using javascool means: doing an "activity" which result is to be stored in a "file-name".</p>
   * @param args usage <tt>java org.javascool.Main [activity [file-name]]</tt><ul>
   * <li><tt>activity</tt> specifies the activity to be done.</li>
   * <li><tt>file-name</tt> specifies the file used for the activity.</li>
   * </ul>
   */
  public static void main(String[] args) {
    System.out.println("Hi !");
  }
}
