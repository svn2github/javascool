/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2004.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to build the gui
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

// Used to manage links
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/** Defines a panel in which a HTML text is shown.
 *
 * @see <a href="HtmlDisplay.java.html">source code</a>
 * @serial exclude
 */
public class HtmlDisplay extends JPanel implements Widget { /**/public HtmlDisplay() { }
  private static final long serialVersionUID = 1L;

  private JEditorPane pane; private JScrollPane spane;
  { 
    pane = new JEditorPane();
    pane.setEditable(false);
    pane.setContentType("text/html");
    pane.addHyperlinkListener(new HyperlinkListener() {
    public void hyperlinkUpdate(HyperlinkEvent e) { if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { show(e.getDescription()); } } });
    add(new  JScrollPane(pane));
  }

  /** Sets the HTML text to show and return this.
   * @param text The HTML text to show.
   * @return This, allowing to use the <tt>new HtmlDisplay().reset(..)</tt> construct.
   */
  public HtmlDisplay reset(String text) { 
    pane.setText(text); 
    setMinimumSize(getPreferredSize()); 
    setSize(getPreferredSize()); 
    return this; 
  }

  /** Called when a link is clicked in the page.
   * <div>To be overloaded in order to manage the way link are displayed.</div>
   * <div>The default behavior is to attempt to load the location in the panel.</div>
   */
  public void show(String location) { 
    //try { pane.setPage(location); } catch(Exception e) { pane.setText(e.toString()); } 
    try{pane.setText(Utils.loadString(location));} catch(Exception e) { pane.setText(e.toString()); } 
  }
}


