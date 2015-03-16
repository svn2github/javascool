/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package org.javascool.proglets.paintBrush;

// Used to define the gui
import javax.swing.JPanel;
import org.javascool.macros.Macros;

/** Définit le panneau graphique de la proglet qui permet de faire des tracés dans une image.
 *
 * @see <a href="Panel.java.html">code source</a>
 * @see <a href="PaintBrushMain.java.html">PaintBrushMain.java</a>, <a href="PaintBrushImage.java.html">PaintBrushImage.java</a>,  <a href="PaintBrushManipImage.java.html">PaintBrushManipImage.java</a>
 * @serial exclude
 */
public class Panel extends JPanel {
  private static final long serialVersionUID = 1L;
  // @bean
  public Panel() {
    add(mainPanel = new MainPanel());
  }
  /** Registers a new ManipImage implementation. */
  void setManipImage(PaintBrushManipImage manipImage) {
    System.out.println("Le mode proglet a été mis à jour pour prendre en compte vos modifications");
    MyPanel.progletManipImage = manipImage;
    if(MyPanel.manipImage != MyPanel.demoManipImage) {
      MyPanel.manipImage = manipImage;
    }
  }
  private MainPanel mainPanel;
}
