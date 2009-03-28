package org.unice.javascool.traducteurJVS;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.conf.BeanFonctions;
import org.unice.javascool.conf.BeanMacros;
import org.unice.javascool.conf.ConfException;
import org.unice.javascool.util.rmi.UtilServer;
import java.net.URL;
import java.net.URLClassLoader;
import javax.tools.*;

/**
 * cette classe contient toutes les methodes nececssaire a la traduction du code javascool 
 * en code java
 * @author Chalmeton Sébastien 
 */
public class Translator {

	//le nombre de ligne rajoute
	public static int nbLigne = 0;
	// le nom de la classe
	private static String className="";
	//la liste des fonction presente dans le code source
	private static ArrayList<BeanFonctions> listFonctions;
	//la liste des macros presente dans le source
	private static ArrayList<BeanMacros> listMacros;
	//la liste des imports a faire
	private static ArrayList<String >listImport = new ArrayList<String>();
	//le texte correspondant au code source
	private static String text = "";
	private static Method[] list_methods;


	static{
		try {
			listFonctions = BeanFactory.getBeanFonctions(BeanFactory.fonConfFile);
			listMacros = BeanFactory.getBeanMacros(BeanFactory.macConfFile);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfException e) {
			e.printStackTrace();
		}
	}

	/**
	 * cette methode initialise le traducteur, elle cree la classe Macro en fonction des fichiers de
	 * configuration et ajoute les imports necessaires
	 * @param path le chemin ou l'on veut sauvegarder le fichier Macro
	 */
	public static void init(String path){
		File f = new File(path);
		String myFile=path+File.separator+"Macro.java";
		String ls=System.getProperty("line.separator");
		String ps=File.separator;
		try {
			f.mkdir();
			PrintWriter  file = new PrintWriter(myFile);
			file.println("package javascool;");
			file.println("public class Macro {");
			for(int i=0; i<listMacros.size(); i++){
				BeanMacros bean = listMacros.get(i);
				file.println("public static "+bean.getSignature()+"{");
				file.println("\t"+bean.getTraduction());
				file.println("}");
			}
			file.println("}");
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		listImport.add("import static javascool.Macro.*;");
		listImport.add("import java.util.ArrayList;");
		nbLigne = nbLigne +2;
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if(compiler==null){
			System.err.println("Aucun JDK detecte."+ls+"Assurez vous d'installer la version 6"+ls+"et de le rendre accessible par la variable PATH");
			return;	
		}
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
		.getJavaFileObjectsFromStrings(Arrays.asList(myFile));
		String rootDir=UtilServer.getPluginFolder(org.unice.javascool.editor.Activator.PLUGIN_ID);
		String editDir=rootDir+"bin";
		String destDir=rootDir+"ServerSide"+ps;
		Iterable<String> options=Arrays.asList("-classpath",editDir,"-d",destDir);
		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics,options,null, compilationUnits);
		boolean success = task.call();
		if(!success){
			System.err.println("La compilation du fichier de Macros a echoue.\nRedemmarez JavaScool.\nVerrifiez la correction du fichier de configuration.");
			return;	
		}
	}

	/**
	 * cette methode traduit un un fichier javascool en fichier java
	 * @param path le chemin du fichier javascool a traduire
	 */
	public static void translate(String path) {
		nbLigne = 0;
		text="";
		listImport = new ArrayList<String>();
		listImport.add("import static javascool.Macro.*;");
		listImport.add("import java.util.ArrayList;");
		nbLigne = nbLigne +2;
		
		className = path.substring(path.lastIndexOf(File.separator)+1, path.lastIndexOf("."));
		BufferedReader buffer = null;
		PrintWriter myFile = null;
		String line="";

		try{
			buffer = new BufferedReader(new FileReader(path));
			path = path.replace(".jvs",".java");
			myFile = new PrintWriter(path);

		}catch(Exception e){
			//	e.printStackTrace();
		}

		try {

			//ajout de pulic class devant nom de la class
			text = "public class "+buffer.readLine()+System.getProperty("line.separator");

			//on recupere les imports neccessaire a faire
			while ((line = buffer.readLine()) != null){
				getImportFct(line);
				text+=line+System.getProperty("line.separator");
			}

			buffer.close();

			addImport(text);//ajout des imports
			
			myFile.println(text);//ecriture dans le fichier
			myFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * cette methode ajoute les imports necessaire dans le fichier 
	 * source java
	 * @param text2
	 */
	private static void addImport(String text2) {
		String tmp="";
		//ajout ds imports au debut du fichier
		for(int i=0; i<listImport.size() ;i++){
			tmp += listImport.get(i)+System.getProperty("line.separator");
		}

		text = tmp+text;
	}


	/**
	 * cette methode regarde toutes fonctions sur une ligne et recupere les imports necessaire
	 * @param line la ligne a analyser
	 */
	private static void getImportFct(String line){
		for(int i=0; i< listFonctions.size(); i++){
			BeanFonctions bean = listFonctions.get(i);
			if(line.contains(bean.getNom())){
				if(!listImport.contains(bean.getImport())){
					listImport.add("import static "+bean.getImport()+";");
					nbLigne++;
				}
			}
		}

	}

	/**
	 * cette methode rend tous les methodes du code javascool "public static"
	 * @param path
	 */
	public static void SetSignatureCode(String path) {
		//list_methods = null ;
		try {
			
			String tmp =  path.substring(0, path.lastIndexOf(File.separator)+1);
			
			File f = new File(tmp);
			URL url = f.toURI().toURL();
			
			URLClassLoader loader = URLClassLoader.newInstance(new URL[] {url});
			
			Class<?> c = loader.loadClass(className);
			list_methods = c.getDeclaredMethods();
			Field[] fields = c.getDeclaredFields();

			for(Method m : list_methods){
				String name = m.getName();
				String type = m.getReturnType().getSimpleName();
				
				
				String expr_meth = "[^\"public static\"]"+type+"((\\s)*)"+name;
				text = text.replaceAll(expr_meth, "public static "+type+" "+name);
				
			}	

			replaceField(fields);
			
			String tmp_path = path.substring(path.lastIndexOf(File.separator)+1, path.length());
			
			insertMyMain(tmp_path);
			
			
			PrintWriter myFile = new PrintWriter(path);	
			myFile.println(text);//ecriture dans le fichier
			myFile.close();
		} catch (Exception e) {
			System.err.println("probleme de traduction du code");
			e.printStackTrace();
		}
	}

	/**
	 * cette fonction insere dans le ficher source notre propre main qui fait appel
	 * a la fonction main definie par l'utilisateur
	 * @param filename le nom du fichier
	 */
	private static void insertMyMain(String filename) {
		String tmp = System.getProperty("line.separator")+"public static void main(String[] args){"+System.getProperty("line.separator");
		tmp+= "try{"+System.getProperty("line.separator");
		if(containsMain(list_methods))
			tmp+= "main();"+System.getProperty("line.separator");
		tmp+= "}catch(Exception e){org.unice.javascool.util.erreur.ReThrower.log(e, \""+filename+"\", "+nbLigne+");}"+System.getProperty("line.separator");
		tmp+= "}"+System.getProperty("line.separator");
		tmp+= "}"+System.getProperty("line.separator");
				
		int index_lastBracket = text.lastIndexOf("}");
		text = text.substring(0,index_lastBracket);
		text += tmp;
	}

	/**
	 * cette ùethode nous dit si le code source contient une fonction main 
	 * @param list_methods2
	 * @return
	 */
	private static boolean containsMain(Method[] list_methods2) {
		for(Method m : list_methods2){
			if(m.getName().equals("main"))
				return true;
		}
		return false;
	}

	/**
	 * cette methode rajoute public static devant tous les champs du code source javascool
	 * @param fields
	 */
	private static void replaceField(Field[] fields) {
		if(fields.length == 0 ) return;//case of no fields
		
		int index_firstMeth = text.indexOf(list_methods[0].getName());
		
		if(index_firstMeth != -1){
			String tmp_textPart1 = text.substring(0, index_firstMeth);
			String tmp_textPart2 = text.substring(index_firstMeth, text.length());

			for(Field f : fields){
				String type = f.getType().getSimpleName();//.toString();
				String nom = f.getName();
				
				String type_tmp = type.replace("[", "(\\s)*\\[(\\s)*");
				type_tmp = type_tmp.replace("]", "\\]");
						
				String expr = "[^\"public static\"]"+type_tmp+"(\\s)+"+nom;
				tmp_textPart1 = tmp_textPart1.replaceAll(expr, "public static "+type+" "+nom);
				
			}
			text = tmp_textPart1+tmp_textPart2;
		}else{

			for(Field f : fields){
				String type = f.getType().toString();
				String nom = f.getName();
				String expr = type+"(\\s)+"+nom;
				text = text.replaceAll(expr, "public static "+type+" "+nom);
			}
		}
	}
}