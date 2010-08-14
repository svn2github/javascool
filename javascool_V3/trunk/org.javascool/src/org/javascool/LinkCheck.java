/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2005.  All rights reserved. *
 *******************************************************************************/

package org.javascool;

// Used for the string interface
import java.lang.String;

// Used in check the link
import java.net.URI;
import java.net.URL;
import java.io.IOException;
import java.net.URISyntaxException;

// Used to cache the links
import java.util.HashSet;
import java.util.HashMap;

// Used to patch href
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** This factory encapsulates a java implemented link check. 
 * @see <a href="LinkCheck.java.html">source code</a>
 * @serial exclude
 */
public class LinkCheck { private LinkCheck() { } 

  /** Checks links in a HTML text.
   * @param usage <tt>java org.javascool.LinkCheck [-recursive] location</tt> to check links at the given HTTP location, and optionnaly recurse in sub-directories.
   */
  public static void main(String usage[]) {
    if (usage.length > 0)
      check(usage[usage.length-1], "-recursive".equals(usage[0]));
  }

  /** Checks links in a HTML text.
   * @param location The HTML URL which links are to be checked.
   * @param recursive If true, links in URL files in sub-directories with HTML extensions are also checked.
   * @throws IllegalArgumentException If the URL is malformed.
   */
  public static void check(String location, boolean recursive) {
    echoBroken("LINKCHECK: "+getRoot(location));
   links = new HashSet<String>(); anchors = new HashMap<String,HashSet<String>>(); root = getRoot(location); loop = recursive; npages = nlinks = nbrokens = 0; 
   check(location);
   echoBroken("  SCANNED PAGES: "+npages+" SCANNED LINKS: "+nlinks+" BROKENS LINKS: "+nbrokens);
   links = null; anchors = null;
  }
  private static HashSet<String> links; private static HashMap<String,HashSet<String>> anchors; 
  private static String root; private static boolean loop; private static int npages, nlinks, nbrokens;
  private static void check(String location) {
    //-System.err.println("Check "+ location);
    try { 
      String text = Utils.loadString(location); npages++;
      for(String href : getLinks(text)) if (!href.matches("^(https|javascript|rtsp|mailto):.*$")) {
	try {
	  href = new URL(new URL(location), href.replaceAll("%2e",".")).toString();
	  if(!links.contains(href)) {
	    links.add(href); nlinks++;
	    String anchor = null; int i = href.indexOf("#"); if (i != -1) { anchor = href.substring(i+1); href = href.substring(0, i); }
	    //-System.err.println(" ? "+ href + (anchor == null ? "" : " #"+ anchor));
	    if (!isReadable(href)) 
	      echoBroken("BROKEN  Link in "+location+" -> "+href);
	    if (loop && href.startsWith(root) && href.matches("^http:.*([?][^?]*|/|\\.(htm|html|shtml|php))$"))
	      check(href);
	    if (anchor != null) {
	      if (!anchors.containsKey(href)) anchors.put(href, getAnchorSet(Utils.loadString(href)));
	      if (!anchors.get(href).contains(anchor))
		echoBroken("BROKEN  Anchor in "+location+" -> "+href+" #"+anchor);
	    }
	  }
	} catch(Exception e) { 
	  echoBroken("SPURIOUS Link in "+location+" -> "+href+" ("+e+") ");
	}
      }
    } catch(Exception e) { 
      echoBroken("SPURIOUS   "+location+" ("+e+") "); 
    }
  }
  private static String getRoot(String location) { try { return new URI(location).normalize().toString(); } catch(Exception e) { return location; } }
  private static void echoBroken(String message) { System.out.println(message); nbrokens++; }

  /** Checks if an URL is readable.
   * @param location The URL to be checked.
   * @return True if readable else false.
   * @throws IllegalArgumentException If the URL is malformed.
   */
  public static boolean isReadable(String location) {
    try {
      URL url = new URL(location); try { url.openStream().close(); return true; } catch(IOException e) { return false; } 
    } catch(IOException e) { 
      throw new IllegalArgumentException("Bad URL "+location+" ("+e+")"); 
    } 
  }

  /** Returns all links in a given HTML text.
   * <div> Here all <tt>&lt;a href=..</tt> <tt>&lt;img src=..</tt> attributes are considered as links.</div>
   */
  public static String[] getLinks(String text) {
    HashSet<String> links = new HashSet<String>();
    for(int i = 0, l = text.length(); i < l;) {
      Matcher matcher = linkPattern.matcher(text).region(i, l);
      if (matcher.find()) {
	int i1 = matcher.end(), i2 = nextQuote(text, i1);
	links.add(text.substring(i1, i2));
	i = i2;
      } else
	break;
    }
    return links.toArray(new String[links.size()]);
  }
  /** Returns all anchors in a given HTML text.
   * <div> Here all <tt>&lt;a name=..</tt> <tt>&lt; id=..</tt> attributes are considered as anchors.</div>
   */
  public static String[] getAnchors(String text) {
    HashSet<String> anchors = getAnchorSet(text);
    return anchors.toArray(new String[anchors.size()]);
  }
  private static HashSet<String> getAnchorSet(String text) {
    HashSet<String> anchors = new HashSet<String>();
    for(int i = 0, l = text.length(); i < l;) {
      Matcher matcher = anchorPattern.matcher(text).region(i, l);
      if (matcher.find()) {
	int i1 = matcher.end(), i2 = nextQuote(text, i1);
	anchors.add(text.substring(i1, i2));
	i = i2;
      } else
	break;
    }
    return anchors;
  }
  private static int nextQuote(String text, int i1) {
    char c = text.charAt(i1-1); int i2 = text.indexOf(c, i1); return i2 == -1 ? text.length() : i2;
  }
  private static final Pattern linkPattern = Pattern.compile("(href|HREF|src|SRC)\\s*=\\s*[\"']");  
  private static final Pattern anchorPattern = Pattern.compile("<([aA]\\s+(name|NAME)|[^>]+(id|ID))\\s*=\\s*[\"']");
}
