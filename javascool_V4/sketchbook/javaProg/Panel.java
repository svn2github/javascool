/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2009.  All rights reserved . *
*******************************************************************************/

package org.javascool.proglets.javaProg;

import javax.swing.JLayeredPane;

/** Définit une proglet javascool qui permet d'utiliser toute les classes des swings.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @serial exclude
 */
public class Panel extends JLayeredPane {
  private static final long serialVersionUID = 1L;

  // @bean
  public Panel() { setOpaque(true); }
}

