package org.javascool.ui.editor.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;
import org.eclipse.swt.SWT;
import org.javascool.ui.editor.Activator;
import org.javascool.ui.editor.preferences.PreferencesConstants;


public class JVSScanner extends RuleBasedScanner {

	/** Mots cles du langage java */
	public static final String[] fgKeyword = new String[] {
		"abstract", "assert", "break", "case", "catch",
		"class", "const", "continue","default", "do",  "else",
		"extends", "false", "final", "finally", "float", "for", "goto", "if",
		"implements", "import", "instanceof",  "interface",  "native",
		"new", "null", "package", "private", "protected", "public", "return",
		"static", "strictfp", "super", "switch", "synchronized", "this",
		"throw", "throws", "transient", "true", "try", "void", "volatile", "while",
	};


	/*Macro**/
	/*TODO revenir ici
	public static String[] fgMacro;
	static{
		try {
			ArrayList<BeanMacros> macros = BeanFactory.getBeanMacros(BeanFactory.macConfFile);
			fgMacro  = new String[macros.size()];
			for(int i=0; i< macros.size(); i++)
				fgMacro[i] = macros.get(i).getNom();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	/*types*/
	public static final String[] fgType = new String[] {
		"boolean", "byte", "char", "double", "int", "long","short", "String",
	};


	public JVSScanner(ColorManager manager) {

		IToken string = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_STRING))));
					
		IToken macro = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_MACRO)),null, SWT.BOLD));
		
		IToken keyword = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_KEYWORD)),null, SWT.BOLD));
		
		IToken type = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_TYPE)),null, SWT.BOLD));
		
		IToken other = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_DEFAULT))));
		
		IToken number = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_NUMBER))));
		
		IToken comment = new Token(new
				TextAttribute(manager.getColor(
						PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
								, PreferencesConstants.P_COMMENT))));
		
	

		WordRule wr = new WordRule(new IWordDetector() {
			//@Override
			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}

			//@Override
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}			
		}, other);




		List<Object> rules = new ArrayList<Object>();		

		//regles pour les chaines
		rules.add(new SingleLineRule("\"", "\"", string, '\0', true));
		rules.add(new SingleLineRule("'", "'", string, '\0', true));
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new JVSWhitespaceDetector()));

		rules.add(new SingleLineRule("'", "'", string, '\0', true));
		
		rules.add(new MultiLineRule("/*", "*/", comment));
		rules.add(new EndOfLineRule("//", comment));

		//regle pour les mots cles
		for(int i = 0 ; i < fgKeyword.length ; ++i) {
			wr.addWord(fgKeyword[i], keyword);
		}

		/*TODO revenir ici
		//regles pour les types
		for(int i = 0 ; i < fgMacro.length ; ++i) {
			wr.addWord(fgMacro[i], macro);
		}
*/
		//regles pour les macros
		for(int i = 0 ; i < fgType.length ; ++i) {
			wr.addWord(fgType[i],type);
		}

		rules.add(wr);

		rules.add(new NumberRule(number));

		// Conversion de la List en tableau pour la passer a la methode setRules
		IRule[] param = new IRule[rules.size()];
		rules.toArray(param);

		setRules(param);
	}
}