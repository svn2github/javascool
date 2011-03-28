/*******************************************************************************
* David.Pichardie@inria.fr, Copyright (C) 2011.           All rights reserved. *
*******************************************************************************/

package proglet.paintbrush;

// Used to define the gui
import javax.swing.JPanel;
import proglet.ingredients.Console;

/** Définit une proglet javascool qui permet de faire des tracés dans une image.
 * @see <a href="doc-files/about-proglet.htm">Description</a>
 * @see <a href="doc-files/the-proglet.htm">La proglet</a>
 * @see <a href="PaintBrush.java.html">code source</a>
 * @see <a href="PaintBrushMain.java.html">PaintBrushMain.java</a>, <a href="PaintBrushImage.java.html">PaintBrushImage.java</a>,  <a href="PaintBrushManipImage.java.html">PaintBrushManipImage.java</a>
 * @serial exclude
 */
public class PaintBrush implements org.javascool.Proglet {
  private PaintBrush() {}
  private static final long serialVersionUID = 1L;
  // This defines the panel to display
  private static class Panel extends JPanel {
    private static final long serialVersionUID = 1L;
    public Panel() {
      add(mainPanel = new MainPanel());
    }
    /** Registers a new ManipImage implementation. */
    void setManipImage(PaintBrushManipImage manipImage) {
      System.out.println("Le mode proglet a été mis à jour pour prendre en compte vos modifications");
      mainPanel.myPanel.progletManipImage = manipImage;
      if(mainPanel.myPanel.manipImage != mainPanel.myPanel.demoManipImage)
        mainPanel.myPanel.manipImage = manipImage;
    }
    private MainPanel mainPanel;
  }

  //
  // This defines the javascool interface
  //

  /** Définit le mécanisme de dessin de l'image à partir d'un codage de l'utilisateur.
   * @param manipImage L'implémentation de dessin à utiliser.
   */
  public static void setManipImage(PaintBrushManipImage manipImage) {
    panel.setManipImage(manipImage);
  }
  public static int getPixel(int x, int y) {
    return PaintBrushImage.getPixel(x, y);
  };

  public static void setPixel(int x, int y, int col) {
    PaintBrushImage.setPixel(x, y, col);
  };

  //
  // This defines the tests on the panel
  //
  /*public*/ static void test() {
    Console.clear();
    Console.println("Pour la démo . . à vous de manipuler l'interface\n .. en \"mode démo\", tout simplement !");
  }
  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}

