package org.javascool.ui.editor.preferences;


/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.javascool.ui.editor.Activator;
import org.javascool.ui.editor.editors.JVSColorConstants;


/**
 * cette classe permet de definir l'initialisteur des pages de preferences
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	
	
	public PreferenceInitializer() {
		super();
	}

	@Override
	public void initializeDefaultPreferences() {

		final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		PreferenceConverter.setDefault(store, PreferencesConstants.P_COMMENT, JVSColorConstants.JAVA_COMMENT);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_DEFAULT, JVSColorConstants.DEFAULT);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_KEYWORD, JVSColorConstants.PROC_INSTR);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_MACRO, JVSColorConstants.MACRO);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_NUMBER, JVSColorConstants.NUMBER);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_STRING, JVSColorConstants.STRING);
		PreferenceConverter.setDefault(store, PreferencesConstants.P_TYPE, JVSColorConstants.TYPE);
		
	}
}