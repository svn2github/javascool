/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2004.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used to build the gui
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

// Used to manage links
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.net.URL;
import java.util.Vector;
import java.net.URLEncoder;
import java.net.URLDecoder;
import javax.swing.text.Document;

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

  /** The Html Display pane. */
  private JEditorPane pane;
  /** The navigation buttons. */
  private JButton home, prev, next;
  {
    // Builds the GUI
    setLayout(new BorderLayout());
    JToolBar bar = new JToolBar();
    bar.setOrientation(JToolBar.HORIZONTAL); bar.setBorderPainted(false); bar.setFloatable(false);
    bar.add(home = new JButton("Page initiale", Utils.getIcon("org/javascool/doc-files/icones16/refresh.png")));
    bar.add(prev = new JButton("Page précédente", Utils.getIcon("org/javascool/doc-files/icones16/prev.png")));
    bar.add(next = new JButton("Page suivante", Utils.getIcon("org/javascool/doc-files/icones16/next.png")));
    add(bar, BorderLayout.NORTH);
    pane = new JEditorPane();
    pane.setEditable(false);
    pane.setContentType("text/html");
    pane.addHyperlinkListener(new HyperlinkListener() {
	public void hyperlinkUpdate(HyperlinkEvent e) { if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) { load(e.getDescription()); } } });
    JScrollPane spane = new JScrollPane(pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(spane, BorderLayout.CENTER);
    // Defines the backward/forward key-stroke and buttons
    {
      AbstractAction doHome, doPrev, doNext;
      pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, KeyEvent.CTRL_MASK), "home");
      pane.getActionMap().put("home",  doHome = new AbstractAction("home") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) { 
	    if(urls.hasHome()) load(urls.home().toString(), false);
	    updateButtons();
	  }});
      home.addActionListener(doHome); home.setEnabled(false);
      pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK), "backward");
      pane.getActionMap().put("backward",  doPrev = new AbstractAction("backward") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) { 
	    if(urls.hasPrev()) load(urls.prev().toString(), false);
	    updateButtons();
	  }});
      prev.addActionListener(doPrev); prev.setEnabled(false);
      pane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK), "forward");
      pane.getActionMap().put("forward",  doNext = new AbstractAction("forward") {
	  private static final long serialVersionUID = 1L;
	  public void actionPerformed(ActionEvent e) { 
	    if(urls.hasNext()) load(urls.next().toString(), false);
	    updateButtons();
	  }});
      next.addActionListener(doNext); next.setEnabled(false);
    }
  }
  private void updateButtons() {
    home.setEnabled(urls.hasHome());
    prev.setEnabled(urls.hasPrev());
    next.setEnabled(urls.hasNext());
  }

  /** Sets the HTML text to show and return this.
   * - Called to show a HTML string.
   * @param text The HTML text location to show.
   * @return This, allowing to use the <tt>new HtmlDisplay().setText(..)</tt> construct.
   */
  public HtmlDisplay reset(String text) { 
    try { load(prefix+URLEncoder.encode(text, "UTF-8"), true); } catch(Exception e) { }
    return this;
  }
  static private final String prefix = "file://string/?value=:";

  /** Sets the HTML text to show and return this.
   * - Called when a link is clicked in the page.
   * @param location The HTML text location to show.
   * @return This, allowing to use the <tt>new HtmlDisplay().loads(..)</tt> construct.
   */
  public HtmlDisplay load(String location) { 
    return load(location, true);
  }
  private HtmlDisplay load(String location, boolean stack) { 
    try {
      URL url = urls.empty() ? Utils.toUrl(location) : new URL(urls.current(), location);
      if (stack) urls.push(url);
      updateButtons();
      if (urls.current().toString().startsWith(prefix)) {
	pane.setText(URLDecoder.decode(urls.current().toString().substring(prefix.length()), "UTF-8"));
      } else {
	pane.getDocument().putProperty(Document.StreamDescriptionProperty, null);
	pane.setPage(urls.current());
      }
    } catch(Exception e) {
      pane.setText("Upps : erreur de lien internet «"+e.toString()+"»");
    }
    return this;
  }
  /** Defines the URL backward/forward mechanism. */
  private class Stack extends Vector<URL> {
    private static final long serialVersionUID = 1L;
    /** Current index in the URL vector. */
    private int current = -1;
    /** Returns the current URL, if any. */
    public URL current() { return current < 0 ? null : get(current); }
    /** Checks if the statck is empty. */
    public boolean empty() { return size() == 0; }
    /** Pushs an URL in the stack. */
    public void push(URL url) { setSize((++current)+1); set(current, url); }
    /** Checks if there is a home page. */
    public boolean hasHome() { return current >= 0; }
    /** Returns the home URL, if any. */
    public URL home() { if(hasHome()) current = 0; return current(); }
    /** Checks if there is a previous page. */
    public boolean hasPrev() { return current > 0; }
    /** Returns the previous URL, if any. */
    public URL prev() { if(hasPrev()) current--; return current(); }
    /** Checks if there is a next page. */
    public boolean hasNext() { return current < size() - 1; }
    /** Returns the next URL, if any. */
    public URL next() { if (hasNext()) current++; return current(); }
    /** Dumps the stack (for debug) */
    private void dump() { for (int i = 0; i < size(); i++) System.err.println((i == current ? "*" : " ")+" "+get(i)); System.err.println(); }
  }
  /** The URL stack. */
  private Stack urls = new Stack();
  
  /** Shows a HTML3.2 page.
   * @param usage <tt>java org.javascool.HtmlDisplay location</tt>
   */
  public static void main(String[] usage) {
    if (usage.length > 0) Utils.show(new HtmlDisplay().load(usage[0]), "Html3.2 browser", 1024, 800);
  }
}


