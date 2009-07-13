package org.javascool.translation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import java.net.URL;
import java.net.URLClassLoader;
import javax.tools.*;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.javascool.conf.BeanFactory;
import org.javascool.conf.BeanFonctions;
import org.javascool.conf.BeanMacros;
import org.javascool.conf.ConfException;

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
			String directory = System.getProperty("user.home")+File.separator+"JavasCoolConf";
			
			BeanFactory.fonConfFile = directory+File.separator+"fonctions_conf.bml";
			BeanFactory.macConfFile = directory+File.separator+"macros_conf.bml";

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

		} catch (Exception e) {
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
		//TODO venir ici et modifier
		
		String rootDir = path;
		String editDir = rootDir+File.separator+"bin";
	
		//String destDir=rootDir+"ServerSide"+ps;
		String destDir = rootDir;
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

			//on recupere les imports neccessaire a faire en parcourant chaque ligne du code source
			while ((line = buffer.readLine()) != null){
				getImportFct(line);
				text+=line+System.getProperty("line.separator");
			}

			buffer.close();

			//ajout des imports ncecesssaire
			addImport(text);

			//ecriture du code source modifier dans le fichier
			myFile.println(text);
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
				String expr_import = "import static "+bean.getImport()+";";

				if(!listImport.contains(expr_import)){
					listImport.add(expr_import);
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
			//on re"cupere le nom de la classe par rapport au nom du fchier
			String tmp =  path.substring(0, path.lastIndexOf(File.separator)+1);

			File f = new File(tmp);
			URL url = f.toURI().toURL();

			URLClassLoader loader = URLClassLoader.newInstance(new URL[] {url});
			Class<?> c = loader.loadClass(className);
			list_methods = c.getDeclaredMethods();//la liste de methods declare
			Field[] fields = c.getDeclaredFields();//la liste des vairiable de classe declare

			//System.out.println("nb methods found = "+list_methods.length);
			for(Method m : list_methods){
				String name = m.getName();
				String type = m.getReturnType().getSimpleName();

				String expr_meth = type+"((\\s)*)"+name+"((\\s)*)"+"[(]";
				text = text.replaceAll(expr_meth, "public static "+type+" "+name+" (");

				//System.out.println("method name = "+name);
			}	

			replaceField(fields);

			String tmp_path = path.substring(path.lastIndexOf(File.separator)+1, path.length());

			if(containsMain(list_methods))
				insertMain(tmp_path);


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
	private static void insertMain(String filename) {
		String tmp = System.getProperty("line.separator")+"public static void main(String[] args){"+System.getProperty("line.separator");
		tmp+= "try{"+System.getProperty("line.separator");
		tmp+= "main();"+System.getProperty("line.separator");
		tmp+= "}catch(Exception e){org.javascool.util.erreur.ReThrower.log(e, \""+filename+"\", "+nbLigne+");}"+System.getProperty("line.separator");
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
	 * que ce soit des methods ou des attributs de classe
	 * @param fields
	 */
	private static void replaceField(Field[] fields) {
		//System.err.println("[replaceFields ] nb fields : "+fields.length);
		//System.err.println("[replaceFields ] nb methods : "+list_methods.length);


		if(list_methods.length != 0){
			int index_firstMeth = text.indexOf(list_methods[0].getName());
			if (index_firstMeth != -1){

				String tmp_textPart1 = text.substring(0, index_firstMeth);
				String tmp_textPart2 = text.substring(index_firstMeth, text.length());

				for(Field f : fields){
					
					String type = f.getType().getSimpleName();//.toString();
					String nom = f.getName();
					//System.err.println("type = "+type);
					//System.err.println("nom = "+nom);
					//String type_tmp = type.replace("[", "(\\s)*\\[(\\s)*");
					//System.err.println("type_tmp = "+type_tmp);
					//type_tmp = type_tmp.replace("]", "\\]");

					String expr = "[^\"public static\"]"+type+"(\\s)+"+nom;
				//	String expr = type+"((\\s)*)"+nom+"((\\s)*)"+"[(]";

					tmp_textPart1 = tmp_textPart1.replaceAll(expr, "public static "+type+" "+nom);
					
				}
				text = tmp_textPart1+tmp_textPart2;
			}
		}
		
		else{
			for(Field f : fields){
				String type = f.getType().toString();
				String nom = f.getName();
				String expr = type+"(\\s)+"+nom;
				text = text.replaceAll(expr, "public static "+type+" "+nom);
			}
		}
	}
}