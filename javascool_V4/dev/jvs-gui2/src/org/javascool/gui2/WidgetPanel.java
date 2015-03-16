package org.javascool.gui2;

import javax.swing.JScrollPane;

import org.javascool.Core;
import org.javascool.core.Proglet;
import org.javascool.core.ProgletEngine;
import org.javascool.macros.Macros;
import org.javascool.widgets.Console;
import org.javascool.widgets.HtmlDisplay;
import org.javascool.widgets.TabbedPane;

/**
 * Le panneau contenant les widgets Les widgets de Java's cool sont disposés
 * dans des onglets. Certain onglets de navigation web peuvent être fermés par
 * une croix.
 *
 * @see org.javascool.widgets.Console
 * @see org.javascool.widgets.HtmlDisplay
 * @see org.javascool.widgets
 */
class WidgetPanel extends TabbedPane {
  private static final long serialVersionUID = 1L;
  private String progletTabId;
  /** Instance du JVSWidgetPanel */
  private static WidgetPanel jwp;

  public static WidgetPanel getInstance() {
    if(WidgetPanel.jwp == null) {
      WidgetPanel.jwp = new WidgetPanel();
    }
    return WidgetPanel.jwp;
  }
  private WidgetPanel() {
    super();
    this.addTab("Console", "", Console.getInstance());
  }
  /**
   * Charge les tabs de la proglet Charge le tab de la proglet (Panel) et
   * l'HTMLDisplay avec le fichier d'aide.
   *
   * @param name
   *            Le nom du package de la proglet
   */
  public void setProglet(String name) {
    this.removeAll();
    this.addTab("Console", "", Console.getInstance());
    Proglet proglet = ProgletEngine.getInstance().setProglet(name);
    if(proglet.getPane() != null) {
      this.addTab("Proglet " + name, "",
                  new JScrollPane(proglet.getPane()));
      this.progletTabId = "Proglet " + name;
    }
    if(proglet.getHelp() != null) {
      this.addTab("Aide de la proglet", "", new HtmlDisplay().setPage(Macros
                                                                      .getResourceURL(proglet.getHelp())));
      this.switchToTab("Aide de la proglet");
    }
    HtmlDisplay memo = new HtmlDisplay();
    memo.setPage(ClassLoader.getSystemResource(Core.help));
    this.addTab("Mémo", "", memo);
  }
  /** Affiche l'onglet de la Proglet si il existe */
  public void focusOnProgletPanel() {
    if(progletTabId != null) {
      this.switchToTab(progletTabId);
    } else {
      this.focusOnConsolePanel();
    }
  }
  /** Affiche la console */
  public void focusOnConsolePanel() {
    this.setSelectedIndex(this.indexOfTab("Console"));
  }
  /**
   * Ouvre un nouvel onglet web Ouvre un nouveau HTMLDisplay dans un onglet.
   * Cet onglet peut être fermer à l'aide de la croix qui se situe à droite du
   * titre de l'onglet.
   *
   * @param url
   *            L'url de la page à charger
   * @param tabName
   *            Le titre du tab à ouvrir
   * @see org.javascool.widgets.HtmlDisplay
   * @see String
   */
  public void openWebTab(String url, String tabName) {
    if(this.indexOfTab(tabName) >= 0) {
      this.switchToTab(tabName);
      return;
    }
    HtmlDisplay memo = new HtmlDisplay();
    memo.setPage(url);
    this.addTab(tabName, "", memo, null, true);
    this.setSelectedComponent(memo);
  }
}
