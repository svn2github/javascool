/*******************************************************************************
* Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
*******************************************************************************/

package org.javascool.tools;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Transforme une structure XML en une autre structure XML avec un XSLT.
 *
 * <p>
 * Note: utilise <tt>saxon.jar</tt> qui doit être dans le CLASSPATH.
 * </p>
 *
 * @see <a href="Xml2Xml.java.html">code source</a>
 * @serial exclude
 */
public class Xml2Xml {
  // @factory
  private Xml2Xml() {}
  /**
   * Convertit une chaîne XML en une autre chaîne XML selon des règles XSL.
   *
   * @param xml
   *            Le nom de fichier ou la chaîne XML en entrée.
   * @param xsl
   *            Le nom de fichier ou la chaîne avec les règles de
   *            transformation XSL.
   *            <p>
   *            - Si la chaîne commence par un <tt>&lt;</tt> elle est reconnue
   *            comme un texte XSLT.
   *            </p>
   *            <p>
   *            - Sinon elle est reconnue comme un non de fichier.
   *            </p>
   * @param params
   *            Paramètres de la transformation. La valeur null indique qu'il
   *            n'y a pas de paramètres.
   *
   * @return La chaîne en sortie.
   *
   * @throws IllegalArgumentException
   *             Si une erreur de syntaxe est détecté.
   * @throws RuntimeException
   *             Si une erreur d'entrée-sortie s'est produite.
   */
  public static String run(String xml, String xsl, Properties params) {
    // Compile la tranformation XSLT dans le cache des XSLT
    try {
      if(!Xml2Xml.tranformers.containsKey(xsl)) {
        StreamSource xslSource = null;
        if(xsl.trim().startsWith("<")) {
          xslSource = new StreamSource(new StringReader(xsl));
        } else {
          xslSource = new StreamSource(xsl);
          xslSource.setSystemId(xsl);
        }
        Xml2Xml.tranformers.put(xsl,
                                Xml2Xml.tfactory.newTransformer(xslSource));
      }
    } catch(TransformerConfigurationException e) { throw new RuntimeException(e + " when compiling: " + xsl);
    }
    // Applique la transformation
    try {
      if(xml == null) {
        xml = "<null/>";
      }
      StringWriter writer = new StringWriter();
      Transformer transformer = Xml2Xml.tranformers.get(xsl);
      // Ajoute les paramètres de la transformation, si définis
      {
        transformer.clearParameters();
        if(params != null) {
          for(String name : params.stringPropertyNames())
            transformer
            .setParameter(name, params.getProperty(name));
        }
      }
      StreamSource in = xml.trim().startsWith("<") ? new StreamSource(
        new StringReader(xml)) : new StreamSource(xml);
      transformer.transform(in, new StreamResult(writer));
      return writer.toString();
    } catch(TransformerException e) { throw new IllegalArgumentException(e.getMessageAndLocation());
    }
  }
  /**
   * @see #run(String, String, Properties)
   */
  public static String run(String xml, String xsl) {
    return Xml2Xml.run(xml, xsl, null);
  }
  /**
   * @see #run(String, String, Properties)
   */
  public static String run(String xml, String xsl, String paramName,
                           String paramValue) {
    Properties params = new Properties();
    params.setProperty(paramName, paramValue);
    return Xml2Xml.run(xml, xsl, params);
  }
  // Cash mechanism
  private static TransformerFactory tfactory;
  private static HashMap<String, Transformer> tranformers = new HashMap<String, Transformer>();
  static {
    try {
      System.setProperty("javax.xml.parsers.SAXParserFactory",
                         "com.icl.saxon.aelfred.SAXParserFactoryImpl");
      System.setProperty("javax.xml.transform.TransformerFactory",
                         "com.icl.saxon.TransformerFactoryImpl");
      System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
                         "com.icl.saxon.om.DocumentBuilderFactoryImpl");
      Xml2Xml.tfactory = TransformerFactory.newInstance();
    } catch(Throwable e) {
      System.err.println("Configuration error: " + e);
    }
  }

  /**
   * Convertit une chaîne en HTML en chaîne XHTML.
   * <p>
   * Elimine les entitées HTML connues (tout n'est pas implémenté) et passe à
   * de l'accentuation liée au codage local, élimine les constructions
   * (commentaires, instructions) qui ne sont pas structures logiques XML et
   * ferme les balises pur avoir une syntaxe bien formée.
   * </p>
   * <p>
   * C'est une commande "fragile" au sens où un text HTML mal formé ne sera
   * pas correctement traduit.
   * </p>
   *
   * @param htm
   *            La chaîne HTML en entrée.
   * @return La chaîne XML en sortie.
   */
  public static String html2xhtml(String htm) {
    return htm
           . // Elimine les accentuation HTML
           replaceAll("&agrave;", "à").replaceAll("&acirc;", "â")
           .replaceAll("&eacute;", "é").replaceAll("&egrave;", "è")
           .replaceAll("&euml;", "ë").replaceAll("&ecirc;", "ê")
           .replaceAll("&iuml;", "ï").replaceAll("&icirc;", "î")
           .replaceAll("&ouml;", "ö").replaceAll("&ocirc;", "ô")
           .replaceAll("&ldquo;", "&#8220;")
           .replaceAll("&rdquo;", "&#8221;")
           .replaceAll("&laquo;", "&#171;")
           .replaceAll("&raquo;", "&#172;;").replaceAll("&ugrave;", "ù")
           .replaceAll("&ccedil;", "ç").
           // Eliminate les constructions étranges
           replaceAll("<[!?][^>]*>", "").replaceAll("&nbsp;", "&#160;").
           // Encapsule les constructions non XML
           replaceAll("(<(meta|img|hr|br|link)[^>]*[^>/]?)>", "$1/>");
  }
  /**
   * Lanceur de la transformation XML -XSLT-> XML.
   *
   * @param usage
   *            <tt>java org.javascool.tools.Xml2Xml input-file XSL-file [output-file] [paramName paramValue]</tt>
   */
  public static void main(String[] usage) {
    // @main
    if(usage.length == 5) {
      FileManager.save(usage[2], Xml2Xml.run(FileManager.load(usage[0]),
                                             usage[1], usage[3], usage[4]));
    } else if(usage.length > 1) {
      FileManager.save(usage.length > 2 ? usage[2] : "stdout:",
                       Xml2Xml.run(FileManager.load(usage[0]), usage[1]));
    }
  }
}
