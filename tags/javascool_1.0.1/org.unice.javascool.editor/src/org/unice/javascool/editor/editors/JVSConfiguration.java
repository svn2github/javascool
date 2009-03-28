package org.unice.javascool.editor.editors;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.unice.javascool.editor.Activator;
import org.unice.javascool.editor.preferences.PreferencesConstants;

public class JVSConfiguration extends SourceViewerConfiguration {
	private ColorManager colorManager;
	private JVSScanner scanner;

	/**constructor
	 * @param colorManager
	 **/
	public JVSConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			JVSPartitionScanner.JAVA_COMMENT,
			//JVSPartitionScanner.XML_TAG };
		};
	}
	protected JVSScanner getJVSScanner() {
		if (scanner == null) {
			scanner = new JVSScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
							colorManager.getColor(
									PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
											, PreferencesConstants.P_DEFAULT)))));
		}
		return scanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getJVSScanner());
		//DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new JVSScanner(colorManager));
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
					new TextAttribute(
							colorManager.getColor(
									PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
											, PreferencesConstants.P_COMMENT))));
		reconciler.setDamager(ndr, JVSPartitionScanner.JAVA_COMMENT);
		reconciler.setRepairer(ndr, JVSPartitionScanner.JAVA_COMMENT);
		return reconciler;
	
	}
	
	
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { new JVSIdentStrategy() };
	}
	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return new String[] { "\t", "        " };
	}

}