/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2004.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to build the gui
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

// Used to manage links
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.net.URL;
import java.util.Vector;

// Used to manage keystroke
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Defines a panel in which a HTML text is shown.
 *
 * @see <a href="HtmlDisplay.java.html">source code</a>
 * @serial exclude
 */
public class HtmlDisplay extends JPanel implements Widget { /**/public HtmlDisplay() { }
  private static final long serialVersionUID = 1L;

  private JTextPane pane;
  { 
    pane = new JTextPane();
    pane.setSize(1024, 2000);
    pane.setEditable(false);
    pane.setContentType("text/html");
    pane.addHyperlinkListener(new HyperlinkListener() {
	public void hyperlinkUpdate(HyperlinkEvent e) { if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { load(e.getDescription()); } } });
    JScrollPane spane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    spane.setSize(1024, 400);
    add(spane);
  }

  /** Sets the HTML text to show and return this.
   * @param text The HTML text to show.
   * @return This, allowing to use the <tt>new HtmlDisplay().reset(..)</tt> construct.
   */
  public HtmlDisplay reset(String text) { 
    pane.setText("<html><head></head><body width='"+(pane.getWidth()-40)+"' height='2000'>"+text+"</body></html>"); 
    return this; 
  }

  /** Sets the HTML text to show and return this.
   * - Called when a link is clicked in the page.
   * @param location The HTML text location to show.
   * @return This, allowing to use the <tt>new HtmlDisplay().loads(..)</tt> construct.
   */
  public HtmlDisplay load(String location) { 
    return load(location, true);
  }
  /**/public HtmlDisplay load(String location, boolean stack) { 
    try {
      URL url = urls.empty() ? Utils.toUrl(location) : new URL(urls.current(), location);
      if (stack) urls.push(url);
      System.err.println("HtmlDisplay #"+urls.current+" : "+urls.current());
      return reset(Utils.loadString(urls.current().toString()));
    } catch(Exception e) {
      e.printStackTrace();
      return reset(e.toString());
    }
  }
  // Defines the URL backward/forward mechanism
  private static class Stack extends Vector<URL> {
    private static final long serialVersionUID = 1L;
    // Current index in the URL vector
    private int current = -1;
    /** Returns the current URL, if any. */
    public URL current() { return current < 0 ? null : get(current); }
    /** Checks if the statck is empty. */
    public boolean empty() { return size() == 0; }
    /** Pushs an URL in the stack. */
    public void push(URL url) { setSize((++current)+1); set(current, url); }
    /** Returns the previous URL, if any. */
    public URL prev() { if(current > 0) current--; return current(); }
    /** Returns the next URL, if any. */
    public URL next() { if(current < size() - 1) current++; return current(); }
  }
  private Stack urls = new Stack();

  // Defines the backward/forward key-stroke
  {
    pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_MASK), "backward");
    pane.getActionMap().put("backward",  new AbstractAction("backward") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if(urls.prev() != null) load(urls.current().toString(), false);
	}});
    pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_MASK), "forward");
    pane.getActionMap().put("forward",  new AbstractAction("forward") {
	private static final long serialVersionUID = 1L;
	public void actionPerformed(ActionEvent e) { 
	  if(urls.next() != null) load(urls.current().toString(), false);
	}});
  }
  
  /** Shows a HTML3.2 page.
   * @param usage <tt>java org.javascool.HtmlDisplay location</tt>
   */
  public static void main(String[] usage) {
    if (usage.length > 0) Utils.show(new HtmlDisplay().load(usage[0]), "Html3.2 browser", 1024, 400);
  }
}


