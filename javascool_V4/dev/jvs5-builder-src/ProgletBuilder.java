// Builder classes
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.jar.Manifest;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.javascool.core.JarManager;
import org.javascool.core.ProgletApplet;
import org.javascool.tools.FileManager;
import org.javascool.tools.UserConfig;
import org.javascool.widgets.Console;
import org.javascool.widgets.ToolBar;
import org.json.JSONException;
import org.json.JSONObject;

// Applet classes

/**
 * Ce script pour web service implémente la création et compilation d'une
 * proglet.
 */
public class ProgletBuilder extends JApplet {
    public ProgletBuilder() {}
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Usage en tant qu'application.
     * Usage:
     * <tt>$java ProgletBuilder [applet|create|compile] &lt;proglets-directory-location/proglet-name></tt>
     * Usage:
     * <tt>$java ProgletBuilder javadoc &lt;doc-name> &lt;classPath> &lt;srcDir> &lt;apiDir></tt>
     */
    public static void main(String usage[]) {
        try {
            if (usage.length == 2 && "create".equals(usage[0])) {
                createProglet(usage[1]);
            } else if (usage.length == 2 && "applet".equals(usage[0])) {
                compileProglet(usage[1]);
            } else if (usage.length == 2 && "compile".equals(usage[0])) {
                buildProgletJar(usage[1]);
                createConfigJson(usage[1]);
            } else if (usage.length == 5 && "javadoc".equals(usage[0])) {
                javadoc(usage[1], usage[2], usage[3], usage[4]);
            } else
                ProgletApplet.open("Proglet Builder", 850, 350,
                        new ProgletBuilder());
        } catch (BuildError e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        } catch (Throwable e) {
            System.out.println("" + e);
            System.exit(-1);
        }
    }
    
    private static final JSONObject progletConfig = new JSONObject();
    
    /**
     * Ajoute un paramettre au fichier de configuration de la Proglet.
     * 
     * @param key
     *            La clef pour la valeur
     * @param value
     */
    private static void setOnProgletConf(String key, Object value) {
        try {
            progletConfig.put(key, value);
        } catch (JSONException e1) {}
    }
    
    /**
     * Raccourci pour le File separator
     * 
     * @see java.io.File#separator
     */
    private static final String fs     = File.separator;
    
    /**
     * Raccourci pour le séparateur de chemin
     * 
     * @see java.io.File#pathSeparator
     */
    private static final String fPathS = File.pathSeparator;
    
    /**
     * Crée un Jar des ressource et du code compilé de la proglet
     * 
     * @param location
     *            L'adresse de la proglet
     */
    public static void buildProgletJar(String location) {
        
        location = new File(location).getAbsolutePath();
        String name = getProgletName(location, true);
        buildMessage("Création du JAR de " + name + ", patientez quelques secondes...");
        
        // Répertoire temporaire de compilation
        String compilationDir = FileManager.createTempDir("jvs-build-").getAbsolutePath();
        
        // Répertoire pour les resources
        String javaDir = compilationDir + fs + "org" + fs + "javascool" + fs + "proglets" + fs + name;
        
        // On vérifie que les dossiers existent
        new File(javaDir).mkdirs();
        
        // On copie les resources et les sources de la proglet
        for (String j : FileManager.list(location)) {
            try {
                if (!j.endsWith(".jar") && !j.endsWith(".zip") && new File(j).isFile())
                    JarManager.copyFile(j, new File(javaDir + fs + new File(j).getName()).getAbsolutePath());
            } catch (IOException e) {
                buildMessage("Impossible de copier " + new File(j).getName());
            }
        }
        
        // Construction du class.path
        String classPath = location;
        for (String j : FileManager.list(location, ".*\\.jar$")) {
            classPath += fPathS + j;
        }
        classPath += fPathS + System.getProperty("java.class.path");
        
        // On compile
        String[] javaFiles = FileManager.list(javaDir, ".*\\.java");
        if (javaFiles.length > 0) {
            javac(classPath, javaFiles);
        }
        
        // On met tous ça dans un JAR
        JarManager.jarCreate(location + File.separator + name + ".jar", (new Manifest()), compilationDir);
        
        // On supprime le dossier temporaire
        JarManager.rmDir(new File(compilationDir));
    }
    
    /**
     * Fabrique le config.json necessaire à Java's Cool
     * 
     * @param location
     *            Le chemin de la proglet
     */
    public static void createConfigJson(String location) {
        location = new File(location).getAbsolutePath();
        String name = getProgletName(location, true);
        buildMessage("Création du config.json de " + name + " ...");
        
        JarManager.rmDir(new File(location + fs + "config.json"));
        
        setOnProgletConf("name", name);
        setOnProgletConf("hasFunctions", FileManager.exists(location + File.separator + "Functions.java"));
        setOnProgletConf("hasTranslator", FileManager.exists(location + File.separator + "Translator.java"));
        setOnProgletConf("hasPanel", FileManager.exists(location + File.separator + "Panel.java"));
        try {
            setOnProgletConf("proglet", new JSONObject(FileManager.load(location + File.separator + "proglet.json")));
        } catch (JSONException e) {
            throw new BuildError("Le fichier JSON de la proglet n'est pas correctement écrit");
        }
        
        FileManager.save(location + fs + "config.json", progletConfig.toString());
    }
    
    //
    // Mécanisme de compilation de la proglet
    //
    private static void compileProglet(String location) {
        String name = getProgletName(location, true);
        setOnProgletConf("name", name);
        buildMessage("La proglet «"
                + name
                + "» est en cours de compilation (cela peut prendre qq 10zaines de secondes) . .");
        // Création du répertoire cible
        String targetDir = location + File.separator + "applet";
        {
            // Copies des fichiers de la proglet
            try {
                targetDir = new File(targetDir).getCanonicalPath().toString();
                new File(targetDir).mkdir();
                JarManager.copyFiles(location, targetDir, false);
            } catch (Throwable e) {
                throw new BuildError(
                        "erreur à la création du répertoire cible \""
                                + location + "\" «" + e + "»");
            }
            FileManager.save(targetDir + File.separator + "index.html",
                    "<script>location.replace('help.html');</script>");
            FileManager.save(targetDir + File.separator + "_applet.html",
                    setDescriptionValues(location, progletApplet));
        }
        // Import des librairies à utiliser : @todo à corriger créer un jar
        // complet signé
        {
            try {
                JarManager.copyResource("javascool.jar", targetDir + File.separator + "javascool.jar");
            } catch (IOException e) {}
        }
        buildProgletJar(targetDir);
        // Ajout des header/trailer aux fichiers HTML
        {
            addBootstrap(targetDir);
            for (String file : FileManager.list(targetDir, ".*\\.html", 5))
                FileManager.save(file,
                        setDescriptionValues(location, progletHtmlHeader + FileManager.load(file) + progletHtmlTrailer)
                                .replaceAll("@base-url", file.substring(targetDir.length() + 1).replaceAll("[^/]+", "..")
                                        .substring(1)));
        }
        // Nettoyage des fichiers inutiles
        {
            for (String j : FileManager.list(targetDir, ".*\\.java")) {
                new File(j).delete();
            }
            new File(targetDir + File.separator + "proglet.json").delete();
            new File(targetDir + File.separator + "assets.zip").delete();
        }
        // @todo Ici on cree un fichier config.json @todo
        
        buildMessage("La proglet «" + name + "» est compilée dans " + targetDir + ".");
    }
    
    private static final String progletHtmlHeader0 = FileManager.load("progletHtmlHeader0");
    private static final String progletHtmlNavBar  = FileManager.load("progletHtmlNavBar");
    private static final String progletHtmlHeader  = progletHtmlHeader0 + progletHtmlNavBar;
    private static final String javadocHtmlNavBar  = FileManager.load("javadocHtmlNavBar");
    private static final String javadocHtmlHeader  = progletHtmlHeader0 + javadocHtmlNavBar;
    private static final String progletHtmlTrailer = FileManager.load("progletHtmlTrailer");
    private static final String progletApplet      = FileManager.load("progletApplet");
    
    /**
     * Construction de la doc avec le java2html des sources.
     * 
     * @param name
     *            Nom de la proglet.
     * @param classPath
     *            Chemin nécessaire à la compilation.
     * @param srcDir
     *            Répertoire racine où se trouvent les sources.
     * @param apiDir
     *            Répertoire cible de la documentation.
     */
    public static void javadoc(String name, String classPath, String srcDir, String apiDir) {
        javadoc(name, classPath, srcDir, apiDir, true);
    }
    
    private static void javadoc(String name, String classPath, String srcDir, String apiDir, boolean bootstrapEncapsulated) {
        // Mise en place des dossiers source et cible
        try {
            apiDir = new File(apiDir).getCanonicalPath();
            srcDir = new File(srcDir).getCanonicalPath();
        } catch (Throwable e) {
            throw new BuildError(
                    "erreur à la mise en place du répertoire src::\"" + srcDir
                            + "\" ou api::\"" + apiDir + "\" de javadoc, «" + e
                            + "»");
        }
        new File(apiDir).mkdirs();
        String files[] = FileManager.list(srcDir, ".*\\.java$", 5);
        // Lancement du logiciel javadoc avec les bonnes options
        if (files.length > 0) {
            String argv = "-quiet\t-classpath\t" + classPath + "\t-d\t" + apiDir +
                    "\t-link\thttp://download.oracle.com/javase/6/docs/api" + "\t-windowtitle\tJava's Cool "
                    + name + "\t-doctitle\tJava's Cool " + name +
                    "\t-public\t-author\t-version\t-nodeprecated\t-nohelp\t-nonavbar\t-notree\t-charset\tutf-8";
            for (String f : files)
                argv += "\t" + f;
            try {
                com.sun.tools.javadoc.Main.execute(argv.split("\t"));
            } catch (Throwable e) {
                throw new BuildError(
                        "erreur à la création de la javadoc du répertoire \""
                                + srcDir + "\", «" + e + "»");
            }
            // Manipulation des fichiers html générés pour lisser le style
            for (String file : FileManager.list(apiDir, ".*\\.html", 5)) {
                String text = FileManager.load(file)
                        .replaceFirst("(?s)<!DOCTYPE.*<body[^>]*>", "")
                        .replaceFirst("</body>\\s*</html>", "")
                        .replaceAll("<table[^>]*>", "<table class=\"table table-striped table-bordered\">")
                        .replaceAll("<(/?)code>", "<$1pre>");
                if (bootstrapEncapsulated)
                    text = (javadocHtmlHeader + text + progletHtmlTrailer)
                            .replaceAll("@name", name).replaceAll(
                                    "@base-url",
                                    file.substring(apiDir.length() + 1)
                                            .replaceAll("[^/]+", "..")
                                            .substring(1));
                FileManager.save(file, text);
            }
            if (bootstrapEncapsulated) {
                addBootstrap(apiDir);
            }
            // Copie et encapsulation des sources pour accéder au code
            for (String file : files)
                FileManager.save(
                        apiDir + File.separator
                                + file.substring(srcDir.length()) + ".html",
                        "<pre class=\"prettyprint linenums\">\n"
                                + FileManager.load(file) + "\n</pre>");
        }
    }
    
    /**
     * Lance la compilation java sur un groupe de fichiers.
     * 
     * @param classPath
     *            Chemin nécessaire à la compilation.
     * @param javaFiles
     *            Liste des fichiers source
     */
    private static void javac(String classPath, String[] javaFiles) {
        try {
            String args[] = new String[javaFiles.length + 2];
            args[0] = "-cp";
            args[1] = classPath;
            System.arraycopy(javaFiles, 0, args, 2, javaFiles.length);
            java.io.StringWriter out = new java.io.StringWriter();
            Class.forName("com.sun.tools.javac.Main")
                    .getDeclaredMethod("compile",
                            Class.forName("[Ljava.lang.String;"),
                            Class.forName("java.io.PrintWriter"))
                    .invoke(null, args, new java.io.PrintWriter(out));
            String sout = out.toString().trim();
            sout = sout
                    .replaceFirst(
                            "Note: .* uses unchecked or unsafe operations.\\s*Note: Recompile with -Xlint:unchecked for details.",
                            "");
            if (sout.length() > 0) {
                throw new BuildError("Erreur de compilation java:\n" + sout);
            }
        } catch (Throwable e) {
            throw new BuildError("erreur à la compilation java, «" + e + "»");
        }
    }
    
    //
    // Creation d'une proglet "vide"
    //
    
    private static void createProglet(String location) {
        String name = getProgletName(location, false);
        for (String fileName : filePatterns) {
            String pathName = location + File.separator + fileName;
            if (new File(pathName).exists())
                buildMessage("le fichier " + fileName
                        + " existe déjà, on ne le modifie pas");
            else
                FileManager.save(pathName, FileManager.load(fileName)
                        .replaceAll("@name", name), true);
        }
        buildMessage("La proglet «" + name + "» est crée dans " + location
                + ".");
    }
    
    private static ArrayList<String> filePatterns = new ArrayList<String>();
    static {
        filePatterns.add("proglet.json");
        filePatterns.add("help.html");
        filePatterns.add("Panel.java");
        filePatterns.add("Functions.java");
        filePatterns.add("completion.json");
        filePatterns.add("Translator.java");
    }
    
    //
    // Routines utilitaires
    //
    
    /**
     * Teste si le répertoire et son contenu sont bien ceux d'une proglet et
     * renvoie le nom de la proglet.
     * 
     * @param location
     *            L'adresse de la proglet
     * @param shouldExist
     *            Si vrai alors l'absence de proglet entraîne une erreur
     * @return Le nom de la proglet sans le `proglet-` devant.
     */
    private static String getProgletName(String location, boolean shouldExist) throws BuildError {
        File where = new File(location);
        if (!where.getName().matches("^proglet-.*$"))
            throw new BuildError(
                    "le nom du répertoire n'est pas de la forme 'proglet-<identificateur>' mais '"
                            + where.getName() + "'");
        String name = where.getName().replaceFirst("proglet-", "");
        if (!(4 <= name.length() && name.length() <= 16 && name
                .matches("[a-z][a-zA-Z0-9]+")))
            throw new BuildError(
                    "le nom \""
                            + name
                            + "\" est invalide,\n\t il ne doit contenir que des lettres, faire au moins quatre caractères et au plus seize et démarrer par une lettre minuscule");
        if (shouldExist) {
            if (!where.isDirectory())
                throw new BuildError("le répertoire " + location
                        + " n'existe pas");
            if (!new File(location + File.separator + "proglet.json").exists())
                throw new BuildError("le fichier de description " + location
                        + "/proglet.json n'existe pas");
            if (!new File(location + File.separator + "help.html").exists())
                throw new BuildError("le fichier de documentation " + location
                        + "/help.html n'existe pas");
        } else {
            if (where.exists() && !where.isDirectory())
                throw new BuildError("le ficihier " + location
                        + " existe déja et n'est pas un répertoire");
            if (!where.exists() && !where.mkdirs())
                throw new BuildError("impossible de créer le répertoire "
                        + location);
        }
        return name;
    }
    
    /**
     * Lit un des champs de spécification.
     * 
     * @param location
     *            L'adresse de la proglet
     * @param key
     *            La clef à lire
     * @return La valeur lue sous forme de chaîne de caractère
     */
    private static String getDescriptionValue(String location, String key) {
        try {
            if (!location.equals(jsonLocation)) {
                jsonObject = new JSONObject(FileManager
                        .load(location + File.separator + "proglet.json"));
                if (jsonObject == null)
                    throw new BuildError(
                            "impossible de lire le fichier de description "
                                    + location
                                    + File.separator
                                    + "proglet.json (probablement une erreur de syntaxe)");
                jsonLocation = location;
            }
            return jsonObject.getString(key);
        } catch (Throwable e) {
            throw new BuildError("impossible de lire le champ textuel \"" + key
                    + "\" du fichier de description " + location
                    + File.separator + "proglet.json :«" + e + "»");
        }
    }
    
    private static Object     jsonLocation = null;
    private static JSONObject jsonObject;
    
    // Instancie les champs de spécification dan sun texte
    private static String setDescriptionValues(String location, String text) {
        String who = getDescriptionValue(location, "author");
        return text
                .replaceAll(
                        "@name",
                        new File(location).getName().replaceFirst("proglet-",
                                ""))
                .replaceAll("@title", getDescriptionValue(location, "title"))
                .replaceAll("@icon", getDescriptionValue(location, "icon"))
                .replaceAll("@author", who.replaceFirst("<[^>]*>", "").trim())
                .replaceAll("@email",
                        who.replaceFirst("[^<]*<([^>]*)>.*", "$1").trim());
    }
    
    // Erreur du buidler
    private static class BuildError extends RuntimeException {
        private static final long serialVersionUID = 1L;
        
        public BuildError(String message) {
            super("Erreur à la construction de la proglet :\n\t" + message);
        }
    }
    
    // Message du builder
    private static void buildMessage(String message) {
        System.out.println(message);
    }
    
    /**
     * Ajoute les assets de bootstrap dans un répertoire
     * 
     * @param directory
     *            Le répertoire qui doit contenir assets
     */
    private static void addBootstrap(String directory) {
        try {
            JarManager.copyResource("proglet-css.zip", directory + File.separator + "assets.zip");
            JarManager.jarExtract(directory + File.separator + "assets.zip", directory);
            new File(directory + File.separator + "assets.zip").delete();
        } catch (Exception e) {
            throw new RuntimeException("" + e);
        }
    }
    
    //
    // Implémentation de l'applet interface
    //
    
    @Override
    public void init() {
        getContentPane().add(new ToolBar() {
            private static final long serialVersionUID = 1L;
            {
                addTool("Browse : ", new Runnable() {
                    @Override
                    public void run() {
                        (new JFileChooser() {
                            private static final long serialVersionUID = 1L;
                            {
                                setDialogTitle("Proglet directory finder . . ");
                                setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                String path = UserConfig.getInstance(
                                        "proglet-builder").getProperty(
                                        "proglet-path");
                                if (path != null)
                                    setCurrentDirectory(new File(path));
                            }
                            
                            public void run() {
                                if (showOpenDialog(ProgletBuilder.this
                                        .getContentPane()) == JFileChooser.APPROVE_OPTION) {
                                    if (getSelectedFile().getName().matches(
                                            "^proglet-.*")) {
                                        ((JTextField) getTool("path"))
                                                .setText(getSelectedFile()
                                                        .getParent());
                                        ((JTextField) getTool("name"))
                                                .setText(getSelectedFile()
                                                        .getName()
                                                        .replaceFirst(
                                                                "proglet-", ""));
                                    } else {
                                        ((JTextField) getTool("path"))
                                                .setText(getSelectedFile()
                                                        .getPath());
                                    }
                                    updateLocation();
                                }
                            }
                        }).run();
                    }
                });
                addTool("label1", new JLabel(" "));
                addTool("path", new JTextField(40) {
                    private static final long serialVersionUID = 1L;
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                updateLocation();
                            }
                        });
                    }
                });
                addTool("label2", new JLabel("/proglet-"));
                addTool("name", new JTextField(16) {
                    private static final long serialVersionUID = 1L;
                    {
                        addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                updateLocation();
                            }
                        });
                    }
                });
                addTool("label3", new JLabel(" : "));
                addTool("..", new Runnable() {
                    @Override
                    public void run() {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String location = updateLocation();
                                    if ("Compile"
                                            .equals(((JButton) getTool(".."))
                                                    .getText())) {
                                        compileProglet(location);
                                    } else {
                                        createProglet(location);
                                        updateLocation();
                                    }
                                } catch (Throwable e) {
                                    buildMessage("\n" + e);
                                }
                            }
                        }).start();
                    }
                });
                updateLocation();
            }
            
            private String updateLocation() {
                String path = ((JTextField) getTool("path")).getText();
                if (path.length() > 0) {
                    String location = path + File.separator + "proglet-"
                            + ((JTextField) getTool("name")).getText();
                    ((JButton) getTool("..")).setText(new File(location
                            + File.separator + "proglet.json").exists() ? "Compile"
                            : "Create");
                    if (new File(path).isDirectory())
                        UserConfig.getInstance("proglet-builder").setProperty(
                                "proglet-path", path);
                    return location;
                } else
                    return "";
            }
        }, BorderLayout.NORTH);
        getContentPane().add(new Console(), BorderLayout.CENTER);
    }
}
