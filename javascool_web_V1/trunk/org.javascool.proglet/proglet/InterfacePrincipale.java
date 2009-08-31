/*******************************************************************************
 * Hamdi.Ben_Abdallah@inria.fr, Copyright (C) 2009.  All rights reserved.      *
 *******************************************************************************/

package proglet;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JApplet;

import javax.swing.JFrame;
import java.awt.Point;

import java.awt.Rectangle;
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
import javax.swing.JTextArea;
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
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import java.net.URLClassLoader;

/** Defines the main javascool proglet interface.
 * File used: <pre>
 * img/charger.png
 * img/save.png
 * img/compile.png
 * img/execute.png
 * img/stop.png
 * img/demo.png
 * </pre>
 * @see <a href="InterfacePrincipale.java">source code</a>
 */
public class InterfacePrincipale extends JApplet {
  private static final long serialVersionUID = 1L;

  //
  // Cette partie définie le paramétrage de l'interface graphique
  //

  /** Sets the proglet to use in this interface.
   * @param proglet The proglet class name.
   */
  void setProglet(String proglet) { getJProgletButton(); jProgletBox.setSelectedItem(this.proglet = proglet); } private String proglet = "Konsol";
	
  /** Sets the mode to use in this interface.
   * @param edit If true in edit mode, else in run mode.
   */
  void setEdit(boolean edit) { this.edit = edit; } private boolean edit = false;
  
  // Sets the class name and file
  private void setMainFile(String pFile) {
    String s = File.separatorChar == '\\' ? "\\\\" : File.separator;
    main = pFile.replaceAll(".*"+s+"([^"+s+"]+)\\.[a-z]+$", "$1");
    path = pFile.replaceAll("\\.[a-z]+$", "");
  }  
  private String main = null, path = null;

  /** This routines is the entry point to run the proglet pupil's program. */
  public void main() { Proglet.test(proglet); }

  // Flag whether we are in standalone mode or web-browser mode
  boolean standalone = true; 

  // Flags whether we have derived an application derivating this class or if this class hase been constructed.
  boolean application = false; 

  private void initParameters() {
    { try { if (getParameter("proglet") != null) setProglet(getParameter("proglet")); } catch(Exception e) { } }
    { try { if (getParameter("edit") != null) setEdit(true); } catch(Exception e) { } }
    { try { if (getParameter("path") != null) doLire(getParameter("path")+".jvs");  } catch(Exception e) { } }
    { try { standalone = getAppletContext() == null; } catch(Exception e) { } }
    { try { application = !getClass().getName().equals("proglet.InterfacePrincipale"); } catch(Exception e) { } }
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
  private JScrollPane jProgramScrollPane = null;
  private JTextArea jProgramEditorPane = null;
  private JPanel jResultPanel = null;
  private JPanel  jConsolePanel = null;
  private JScrollPane jScrollPane = null;
  private JScrollPane jConsoleScrollPane = null;
  private JEditorPane jConsoleTextPane = null;
  private JFileChooser fileChooser=null;

  public void init() {
    Proglet.setApplet(this);
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
      jMenuPanel.setBounds(new Rectangle(9, 7, 540, 70));
      if (edit) {
	jMenuPanel.add(getJProgletButton(), null);
	jMenuPanel.add(getJOpenButton(), null);
	jMenuPanel.add(getJSaveButton(), null);
	jMenuPanel.add(getJCompileButton(), null);
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
      jProgletLabel.setIcon(Proglet.getIcon("execute.png"));
      jProgletButton.add(jProgletLabel);
      String proglets[] = new String[] { "Konsol", "Dicho", "Smiley", "Scope", "Conva", "Synthe", "Tortue" };
      jProgletBox = new JComboBox(proglets);
      jProgletBox.setEditable(false);
      jProgletBox.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    proglet = (String) jProgletBox.getSelectedItem();
	  }
	});
      jProgletButton.add(jProgletBox);
    }
    return jProgletButton;
  }

  private JButton getJOpenButton() {
    if (jOpenButton == null) {
      jOpenButton = new JButton();
      jOpenButton.setIcon(Proglet.getIcon("charger.png"));
      jOpenButton.setText("Ouvrir");
      jOpenButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    getFileChooser().setDialogTitle("Ouvrir un programme");
	    getFileChooser().setApproveButtonText("Ouvrir");
	    int value = getFileChooser().showOpenDialog(null);
	    if (value == 0){
	      try { 
		String check = getFileChooser().getSelectedFile().getPath();
		doLire(check); 
	      } catch(Exception e1){ 
		Proglet.report(e1);
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
      jSaveButton.setIcon(Proglet.getIcon("save.png"));
      jSaveButton.setText("Enregister");
      jSaveButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    getFileChooser().setDialogTitle("Enregister un programme");
	    getFileChooser().setApproveButtonText("Enregister ");
	    int value = getFileChooser().showOpenDialog(null);
	    if (value == 0) {
	      try { 
		String check = getFileChooser().getSelectedFile().getPath();
		doSave(check); 
	      } catch(Exception e1){ 
		Proglet.report(e1);
	      }
	    }
	  }
        });
    }
    return jSaveButton;
  }
	
  private JButton getJCompileButton() {
    if (jCompileButton == null) {
      jCompileButton = new JButton();
      jCompileButton.setMnemonic(KeyEvent.VK_UNDEFINED);
      jCompileButton.setText("Compiler");
      jCompileButton.setIcon(Proglet.getIcon("compile.png"));
      jCompileButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doCompile();
	    } catch (Exception e1) {
	      Proglet.report(e1);
	    }
	  }});
    }
    return jCompileButton;
  }

  private JButton getJRunButton() {
    if (jRunButton == null) {
      jRunButton = new JButton();
      jRunButton.setIcon(Proglet.getIcon("execute.png"));
      jRunButton.setText("Executer");
      jRunButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doRun();
	    } catch (Exception e1) {
	      Proglet.report(e1);
	    }
	  }});
    }
    return jRunButton;
  }

  private JButton getJStopButton() {
    if (jStopButton == null) {
      jStopButton = new JButton();
      jStopButton.setIcon(Proglet.getIcon("stop.png"));
      jStopButton.setText("Arrêter");
      jStopButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      doStop();
	    } catch (Exception e1) {
	      Proglet.report(e1);
	    }
	  }});
    }
    return jStopButton;
  }

  private JButton getJDemoButton() {
    if (jDemoButton == null) {
      jDemoButton = new JButton();
      jDemoButton.setIcon(Proglet.getIcon("demo.png"));
      jDemoButton.setText("Demo");
      jDemoButton.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent e) {
	    try {
	      Proglet.test(proglet);
	    } catch (Exception e1) {
	      Proglet.report(e1);
	    }
	  }});
    }
    return jDemoButton;
  }

  private JPanel getJProgramPanel() {
    if (jProgramPanel == null) {
      jProgramPanel = new JPanel();
      jProgramPanel.setLayout( null);
      jProgramPanel.setBounds(new Rectangle(11, 92, 540, 398));
      jProgramPanel.setBorder(BorderFactory.createTitledBorder(null, "Programme", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jProgramPanel.add(getJProgramScrollPane(), null);
    }
    return jProgramPanel;
  }

  private JScrollPane getJProgramScrollPane() {
    if (jProgramScrollPane == null) {
      jProgramScrollPane = new JScrollPane();
      jProgramScrollPane.setBounds(new Rectangle(8, 18, 520, 364));
      jProgramScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jProgramScrollPane.setViewportView(getJProgramEditorPane());
      jProgramScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jProgramScrollPane;
  }

  private JTextArea getJProgramEditorPane() {
    if (jProgramEditorPane == null) {
      jProgramEditorPane = new JTextArea();
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
      jResultPanel.setBorder(BorderFactory.createTitledBorder(null, "Résultat", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jResultPanel.add(Proglet.getPanel(proglet), gridBagConstraints);
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
      PrintStream ps = new PrintStream(console = new ConsoleOutputStream());
      System.setOut(ps);
      //-virer car messages de tests encore en place-// if (!standalone) System.setErr(ps);
    }
    return jConsoleTextPane;
  }
  private ConsoleOutputStream console = null;

  // Echos a message in the console. Style: 'b' for bold, 'i' for italic, 'c' for code.
  private void printConsole(String string, char style) { 
    if (console == null)
      getJConsoleTextPane();
    switch(style) {
    case 'b': string = "<b>" + string + "</b>"; break;
    case 'c': string = "<b><tt>" + string + "</tt></b>"; break;
    case 'i': string = "<i>" + string + "</i>"; break;
    }
    console.writeln(string);
  }

  // Defines a writer able to append chars as a stream and strings
  private class ConsoleOutputStream extends OutputStream {
    private StringBuffer text = new StringBuffer();
    public synchronized void write(int b) throws IOException {
      if (b == '\n') {
	text.append("<br>\n"); 
	show();
      } else {
	text.append((char) b); 
      }
    }
    public synchronized void writeln(String s) {
      text.append(s+"<br>\n");
      show();
    }
    private void show() {
      jConsoleTextPane.setText("<html><body>"+text+"</body></html>");
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
	    this.setDialogType(JFileChooser.OPEN_DIALOG);
	    this.setMultiSelectionEnabled(false);
	    // Définition des extensions
	    JFileFilter filtre = new JFileFilter();
	    filtre.addType("txt");
	    filtre.addType("java");
	    filtre.addType("jvs");
	    filtre.setDescription("Fichiers Java: .txt, .java, .jvs");
	    this.addChoosableFileFilter(filtre);
	    // - Canceled // this.setAccessory(new FilePreviewer(this));
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

  private void doLire(String pFile) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(pFile));
    getJProgramEditorPane().setText("");
    for(String line; (line = in.readLine()) != null; ) {
      getJProgramEditorPane().append(line+"\n");
    }
    in.close(); 
    setMainFile(pFile);
    printConsole("Le fichier "+(new File(pFile).getName())+" est chargé", 'i');
  }

  private void doSave(String pFile) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(pFile));
    out.write(getJProgramEditorPane().getText()); 
    out.close(); 
    setMainFile(pFile);
    printConsole("Le fichier "+(new File(pFile).getName())+" est sauvegardé", 'i');
  }

  private void doCompile() throws Exception {
    if (main != null) {
      // Save and manage the temporary java file if any
      if (main != null)
	doSave(path+".jvs");
      if (new File(path+".java").exists())
	new File(path+".java").renameTo(new File(path+".java~"));
      if (new File(path+".class").exists())
	new File(path+".class").delete();
      // Translate and compile
      Translator.translate(path+".jvs", proglet);
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
  private JFrame runWindow = null;

  // Compiles and load in standalone mode
  private void doStandAloneCompile() throws Exception {
    String compilation = Compiler.compile(path+".java");
    if (compilation.length() > 0) {
      // Reports compilation errors
      printConsole("Le fichier "+main+".jvs n'a pas pu être compilé", 'b');
      printConsole(compilation.replaceAll("\n", "<br/>\n"), 'c');
    } else {
      new File(path+".java").delete();
      // Loads the compiled proglet for execution
      URL[] urls = new URL[] { new URL("file:"+new File(path+".class").getParent()+File.separator) };
      final Class<?> s = new URLClassLoader(urls).loadClass(main);
      InterfacePrincipale r = (InterfacePrincipale) s.newInstance(); r.setProglet(proglet);
      if (runWindow != null) { runWindow.dispose(); runWindow = null; }
      Point where = getLocationOnScreen(); where.x -= 570;
      runWindow = Proglet.show(r, "javascool «"+proglet+"» proglet's runner", null, 560, 720);
    }
  }

  // Loads the contents of a text file
  private static String loadString(String location) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(location), 10240);
    StringBuilder buffer = new StringBuilder(); char chars[] = new char[10240];
    while(true) { int l = reader.read(chars); if (l == -1) break; buffer.append(chars, 0, l); }
    return buffer.toString();
  }

  private void doRun() throws Exception {
    doStop();
    tache = new Thread(new Runnable() { public void run() {
      try {
	main();
      } catch (Throwable e) {  
	if (e instanceof java.lang.Error) {
	  // This exception thrown when a interrupt() is issued
	} else {
	  Proglet.report(e);
	}
      }
    }});
    tache.start();
  }

  private void doStop() throws Exception {
    if (tache != null) { tache.interrupt(); tache = null; }
  }
  private Thread tache = null;

}
