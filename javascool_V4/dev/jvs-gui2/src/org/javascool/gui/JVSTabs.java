package org.javascool.gui;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.javascool.macros.Macros;

/**
 * Create a tab structure Java's cool Tab Structure is used to make easy
 * JTabbedPane
 *
 * @author Philippe VIENNE
 */
class JVSTabs extends JTabbedPane {
  private static final long serialVersionUID = 1L;

  /** Contain JPanels opened */
  protected HashMap<String, JPanel> tabs = new HashMap<String, JPanel>();

  /**
   * Add a tab with a JPanel
   *
   * @param name
   *            The tab name
   * @param icon
   *            The link to the icon, can be an empty String
   * @param panel
   *            The JPanel to show into the tab
   * @return The new id of your tab
   */
  public String add(String name, String icon, JPanel panel) {
    return this.add(name, icon, panel, null);
  }
  /**
   * Add a tab with an Applet
   *
   * @param name
   *            The tab name
   * @param icon
   *            The link to the icon, can be an empty String
   * @param panel
   *            The Applet to show into the tab
   * @return The new id of your tab
   */
  public String add(String name, String icon, Component panel) {
    tabs.put(name, new JPanel());
    if(!icon.equalsIgnoreCase("")) {
      ImageIcon logo = Macros.getIcon(icon);
      this.addTab(name, logo, panel);
    } else {
      this.addTab(name, null, panel);
    }
    this.revalidate();
    return name;
  }
  /**
   * Add a tab with a JPanel
   *
   * @param name
   *            The tab name
   * @param icon
   *            The link to the icon, can be an empty String
   * @param panel
   *            The JPanel to show into the tab
   * @param tooltip
   *            An tooltip for the tab
   * @return The new id of your tab
   */
  public String add(String name, String icon, JPanel panel, String tooltip) {
    tabs.put(name, panel);
    if(!icon.equalsIgnoreCase("")) {
      ImageIcon logo = Macros.getIcon(icon);
      this.addTab(name, logo, panel, tooltip);
    } else {
      this.addTab(name, null, panel, tooltip);
    }
    this.revalidate();
    return name;
  }
  /**
   * Get a JPanel
   *
   * @param name
   *            The id of JPanel
   * @return The JPanel
   */
  public JPanel getPanel(String name) {
    return this.tabs.get(name);
  }
  /**
   * Delete a tab
   *
   * @param name
   *            The tab id
   */
  public void del(String name) {
    this.removeTabAt(this.indexOfTab(name));
    tabs.remove(name);
  }
  /**
   * Switch to a tab
   *
   * @param name
   *            The id of the tab
   */
  public void switchToTab(String name) {
    this.setSelectedIndex(this.indexOfTab(name));
  }
}
