/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2005.  All rights reserved. *
*******************************************************************************/

package org.javascool.builder;

// Used in check the link
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javascool.tools.FileManager;

/**
 * Permet de vérifier les liens cassés sur un site.
 *
 * @see <a href="LinkCheck.java.html">source code</a>
 * @serial exclude
 */
public class LinkCheck {
  private LinkCheck() {}
  /**
   * Lanceur du test des liens cassés.
   *
   * @param usage
   *            <tt>java org.javascool.tools.LinkCheck [-recursive] location</tt>
   *            <p>
   *            Teste les liens d'une adresse web, avec récursion à travers
   *            les sous-pages si besoin.
   *            </p>
   */
  public static void main(String usage[]) {
    if(usage.length > 0) {
      LinkCheck.check(usage[usage.length - 1],
                      "-recursive".equals(usage[0]));
    }
  }
  /**
   * Teste les liens d'une adresse web et donne les liens cassés à la console.
   *
   * @param location
   *            L'adresse web à tester.
   * @param recursive
   *            Si true, effectue récursion à travers les sous-pages.
   *
   * @throws IllegalArgumentException
   *             Si l'URL est mal formée.
   */
  public static void check(String location, boolean recursive) {
    LinkCheck.echoBroken("LINKCHECK: " + LinkCheck.getRoot(location));
    LinkCheck.links = new HashSet<String>();
    LinkCheck.anchors = new HashMap<String, HashSet<String> >();
    LinkCheck.root = LinkCheck.getRoot(location);
    LinkCheck.loop = recursive;
    LinkCheck.npages = LinkCheck.nlinks = LinkCheck.nbrokens = 0;
    LinkCheck.check(location);
    LinkCheck.echoBroken("  SCANNED PAGES: " + LinkCheck.npages
                         + " SCANNED LINKS: " + LinkCheck.nlinks + " BROKENS LINKS: "
                         + LinkCheck.nbrokens);
    LinkCheck.links = null;
    LinkCheck.anchors = null;
  }
  private static HashSet<String> links;
  private static HashMap<String, HashSet<String> > anchors;
  private static String root;
  private static boolean loop;
  private static int npages, nlinks, nbrokens;

  private static void check(String location) {
    try {
      String text = FileManager.load(location);
      LinkCheck.npages++;
      for(String href : LinkCheck.getLinks(text))
        if(!href.matches("^(https|javascript|rtsp|mailto):.*$")) {
          try {
            href = new URL(new URL(location), href.replaceAll(
                             "%2e", ".")).toString();
            if(!LinkCheck.links.contains(href)) {
              LinkCheck.links.add(href);
              LinkCheck.nlinks++;
              String anchor = null;
              int i = href.indexOf("#");
              if(i != -1) {
                anchor = href.substring(i + 1);
                href = href.substring(0, i);
              }
              if(!FileManager.exists(href)) {
                LinkCheck.echoBroken("BROKEN  Link in "
                                     + location + " -> " + href);
              } else {
                if(LinkCheck.loop
                   && href.startsWith(LinkCheck.root)
                   && href.matches("^http:.*([?][^?]*|/|\\.(htm|html|shtml|php))$")
                   && !href.matches("^.*\\.(xslt|java)"))
                {
                  LinkCheck.check(href);
                }
                if(anchor != null) {
                  if(!LinkCheck.anchors.containsKey(href)) {
                    LinkCheck.anchors.put(href, LinkCheck
                                          .getAnchorSet(FileManager
                                                        .load(href)));
                  }
                  if(!LinkCheck.anchors.get(href).contains(
                       anchor))
                  {
                    LinkCheck
                    .echoBroken("BROKEN  Anchor in "
                                + location
                                + " -> "
                                + href + " #" + anchor);
                  }
                }
              }
            }
          } catch(Exception e) {
            LinkCheck.echoBroken("SPURIOUS Link in " + location
                                 + " -> " + href + " (" + e + ") ");
          }
        }
    } catch(Exception e) {
      LinkCheck.echoBroken("SPURIOUS   " + location + " (" + e + ") ");
    }
  }
  private static String getRoot(String location) {
    try {
      return new URI(location).normalize().toString();
    } catch(Exception e) {
      return location;
    }
  }
  private static void echoBroken(String message) {
    System.out.println(message);
    LinkCheck.nbrokens++;
  }
  /**
   * Renvoie les ancres d'un texte HTML. <div> Ici les attributs
   * <tt>&lt;a href=..</tt> <tt>&lt;img src=..</tt> sont considérés comme des
   * liens.</div>
   */
  public static String[] getLinks(String text) {
    HashSet<String> h = new HashSet<String>();
    for(int i = 0, l = text.length(); i < l;) {
      Matcher matcher = LinkCheck.linkPattern.matcher(text).region(i, l);
      if(matcher.find()) {
        int i1 = matcher.end(), i2 = LinkCheck.nextQuote(text, i1);
        String link = text.substring(i1, i2);
        if(!link.startsWith("'")) {
          h.add(link);
        }
        i = i2;
      } else {
        break;
      }
    }
    return h.toArray(new String[h.size()]);
  }
  /**
   * Renvoie les ancres d'un texte HTML. <div> Ici les attributs
   * <tt>&lt;a name=..</tt> et <tt>&lt; id=..</tt> sont considérés comme des
   * ancres.</div>
   */
  public static String[] getAnchors(String text) {
    HashSet<String> a = LinkCheck.getAnchorSet(text);
    return a.toArray(new String[a.size()]);
  }
  private static HashSet<String> getAnchorSet(String text) {
    HashSet<String> a = new HashSet<String>();
    for(int i = 0, l = text.length(); i < l;) {
      Matcher matcher = LinkCheck.anchorPattern.matcher(text)
                        .region(i, l);
      if(matcher.find()) {
        int i1 = matcher.end(), i2 = LinkCheck.nextQuote(text, i1);
        a.add(text.substring(i1, i2));
        i = i2;
      } else {
        break;
      }
    }
    return a;
  }
  private static int nextQuote(String text, int i1) {
    char c = text.charAt(i1 - 1);
    int i2 = text.indexOf(c, i1);
    return i2 == -1 ? text.length() : i2;
  }
  private static final Pattern linkPattern = Pattern
                                             .compile("(href|HREF|src|SRC)\\s*=\\s*[\"']");
  private static final Pattern anchorPattern = Pattern
                                               .compile("<([aA]\\s+(name|NAME)|[^>]+(id|ID))\\s*=\\s*[\"']");
}
