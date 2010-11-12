/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distributed on GNU General Public Licence    |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JApplet;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;
import javax.swing.JButton;
import java.awt.Container;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 

// Used to register elements
import java.util.HashMap;

/** Defines the general layout of the JavaScool interface in terms of a button toolbar and splitted tab panes.
 * @author Philippe Vienne <philoumailabo@gmail.com>
 * @see <a href="JsLayout.java.html">source code</a>
 * @serial exclude
 */
public class JsLayout extends JApplet { 
  /**/public JsLayout() { }
  private static final long serialVersionUID = 1L;

  // Defines the main panel and defines how to edit the toolbar, activityList and tabbedpane
  private JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
  private JTabbedPane westPane = new JTabbedPane(), eastPane = new JTabbedPane();
  private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, westPane, eastPane);

  /** Builds the GUI. */
  {
    getContentPane().add(toolBar, BorderLayout.NORTH);
    westPane.setMinimumSize(new Dimension(100, 100));
    eastPane.setMinimumSize(new Dimension(100, 100));
    splitPane.setResizeWeight(0.5);
    splitPane.setContinuousLayout(true); 
    splitPane.setOneTouchExpandable(false);
    getContentPane().add(splitPane, BorderLayout.CENTER);
  }

  /** Resets the GUI. 
   * @param logoFile If not null, displays a logo to label the interface.
   */
  public void reset(String logoFile) {
    toolBar.removeAll();
    westPane.removeAll();
    eastPane.removeAll();
    if (logoFile != null)  
      toolBar.add(new JLabel(Utils.getIcon(logoFile)));
  }

  /** Adds a button to the toolbar.
   * @param label Button label.
   * @param icon Button icon. If null do not show icon.
   * @param action Button action.
   */
  public void addTool(String label, String icon, Runnable action) { 
    JButton button = icon == null ? new JButton(label) : new JButton(label, Utils.getIcon(icon));
    button.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	  actions.get(((JButton) e.getSource()).getText()).run();
	}});
    toolBar.add(button);
    buttons.put(label, button);
    actions.put(label, action);
    toolBar.revalidate();
  }
  /** Removes a button from the tool bar. */
  public void delTool(String label) {
    if (buttons.containsKey(label)) {
      toolBar.remove(buttons.get(label));
      buttons.remove(label);
      actions.remove(label);
      toolBar.revalidate();
    }
  }
  /** Adds a separator on the toolbar. */
  public void addToolSeparator() {
    toolBar.addSeparator();
  }
  /** HashMap for action list.
   * The map associate a String to a Runnable
   */
  private HashMap<String,Runnable> actions = new HashMap<String,Runnable>();
  /** HashMap for button list.
   * The map associate a String to a JButton
   */
  private HashMap<String,JButton> buttons = new HashMap<String,JButton>();
  /** Adds a tab to the tabbed panel.
   * @param label Tab label.
   * @param pane Tab panel.
   * @param icon Location of the icon for the tab. If null, no icon.
   * @param east If true use the east pane, else the west pane.
   */
  public void addTab(String label, Container pane, String icon, boolean east) {
    if (east) { 
      eastPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
      eastPane.revalidate();
      etabs.put(label, pane);
    } else {
      westPane.addTab(label, icon == null ? null : Utils.getIcon(icon), pane, label);
      westPane.revalidate();
      wtabs.put(label, pane);
    }
    splitPane.setResizeWeight(eastPane.getTabCount() == 0 ? 1 : westPane.getTabCount() == 0 ? 0 : 0.5);
    splitPane.revalidate();
  }
  /** Adds a tab to the tabbed panel to display a text.
   * @param label Tab label.
   * @param location Tab Html text to display.
   * @param icon Location of the icon for the tab. If null, no icon.
   * @param east If true use the east pane, else the west pane.
   */
  public void addTab(String label, String location, String icon, boolean east) {
    addTab(label, new HelpDisplay().load(location),icon,east);
  }
  /** A display with external links canceled. */
  private class HelpDisplay extends HtmlDisplay {
    private static final long serialVersionUID = 1L;
    public HtmlDisplay load(String location) { 
      if(location.matches("^(http|https|rtsp|mailto):.*$")) {
	try {
	  java.awt.Desktop.getDesktop().browse(new java.net.URI(location));
	} catch(Exception e) {
	  reset("Cette page est à l'adresse internet: <tt>"+location.replaceFirst("^(mailto):.*", "$1: ...")+"</tt> (non accessible ici).");  
	}
      } else {
        super.load(location);
      }
      return this;
    }
  }
  /** Removes a tab from the tabbed panel.
   * @param label Tab label.
   */
  public void delTab(String label) {
    if (etabs.containsKey(label)) {
      eastPane.remove(etabs.get(label));
      eastPane.revalidate();    
      etabs.remove(label);
    } 
    if (wtabs.containsKey(label)) {
      westPane.remove(wtabs.get(label));
      westPane.revalidate();
      wtabs.remove(label);
    }
  }
  /** Makes a tab from the tabbed panel visible.
   * @param label Tab label.
   */
  public void showTab(String label) {
    if (etabs.containsKey(label)) {
      eastPane.setSelectedComponent(etabs.get(label));
      eastPane.revalidate();
    }
    if (wtabs.containsKey(label)) {
      westPane.setSelectedComponent(wtabs.get(label));
      westPane.revalidate();
    }
  }
  /** HashMap for west and east tab list.
   * The map associate a String to a JPanel
   */
  private HashMap<String,Container> wtabs = new HashMap<String,Container>(), etabs = new HashMap<String,Container>();
}
