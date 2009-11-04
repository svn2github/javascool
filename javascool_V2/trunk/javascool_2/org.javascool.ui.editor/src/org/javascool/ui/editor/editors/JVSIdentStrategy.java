/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.editors;

/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/



import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextUtilities;



/**
 * This strategy always copies the indentation of the previous line.
 * <p>
 * This class is not intended to be subclassed.</p>
 *
 * @since 3.1
 */
public class JVSIdentStrategy implements IAutoEditStrategy {

	final String spaceIndentation = "\t"; 

	/**
	 * Creates a new indent line auto edit strategy which can be installed on
	 * text viewers.
	 */
	public JVSIdentStrategy() {
	}


	private int getBracketCount(IDocument d, int start, int end, boolean ignoreCloseBrackets) throws BadLocationException {
		int bracketcount = 0;
		while (start < end) {
			char curr = d.getChar(start);
			start++;
			switch (curr) {
			case '/' :
				if (start < end) {
					char next = d.getChar(start);
					if (next == '*') {
						// a comment starts, advance to the comment end
						start = getCommentEnd(d, start + 1, end);
					} else if (next == '/') {
						// '//'-comment: nothing to do anymore on this line 
						start = end;
					}
				}
				break;
			case '*' :
				if (start < end) {
					char next = d.getChar(start);
					if (next == '/') {
						// we have been in a comment: forget what we read before
						bracketcount = 0;
						start++;
					}
				}
				break;
			case '{' :
				bracketcount++;
				ignoreCloseBrackets = false;
				break;
			case '}' :
				if (!ignoreCloseBrackets) {
					bracketcount--;
				}
				break;
			case '"' :
			case '\'' :
				start = getStringEnd(d, start, end, curr);
				break;
			default :
			}
		}
		return bracketcount;
	}

	// ----------- bracket counting ------------------------------------------------------

	private int getCommentEnd(IDocument d, int pos, int end) throws BadLocationException {
		while (pos < end) {
			char curr = d.getChar(pos);
			pos++;
			if (curr == '*') {
				if (pos < end && d.getChar(pos) == '/') {
					return pos + 1;
				}
			}
		}
		return end;
	} 


	private int getStringEnd(IDocument d, int pos, int end, char ch) throws BadLocationException {
		while (pos < end) {
			char curr = d.getChar(pos);
			pos++;
			if (curr == '\\') {
				// ignore escaped characters
				pos++;
			} else if (curr == ch) {
				return pos;
			}
		}
		return end;
	}



	/**
	 * Returns the first offset greater than <code>offset</code> and smaller than
	 * <code>end</code> whose character is not a space or tab character. If no such
	 * offset is found, <code>end</code> is returned.
	 *
	 * @param document the document to search in
	 * @param offset the offset at which searching start
	 * @param end the offset at which searching stops
	 * @return the offset in the specified range whose character is not a space or tab
	 * @exception BadLocationException if position is an invalid range in the given document
	 */
	protected int findEndOfWhiteSpace(IDocument document, int offset, int end) throws BadLocationException {
		while (offset < end) {
			char c= document.getChar(offset);
			if (c != ' ' && c != '\t') {
				return offset;
			}
			offset++;
		}
		return end;
	}


	private void indentation(int nbBracket,IDocument d,StringBuffer buf){
		for (int i = 0  ; i < nbBracket ; ++i){
			buf.append(spaceIndentation);
		}
	}



	/**
	 * Copies the indentation of the previous line.
	 *
	 * @param d the document to work on
	 * @param c the command to deal with
	 */
	//TODO : Repositionner le curseur
	private void autoIndentAfterNewLine(IDocument d, DocumentCommand c) {

		if (c.offset == -1 || d.getLength() == 0)
			return;
		
		
		try {
			
			int nbBracket = getBracketCount(d,0,c.offset,false);
			StringBuffer buf = new StringBuffer(c.text);
			
			//sert si jamais il n'y a pas d'autre character 
			/*char nextCharacter = ' ';
			if (d.getLength() >= c.offset+1){
				nextCharacter = d.get(c.offset, 1).charAt(0);
			}*/

			//reflechir a un meilleur moyen
			//ajout d'un bracket manquant
			if (('{' == d.get(c.offset-1, 1).charAt(0)) && (getBracketCount(d,0,d.getLength(),false) != 0)){
				
				indentation(nbBracket,d,buf);
				buf.append("\n");

				//Ajout du bracket
				--nbBracket ;
				indentation(nbBracket,d,buf);
				buf.append("}");
				
			}
			else {
				indentation(nbBracket,d,buf);
			}
			
			c.text = buf.toString();
			
		} catch (BadLocationException excp) {
			// stop work
		}
	}

	/*
	 * @see org.eclipse.jface.text.IAutoEditStrategy#customizeDocumentCommand(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.DocumentCommand)
	 */
	public void customizeDocumentCommand(IDocument d, DocumentCommand c) {
		if (c.length == 0 && c.text != null && TextUtilities.endsWith(d.getLegalLineDelimiters(), c.text) != -1){
			autoIndentAfterNewLine(d, c);
		}
	}
}

