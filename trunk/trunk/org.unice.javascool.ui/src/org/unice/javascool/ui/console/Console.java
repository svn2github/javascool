package org.unice.javascool.ui.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.unice.javascool.ui.console.input.Input;

public class Console extends IOConsole{

	public final static String ID = "javaSchool.Console";
	private InputStream input=null;

	/**
	 * Some useful colors.
	 */
	private static final Color RED;
	private static final Color BLUE;
	static {
		Display device = Display.getCurrent();
		RED = new Color(device, 255, 0, 0);
		BLUE = new Color(device, 0, 0, 128);
	}

	public Console() {
		super("Java's cool console", null);

		IOConsoleOutputStream out = newOutputStream(); 
		out.setColor(BLUE);
		System.setOut(new PrintStream(out));

		IOConsoleOutputStream err = newOutputStream(); 
		err.setColor(RED);
		System.setErr(new PrintStream(err));
		input=getInputStream();
		System.setIn(getInputStream());
		Input.Initialize();
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{ this });
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(this);
	}
}