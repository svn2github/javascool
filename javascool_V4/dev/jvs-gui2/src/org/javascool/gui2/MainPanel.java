/**
 *
 */
package org.javascool.gui2;

import javax.swing.JSplitPane;

/** Paneau principale de Java's cool.
 * Il sépart l'environement en deux avec à gauche un IdePanel et à droite un WidgetPanel
 * @since 4.5
 */
public class MainPanel extends JSplitPane {
  private static final long serialVersionUID = 5739879465559058517L;

  /** Construit le MainPanel.
   *
   */
  public MainPanel() {
    super(JSplitPane.HORIZONTAL_SPLIT);
    setLeftComponent(IdePanel.getInstance());
    setRightComponent(MediaPanel.getInstance());
  }
  /** Instance de la classe. */
  private static MainPanel panel;

  /** Permet d'avoir une instance unique de la classe.*/
  public static MainPanel getInstance() {
    if(MainPanel.panel == null) {
      MainPanel.panel = new MainPanel();
    }
    return MainPanel.panel;
  }
}
