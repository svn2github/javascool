/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.compilation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Execution {


	/**
	 * construction du CLASSPATH pour executer la classe dynamiquement
	 * @param classPath
	 * @param filePath
	 * @return
	 */
	private static URL[] buildClassPath(String[] classPath, String filePath){
		URL[] res = new URL[classPath.length + 1];
		URL urlFile = null;

		try {
			urlFile = new File(filePath).toURI().toURL();

			for(int i =0; i< classPath.length; i++){
				res[i]  = new File(classPath[i]).toURI().toURL();
			}

			res[classPath.length] = urlFile;

		} catch (MalformedURLException e) {
			System.err.println("Execution method :: invalide URL during build");
			e.printStackTrace();
		}	


		return res;
	}


	/**
	 * cette methode permet d'executer la méthode main presente dans la classe placée en paramètre.
	 * si le classpath n'est pas <code>null</code> il doit referencer le path des différents path necessaire
	 * a l'execution de la classe
	 * 
	 * @param pathFile le chemin de la classe que l'on souhaite executer
	 * @param className le nom de la classe que l'on souhaite executer
	 * @param classpath les differents path necessaire a l'execution de la classe
	 * @throws ClassNotFoundException 
	 */
	public static void execute(String filePath, String className, String[] classPath) throws ClassNotFoundException{


		//construction of the classPath
		URL[] paths = buildClassPath(classPath, filePath);

		URLClassLoader loader = new URLClassLoader(paths); 
		Class<?> c = null;

	
		c = loader.loadClass(className);
	
		//maintenant l'appel du main
		Method myMethod = null;
		try {

			myMethod = c.getMethod("main",String[].class);

		} catch (SecurityException e) {
			System.err.println("Execution method :: security main invoke problem");
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			System.err.println("Execution method :: no sch method main for invoke");
			e.printStackTrace();
		}


		try {
			myMethod.invoke(null,new Object[1]);
		} catch (IllegalArgumentException e) {
			System.err.println("Execution method :: invoke main -> illegal Argument");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			System.err.println("Execution method :: invoke main -> illegal Acces");
			e.printStackTrace();
		} catch (InvocationTargetException e) {}//not print for stop execution
	}
}

