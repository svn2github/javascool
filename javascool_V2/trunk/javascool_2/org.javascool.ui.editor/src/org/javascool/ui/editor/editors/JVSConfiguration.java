package org.javascool.ui.editor.editors;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.javascool.ui.editor.Activator;
import org.javascool.ui.editor.preferences.PreferencesConstants;

public class JVSConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;
	private JVSTagScanner tagScanner;
	private JVSScanner scanner;
	private ColorManager colorManager;

	public JVSConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			JVSPartitionScanner.JAVA_COMMENT,
			//TODO a voir XMLPartitionScanner.XML_TAG 
			};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
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
								//TODO old JVSColorConstants.DEFAULT))));
		}
		return scanner;
	}
	
	
	protected JVSTagScanner getJVSTagScanner() {
		if (tagScanner == null) {
			tagScanner = new JVSTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
							colorManager.getColor(
									PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore()
											, PreferencesConstants.P_DEFAULT)))));
	
						//colorManager.getColor(JVSColorConstants.JVSColorConstants))));
		}
		return tagScanner;
	}

	
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
		/*TODO old
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IXMLColorConstants.JVSColorConstants)));
		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

		return reconciler;
		*/
	}
	
	//TODO add by me
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { new JVSIdentStrategy() };
	}
	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return new String[] { "\t", "        " };
	}

}