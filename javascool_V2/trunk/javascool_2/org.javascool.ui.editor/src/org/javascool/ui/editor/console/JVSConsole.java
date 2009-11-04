/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.console;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;




public class JVSConsole extends MessageConsole{

	public static final String ID = "org.javascool.ui.editor.console.consoleJVS";
	
	private static final Color RED  = new Color(Display.getCurrent(), 255, 0, 0);
	private static final Color BLUE = new Color(Display.getCurrent(), 0, 0, 128);

	
	public JVSConsole() {
		super("titre console", null);

		IOConsoleOutputStream out = newOutputStream(); 
		out.setColor(BLUE);
		System.setOut(new PrintStream(out));

		IOConsoleOutputStream err = newOutputStream(); 
		err.setColor(RED);
		System.setErr(new PrintStream(err));


	}
}
