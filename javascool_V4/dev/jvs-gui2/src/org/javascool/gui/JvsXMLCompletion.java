/*
 *  Edited from JvsXMLCompletion.java - Parses XML representing code completion for a
 * C-like language.
 * Copyright (C) 2010 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 */
package org.javascool.gui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.FunctionCompletion;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.javascool.macros.Macros;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parser for an XML file describing a procedural language such as JVS.
 * <p>
 *
 * @version 1.0
 */
class JvsXMLCompletion extends DefaultHandler {
  /**
   * The completions found after parsing the XML.
   */
  private List<Completion> completions;

  /**
   * The provider we're getting completions for.
   */
  private CompletionProvider provider;

  /**
   * The completion provider to use when loading classes, such as custom
   * {@link FunctionCompletion}s.
   */
  private ClassLoader completionCL;
  private String name;
  private StringBuffer desc;
  private String shortDesc;
  private StringBuffer code;
  private boolean doingKeywords;
  private boolean inKeyword;
  private boolean gettingDoc;
  private boolean gettingCode;
  private boolean inCompletionTypes;

  /**
   * The class loader to use to load custom completion classes, such as the
   * one defined by {@link #funcCompletionType}. If this is <code>null</code>,
   * then a default class loader is used. This field will usually be
   * <code>null</code>.
   */
  private static ClassLoader DEFAULT_COMPLETION_CLASS_LOADER;

  /**
   * Constructor.
   *
   * @param provider
   *            The provider to get completions for.
   * @see #reset(CompletionProvider)
   */
  public JvsXMLCompletion(CompletionProvider provider) {
    this(provider, null);
  }
  /**
   * Constructor.
   *
   * @param provider
   *            The provider to get completions for.
   * @param cl
   *            The class loader to use, if necessary, when loading classes
   *            from the XML (custom {@link FunctionCompletion}s, for
   *            example). This may be <code>null</code> if the default is to
   *            be used, or if the XML does not define specific classes for
   *            completion types.
   * @see #reset(CompletionProvider)
   */
  public JvsXMLCompletion(CompletionProvider provider, ClassLoader cl) {
    this.provider = provider;
    this.completionCL = cl;
    if(completionCL == null) {
      // May also be null, but that's okay.
      completionCL = JvsXMLCompletion.DEFAULT_COMPLETION_CLASS_LOADER;
    }
    completions = new ArrayList<Completion>();
    desc = new StringBuffer();
    code = new StringBuffer();
  }
  /**
   * Called when character data inside an element is found.
   */
  @Override
  public void characters(char[] ch, int start, int length) {
    if(gettingDoc) {
      desc.append(ch, start, length);
    }
    if(gettingCode) {
      code.append(ch, start, length);
    }
  }
  private BasicCompletion createCompletion() {
    BasicCompletion bc = new BasicCompletion(provider, name);
    if(code.length() > 0) {
      bc = new ShorthandCompletion(provider, name, code.toString());
      code = new StringBuffer();
    }
    if(desc.length() > 0) {
      bc.setSummary(desc.toString());
      desc = new StringBuffer();
    }
    if(shortDesc.length() > 0) {
      bc.setShortDescription(shortDesc.toString());
      shortDesc = "";
    }
    return bc;
  }
  /**
   * Called when an element is closed.
   */
  @Override
  public void endElement(String uri, String localName, String qName) {
    if("keywords".equals(qName)) {
      doingKeywords = false;
    } else if(doingKeywords) {
      if("keyword".equals(qName)) {
        Completion c = null;
        c = this.createCompletion();
        completions.add(c);
        inKeyword = false;
      } else if(inKeyword) {
        if("doc".equals(qName)) {
          gettingDoc = false;
        }
        if("code".equals(qName)) {
          gettingCode = false;
        }
      }
    } else if(inCompletionTypes) {
      if("completionTypes".equals(qName)) {
        inCompletionTypes = false;
      }
    }
  }
  /**
   * Resets this parser to grab more completions.
   *
   * @param provider
   *            The new provider to get completions for.
   */
  public void reset(CompletionProvider provider) {
    this.provider = provider;
    completions.clear();
    doingKeywords = inKeyword = gettingDoc = false;
    desc = new StringBuffer();
    code = new StringBuffer();
  }
  /**
   * Sets the class loader to use when loading custom classes to use for
   * various {@link Completion} types, such as {@link FunctionCompletion}s,
   * from XML.
   * <p>
   *
   * Users should very rarely have a need to use this method.
   *
   * @param cl
   *            The class loader to use. If this is <code>null</code>, then a
   *            default is used.
   */
  public static void setDefaultCompletionClassLoader(ClassLoader cl) {
    JvsXMLCompletion.DEFAULT_COMPLETION_CLASS_LOADER = cl;
  }
  /**
   * Called when an element starts.
   */
  @Override
  public void startElement(String uri, String localName, String qName,
                           Attributes attrs) {
    if("keywords".equals(qName)) {
      doingKeywords = true;
    } else if(doingKeywords) {
      if("keyword".equals(qName)) {
        name = attrs.getValue("name");
        shortDesc = attrs.getIndex("title") > -1 ? attrs
                    .getValue("title") : "";
        inKeyword = true;
      } else if(inKeyword) {
        if("doc".equals(qName)) {
          gettingDoc = true;
        }
        if("code".equals(qName)) {
          gettingCode = true;
        }
      }
    }
  }
  public List<Completion> getCompletions() {
    return this.completions;
  }
  public static DefaultCompletionProvider readCompletionToProvider(
    String file, DefaultCompletionProvider cp) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    JvsXMLCompletion handler = new JvsXMLCompletion(cp,
                                                    ClassLoader.getSystemClassLoader());
    BufferedInputStream bin = new BufferedInputStream(ClassLoader
                                                      .getSystemClassLoader().getResourceAsStream(file));
    try {
      SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(bin, handler);
      List<Completion> completions = handler.getCompletions();
      cp.addCompletions(completions);
    } catch(Exception ex) {
      Macros.message(
        "Erreur lors de la lecture de la librairie de<br/>complétion vérifier si le fichier xml<br/>est correctement écrit.<hr><i>Erreur : "
        + ex.getMessage() + "</i>", true);
    }
    finally {
      try {
        bin.close();
      } catch(IOException ex) { throw new RuntimeException(ex.toString());
      }
    }
    return cp;
  }
}