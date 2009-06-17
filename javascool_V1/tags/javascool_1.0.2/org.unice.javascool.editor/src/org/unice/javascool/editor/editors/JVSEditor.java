package org.unice.javascool.editor.editors;

import java.net.URI;
import java.net.URISyntaxException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.PaintManager;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.ISourceViewerExtension2;
import org.eclipse.jface.text.source.MatchingCharacterPainter;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.unice.javascool.editor.Activator;



/**
 * cette classe permet de definir l'editeur de code javascool
 * rt contient toutes les methodes necessaire a sa definition
 */
public class JVSEditor extends TextEditor{

	public static final String ID = "org.unice.javascool.editor.editors.JVSEditor";
	private ColorManager colorManager;
	protected MatchingCharacterPainter fBracketPainter;
	protected PaintManager fPaintManager;

	

	/**
	 * le constructeur de de l'editeur
	 */
	public JVSEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new JVSConfiguration(colorManager));
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//setDocumentProvider(new JVSDocumentProvider()); 
	}

	@Override
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		super.handlePreferenceStoreChanged(event);
		colorManager = new ColorManager() ;
		((ISourceViewerExtension2) getSourceViewer2()).unconfigure(); 
		getSourceViewer().configure(new JVSConfiguration(colorManager)); 
		
	}

	/**
	 * cette methode nous permet de recuprer le code source present
	 * dans l'editeur
	 * @return le code source de l'editeur
	 */
	public ISourceViewer getSourceViewer2(){
		return getSourceViewer();
	}

	/**
	 * cette methode permet de connaitre la position du curseur dans l'editeur
	 * @return la position x du curseur dans 'editeur
	 */
	public int getOffset(){
		return getSourceViewer().getSelectedRange().x;		
	}

	/**
	 * cette methode permet d'inserer ue chaine de caractere a une certaine position dans le texte
	 * de l'editeur
	 * @param s la chaine de caractere que l'on souhaite inserer
	 * @param offset la position a laquelle on souhaite inserer le texte dand l'editeur
	 */
	public void insertText(String s, int offset){
		IDocument doc = getSourceViewer().getDocument();
		try {
			doc.replace(offset, 0, s);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}


	/**
	 * cette methode nous permet de connaitre le path du fichier associe a l'editeur courant
	 * @return le path du fichier ouvert dans l'editeur
	 */
	public String getFilePath(){
		URI uri=null;
		Path res=null;
		IEditorInput input=getEditorInput();
		if(input instanceof FileStoreEditorInput){
			uri=((FileStoreEditorInput)input).getURI();
		}else{
			try {
				uri = new URI(getTitleToolTip());
			} catch (URISyntaxException e) {
				return getTitleToolTip();
			}
		}
		String path=uri.getPath();
		res=new Path(path);
		return res.toOSString();
	}


	public void startBracketHighlighting() {
		if (fBracketPainter == null) {
			fBracketPainter = new MatchingCharacterPainter(getSourceViewer(),new JVSPairMatcher());
			fBracketPainter.setColor(new Color(Display.getCurrent(), new RGB(127, 0, 85)));
			fPaintManager.addPainter(fBracketPainter);
		}
	}

	public void stopBracketHighlighting() {
		if (fBracketPainter != null) {
			fPaintManager.removePainter(fBracketPainter);
			fBracketPainter.deactivate(true);
			fBracketPainter.dispose();
			fBracketPainter = null;
		}
	}

	protected void createActions() {
		super.createActions();
		fPaintManager = new PaintManager(getSourceViewer());
		startBracketHighlighting();
	}

	/*
	protected void configureSourceViewerDecorationSupport(SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		//Enhance the stock source viewer decorator with a bracket matcher
	}
	 */

	/**
	 * cette methode permet de detruire l'editeur
	 */
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}






}
