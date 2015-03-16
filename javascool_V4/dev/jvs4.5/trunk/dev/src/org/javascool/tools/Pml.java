/*******************************************************************************
 * Thierry.Vieville@sophia.inria.fr, Copyright (C) 2010.  All rights reserved. *
 *******************************************************************************/
package org.javascool.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Définit la syntaxe PML (Programmatic Markup Language) et son DOM (Data Object
 * Model) Java.
 * 
 * <p>
 * Un contenu PML (pour «Programmatic Métadata Logicalstructure») et une
 * structure-logique minimale (Parametric Minimal Logical-structure) qui permet
 * de définir les paramètres d'un objet numérique (algorithme, web-service) ou
 * d'interfacer entre des applications. C'est une forme minimale de structure
 * à-la XML.
 * </p>
 * 
 * <p>
 * Ses paramètres sont
 * <ul>
 * <li>Son <i>tag</i>, c'est à dire le nom qui définit son type.</li>
 * <li>Des <i>attributs</i> c'est à dire des valeurs indexes par un nom.</li>
 * <li>Des <i>éléments</i> c'est à dire des valeurs indexees par un entier
 * non-négatif <tt>>= 0</tt>.</li>
 * </ul>
 * Chaque valeur étant elle même un PML ou une chaîne de caractères. Rien de
 * plus.
 * </p>
 * 
 * <p>
 * La syntaxe est de la forme: <div style="margin-left: 40px">
 * <tt>"{tag name = value .. element .. }"</tt></div> où
 * <ul>
 * <li>Les PML sont encapsules avec des accolades <tt>{</tt> .. <tt>}</tt>.</li>
 * <li>Les String avec des espaces, <tt>{</tt> ou <tt>}</tt> sont encapsulés
 * avec des double quotes <tt>'"'</tt> (en utilisant <tt>'\"'</tt> pour y
 * échapper).</li>
 * </ul>
 * Cette syntaxe est minimale, proche des langages à accolades (
 * <tt>C/C++, PHP, Java</tt>), facile à lire ou éditer et surtout complètement
 * standar.
 * </p>
 * 
 * <p>
 * L'analyse syntaxique de PML est <i>tolérante</i> au sens où une valeur est
 * toujours obtenue sans générer d'erreur de syntaxe, en utilisant des valeurs
 * par défaut:
 * <ul>
 * <li>La construction <tt>"name = value .. "</tt> sans accolade sera vue comme
 * une liste de valeur de tag <tt>null</tt>,</li>
 * <li>Un attribut sans valeur recevra la valeur <tt>true</tt>,</li>
 * </ul>
 * etc..
 * </p>
 * 
 * @see <a href="Pml.java.html">source code</a>
 * @serial exclude
 */
public class Pml {
	// @bean
	public Pml() {
	}

	private HashMap<String, Object> data = new HashMap<String, Object>();

	/**
	 * Initialise la PML en la lisant dans une chaîne de caractères.
	 * 
	 * @param value
	 *            La chaîne de syntaxe
	 *            <tt>"{tag name = value .. element .. }"</tt>.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().reset(..)</tt>.
	 */
	public Pml reset(String value) {
		// Initializes the Pml
		data = new HashMap<String, Object>();
		tag = "";
		parent = null;
		count = -1;
		// Parses the string
		new PmlReader().read(value, this);
		return this;
	}

	/**
	 * Initialise la PML en la lisant dans une chaîne de caractères dans un
	 * format donné.
	 * 
	 * @param value
	 *            La chaîne de syntaxe
	 *            <tt>"{tag name = value .. element .. }"</tt>.
	 * @param format
	 *            Les formats possible sont: <div id="input-format">
	 *            <ul>
	 *            <li>"PML" (valeur par défaut).</li>
	 *            <li>"XML" pour utiliser les structure-logiques XML de la forme
	 *            <tt>&lt;tag name = value .. > &lt;element .. &lt;/tag></tt>
	 *            <li>"HTM" pour utiliser les structure-logiques HTML.</li>
	 *            </ul>
	 *            </div>
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().reset(..)</tt>.
	 */
	public Pml reset(String value, String format) {
		format = format.toLowerCase();
		if ("xml".equals(format))
			return reset(
					Xml2Xml.run(value, Pml.xml2pml).replaceAll("¨", "\\\\\"")
							.replaceAll("«", "\\{"), "pml");
		else if ("htm".equals(format) || "html".equals(format))
			return reset(Xml2Xml.run(Xml2Xml.html2xhtml(value), Pml.xml2pml),
					"pml");
		else {
			reset(value);
		}
		return this;
	}

	/**
	 * Initialise la PML en en recopiant la PML en entrée.
	 * 
	 * @param pml
	 *            Le PML à copier.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().reset(..)</tt>.
	 */
	public Pml reset(Pml pml) {
		// Initializes the Pml
		data = new HashMap<String, Object>();
		tag = "";
		parent = null;
		count = -1;
		if (pml != null) {
			setTag(pml.getTag());
			for (String name : pml.attributes()) {
				set(name, pml.getChild(name));
			}
			for (int i = 0; i < pml.getCount(); i++) {
				set(i, pml.getChild(i));
			}
		}
		return this;
	}

	/**
	 * Initialise la PML à partir des arguments d'une ligne de commande.
	 * <p>
	 * La méthode s'utilise dans la construction:
	 * 
	 * <pre>
	 * public static void main(String usage[]) {
	 *   Pml arguments = new Pml.reset(usage);
	 * ../..
	 * 
	 * <pre>
	 * </p>
	 * <p>
	 * Il respecte les conventions suivantes:
	 * <ul>
	 * <li><tt>-name</tt> définit un paramètre à la valeur true (la syntaxe
	 * <tt>--name</tt> est aussi acceptée),</li>
	 * <li><tt>-name value</tt> définit la valeur d'un paramètre,</li>
	 * <li><tt>file</tt> ajoute un élément de type string,</li>
	 * <li><tt>- file</tt> ajoute un élément de type string qui commence par un
	 * <tt>-</tt>.</li>
	 * </ul>
	 * Par exemple: <tt>command -quiet -level 123 input1 input2</tt> definit la
	 * PML <tt>{usage quiet=true level=123 input1 inpu2}</tt>.
	 * </p>
	 * 
	 * @param usage
	 *            Les éléments de la ligne de commande.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().reset(..)</tt>.
	 */
	public Pml reset(String[] usage) {
		reset("{usage}");
		for (int i = 0; i < usage.length; i++) {
			if (!"-".equals(usage[i])) {
				if (usage[i].startsWith("-")
						&& (i == 0 || !"-".equals(usage[i - 1]))) {
					String name = usage[i].replaceFirst("-+", "");
					if (i == usage.length - 1 || usage[i + 1].startsWith("-")) {
						set(name, true);
					} else {
						set(name, usage[++i]);
					}
				} else {
					add(usage[i]);
				}
			}
		}
		return this;
	}

	/**
	 * Retourne la PML sous forme de chaîne de caractères.
	 * 
	 * @param format
	 *            de sortie <div id="output-format">
	 *            <ul>
	 *            <li>"RAW" Retourne une chaîne 1D de longueur minimale (par
	 *            défault).</li>
	 *            <li>"TXT" Retourne une chaîne 2D formattée.</li>
	 *            <li>"XML" Retourne une structure logique XML, en réduisant les
	 *            tag et attributs à des nom XML valides et en considérant les
	 *            PML sans attribut ni élément comme des chaînes.
	 *            <li>
	 *            <li>"PHP" Retourne un élément de code PHP de la forme:
	 *            <tt>&lt;php $tag = array("_tag" = getTag(), . . "name" => "value", . . , "element");?></tt>
	 *            .</li>
	 *            <li>"JMF" Retourne un format de fichier de manifeste de JAR de
	 *            la forme: <tt> name : value \n .. </tt> en omettant le tag et
	 *            les éléments.</li>
	 *            </ul>
	 *            </div>
	 * @return La chaîne qui représente la PML.
	 */
	public String toString(String format) {
		format = format.toLowerCase();
		return "xml".equals(format) ? new XmlWriter().toString(this) : "raw"
				.equals(format) ? new PlainWriter().toString(this, 0) : "php"
				.equals(format) ? new PhpWriter().toString(this) : "jmf"
				.equals(format) ? new JmfWriter().toString(this)
				: new PlainWriter().toString(this, 180);
	}

	@Override
	public final String toString() {
		return toString("raw");
	}

	/**
	 * Initialise la PML en la lisant dans un fichier donné.
	 * 
	 * @param location
	 *            L'URL (Universal Resource Location) de chargement de <a
	 *            href="FileManager.html#load-format">format standard</a>.
	 * @param format
	 *            Le format de lecture parmi les <a href="#input-format">formats
	 *            supportés</a>, par défaut donné par l'extension du fichier.
	 * @param utf8
	 *            Si la valeur est vraie, force l'encodage en UTF-8 à la
	 *            lecture. Par défaut (false) utilise l'encodage local.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().load(..)</tt>.
	 */
	public final Pml load(String location, String format, boolean utf8) {
		if (format == null) {
			format = location.replaceAll("^.*\\.([A-Za-z]+)$", "$1");
		}
		return reset(FileManager.load(location, utf8), format);
	}

	/**
	 * @see #load(String, String, boolean)
	 */
	public final Pml load(String location, String format) {
		return load(location, format, false);
	}

	/**
	 * @see #load(String, String, boolean)
	 */
	public final Pml load(String location, boolean utf8) {
		return load(location, null, utf8);
	}

	/**
	 * @see #load(String, String, boolean)
	 */
	public final Pml load(String location) {
		return load(location, null, false);
	}

	/**
	 * Sauve la PML dans un fichier donné.
	 * 
	 * @param location
	 *            L'URL (Universal Resource Location) d'écriture de <a
	 *            href="FileManager.html#save-format">format standard</a>.
	 * @param format
	 *            Le format d'écriture parmi les <a
	 *            href="#output-format">formats supportés</a>, par défaut donné
	 *            par l'extension du fichier.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().save(..)</tt>.
	 */
	public final Pml save(String location, String format) {
		FileManager.save(location, toString(format) + "\n");
		return this;
	}

	/**
	 * @see #save(String, String)
	 */
	public final Pml save(String location) {
		return save(location, location.replaceAll("^.*\\.([A-Za-z]+)$", "$1"));
	}

	/**
	 * Definit l'analyseur lexical qui lit la chaîne mot à mot en normlisant les
	 * espaces et en titant le caractère '"'.
	 */
	protected static class TokenReader {
		/** Definit un élément lexical. */
		private static class token {
			String string;
			int line;

			token(String s, int i0, int i1, int l) {
				string = s.substring(i0, i1);
				line = l;
			}

			@Override
			public String toString() {
				return "#" + line + " \"" + string + "\" ";
			}
		}

		ArrayList<token> tokens;
		int itoken;

		/** Initialise le lecteur. */
		public TokenReader reset(String string) {
			// Initializes the buffer
			tokens = new ArrayList<token>();
			itoken = 0;
			// Split the string into tokens
			{
				char[] chars = string.toCharArray();
				for (int ichar = 0, ln = 0; ichar < chars.length;) {
					// Skips spaces
					while (ichar < chars.length
							&& Character.isWhitespace(chars[ichar])) {
						if (chars[ichar] == '\n') {
							ln++;
						}
						ichar++;
					}
					if (ichar < chars.length) {
						int ichar0 = ichar;
						// Detects a quoted string taking "{" "}" and \"
						// constructs into account
						if (chars[ichar0] == '"') {
							while (ichar < chars.length
									&& (ichar == ichar0 || chars[ichar] != '"' || chars[ichar - 1] == '\\')) {
								if (chars[ichar] == '\n') {
									ln++;
								}
								ichar++;
							}
							ichar++;
							int ichar1;
							if ((ichar == ichar0 + 3)
									&& ((chars[ichar0 + 1] == '{') || (chars[ichar0 + 1] == '}'))) {
								ichar1 = ichar;
							} else {
								ichar0++;
								ichar1 = ichar - 1;
							}
							tokens.add(new token(string, ichar0, ichar1, ln));
							// Detects a name
						} else if (Character.isLetter(chars[ichar0])
								|| (chars[ichar0] == '_')) {
							while (ichar < chars.length
									&& (Character.isLetterOrDigit(chars[ichar]) || chars[ichar0] == '_')) {
								ichar++;
							}
							tokens.add(new token(string, ichar0, ichar, ln));
							// Detects a number
						} else if (Character.isDigit(chars[ichar0])
								|| (chars[ichar0] == '.')) {
							while (ichar < chars.length
									&& Character.isDigit(chars[ichar])) {
								ichar++;
							}
							if ((ichar < chars.length) && (chars[ichar] == '.')) {
								ichar++;
								while (ichar < chars.length
										&& Character.isDigit(chars[ichar])) {
									ichar++;
								}
							}
							if ((ichar < chars.length)
									&& ((chars[ichar] == 'E') || (chars[ichar] == 'e'))) {
								ichar++;
								if ((ichar < chars.length)
										&& ((chars[ichar] == '+') || (chars[ichar] == '-'))) {
									ichar++;
								}
								while (ichar < chars.length
										&& Character.isDigit(chars[ichar])) {
									ichar++;
								}
							}
							tokens.add(new token(string, ichar0, ichar, ln));
							// Detects operators and punctuation
						} else if (TokenReader.isOperator(chars[ichar0])) {
							while (ichar < chars.length
									&& TokenReader.isOperator(chars[ichar])) {
								ichar++;
							}
							tokens.add(new token(string, ichar0, ichar, ln));
						} else {
							tokens.add(new token(string, ichar0, ++ichar, ln));
						}
					}
				}
			}
			itoken = 0;
			return this;
		}

		private static boolean isOperator(char c) {
			switch (c) {
			case '+':
			case '-':
			case '*':
			case '/':
			case '%':
			case '&':
			case '|':
			case '^':
			case '=':
			case '!':
			case '<':
			case '>':
			case '.':
			case ':':
				return true;
			default:
				return false;
			}
		}

		/**
		 * Renvoie un des éléments.
		 * 
		 * @param next
		 *            Si 0 renvoie l'élément courant. Si 1 renvoie l'élément à
		 *            suivre, etc..
		 * @return L'élément ou '}' à l afin du fichier.
		 */
		public String getToken(int next) {
			String current = itoken + next < tokens.size() ? tokens.get(itoken
					+ next).string : "}";
			return current;
		}

		/** Teste si il y reste des éléments. */
		public boolean isNext() {
			return itoken < tokens.size();
		}

		/**
		 * Avance à un élément suivant.
		 * 
		 * @param next
		 *            Si 1 avance d'un élément, etc..
		 */
		public void next(int next) {
			itoken += next;
		}

		/** Renvoie la fin de la chaîne. */
		public String trailer() {
			String t = "";
			while (itoken < tokens.size()) {
				t += " " + tokens.get(itoken++).string;
			}
			return t.trim();
		}

		/** Teste une condition de syntaxe. */
		public void check(boolean ok, String message) {
			if (!ok) {
				System.out.println("Erreur de syntaxe \""
						+ message
						+ "\", ligne "
						+ (itoken < tokens.size() ? ""
								+ tokens.get(itoken).line + " vers \""
								+ getToken(0) + "\"" : "finale"));
			}
		}

		@Override
		public String toString() {
			String s = "[";
			for (int i = 0; i < tokens.size(); i++) {
				s += (i == itoken ? " ! " : " ") + "\"" + tokens.get(i).string
						+ "\"#" + tokens.get(i).line;
			}
			return s + " ]";
		}
	}

	/** Définit un lecteur de PML. */
	private static class PmlReader extends TokenReader {
		/** Lit la chaîne et en affecte les valeurs du PML. */
		public void read(String string, Pml pml) {
			reset(string);
			// Parses the string
			parse(pml);
			// Detects the trailer if any
			String trailer = trailer();
			if (trailer.length() > 0) {
				Pml p = new Pml();
				p.setTag(trailer);
				pml.set("string_trailer", p);
			}
		}

		/** Effectue l'analyse syntaxique récursive. */
		private Pml parse(Pml pml) {
			String b = getToken(0);
			// Parses a { } Pml construct
			if ("{".equals(b)) {
				next(1);
				for (boolean start = true; true; start = false) {
					String t = getToken(0);
					if ("}".equals(t)) {
						next(1);
						break;
						// Adds an element
					} else if ("{".equals(t)) {
						Pml p = new Pml();
						parse(p);
						pml.add(p);
						// Adds an attribute
					} else if ("=".equals(getToken(1))) {
						next(2);
						if ("}".equals(getToken(0))) {
							pml.set(t, "true");
						} else {
							Pml p = new Pml();
							parse(p);
							pml.set(t, p);
						}
						// Adds an attribute tag
					} else if (start) {
						next(1);
						pml.setTag(t);
						// Adds an atomic element
					} else {
						Pml p = new Pml();
						parse(p);
						pml.add(p);
					}
				}
				// Considers the Pml as a list of name=value
			} else if ("=".equals(getToken(1))) {
				while ("=".equals(getToken(1))) {
					String t = getToken(0);
					next(2);
					if ("}".equals(getToken(0))) {
						pml.set(t, "true");
					} else {
						Pml p = new Pml();
						parse(p);
						pml.set(t, p);
					}
				}
				// Considers the Pml as a simple string
			} else {
				pml.setTag(b);
				next(1);
			}
			return pml;
		}
	}

	/** Définit le convertisseur de XML en PML. */
	private static String xml2pml = "<?xml version='1.0' encoding='utf-8'?>\n"
			+ "<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' version='1.0'>\n"
			+ "  <xsl:output method='text' encoding='utf-8' omit-xml-declaration='yes'/>\n"
			+ "  <xsl:template match='*'>\n"
			+ "  {<xsl:value-of select='name(.)'/><xsl:text> </xsl:text>\n"
			+ "    <xsl:for-each select='@*'><xsl:value-of select='name(.)'/>=\"<xsl:value-of select=\"translate(., '&quot;','¨')\"/>\"<xsl:text> </xsl:text></xsl:for-each>\n"
			+ "    <xsl:if test='count(*) = 0'>\n"
			+ "      <xsl:apply-templates select='*'/>\n"
			+ "      <xsl:value-of select=\"concat('&quot;', translate(translate(text(), '&quot;','¨'), '{', '«'), '&quot;')\"/>\n"
			+ "    </xsl:if>\n"
			+ "    <xsl:if test='count(*) > 0'><xsl:apply-templates/></xsl:if>\n"
			+ "  }</xsl:template>\n" + "</xsl:stylesheet>";

	/** Définit le convertisseur de PML en chaîne de caractères. */
	private static class PlainWriter {
		private StringBuffer string;
		int width, l;

		/**
		 * Convertit la PML en chaîne.
		 * 
		 * @param pml
		 *            Le PML à convertir.
		 * @param width
		 *            si width == 0 retourne une chaîne 1D de longueur minimale,
		 *            sinon retourne une chaîne 2D de la largeur donnée.
		 * @return La chaîne générée.
		 */
		public String toString(Pml pml, int width) {
			if (pml == null)
				return "null";
			// Initializes the variables
			string = new StringBuffer();
			if (width == 0) {
				write1d(pml);
			} else {
				this.width = width;
				l = 0;
				write2d(pml, 0, 0);
			}
			return string.toString();
		}

		private void write1d(Pml pml) {
			if (pml == null) {
				string.append(" {null} ");
				return;
			}
			string.append("{").append(PlainWriter.quote(pml.getTag()));
			for (String name : pml.attributes()) {
				string.append(" ").append(PlainWriter.quote(name)).append("=");
				write1d(pml.getChild(name));
			}
			for (int n = 0; n < pml.getCount(); n++) {
				string.append(" ");
				write1d(pml.getChild(n));
			}
			string.append("}");
		}

		private boolean write2d(Pml pml, int n, int tab) {
			if (pml == null) {
				string.append(" {null} ");
				return false;
			}
			if (pml.getSize() == 0) {
				boolean ln = n >= 0
						&& (n == 0 || (pml.getParent() != null
								&& pml.getParent().getChild(n - 1) != null && pml
								.getParent().getChild(n - 1).getSize() > 0));
				writeln(ln, tab);
				write(PlainWriter.quote(pml.getTag()), tab);
				return ln;
			} else {
				boolean ln = pml.getTag().length() > 1
						|| "p".equals(pml.getTag());
				writeln(n >= 0 && ln, tab);
				write("{" + PlainWriter.quote(pml.getTag()), tab);
				ln = false;
				for (String name : pml.attributes()) {
					write(" " + PlainWriter.quote(name) + " =", tab);
					ln |= write2d(pml.getChild(name), -1, tab + 1);
				}
				for (int i = 0; i < pml.getCount(); i++) {
					ln |= write2d(pml.getChild(i), i, tab + 2);
				}
				writeln(ln, tab);
				write("}", tab);
				return ln;
			}
		}

		private void writeln(boolean ln, int tab) {
			if (ln) {
				string.append("\n");
				for (int t = 0; t < tab; t++) {
					string.append(" ");
				}
				l = tab;
			} else {
				string.append(" ");
			}
		}

		private void write(String word, int tab) {
			if (l + word.length() > width) {
				writeln(false, tab + 1);
			}
			string.append(word);
			l += word.length();
		}

		/** Retourne la chaîne en tenant compte des "{" "}" et \". */
		private static String quote(String string) {
			return string == null ? "null"
					: string.matches("[a-zA-Z_][a-zA-Z0-9_]*")
							|| "\"{\"".equals(string) || "\"}\"".equals(string) ? string
							: "\""
									+ string.replaceAll("\\\\", "\\\\\\\\")
											.replaceAll("\"", "\\\\\"") + "\"";
		}
	}

	/** Définit le convertisseur de PML en XMl. */
	private static class XmlWriter {
		private StringBuffer string;

		/** Convertit la PML en chaîne XML 1D. */
		public String toString(Pml pml) {
			string = new StringBuffer();
			if (pml == null)
				return "<null/>";
			write(pml);
			return string.toString();
		}

		private void write(Pml pml) {
			if (pml.getSize() == 0) {
				string.append(" ").append(
						pml.getTag().replaceFirst("^\"([{}])\"$", "$1")
								.replaceAll("&", "&amp;")
								.replaceAll("<", "&lt;"));
			} else {
				string.append(" <").append(Pml.toName(pml.getTag()));
				for (String name : pml.attributes()) {
					string.append(" ")
							.append(Pml.toName(name))
							.append("=\"")
							.append(pml.getChild(name).toString()
									.replaceFirst("^\\{(.*)\\}$", "$1")
									.replaceFirst("^\"(.*)\"$", "$1")
									.replaceAll("&", "&amp;")
									.replaceAll("<", "&lt;")
									.replaceAll("\"", "&quot;")).append("\"");
				}
				if (pml.getCount() > 0) {
					string.append(">");
					for (int n = 0; n < pml.getCount(); n++) {
						write(pml.getChild(n));
					}
					string.append("</").append(Pml.toName(pml.getTag()))
							.append(">");
				} else {
					string.append("/>");
				}
			}
		}
	}

	/** Définit le convertisseur de PML en PHP. */
	private static class PhpWriter {
		private StringBuffer string;

		/** Convertit la PML en tableau PHP. */
		public String toString(Pml pml) {
			string = new StringBuffer();
			if (pml == null)
				return "<?php $pml = array(); ?>";
			else {
				String tag = Pml.toName(pml.getTag());
				if (tag.length() == 0) {
					tag = "pml";
				}
				string.append("<?php $").append(tag)
						.append(" = array(\"_tag\" => ")
						.append(PhpWriter.quote(pml.getTag()));
				for (String name : pml.attributes()) {
					string.append(", ").append(PhpWriter.quote(name))
							.append(" => ")
							.append(PhpWriter.quote(pml.getChild(name)));
				}
				for (int n = 0; n < pml.getCount(); n++) {
					string.append(", ")
							.append(PhpWriter.quote(pml.getChild(n)));
				}
				string.append("); ?>");
			}
			return string.toString();
		}

		/** Prends en compte les \". */
		private static String quote(String string) {
			return "\""
					+ string.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"",
							"\\\\\"") + "\"";
		}

		private static String quote(Pml pml) {
			return PhpWriter.quote(pml.getSize() == 0 ? pml.getTag() : pml
					.toString());
		}
	}

	/** Définit le convertisseur de PML en JMF. */
	private static class JmfWriter {
		private StringBuffer string;

		/** Convertit la PML en fichier JMF. */
		public String toString(Pml pml) {
			string = new StringBuffer();
			if (pml == null)
				return "";
			else {
				for (String name : pml.attributes()) {
					string.append(name).append(": ")
							.append(JmfWriter.quote(pml.getChild(name)))
							.append("\n");
				}
			}
			return string.toString();
		}

		/** Elimine les \n. */
		private static String quote(String string) {
			return string.replaceAll("\n", " ");
		}

		private static String quote(Pml pml) {
			return JmfWriter.quote(pml.getSize() == 0 ? pml.getTag() : pml
					.toString());
		}
	}

	private static String toName(String string) {
		if (string.length() > 0) {
			String c_0 = string.substring(0, 1);
			String name = c_0.matches("[_-]")
					|| Character.isLetter(c_0.charAt(0)) ? "" : "_";
			for (int i = 0; i < string.length(); i++) {
				String c_i = string.substring(i, i + 1);
				name += c_i.matches("_-")
						|| Character.isLetterOrDigit(c_i.charAt(0)) ? c_i : "_";
			}
			return name;
		} else
			return string;
	}

	/**
	 * Renvoie le type de ce PML.
	 * 
	 * @return The tag définit lors de l'initialisation, sinon le nom de la
	 *         classe du PML.
	 */
	public final String getTag() {
		return tag;
	}

	protected final Pml setTag(String value) {
		tag = value;
		return this;
	}

	private String tag = getClass().getName();

	/**
	 * Renvoie le parent du PML si défini.
	 * 
	 * @return Si ce PML est un sous-partie d'un PML renvoie son parent, sinon
	 *         renvoie null.
	 */
	public final Pml getParent() {
		return parent;
	}

	private void setParent(Pml value) {
		if (parent == null) {
			parent = value;
		}
	}

	private Pml parent = null;

	/**
	 * Teste si un paramètre de ce PML est défini.
	 * <p>
	 * Cet appel est formellement équivalent à <tt>getChild(name) != null</tt>
	 * </p>
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @return True si le paramètre est défini, false sinon.
	 */
	public final boolean isDefined(String name) {
		return data.containsKey(name);
	}

	/**
	 * @see #isDefined(String)
	 */
	public final boolean isDefined(int index) {
		return isDefined(Integer.toString(index));
	}

	/**
	 * Renvoie la valeur d'un paramètre de ce PML.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @return La valeur du paramètre, ou null si indéfini.
	 */
	public Pml getChild(String name) {
		Object o = data.get(name);
		return o == null ? null : o instanceof Pml ? (Pml) o : new Pml()
				.reset("{\"" + o.toString() + "\"}");
	}

	/**
	 * @see #getChild(String)
	 */
	public final Pml getChild(int index) {
		return getChild(Integer.toString(index));
	}

	/**
	 * Renvoie la d'un paramètre de ce PML en tant qu'objet Java.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @return La valeur du paramètre, ou null si indéfini en tant que
	 *         paramètre.
	 */
	public Object getObject(String name) {
		return data.get(name);
	}

	/**
	 * @see #getObject(String)
	 */
	public final Object getObject(int index) {
		return getObject(Integer.toString(index));
	}

	/**
	 * Renvoie la d'un paramètre de ce PML en tant qu'objet Java.
	 * 
	 * /** Renvoie la valeur d'un paramètre de ce PML en tant que chaîne.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @param value
	 *            La valeur par défaut, sinon "".
	 * @return La valeur de ce paramètre, si défini, sinon sa valeur par défaut.
	 */
	public final String getString(String name, String value) {
		if (data.get(name) == null)
			return "";
		String v = data.get(name).toString();
		if (v.matches("[{]\".*\"[}]")) {
			v = v.substring(2, v.length() - 2);
		}
		if (v.matches("[{].*[}]")) {
			v = v.substring(1, v.length() - 1);
		}
		return v != null ? v : value != null ? value : "";
	}

	/**
	 * @see #getString(String, String)
	 */
	public final String getString(int index, String value) {
		return getString(Integer.toString(index), value);
	}

	/**
	 * @see #getString(String, String)
	 */
	public final String getString(String name) {
		return getString(name, null);
	}

	/**
	 * @see #getString(String, String)
	 */
	public final String getString(int index) {
		return getString(index, null);
	}

	/**
	 * Renvoie la valeur d'un paramètre de ce PML en tant que décimal.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @param value
	 *            La valeur par défaut, sinon "0".
	 * @return La valeur de ce paramètre, si défini, sinon sa valeur par défaut.
	 */
	public final double getDecimal(String name, double value) {
		try {
			return Double.parseDouble(getString(name));
		} catch (NumberFormatException e) {
			return value;
		}
	}

	/**
	 * @see #getDecimal(String, double)
	 */
	public final double getDecimal(int index, double value) {
		return getDecimal(Integer.toString(index), value);
	}

	/**
	 * @see #getDecimal(String, double)
	 */
	public final double getDecimal(String name) {
		return getDecimal(name, 0);
	}

	/**
	 * @see #getDecimal(String, double)
	 */
	public final double getDecimal(int index) {
		return getDecimal(index, 0);
	}

	/**
	 * Renvoie la valeur d'un paramètre de ce PML en tant qu'entier.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @param value
	 *            La valeur par défaut, sinon "0".
	 * @return La valeur de ce paramètre, si défini, sinon sa valeur par défaut.
	 */
	public final int getInteger(String name, int value) {
		try {
			return Integer.parseInt(getString(name));
		} catch (NumberFormatException e) {
			return value;
		}
	}

	/**
	 * @see #getInteger(String, int)
	 */
	public final int getInteger(int index, int value) {
		return getInteger(Integer.toString(index), value);
	}

	/**
	 * @see #getInteger(String, int)
	 */
	public final int getInteger(String name) {
		return getInteger(name, 0);
	}

	/**
	 * @see #getInteger(String, int)
	 */
	public final int getInteger(int index) {
		return getInteger(index, 0);
	}

	/**
	 * Renvoie la valeur d'un paramètre de ce PML en tant que boolean.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @param value
	 *            La valeur par défaut, sinon false.
	 * @return La valeur true ou false si le paramètre est égal à "true" ou
	 *         "false" indépendamment de la casse, la valeur par défaut
	 *         sinon.int
	 */
	public final boolean getBoolean(String name, boolean value) {
		String v = getString(name);
		if (v != null) {
			if ("true".equals(v.toLowerCase()))
				return true;
			if ("false".equals(v.toLowerCase()))
				return false;
		}
		return value;
	}

	/**
	 * @see #getBoolean(String, boolean)
	 */
	public final boolean getBoolean(int index, boolean value) {
		return getBoolean(Integer.toString(index), value);
	}

	/**
	 * @see #getBoolean(String, boolean)
	 */
	public final boolean getBoolean(String name) {
		return getBoolean(name, false);
	}

	/**
	 * @see #getBoolean(String, boolean)
	 */
	public final boolean getBoolean(int index) {
		return getBoolean(index, false);
	}

	/**
	 * Définit la valeur d'un paramètre de ce PML.
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @param value
	 *            La valeur du paramètre (en tant que PML, object Java, entier,
	 *            décimal ou entier). Si null efface la valeur précédente.
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().set(..)</tt>.
	 */
	public Pml set(String name, Object value) {
		// Deletes the attribute value
		if (value == null) {
			try {
				// Shifts removed elements to avoid "null wholes"
				int i = Integer.parseInt(name), l = getCount() - 1;
				if ((0 <= i) && (i <= l)) {
					for (int j = i; j < l; j++) {
						data.put(Integer.toString(j),
								data.get(Integer.toString(j + 1)));
					}
					data.remove(Integer.toString(l));
				} else {
					data.remove(name);
				}
			} catch (NumberFormatException e) {
				data.remove(name);
			}
			// Adds the parameter value
		} else {
			data.put(name, value);
			if (value instanceof Pml) {
				((Pml) value).setParent(this);
			}
		}
		count = -1;
		return this;
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(int index, Object value) {
		return set(Integer.toString(index), value);
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(String name, String value) {
		if (value == null)
			return set(name, (Object) null);
		else {
			Pml v = new Pml();
			v.reset("\"" + value.replaceAll("\"", "\\\"") + "\"");
			return set(name, v);
		}
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(int index, String value) {
		return set(Integer.toString(index), value);
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(String name, double value) {
		return set(name, Double.toString(value));
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(int index, double value) {
		return set(Integer.toString(index), value);
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(String name, int value) {
		return set(name, Integer.toString(value));
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(int index, int value) {
		return set(Integer.toString(index), value);
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(String name, boolean value) {
		return set(name, value ? "true" : "false");
	}

	/**
	 * @see #set(String, Object)
	 */
	public final Pml set(int index, boolean value) {
		return set(Integer.toString(index), value);
	}

	/**
	 * Elimine la valeur d'un paramètre de ce PML.
	 * <p>
	 * Cet appel est formellement équivalent à <tt>set(name, null);</tt>
	 * </p>
	 * 
	 * @param name
	 *            Le nom de l'attribut ou l'index de l'élément (sous forme de
	 *            chaîne ou d'entier).
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().del(..)</tt>.
	 */
	public Pml del(String name) {
		return set(name, (Pml) null);
	}

	/**
	 * @see #del(String)
	 */
	public final Pml del(int index) {
		return set(Integer.toString(index), (Pml) null);
	}

	/**
	 * Ajoute un élément à ce PML.
	 * <p>
	 * Cet appel est formellement équivalent à <tt>set(getCount(), value);</tt>
	 * </p>
	 * 
	 * @param value
	 *            La valeur du paramètre (en tant que PML, entier, décimal ou
	 *            entier).
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().add(..)</tt>.
	 */
	public final Pml add(Pml value) {
		int c = getCount();
		set(c, value);
		count = ++c;
		return this;
	}

	/**
	 * @see #add(String)
	 */
	public final Pml add(String value) {
		Pml v = new Pml();
		v.reset(value);
		return add(v);
	}

	/**
	 * @see #add(String)
	 */
	public final Pml add(double value) {
		return add(Double.toString(value));
	}

	/**
	 * @see #add(String)
	 */
	public final Pml add(int value) {
		return add(Integer.toString(value));
	}

	/**
	 * Définit la valeur de paramètres de ce PML.
	 * 
	 * @param pml
	 *            La structure dont on copie les paramètres
	 * @return Cet objet, permettant de définir la construction
	 *         <tt>Pml pml= new Pml().set(..)</tt>.
	 */
	public final Pml set(Pml pml) {
		for (String name : pml.attributes()) {
			set(name, pml.getObject(name));
		}
		for (int n = 0; n < pml.getCount(); n++) {
			set(n, pml.getObject(n));
		}
		return this;
	}

	/**
	 * @see #set(Pml)
	 */
	public final Pml set(Properties pml) {
		for (String name : pml.stringPropertyNames()) {
			set(name, pml.getProperty(name));
		}
		return this;
	}

	/**
	 * Renvoie le nombre d'éléments de ce PML.
	 * 
	 * @return Le nombre d'éléments (indépendamment des attributs), les éléments
	 *         null étant éliminés
	 */
	public int getCount() {
		if (count < 0) {
			count = 0;
			for (String key : data.keySet())
				if (Pml.isIndex(key)) {
					count = Math.max(Integer.parseInt(key) + 1, count);
				}
		}
		return count;
	}

	private int count = -1;

	/**
	 * Renvoie le nombre de paramètres de ce PML.
	 * 
	 * @return Le nombre d'attributs et d'éléments. Si 0, ce PML correspond
	 *         uniquement à une chaîne: son tag.
	 */
	public int getSize() {
		return data.size();
	}

	/**
	 * Définir un itérateur sur les attributs de ce PML.
	 * <p>
	 * - Les attributes sont énumérés avec une construction de la forme:
	 * <tt>for(String name : pml.attributes()) { Pml value = pml.getChild(name); .. }</tt>
	 * .
	 * </p>
	 * <p>
	 * - Les éléments sont énumérés avec une construction de la forme:
	 * <tt>for(int n = 0; n &lt; pml.getCount(); n++) { Pml value = pml.getChild(n); .. }</tt>
	 * .
	 * </p>
	 */
	public final Iterable<String> attributes() {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return new Iterator<String>() {
					Iterator<String> keys = data.keySet().iterator();
					String key;

					{
						nextKey();
					}

					private void nextKey() {
						for (key = null; keys.hasNext()
								&& Pml.isIndex(key = keys.next()); key = null) {
						}
					}

					@Override
					public String next() {
						String value = key;
						nextKey();
						return value;
					}

					@Override
					public boolean hasNext() {
						return key != null;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	// @return true if the name is an index
	private static boolean isIndex(String name) {
		return Pml.index.matcher(name).matches();
	}

	static Pattern index = Pattern.compile("[0-9]+");

	/**
	 * Renvoie les paramètres de cette PML sous forme de Properties.
	 * 
	 * @return Une structure Properties contenant attributs et éléments sous
	 *         forme de chaîne de caractère.
	 */
	public final Properties toProperties() {
		Properties properties = new Properties();
		for (String name : attributes()) {
			properties.setProperty(name, getObject(name).toString());
		}
		for (int n = 0; n < getCount(); n++) {
			properties.setProperty("" + n, getObject(n).toString());
		}
		return properties;
	}

	/**
	 * Lanceur du mécanisme de vérification/conversion d'une PML.
	 * 
	 * @param usage
	 *            <tt>java org.javascool.tools.Pml input-file [output-file.(pml|xml|php|jmf)]</tt>
	 */
	public static void main(String[] usage) {
		if (usage.length > 0) {
			new Pml().load(usage[0]).save(
					usage.length > 1 ? usage[1] : "stdout:",
					(usage.length > 1 && usage[1]
							.matches(".*\\.(pml|php|xml|jmf)")) ? usage[1]
							.replaceFirst(".*\\.", "") : "pml");
		}
	}
}
