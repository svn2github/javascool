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
 * @see <a href="Console.java.html">code source</a>
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
    void setManipImage(ManipImage manipImage) {
      System.out.println("L'implémentation de la proglet paintbrush a été changée");
      mainPanel.myPanel.progletManipImage = manipImage;
    }
    private MainPanel mainPanel;
  }

  //
  // This defines the javascool interface
  //
  /** Définit le mécanisme de dessin de l'image à partir d'un codage de l'utilisateur.
   * @param manipImage L'implémentation de dessin à utiliser.
   */
  public static void setManipImage(ManipImage manipImage) {
    panel.setManipImage(manipImage);
  }

  //
  // This defines the tests on the panel
  //
  /**/public static void test() {
    Console.clear();
    Console.println("Pour la démo . . à vous de manipuler l'interface\n .. en \"mode démo\", tout simplement !");
  }
  /** Définition de l'interface graphique de la proglet. */
  public static final Panel panel = new Panel();
}

