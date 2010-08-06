/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package org.javascool;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JApplet;
import java.applet.AppletContext;

import javax.swing.JFrame;
import java.awt.Point;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import java.net.URLClassLoader;

/** Defines the main javascool proglet interface of the javasccol/proglet v2 interface.
 * File used: <pre>
 * org/javascool/doc-files/load.png
 * org/javascool/doc-files/save.png
 * org/javascool/doc-files/compile.png
 * org/javascool/doc-files/execute.png
 * org/javascool/doc-files/stop.png
 * org/javascool/doc-files/demo.png
 * </pre>
 * @see <a href="../../org/javascool/MainV2.java">source code</a>
 */
public class MainV2 extends JApplet {
  private static final long serialVersionUID = 1L;

  //
  // Cette partie définie le paramétrage de l'interface graphique
  //

  /** Sets the proglet to use in this interface.
   * @param proglet The proglet class name.
   */
  public void setProglet(String proglet) { getJProgletButton(); jProgletBox.setSelectedItem(this.proglet = proglet); }  private String proglet = "ingredients";
	
  /** Sets the mode to use in this interface.
   * @param edit If true in edit mode, else in run mode.
   */
  void setEdit(boolean edit) { this.edit = edit; } private boolean edit = false;
  
  // Sets the class name and file
  private void setMainFile(String pFile) {
    File file = new File(pFile);
    String folder = file.getParent() == null ? System.getProperty("user.dir") : file.getParent();
    String name = file.getName().replaceAll("\\.[A-Za-z]+$", "");
    if (Jvs2Java.isReserved(name)) {
      main = "my_"+name;
      printConsole("Attention: le nom \""+name+"\" est interdit par Java,\n renommons le \""+main+"\"", 'b');
    } else if (!name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
      main = name.replaceAll("[^A-Za-z0-9_]", "_");
      printConsole("Attention: le nom \""+name+"\" contient quelque caractère interdit par Java,\n renommons le \""+main+"\"", 'b');
    } else
      main = name;
    path = folder + File.separatorChar + main;
  }  
  private String main = null, path = null;

  /**This is the entry point to run the proglet pupil's program: do not modify manually !!. */
  public static Runnable runnable = null;
  
  // Flag whether we are in standalone mode or web-browser mode
  boolean standalone = true; 

  // Flags whether we have derived an application derivating this class or if this class hase been constructed.
  boolean application = true; 

  private void initParameters() {
    { try { if (getParameter("proglet") != null) setProglet(getParameter("proglet")); } catch(Exception e) { } }
    { try { if (getParameter("edit") != null) setEdit(true); } catch(Exception e) { } }
    { try { if (getParameter("path") != null) doLire(getParameter("path")+".jvs");  } catch(Exception e) { } }
    { try { standalone = getAppletContext() == null; } catch(Exception e) { } }
  }

  //
  // Cette partie définie l'interface graphique
  //

  private JPanel jContentPane = null;
  private JPanel jMenuPanel = null;
  private JPanel jProgletButton = null;
  private JComboBox jProgletBox = null;
  private JButton jOpenButton = null;
  private JButton jSaveButton = null;
  private JButton jCompileButton = null;
  private JButton jRunButton = null;
  private JButton jStopButton = null;
  private JButton jDemoButton = null;
  private JPanel jProgramPanel = null;
  private JvsSourceEditor jProgramEditorPane = null;
  private JPanel jResultPanel = null;
  private JPanel  jConsolePanel = null;
  private JScrollPane jScrollPane = null;
  private JScrollPane jConsoleScrollPane = null;
  private JEditorPane jConsoleTextPane = null;
  private JFileChooser fileChooser=null;

  public void init() {
    initParameters();
    this.setContentPane(getJContentPane());
  }

  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getJMenuPanel(), null);
      if (edit) {
	jContentPane.add(getJProgramPanel(), null);
	jContentPane.add(getJConsolePanel(), null);
      } else {
	jContentPane.add(getJResultPanel(), null);
      }
    }
    return jContentPane;
  }

  private JPanel getJMenuPanel() {
    if (jMenuPanel == null) {
      jMenuPanel = new JPanel();
      jMenuPanel.setLayout(new FlowLayout());
      jMenuPanel.setBorder(BorderFactory.createTitledBorder(null, "Commandes", 
         TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jMenuPanel.setBounds(new Rectangle(9, 7, 640, 70));
      if (edit) {
	jMenuPanel.add(getJOpenButton(), null);
	jMenuPanel.add(getJSaveButton(), null);
	jMenuPanel.add(getJCompileButton(), null);
	jMenuPanel.add(getJProgletButton(), null);
      } else {
	if (application) {
	  jMenuPanel.add(getJRunButton(), null);
	  jMenuPanel.add(getJStopButton(), null);
	}
	jMenuPanel.add(new JLabel(" "), null);
	jMenuPanel.add(getJDemoButton(), null);
      }
    }
    return jMenuPanel;
  }

  private JPanel getJProgletButton() {
    if (jProgletButton == null) {
      jProgletButton = new JPanel();
      jProgletButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
      JLabel jProgletLabel = new JLabel();
      jProgletLabel.setIcon(Utils.getIcon("org/javascool/doc-files/execute.png"));
      jProgletButton.add(jProgletLabel);
      jProgletBox = new JComboBox(Jvs2Java.proglets.keySet().toArray());
      jProgletBox.setEditable(false);
      jProgletBox.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    proglet = (String) jProgletBox.getSelectedItem();
	    getJProgramEditorPane().setProglet(proglet); 
	  }
	});
      jProgletButton.add(jProgletBox, null);
    }
    return jProgletButton;
  }

  private JButton getJOpenButton() {
    if (jOpenButton == null) {
      jOpenButton = new JButton();
      jOpenButton.setIcon(Utils.getIcon("org/javascool/doc-files/load.png"));
      jOpenButton.setText("Ouvrir");
      jOpenButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    getFileChooser().setDialogTitle("Ouvrir un programme");
	    getFileChooser().setDialogType(JFileChooser.OPEN_DIALOG);
	    getFileChooser().setApproveButtonText("Ouvrir");
	    int value = getFileChooser().showOpenDialog(null);
	    if (value == 0){
	      try { 
		String check = getFileChooser().getSelectedFile().getPath();
		doLire(check); 
	      } catch(Exception e1){ 
		Utils.report(e1);
	      }
	    }
	  }
	});
    }
    return jOpenButton;
  }

  private JButton getJSaveButton() {
    if (jSaveButton == null) {
      jSaveButton = new JButton();
      jSaveButton.setIcon(Utils.getIcon("org/javascool/doc-files/save.png"));
      jSaveButton.setText("Enregister");
      jSaveButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    doSaveAs();
	  }
        });
    }
    return jSaveButton;
  }

  private void doSaveAs() {
    getFileChooser().setDialogTitle("Enregister un programme");
    getFileChooser().setDialogType(JFileChooser.SAVE_DIALOG);
    getFileChooser().setApproveButtonText("Enregister");
    int value = getFileChooser().showSaveDialog(null);
    if (value == 0) {
      try { 
	String check = getFileChooser().getSelectedFile().getPath();
	doSave(check); 
      } catch(Exception e1){ 
	Utils.report(e1);
      }
    }
  }
	
  private JButton getJCompileButton() {
    if (jCompileButton == null) {
      jCompileButton = new JButton();
      jCompileButton.setMnemonic(KeyEvent.VK_UNDEFINED);
      jCompileButton.setText("Compiler");
      jCompileButton.setIcon(Utils.getIcon("org/javascool/doc-files/compile.png"));
      jCompileButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doSave();
	      doCompile();
	    } catch (Exception e1) {
	      Utils.report(e1);
	    }
	  }});
    }
    return jCompileButton;
  }

  private JButton getJRunButton() {
    if (jRunButton == null) {
      jRunButton = new JButton();
      jRunButton.setIcon(Utils.getIcon("org/javascool/doc-files/execute.png"));
      jRunButton.setText("Executer");
      jRunButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      if (runnable != null) 
		doRun(new Runnable() { public void run() {
		  try {
		    runnable.run();
		  }  catch (Exception e) {
		    Utils.report(e);
		  }
		}});
	    } catch (Exception e1) {
	      Utils.report(e1);
	    }
	  }});
    }
    return jRunButton;
  }

  private JButton getJStopButton() {
    if (jStopButton == null) {
      jStopButton = new JButton();
      jStopButton.setIcon(Utils.getIcon("org/javascool/doc-files/stop.png"));
      jStopButton.setText("Arrêter");
      jStopButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doStop();
	    } catch (Exception e1) {
	      Utils.report(e1);
	    }
	  }});
    }
    return jStopButton;
  }

  private JButton getJDemoButton() {
    if (jDemoButton == null) {
      jDemoButton = new JButton();
      jDemoButton.setIcon(Utils.getIcon("org/javascool/doc-files/demo.png"));
      jDemoButton.setText("Demo");
      jDemoButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doRun(new Runnable() { public void run() {
		try {
		  test(proglet);
		}  catch (Exception e) {
		  Utils.report(e);
		}
	      }});
	    } catch (Exception e1) {
	      Utils.report(e1);
	    }
	  }});
    }
    return jDemoButton;
  }

  private JPanel getJProgramPanel() {
    if (jProgramPanel == null) {
      jProgramPanel = new JPanel();
      jProgramPanel.setLayout(null);
      jProgramPanel.setBounds(new Rectangle(11, 92, 540, 398));
      jProgramPanel.setBorder(BorderFactory.createTitledBorder(null, "Programme", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jProgramPanel.add(getJProgramEditorPane());
    }
    return jProgramPanel;
  }

  private JvsSourceEditor getJProgramEditorPane() {
    if (jProgramEditorPane == null) {
      jProgramEditorPane = new JvsSourceEditor();
      jProgramEditorPane.setBounds(new Rectangle(8, 18, 520, 364));
      jProgramEditorPane.setProglet(proglet);
    }
    return jProgramEditorPane;
  }

  private JPanel getJResultPanel() {
    if (jResultPanel == null) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = GridBagConstraints.BOTH;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.gridx = 0;
      jResultPanel = new JPanel();
      jResultPanel.setLayout(new GridBagLayout());
      jResultPanel.setBounds(new Rectangle(8, 92, 540, 580));
      jResultPanel.setBorder(BorderFactory.createTitledBorder(null, "\""+proglet+"\"", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jResultPanel.add(getPanel(proglet), gridBagConstraints);
    }
    return jResultPanel;
  }

  private JScrollPane getJConsoleScrollPane() {
    if (jConsoleScrollPane == null) {
      jConsoleScrollPane = new JScrollPane();
      jConsoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jConsoleScrollPane.setViewportView(getJConsoleTextPane());
      jConsoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jConsoleScrollPane;
  }

  private JPanel getJConsolePanel() {
    if (jConsolePanel == null) {
      GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
      gridBagConstraints1.fill = GridBagConstraints.BOTH;
      gridBagConstraints1.gridy = 0;
      gridBagConstraints1.weightx = 1.0;
      gridBagConstraints1.weighty = 1.0;
      gridBagConstraints1.gridx = 0;
      jConsolePanel = new JPanel();
      jConsolePanel.setLayout(new GridBagLayout());
      jConsolePanel.setBounds(new Rectangle(15, 509, 540, 176));
      jConsolePanel.setBorder(BorderFactory.createTitledBorder(null, "Console", 
	TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jConsolePanel.add(getJConsoleScrollPane(), gridBagConstraints1);
    }
    return jConsolePanel;
  }

  private JScrollPane getJScrollPane() {
    if (jScrollPane == null) {
      jScrollPane = new JScrollPane();
      jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jScrollPane;
  }

  private JEditorPane getJConsoleTextPane() {
    if (jConsoleTextPane == null) {
      jConsoleTextPane = new JEditorPane();
      jConsoleTextPane.setContentType("text/html; charset=UTF-8");
      jConsoleTextPane.setEditable(false);
      if (edit) {
	try {
	  PrintStream ps = new PrintStream(console = new ConsoleOutput(), true, "UTF-8");;
	  System.setOut(ps);
	  if (!standalone) System.setErr(ps);
	} catch(IOException e) { Utils.report(e); }
      }
    }
    return jConsoleTextPane;
  }
  private ConsoleOutput console = null;

  // Echos a message in the console. Style: 'b' for bold, 'i' for italic, 'c' for code.
  private void printConsole(String string, char style) { 
    if (console == null) { getJConsoleTextPane(); getJConsoleScrollPane(); }
    if (console == null) { 
      System.err.println(string);
    } else {
      switch(style) {
      case 'b': string = "<b>" + string + "</b>"; break;
      case 'c': string = "<b><tt>" + string + "</tt></b>"; break;
      case 'i': string = "<i>" + string + "</i>"; break;
      }
      console.writeln(string);
    }
  }

  // Defines a writer able to append chars as a stream in UTF-8 and strings
  private class ConsoleOutput extends ByteArrayOutputStream {
    public void write(byte[] b, int off, int len) { super.write(b, off, len); show(); }
    public void writeln(String s) { byte[] b = (s+"\n").getBytes(); write(b, 0, b.length); }
    public void clear() { reset(); show(); }
    private void show() {
      jConsoleTextPane.setText("<html><body>"+toString().replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>\n")+"\n</body></html>");
      jConsoleScrollPane.getVerticalScrollBar().setValue(jConsoleScrollPane.getVerticalScrollBar().getMaximum());
    }
  }

  private JFileChooser getFileChooser() {
    if (fileChooser== null) {
      fileChooser = new JFileChooser() {
	  private static final long serialVersionUID = 1L;
	  {
	    // Initialisation
	    this.setCurrentDirectory(new File(System.getProperty("user.dir")));
	    this.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    this.setMultiSelectionEnabled(false);
	    // Définition de l'extension
	    JFileFilter filtre = new JFileFilter();
	    filtre.addType("jvs");
	    filtre.setDescription("Fichiers JavaScool: .jvs");
	    this.addChoosableFileFilter(filtre);
	  }
	};
    }
    return fileChooser;
  }

  // Defines a file filter for some file extensions
  private static class JFileFilter extends FileFilter {
    private ArrayList<String> exts = new ArrayList<String>();
    private String description;
    public void addType(String s) {
      exts.add(s);
    }
    public boolean accept(File f) {
      if(f.isDirectory()) {
	return true;
      } else if(f.isFile()){
	Iterator it = exts.iterator();
	while(it.hasNext()) 
	  if(f.getName().endsWith((String)it.next()))
	    return true;
      }
      return false;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String s) {
      description = s;
    }
  }

  //
  // Cette section définie les actions de l'applet
  //

  void doLire(String pFile) throws IOException {
    setMainFile(pFile);
    // Read from file
    BufferedReader in = new BufferedReader(new FileReader(pFile));
    StringBuffer text = new StringBuffer();
    for(String line; (line = in.readLine()) != null; ) {
      text.append(line+"\n");
    }
    getJProgramEditorPane().setText(text.toString());
    in.close(); 
    printConsole("Le fichier "+(new File(pFile).getName())+" est chargé", 'i');
  }

  private void doSave(String pFile) throws IOException {
    setMainFile(pFile);
    doSave();
  }
  private void doSave() throws IOException  {
    // Adds a newline at line 1 if not yet done to avoid compilation errors mixed with header 
    String text = "\n"+getJProgramEditorPane().getText().trim()+"\n";
    getJProgramEditorPane().setText(text);
    // Saves in file
    BufferedWriter out = new BufferedWriter(new FileWriter(path+".jvs"));
    out.write(text); 
    out.close(); 
    printConsole("Le fichier "+(new File(path+".jvs").getName())+" est sauvegardé", 'i');
  }

  void doCompile() throws Exception {
    if (standalone) {
      getJConsoleTextPane(); if (console != null) console.clear();
    }
    if (main != null) {
      // Save and manage the temporary java file if any
      if (new File(path+".java").exists())
	new File(path+".java").renameTo(new File(path+".java~"));
      if (new File(path+".class").exists())
	new File(path+".class").delete();
      // Translate and compile
      Jvs2Java.translate(path+".jvs");
      if (standalone) {
	doStandAloneCompile();
      } else {
	// Compiles and load via a web service
	String body = URLEncoder.encode(loadString(path+".java"), "UTF-8");
	getAppletContext().showDocument(new URL("http://facets.inria.fr/javascool/index.php?prog="+proglet+"&main="+main+"&path="+path+"&body="+body), "_self");
      }
    } else
      printConsole("Impossible de compiler avant de définir/sauvegarder une programme !", 'b');
  }

  // Compiles and load in standalone mode
  private void doStandAloneCompile() throws Exception {
    if (runWindow != null) { runWindow.dispose(); runWindow = null; }
    String compilation = Jvs2Java.compile(path+".java");
    if (compilation.length() > 0) {
      // Reports compilation errors
      printConsole("Le fichier "+main+".jvs n'a pas pu être compilé", 'b');
      printConsole(compilation.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll("\n", "<br/>\n"), 'c');
    } else {
      new File(path+".java").delete();
      // Loads the compiled proglet for execution
      URL[] urls = new URL[] { new URL("file:"+new File(path+".class").getParent()+File.separator) };
      final Class<?> s = new URLClassLoader(urls).loadClass(main);
      MainV2 r = (MainV2) s.newInstance(); r.setProglet(proglet);
      // Point where = getLocationOnScreen(); where.x -= 570;
      runWindow = Utils.show(r, "javascool \""+proglet+"\" proglet's runner", 560, 720);
    }
  }
  private JFrame runWindow = null;

  // Loads the contents of a text file
  private static String loadString(String location) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(location), 10240);
    StringBuilder buffer = new StringBuilder(); char chars[] = new char[10240];
    while(true) { int l = reader.read(chars); if (l == -1) break; buffer.append(chars, 0, l); }
    return buffer.toString();
  }

  private void doRun(Runnable runnable) throws Exception {
    doStop();
    tache = new Thread(runnable);
    tache.start();
  }

  private void doStop() throws Exception {
    if (tache != null) { tache.interrupt(); tache = null; }
  }
  private static Thread tache = null;

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /** Constructs a proglet attached to the related applet.
   * @param proglet The proglet class name.
   * @return The static instanciation of the proglet.
   */
  static private JPanel getPanel(String proglet) {
    try { return (JPanel) Class.forName(Jvs2Java.proglets.get(proglet)).getField("panel").get(null); } 
    catch(Exception e) { System.err.println(e+" (unkown proglet "+proglet+")"); return new JPanel(); }
  }

  /** Runs one proglet's test.
   * @param proglet The proglet class name.
   */
  static private void test(String proglet) {
    proglet = toUcfirst(proglet);
    try { Class.forName(Jvs2Java.proglets.get(proglet)).getDeclaredMethod("test").invoke(null); } catch(Exception error) { }
  }
  private static String toUcfirst(String string) { return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase(); }

  /** Used to run a proglet as a standalone program. 
   * @param usage <tt>java proglet.Proglets (edit|run) [proglet-name [file-name]]</tt>
   */
  public static void main(String usage[]) { 
    try { 
      MainV2 applet = new MainV2(); 
      String proglet = usage.length >= 2 ? usage[1] : usage.length == 1 ? usage[0] : "ingredients"; applet.setProglet(proglet); 
      boolean edit = usage.length >= 2 ? "edit".equals(usage[0]) : true; applet.setEdit(edit); 
      applet.application = usage.length == 3;
      if (applet.application) applet.doLire(usage[2]);  
      if (usage.length == 3 && !edit) {
	applet.doCompile(); 
      } else {
	Utils.show(applet, "javascool proglet editor v02-11", 650, 720);
      }
    } catch(Exception e) { throw new RuntimeException(e); }
  }
}
