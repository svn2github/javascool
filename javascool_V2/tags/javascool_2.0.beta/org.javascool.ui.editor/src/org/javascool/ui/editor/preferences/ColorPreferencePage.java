/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.preferences;


import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.javascool.ui.editor.Activator;

/**
 *Cette classe permet de definir la page des preferences de la coloration 
 *syntaxique de notre editeur 
 */
public class ColorPreferencePage extends FieldEditorPreferencePage
implements IWorkbenchPreferencePage {

	public static final String ID ="org.javascool.ui.editor.preferences.ColorPreferencePage";
	
	private ColorFieldEditor color_comment;
	private ColorFieldEditor color_keyword;
	private ColorFieldEditor color_string;
	private ColorFieldEditor color_default;
	private ColorFieldEditor color_macro;
	private ColorFieldEditor color_type;
	public static ColorFieldEditor color_number; 

	public ColorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Cette page permet de modifier les preferences de coloration " +
				"syntaxique des differents mots cles de l'editeur de code javascool");  

	}

	@Override
	public void init(IWorkbench workbench){
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	
	/**
	 * cette methode defini la page de preference
	 */
	protected void createFieldEditors() {

		color_comment = 
			new ColorFieldEditor(PreferencesConstants.P_COMMENT, "couleur des commentaires : ", getFieldEditorParent());
		color_keyword = 
			new ColorFieldEditor(PreferencesConstants.P_KEYWORD, "couleur des mots clefs : ", getFieldEditorParent());
		color_string = 
			new ColorFieldEditor(PreferencesConstants.P_STRING, "couleur des chaine de caractere : ", getFieldEditorParent());
		color_default =
			new ColorFieldEditor(PreferencesConstants.P_DEFAULT, "couleur du texte : ", getFieldEditorParent());
		color_macro = 
			new ColorFieldEditor(PreferencesConstants.P_MACRO, "couleur des macros fonctions : ", getFieldEditorParent());
		color_type =
			new ColorFieldEditor(PreferencesConstants.P_TYPE, "couleur des types : ", getFieldEditorParent());
		color_number = 
			new ColorFieldEditor(PreferencesConstants.P_NUMBER, "couleur des nombres : ", getFieldEditorParent());

		addField(color_comment);
		addField(color_default);
		addField(color_keyword);
		addField(color_macro);
		addField(color_number);
		addField(color_string);
		addField(color_type);
		
	}

	@Override
	public void performApply(){
		super.performApply();
	}

	@Override
	public boolean performOk() {
		
		IPreferenceStore currentPreferenceStore = getPreferenceStore();
		
		currentPreferenceStore.setValue(PreferencesConstants.P_COMMENT, StringConverter.asString(
				color_comment.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_DEFAULT, StringConverter.asString(
				color_default.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_KEYWORD, StringConverter.asString(
				color_keyword.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_MACRO, StringConverter.asString(
				color_macro.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_NUMBER, StringConverter.asString(
				color_number.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_STRING, StringConverter.asString(
				color_string.getColorSelector().getColorValue()));
		currentPreferenceStore.setValue(PreferencesConstants.P_TYPE, StringConverter.asString(
				color_type.getColorSelector().getColorValue()));

		return true;
	}
	
	
}
