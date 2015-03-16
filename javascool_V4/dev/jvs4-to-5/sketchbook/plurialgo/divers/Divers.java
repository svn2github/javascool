package org.javascool.proglets.plurialgo.divers;

import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import javax.swing.JTextArea;

/**
 * Cette classe contient divers utilitaires, en particulier pour écrire
 * un programme dans un StringBuffer.
 */
public class Divers {

	// ------------------------------------------
	// operations sur StringBuffer
	// ------------------------------------------
	
	public static void indenter(StringBuffer fich, int indent, String texte) {
		fich.append("\n");
		for (int i = 0; i < indent; i++)
			fich.append("\t");
		fich.append(texte);
	}
	
	public static void indenter(StringBuffer fich, int indent) {
		indenter(fich, indent, "");
	}
	
	public static void ecrire(StringBuffer fich, String texte, int indent) {
		if (indent >= 0)
			indenter(fich, indent, texte);
		else
			fich.append(texte);
	}
	
	public static void ecrire(StringBuffer fich, String texte) {
		ecrire(fich, texte, -1);
	}
	
	public static void ecrire(StringBuffer fich) {
		ecrire(fich, "", -1);
	}
	
	public static void remplacer(StringBuffer buf, String ancien, String nouveau) {
		int lg_ancien = ancien.length();
		try {
			for (int i=buf.length()-lg_ancien; i>=0; i--) {
				if (i+lg_ancien > buf.length()) continue;
				if (ancien.equals(buf.substring(i, i+lg_ancien))) {
					buf.delete(i, i+lg_ancien);
					buf.insert(i, nouveau);
				}
			}
		}
		catch (Exception ex) {
			//ex.printStackTrace();
			System.out.println("erreur remplacer:"+ex.toString());
		}
	}
	
	public static boolean remplacerPredef(StringBuffer buf, String ancien, String nouveau) {
		boolean trouve = false;
		int lg_ancien = ancien.length();
		String ch;
		try {
			for (int i=buf.length()-lg_ancien - 2; i>=2; i--) {
				if (ancien.equalsIgnoreCase(buf.substring(i, i+lg_ancien))) {
					ch = buf.substring(i-1, i);
					if  ( Divers.isLettre(ch) ) continue;
					if  ( Divers.isChiffre(ch) ) continue;
					if  ( ch.equals("_") ) continue;
					if  ( ch.equals("\"") ) continue;
					if  ( ch.equals("'") ) continue;
					ch = buf.substring(i+lg_ancien, i+lg_ancien+1);
					if  ( ch.equals("(") ) {
						buf.delete(i, i+lg_ancien);
						buf.insert(i, nouveau);
						trouve = true;
					}
				}
			}
		}
		catch (Exception ex) {
			//ex.printStackTrace();
			//System.out.println(ex.toString());
		}
		return trouve;
	}
	
	public static void remplacerSpeciaux(StringBuffer buf_xml) {
		Divers.remplacer(buf_xml, "&lt;", "<");
		Divers.remplacer(buf_xml, "&gt;", ">");
		Divers.remplacer(buf_xml, "&apos;", "'");
	}
	
	public static void mettreSpeciaux(StringBuffer buf_xml) {
		StringTokenizer tok = new StringTokenizer(buf_xml.toString(),"\n\r",false);
		buf_xml.delete(0, buf_xml.length());
		while(tok.hasMoreTokens()) {
			String ligne = tok.nextToken();
			int i = ligne.indexOf("<");
			int j = ligne.lastIndexOf(">");
			int n = ligne.length();
			if ((i<0) || (j<0) || (j<i) || (ligne.contains("><"))) {
				buf_xml.append(ligne);
			}
			else {
				buf_xml.append(ligne.substring(0, i+1));
				String mot = new String(ligne.substring(i+1, j));
				mot = Divers.remplacer(mot, "<", "&lt;");
				mot = Divers.remplacer(mot, ">", "&gt;");
				buf_xml.append(mot);
				buf_xml.append(ligne.substring(j, n));
			}
			buf_xml.append("\n");
		}
	}
	
	public static String remplacer(String txt, String ancien, String nouveau) {
		StringBuffer buf = new StringBuffer(txt);
		remplacer(buf,ancien,nouveau);
		return buf.toString();
	}
	
	public static void ecrireAttrXml(StringBuffer buf, String attr, String valeur) {
		if (valeur==null) return;
		Divers.ecrire(buf, " " + attr + "=");
		Divers.ecrire(buf, "\"");
		Divers.ecrire(buf, Divers.remplacer(valeur, "\"", "'"));
		Divers.ecrire(buf, "\"");
	}

	// ------------------------------------------
	// operations sur Fichier
	// ------------------------------------------ 	
	
	public static StringBuffer ouvrir(String nom_f) {
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader in = null;
			if (nom_f.startsWith("http") || nom_f.startsWith("file:")) {
				URL url = new URL(nom_f);
				InputStreamReader  reader =  new InputStreamReader( url.openStream());
				in = new BufferedReader( reader );
			}
			else {
				in = new BufferedReader(new FileReader(nom_f));
			}
			String ligne;
			while ((ligne=in.readLine())!=null) {
				buf.append(ligne + "\n");
			}
		} 
		catch (Exception e) { 
			e.printStackTrace();
			return null;
		}
		return buf;
	}
	
	public static boolean enregistrer(String nom_f, String txt) {
		try {
			PrintStream out = new PrintStream(new FileOutputStream(nom_f));
			out.println(txt);
			out.close();
			return true;
		} 
		catch (Exception e) { return false; }
	}
		
	// ------------------------------------------
	// operations sur TextArea
	// ------------------------------------------ 
	
	public static int getIndent(JTextArea editArea) {
		String txt = editArea.getText();
		int indent = 0;
		try {
			int j = editArea.getCaretPosition() - 1;
			while ((j>0) && (!txt.substring(j, j+1).equals("\n"))) j--;
			j=j+1;
			while (txt.substring(j, j+1).equals("\t")) {
				indent++; j++;
			}
		}
		catch (Exception ex) {}
		return indent;
	}
	
	// ------------------------------------------
	// divers
	// ------------------------------------------
	
	public static boolean isLettre(String ch) {
		if  ( (ch.compareTo("A")>=0) && (ch.compareTo("Z")<=0) ) return true;
		if  ( (ch.compareTo("a")>=0) && (ch.compareTo("z")<=0) ) return true;
		return false;
	}
	
	public static boolean isChiffre(String ch) {
		if  ( (ch.compareTo("0")>=0) && (ch.compareTo("9")<=0) ) return true;
		return false;
	}
	
}
