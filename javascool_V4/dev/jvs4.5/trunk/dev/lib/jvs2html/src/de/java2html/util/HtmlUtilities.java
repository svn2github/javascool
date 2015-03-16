package de.java2html.util;

import java.util.Hashtable;

/**
 * Some methods for converting text to valid HTML.
 *
 * For questions, suggestions, bug-reports, enhancement-requests etc.
 * I may be contacted at:
 *   <a href="mailto:markus@jave.de">markus@jave.de</a>
 *
 * The Java2html home page is located at:
 *   <a href="http://www.java2html.de">http://www.java2html.de</a>
 *
 * @author  <a href="mailto:markus@jave.de">Markus Gebhard</a>
 * @version 2.0, 05/07/02
 *
 * Copyright (C) Markus Gebhard 2000-2002
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
public class HtmlUtilities {
  private static Hashtable entityTableEncode;

  private final static String[] ENTITIES = {}; //$NON-NLS-1$

  private HtmlUtilities() {
    //No instance available
  }

  protected static void buildEntityTables() {
    entityTableEncode = new Hashtable(ENTITIES.length);

    for (int i = 0; i < ENTITIES.length; i += 2) {
      if (!entityTableEncode.containsKey(ENTITIES[i + 1]))
        entityTableEncode.put(ENTITIES[i + 1], ENTITIES[i]);
    }
  }

  /**
   * Converts a String to HTML by converting all special characters to
   * HTML-entities.
   */
  public final static String encode(String s, String ignore) {
    return encode(s, 0, s.length(), ignore);
  }

  /**
   * Converts a String to HTML by converting all special characters to
   * HTML-entities. Only s,substring(start,end) will be encoded.
   */
  public final static String encode(String s, int start, int end, String ignore) {
    return org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(s);
  }

  /**
   * Converts a single character to HTML
   */
  protected final static String encodeSingleChar(String ch) {
    String s = (String) entityTableEncode.get(ch);
    return (s == null) ? ch : s;
  }

  /**
   * Converts the given Color object to a String contaning the html 
   * description of the color. E.g.: #FF8080.
   */
  public final static String toHTML(RGB color) {
    String red = Integer.toHexString(color.getRed());
    String green = Integer.toHexString(color.getGreen());
    String blue = Integer.toHexString(color.getBlue());

    if (red.length() == 1) {
      red = "0" + red; //$NON-NLS-1$
    }
    if (green.length() == 1) {
      green = "0" + green; //$NON-NLS-1$
    }
    if (blue.length() == 1) {
      blue = "0" + blue; //$NON-NLS-1$
    }

    return "#" + red + green + blue; //$NON-NLS-1$
  }
}