/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Unice
 * Just an utility class exposing some usefull methods
 *
 */
public class Util {
	
	/**
	 * 
	 * @param sourcePath the source path of the file to copy
	 * @param destPath the destination path
	 * @param fileName the name of the file 
	 */
	public static void copyFile(String sourcePath,String destPath,String fileName){
		File source=new File(sourcePath+File.separator+fileName);
		File dest=new File(destPath+File.separator+fileName);
		BufferedReader br=null;
		BufferedWriter bw=null;
		try {
			dest.createNewFile();
			br=new BufferedReader(new FileReader(source));
			bw=new BufferedWriter(new FileWriter(dest));
			String line=null;
			while((line=br.readLine())!=null){
				bw.write(line);
				bw.newLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
