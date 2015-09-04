package org.javascool.proglets.plurialgo.interaction2;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.fife.ui.rtextarea.RTextArea;
import org.javascool.core.Jvs2Java;
import org.javascool.gui.EditorWrapper;
import org.javascool.tools.FileManager;
import org.javascool.tools.UserConfig;
import org.javascool.widgets.Console;


/**
 * Cette classe expérimentale cherche à améliorer les messages de compilation de Java2Class
 */
public class CompilateurJVS {

	public static void doCompile(PanelInteraction pInter, boolean allErrors) {
		//pInter.setText( CompilateurWrapper.translate() );
		pInter.clearConsole();
		if (doCompile(allErrors)) {
			pInter.clearConsole();
			org.javascool.gui.Desktop.getInstance().compileFile();
			pInter.writeConsole("\nVous pouvez executer votre programme\n");
		}
		else {
			gererErreur(pInter);
			pInter.writeConsole("\nVous devez modifier votre programme\n");
		}
		pInter.writeConsole("\n");
	}
	
	public static void gererErreur(PanelInteraction pInter) {
		javax.swing.JTextArea area = EditorWrapper.getRTextArea();
		int nb_lig = area.getLineCount();
		String txt_cons = Console.getInstance().getText();
		String msg = "Erreur de syntaxe ligne";
		int i = txt_cons.indexOf(msg);
		if (i<0) return;
		int j = txt_cons.indexOf(":", i+msg.length());
		if (j<0) return;
		String ligne = txt_cons.substring(i+msg.length(), j);
		try {
			int lig = Integer.parseInt(ligne.trim());
			if (lig<=nb_lig) {
				int debut_lig = area.getLineStartOffset(lig-1);	// premiere ligne : ligne 0	
				area.setCaretPosition(debut_lig);
			}
			else {
				int debut_lig = area.getLineStartOffset(nb_lig-1);	
				area.setCaretPosition(debut_lig);
				txt_cons = txt_cons.substring(0,i) + msg + " " + nb_lig + txt_cons.substring(j);	
				pInter.clearConsole();	
				pInter.writeConsole(txt_cons);	
				txt_cons = Console.getInstance().getText();
			}
		}
		catch(NumberFormatException ex) {
			return;
		}
		catch(Exception ex) {
			return;
		}
	}

	public static boolean doCompile(boolean allErrors) {
		RTextArea area = EditorWrapper.getRTextArea();
		if (!area.getText().contains(" main(")) {
			area.append("\n");
			area.append("void main() {");
			area.append("}");
		}
		Jvs2Java jvs2java = new Jvs2Java();
		String javaCode = jvs2java.translate( org.javascool.gui.EditorWrapper.getText() );
		String javaFile;
		try {
			File buildDir = UserConfig.createTempDir("javac");
			javaFile = buildDir + File.separator + jvs2java.getClassName() + ".java";
			FileManager.save(javaFile, javaCode);
	        // Si il y a un problème avec le répertoire temporaire on se rabat sur le répertoire local
		} catch(Exception e1) {
			try {
				javaFile = new File(jvs2java.getClassName() + ".java").getAbsolutePath();
				System.err.println("Sauvegarde locale du fichier : " + javaFile);
				FileManager.save(javaFile, javaCode);
	          // Sinon on signale le problème à l'utilisateur
			} catch(Exception e2) {
				System.out.println("Attention ! le répertoire '" + System.getProperty("user.dir") + "' ne peut etre utilisé pour sauver des fichiers, \n il faut re-lancer javascool dans un répertoire de travail approprié.");
				return false;
	        }
		}
		String javaFiles[] = { javaFile };
		if(compile(javaFiles, allErrors)) {
			try {
				//runnable = Java2Class.load(javaFile);
				return true;
			} catch(Exception e3) {
				System.out.println("Attention ! il y a eu une action externe de nettoyage de fichiers temporaires ou le répertoire '" + new File(javaFile).getParent() + "' ne peut etre utilisé pour sauver des fichiers, \n il faut re-lancer javascool dans un répertoire de travail approprié.");
				return false;
			}
		} else {
			//runnable = null;
			return false;
		}
	}

	public static boolean compile(String javaFiles[], boolean allErrors) {
		// Appel du compilateur par sa methode main
		int options = 2;
		String args[] = new String[options + javaFiles.length];
		args[0] = "-g";
		args[1] = "-nowarn";
		System.arraycopy(javaFiles, 0, args, options, javaFiles.length);
		StringWriter out = new StringWriter();
		Method javac;
		try {
			javac = Class.forName("com.sun.tools.javac.Main").
					getDeclaredMethod("compile", Class.forName("[Ljava.lang.String;"), Class.forName("java.io.PrintWriter"));
		} catch(Exception e) { 
			throw new IllegalStateException("Impossible d'acceder au compilateur javac : " + e);
		}
		try {
			javac.invoke(null, (Object) args, new PrintWriter(out));
		} catch(Exception e) { 
			throw new IllegalStateException("Erreur systeme lors du lancement du compilateur javac : " + e);
		}
		// Traitement du message de sortie
		{
			String sout = out.toString().trim();
			if (sout.length() > 0)
				System.err.println("Notice: Java compilation message : "+sout);
			// Coupure à la premiere erreur
			if((sout.indexOf("^") != -1) && !allErrors) {
				sout = sout.substring(0, sout.indexOf("^") + 1);
			}
			if(javaFiles.length > 1) {
				// Remplacement des chemins des sources par leur simple nom
				for(String javaFile : javaFiles)
					sout = sout.replaceAll(Pattern.quote(new File(javaFile).getParent() + File.separator), "\n");
				// Explicitation du numero de ligne
				for(String javaFile : javaFiles)
					sout = sout.replaceAll("(" + Pattern.quote(new File(javaFile).getName()) + "):([0-9]+):", "$1 : erreur de syntaxe ligne $2 :\n ");
			} else {
				sout = sout.replaceAll("(" + Pattern.quote(new File(javaFiles[0]).getPath()) + "):([0-9]+):", "\n Erreur de syntaxe ligne $2 :\n ");
				sout = sout.replaceAll(Pattern.quote(new File(javaFiles[0]).getName().replaceFirst("java$", "")), "");
			}
			// Escape des constructions derivee d' un Translator
			sout = sout.replaceAll("/\\*(.*)@<nojavac.*@nojavac>\\*/", "$1");
			// Passage en français des principaux diagnostics
			sout = sout.replaceAll("not a statement",
					"L'instruction n'est pas valide.\n (Il se peut qu'une variable indiquee n'existe pas)");
			sout = sout.replaceAll("';' expected",
					"Un ';' est attendu ");
			sout = sout.replaceAll("cannot find symbol\\s*symbol\\s*:\\s*([^\\n]*)[^:]*:\\s*(.*)",
					"Il y a un symbole non-defini");
			sout = sout.replaceAll("illegal start of expression",
					"($0) L'instruction (ou la precedente) est tronquee ou mal ecrite");
			sout = sout.replaceAll("class, interface, or enum expected",
					"($0) Il y a probablement une erreur dans les accolades");
			sout = sout.replaceAll("'.class' expected",
					"($0) Il manque des accolades ou des parentheses pour definir l'instruction");
			sout = sout.replaceAll("incompatible\\Wtypes\\W*found\\W*:\\W([A-Za-z\\.]*)\\Wrequired:\\W([A-Za-z\\.]*)",
					"Vous avez mis une valeur de type $1 alors qu'il faut une valeur de type $2");
			// Elimination des notes de warning de fin de compilation
			if(sout.indexOf("Note:") != -1) {
				sout = sout.substring(0, sout.indexOf("Note:")).trim();
			}
			// Impression du message d'erreur si il existe et retour du statut
			if(sout.length() > 0) {
				System.out.println(sout);
			}
			return sout.length() == 0;
		}
	}	

	public static String translate() {
		// Traduction Jvs -> Java 
		Jvs2Java jvs2java = new Jvs2Java();
		return jvs2java.translate( EditorWrapper.getText() );		
	}

//	public void compilerPlus() {
//		Console.getInstance().clear();
//		String contenu_courant = org.javascool.gui.EditorWrapper.getText();
//		RTextArea area = org.javascool.gui.EditorWrapper.getRTextArea();
//		if (!contenu_courant.contains(" main(")) {
//			area.append("\n");
//			area.append("void main() {");
//			area.append("println(\"Pensez au 'void main()' !\");");
//			area.append("}");
//			Desktop.getInstance().saveCurrentFile();
//			Desktop.getInstance().compileFile();
//			org.javascool.gui.EditorWrapper.setText(contenu_courant);
//			Desktop.getInstance().saveCurrentFile();
//		}
//		else if (contenu_courant.contains("//@import")) {
//			int rep = javax.swing.JOptionPane.showConfirmDialog(null, "Actualiser les importations ?");
//			if (rep==javax.swing.JOptionPane.NO_OPTION) {
//				Desktop.getInstance().saveCurrentFile();
//				Desktop.getInstance().compileFile();
//			}
//			else if (rep==javax.swing.JOptionPane.YES_OPTION) {
//				org.javascool.gui.EditorWrapper.setText(contenu_courant.substring(0, contenu_courant.indexOf("//@import")));
//				Map<String,String> others = org.javascool.gui.EditorWrapper.getOthers();
//				Iterator<String> iter = others.keySet().iterator();
//				while (iter.hasNext()) {
//					String nom_fich = iter.next();
//					String contenu = others.get(nom_fich);
//					if (contenu.contains(" main(")) continue;
//					area.append("\n");
//					area.append("//@import : " + nom_fich + "\n");
//					area.append(contenu);
//					area.append("\n");
//				}
//				Desktop.getInstance().saveCurrentFile();
//				Desktop.getInstance().compileFile();
//			}
//		}
//		else {
//			Map<String,String> others = org.javascool.gui.EditorWrapper.getOthers();
//			Iterator<String> iter = others.keySet().iterator();
//			while (iter.hasNext()) {
//				String nom_fich = iter.next();
//				String contenu = others.get(nom_fich);
//				if (contenu.contains(" main(")) continue;
//				area.append("\n");
//				area.append("//@import : " + nom_fich + "\n");
//				area.append(contenu);
//				area.append("\n");
//			}
//			Desktop.getInstance().saveCurrentFile();
//			Desktop.getInstance().compileFile();
//		}
//	}

}