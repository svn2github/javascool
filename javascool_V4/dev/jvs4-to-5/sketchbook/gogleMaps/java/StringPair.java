package org.javascool.proglets.gogleMaps;

import java.util.AbstractMap;

/** Définit une paire de String. */
public class StringPair extends AbstractMap.SimpleEntry<String, String>{
  private static final long serialVersionUID = 1L;
  public StringPair(String s1, String s2) {
    super(s1, s2);
  }
}
