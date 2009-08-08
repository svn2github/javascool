package proglet;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JApplet;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.JButton;
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
import javax.tools.ToolProvider;
import javax.swing.JFileChooser;
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

import java.net.URLClassLoader;


/** Interface principale pour utiliser des proglets.
 * Fichiers utilisés: <pre>
 * img/charger.png
 * img/save.png
 * img/compile.png
 * img/execute.png
 * img/stop.png
 * </pre>
 */
public class InterfacePrincipale extends JApplet {
  private static final long serialVersionUID = 1L;

  //
  // Cette partie définie l'interface graphique
  //

  private JPanel jContentPane = null;
  private JPanel jMenuPanel = null;
  private JButton jOpenButton = null;
  private JButton jSaveButton = null;
  private JButton jCompileButton = null;
  private JButton jRunButton = null;
  private JButton jStopButton = null;
  private JPanel jProgramPanel = null;
  private JScrollPane jProgramScrollPane = null;
  private JTextArea jProgramEditorPane = null;
  private JPanel jResultPanel = null;
  private JPanel  jConsolePanel = null;
  private JScrollPane jScrollPane = null;
  private JEditorPane jResultEditorPane = null;
  private JScrollPane jConsolScrollPane = null;
  private JTextArea jConsolEditorPane = null;
  private JFileChooser fileChooser=null;

  public void init() {
    this.setSize(920, 720);
    this.setContentPane(getJContentPane());
  }

  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      jContentPane.setLayout(null);
      jContentPane.add(getJMenuPanel(), null);
      jContentPane.add(getJProgramPanel(), null);
      try {
	jContentPane.add(getJResultPanel(), null);
      } catch (Exception e) {
	Proglet.report(e);
      }
      jContentPane.add(getJConsolePanel(), null);
    }
    return jContentPane;
  }

  private JPanel getJMenuPanel() {
    if (jMenuPanel == null) {
      jMenuPanel = new JPanel();
      jMenuPanel.setLayout(new FlowLayout());
      jMenuPanel.setBorder(BorderFactory.createTitledBorder(null, "Commande", 
         TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jMenuPanel.setBounds(new Rectangle(9, 7, 878, 70));
      jMenuPanel.add(getJOpenButton(), null);
      jMenuPanel.add(getJSaveButton(), null);
      jMenuPanel.add(getJCompileButton(), null);
      jMenuPanel.add(getJRunButton(), null);
      jMenuPanel.add(getJStopButton(), null);
    }
    return jMenuPanel;
  }

  private JButton getJOpenButton() {
    if (jOpenButton == null) {
      jOpenButton = new JButton();
      jOpenButton.setIcon(Proglet.getIcon("charger.png"));
      jOpenButton.setText("Ouvrir");
      jOpenButton.addActionListener(new ActionListener(){
	  int value;
	  String check;
	  public void actionPerformed(ActionEvent e){
	    getFileChooser().setDialogTitle("Ouvrir un programme");
	    getFileChooser().setApproveButtonText("Ouvrir");
	    value = getFileChooser().showOpenDialog(null);
	    if (value == 0){
	      try{ 
		check = getFileChooser().getSelectedFile().getPath();
		doLire(check); 
	      }
	      catch(IOException exc){ System.out.println("... problème avec lecture fichier...");} 
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
	  int value;
	  String check;
	  public void actionPerformed(ActionEvent e){
	    getFileChooser().setDialogTitle("Enregister un programme");
	    getFileChooser().setApproveButtonText("Enregister ");
	    value = getFileChooser().showOpenDialog(null);
	    if (value == 0) {
	      try { 
		check = getFileChooser().getSelectedFile().getPath();
		doSave(check); 
	      }
	      catch(IOException exc){ System.out.println("... problème avec écriture fichier...");} 
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

  private JPanel getJProgramPanel() {
    if (jProgramPanel == null) {
      jProgramPanel = new JPanel();
      jProgramPanel.setLayout( null);
      jProgramPanel.setBounds(new Rectangle(11, 92, 479, 398));
      jProgramPanel.setBorder(BorderFactory.createTitledBorder(null, "Programme", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jProgramPanel.add(getJProgramScrollPane(), null);
    }
    return jProgramPanel;
  }

  private JScrollPane getJProgramScrollPane() {
    if (jProgramScrollPane == null) {
      jProgramScrollPane = new JScrollPane();
      jProgramScrollPane.setBounds(new Rectangle(8, 18, 459, 364));
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

  private JPanel getJResultPanel() 
    throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
    if (jResultPanel == null) {
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = GridBagConstraints.BOTH;
      gridBagConstraints.gridy = 0;
      gridBagConstraints.weightx = 1.0;
      gridBagConstraints.weighty = 1.0;
      gridBagConstraints.gridx = 0;
      jResultPanel = new JPanel();
      jResultPanel.setLayout(new GridBagLayout());
      jResultPanel.setBounds(new Rectangle(498, 92, 394, 399));
      jResultPanel.setBorder(BorderFactory.createTitledBorder(null, "Résultat", 
        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jResultPanel.add(Proglet.getPanel(this, getProglet()), gridBagConstraints);
    }
    return jResultPanel;
  }
  private String getProglet() {
    try {
      return getParameter("proglet");
    } catch(Exception e) {
      return proglet;
    }
  }
  
  /** Sets the proglet to use in this interface.
   * @param proglet The proglet class name.
   */
  public void setProglet(String proglet) {
    this.proglet = proglet;
  }
  private String proglet = "Konsol";

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
      jConsolePanel.setBounds(new Rectangle(15, 509, 873, 176));
      jConsolePanel.setBorder(BorderFactory.createTitledBorder(null, "Console", 
	TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
      jConsolePanel.add(getJConsolScrollPane(), gridBagConstraints1);
    }
    return jConsolePanel;
  }

  private JScrollPane getJScrollPane() {
    if (jScrollPane == null) {
      jScrollPane = new JScrollPane();
      jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      jScrollPane.setViewportView(getJResultEditorPane());
    }
    return jScrollPane;
  }

  private JEditorPane getJResultEditorPane() {
    if (jResultEditorPane == null) {
      jResultEditorPane = new JEditorPane();
      jResultEditorPane.setEditable(false);
    }
    return jResultEditorPane;
  }

  private JScrollPane getJConsolScrollPane() {
    if (jConsolScrollPane == null) {
      jConsolScrollPane = new JScrollPane();
      jConsolScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jConsolScrollPane.setViewportView(getJConsolEditorPane());
      jConsolScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jConsolScrollPane;
  }

  private JTextArea getJConsolEditorPane() {
    if (jConsolEditorPane == null) {
      jConsolEditorPane =new JTextArea();
      jConsolEditorPane.setEditable(false);
      PrintStream ps = new PrintStream(new TextAreaOutputStream(jConsolEditorPane));
      System.setOut(ps);
      System.setErr(ps);
    }
    return jConsolEditorPane;
  }
  private static class TextAreaOutputStream extends OutputStream {
   private JTextArea ta;
   public TextAreaOutputStream(JTextArea ta) {
      this.ta = ta;
   }
   public synchronized void write(int b) throws IOException {
      ta.append(String.valueOf((char) b));
   }
  }

  private JFileChooser getFileChooser() {
    if (fileChooser== null) {
      // - Canceled // File vF = new File (System.getProperty("user.dir" )+File.separator+"work"); if (!vF.exists()) vF.mkdir(); 
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
	while(it.hasNext()) {
	  if(f.getName().endsWith((String)it.next())) {
	    return true;
	  }
	}
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
	
  // Sets the class name and file
  private void setMainFile(String pFile) {
    main = pFile.replaceAll(".*/([^/]+)\\.[a-z]+$", "$1");
    file = pFile.replaceAll("\\.[a-z]+$", "");
    System.out.println("Prog name = "+ main);  
  }  
  private String main = null, file = null;

  private void doLire(String pFile) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(pFile));
    getJProgramEditorPane().setText("");
    for(String line; (line = in.readLine()) != null; ) {
      getJProgramEditorPane().append(line+"\n");
    }
    in.close(); 
    setMainFile(pFile);
  }

  private void doSave(String pFile) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(pFile));
    out.write(getJProgramEditorPane().getText()); 
    out.close(); 
    setMainFile(pFile);
  }

  private boolean doCompile() throws Exception {
    if (main != null) {
      // Save and manage the temporary java file if any
      if (main != null)
	doSave(file+".jvs");
      if (new File(file+".java").exists())
	new File(file+".java").renameTo(new File(file+".java~"));
      // Translate and compile
      int offset = Translator.translate(file+".jvs", proglet);
      if (offset < 0)
	return false;
      boolean ok = Compiler.compile(file+".java", System.getProperty("java.class.path"), 0);
      // Cleanup
      new File(file+".java").delete();
      return ok;
    } else {
      System.out.println("Impossible de compiler avant d'ouvrir ou sauvegarder le fichier !");
      return false;
    }
  }

  private void doRun() throws Exception {
    doStop();
    if (main != null) {
      if (new File(file+".class").exists() || doCompile()) {
	URL[] urls = new URL[] { new URL("file:"+new File(file+".class").getParent()+File.separator) };
	System.out.println(urls[0]);
	final Class<?> s = new URLClassLoader(urls).loadClass(main);
	tache = new Thread(new Runnable() { public void run() {
	  try {
	    s.getDeclaredMethod("main").invoke(null);
	  } catch (Exception e) {  
	    if (e instanceof InvocationTargetException && e.getCause() instanceof java.lang.Error) {
	      System.err.println("Execution stoppee!");
	    } else {
	      Proglet.report(e);
	    }
	  }
	}});
	tache.start();
      } else {
	System.out.println("La compilation a echouee !");
      }
    } else {
      System.out.println("Impossible de compiler et executer avant d'ouvrir ou sauvegarder le fichier !");
    }
  }

  private void doStop() throws Exception {
    if (tache != null) {
      tache.interrupt();
      tache = null;
    }
  }

  private Thread tache = null;
}
