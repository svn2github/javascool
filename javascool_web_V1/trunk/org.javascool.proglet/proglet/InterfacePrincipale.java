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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;

/** Interface principale pour utiliser des proglets.
 * Fichiers utilisés: <pre>
 * img/nouveau.png
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
  private JButton jNewButton = null; 
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
  private JScrollPane jConsolScrollPane1 = null;
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
	e.printStackTrace();
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
      jMenuPanel.add(getJNewButton(), null);
      jMenuPanel.add(getJOpenButton(), null);
      jMenuPanel.add(getJSaveButton(), null);
      jMenuPanel.add(getJCompileButton(), null);
      jMenuPanel.add(getJRunButton(), null);
      jMenuPanel.add(getJStopButton(), null);
    }
    return jMenuPanel;
  }

  private JButton getJNewButton() {
    if (jNewButton == null) {
      jNewButton = new JButton();
      jNewButton.setIcon(Proglet.getIcon("nouveau.png"));
      jNewButton.setMnemonic(KeyEvent.VK_UNDEFINED);
      jNewButton.setText("Nouveau");
    }
    return jNewButton;
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
      jCompileButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    try {
	      doCompile();
	    } catch (Exception e1) {
	      e1.printStackTrace();
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
      jRunButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    try {
	      doRun();
	    } catch (Exception e1) {
	      e1.printStackTrace();
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
      jStopButton.addActionListener(new ActionListener(){
	  public void actionPerformed(ActionEvent e){
	    try {
	      doStop();
	    } catch (Exception e1) {
	      e1.printStackTrace();
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
      jConsolePanel.add(getJConsolScrollPane1(), gridBagConstraints1);
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

  private JScrollPane getJConsolScrollPane1() {
    if (jConsolScrollPane1 == null) {
      jConsolScrollPane1 = new JScrollPane();
      jConsolScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      jConsolScrollPane1.setViewportView(getJConsolEditorPane());
      jConsolScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }
    return jConsolScrollPane1;
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
      File vF = new File (System.getProperty("user.dir" )+File.separator+"work");
      if (!vF.exists()) {
	vF.mkdir(); 
      }
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
	    //this.setAccessory(new FilePreviewer(this));
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
	
  private String prog = null;

  private void doLire(String pFile)throws IOException {
    try { 
      BufferedReader in = new BufferedReader(new FileReader(pFile));
      getJProgramEditorPane().setText("");
      for(String line; (line = in.readLine()) != null; ) {
	getJProgramEditorPane().append(line+"\n");
      }
      in.close(); 
      prog = pFile.replaceAll("\\.[a-z]+$", "");
      System.out.println("Prog name = "+ prog);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doSave(String pFile)throws IOException {
    try { 
      BufferedWriter out = new BufferedWriter(new FileWriter(pFile));
      out.write(getJProgramEditorPane().getText()); 
      out.close(); 
      prog = pFile.replaceAll("\\.[a-z]+$", "");
      System.out.println("Prog name = "+ prog);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void doCompile() throws Exception {
    if (prog != null) {


      String filePath = System.getProperty("user.dir" )+File.separator+"work"+File.separator+"Ctest.jvs";
      FileWriter fw = new FileWriter(filePath);
      fw.write(getJProgramEditorPane().getText());
      fw.close();
      String filePath2 =System.getProperty("user.dir" )+File.separator+"work"+File.separator+"tools.jar";
      System.out.println("classPath1"+ System.getProperty("java.class.path"));
      String pathClassConf = "D:\\projetLycée\\seb\\org.javascool.conf";
      try {
	// Initialisation des macros par rapport au fichier de configuration	
	//// Translator.init(pathClassConf+File.separator+"bin");
      } catch(Exception ex){
	System.out.println("erreur initTranslator");
	ex.printStackTrace();
      }
      //// Translator.translate(filePath);
      //// Compile.run(filePath, pathClassConf+File.separator+"bin");

      System.out.println("classPath1"+ System.getProperty("java.class.path"));
    } else {
      System.out.println("Impossible de compiler avant de sauvegarder le fichier !");
    }
  }

  private void doRun() throws Exception {
    doStop();
    final Class<?> s = ToolProvider.getSystemToolClassLoader().loadClass("work.Ctest");
    tache = new Thread(new Runnable(){public void run(){
      try {
	s.getDeclaredMethod("main").invoke(null);
      } catch (Exception e) {
	e.printStackTrace();
      }
    }});
    tache.start();
  }		

  private void doStop() throws Exception {
    if (tache != null) {
      // tache.stop();
      tache = null;
    }
  }

  private Thread tache = null;

  //
  // Ces methodes sont des essais
  //

  public static boolean deplacer(File source, File destination) {
    if( !destination.exists() ) {
      System .out.println(" copie ok");
      boolean result = source.renameTo(destination);
      return(result);
    } else {
      System .out.println(" copie Non");
      return(false);
    } 
  } 
	
  private static boolean deleteClassFile(  File file )
  {
		
    if ( file.exists() )
      file.delete();
    return !file.exists();
  }
  public static boolean copier(InputStream  source, File destination ){ //Methode permettant la copie d'un fichier
    boolean resultat = false;
    //System.out.println("taille: "+((CharSequence) source).length());
    // Declaration des flux
    InputStream  sourceFile=null;
    java.io.FileOutputStream destinationFile=null;
    try {
      // Création du fichier :
      destination.createNewFile();
      // Ouverture des flux
      sourceFile = source;
      destinationFile = new java.io.FileOutputStream(destination);
      // Lecture par segment de 0.5Mo
      int taille = Math.min(1024, 4*1024);
		
      byte buffer[]=new byte[512*1024];
      int nbLecture;
      while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
	destinationFile.write(buffer, 0, nbLecture);
      }

      System.out.println("copie ok");
      resultat = true;
    } catch( java.io.FileNotFoundException f ) {
    } catch( java.io.IOException e ) {
    } finally {
      // Quoi qu'il arrive, on ferme les flux
      try {
	sourceFile.close();
      } catch(Exception e) { }
      try {
	destinationFile.close();
      } catch(Exception e) { }
    }
    return( resultat );
  }
	
	

}
