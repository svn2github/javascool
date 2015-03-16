package org.javascool.gui.editor;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.LanguageAwareCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import org.javascool.core.ProgletEngine;

class JVSAutoCompletionProvider extends AutoCompletion {

	/**
	 * Returns the provider to use when editing code.
	 * 
	 * @return The provider.
	 */
	public static CompletionProvider createCodeCompletionProvider() {
		final DefaultCompletionProvider cp = new DefaultCompletionProvider();
		if (!ProgletEngine.getInstance().getProglet().getCompletion()
				.equals("")) {
			JVSXMLCompletion.readCompletionToProvider(ProgletEngine
					.getInstance().getProglet().getCompletion(), cp);
		}
		JVSXMLCompletion.readCompletionToProvider(
				"org/javascool/gui/editor/completion-macros.xml", cp);
		final LanguageAwareCompletionProvider lacp = new LanguageAwareCompletionProvider(
				cp);
		return lacp;
	}

	public JVSAutoCompletionProvider(RSyntaxTextArea TextPane) {
		super(JVSAutoCompletionProvider.createCodeCompletionProvider());
		install(TextPane);
		setAutoCompleteSingleChoices(false);
		setAutoActivationEnabled(true);
		setAutoActivationDelay(1500);
	}

	@Override
	public void doCompletion() {
		if (isAutoCompleteEnabled()) {
			super.doCompletion();
		}
	}

}
