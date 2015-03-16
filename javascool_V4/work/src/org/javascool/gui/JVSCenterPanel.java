package org.javascool.gui;

import javax.swing.JSplitPane;

/** Le double pannau central de Java's cool
 * Le JVSCenterPanel est utilisé pour plaçer correctemment le JVSWidgetPanel à
 * droite et le JVSFileTabs à gauche
 * @see org.javascool.gui.JVSFileTabs
 * @see org.javascool.gui.JVSWidgetPanel
 */
class JVSCenterPanel extends JSplitPane {
  private static final long serialVersionUID = 1L;
  /** L'instance en cour de cette classe */
  private static JVSCenterPanel jvssplitpane;

  /** Retourne l'instance actuelle de la classe */
  public static JVSCenterPanel getInstance() {
    if(jvssplitpane == null) {
      jvssplitpane = new JVSCenterPanel();
    }
    return jvssplitpane;
  }
  /** Constructeur de la classe */
  private JVSCenterPanel() {
    super(JSplitPane.HORIZONTAL_SPLIT);
    this.setLeftComponent(JVSFileTabs.getInstance());
    this.setRightComponent(JVSWidgetPanel.getInstance());
    this.setVisible(true);
    this.validate();
  }
}
