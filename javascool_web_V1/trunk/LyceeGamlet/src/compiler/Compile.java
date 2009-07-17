package compiler;

import java.io.*;

import java.util.*;

import java.lang.reflect.*;

// API contenu dans la bibliothèque tools.jar de SUN

import com.sun.tools.javac.Main;

 

 

public class Compile {

 

  String ClassName = "MyClass";

  String ClassSource = "MyClass.java";

 

  public static void main (String args[]){

    Compile mtc = new Compile();

 

  // On génère à la volée le code source Java du programme

  //  mtc.createIt();

    // Puis on le compile

   /* if (mtc.compileIt()) {

       System.out.println("Running " + mtc.ClassName + ":\n\n");

    // Et enfin on l'exécute*/

       mtc.runIt();

     /*  }

    else

       System.out.println(mtc.ClassSource + " contient des erreurs de compilation.");*/

    }

 

  public void createIt() {

    try {

      // Créer le fichier dans un répertoire visible du default ClassLoader

      FileWriter aWriter = new FileWriter(ClassSource, true);

          // Voici la génération du contenu

      aWriter.write("      ");


      aWriter.write(" public class MyClass {");

      aWriter.write("    public void hello() {");

      aWriter.write("         System.out.println(\"Hello tous le monde ! \");");

      aWriter.write(" }}\n");

      aWriter.flush();     

      aWriter.close();

      }

    catch(Exception e){

      e.printStackTrace();

      }

    }

 

  public boolean compileIt() {

    String [] source = { new String(ClassSource)};

    ByteArrayOutputStream baos= new ByteArrayOutputStream();

 

    //new sun.tools.javac.Main(baos,source[0]).compile(source);

    // if using JDK >= 1.3 then use

       int result = com.sun.tools.javac.Main.compile(source);   

    System.out.println(baos.toString());

    return (baos.toString().indexOf("error")==-1);

    }

   

  public void runIt() {

    try {

        // Utilise l'API Reflection pour charger la classe

      Class params[] = {};

      Object paramsObj[] = {};

      // Introduit le nouveau type : magique, non !?

      Class thisClass = Class.forName(ClassName);

      Object iClass = thisClass.newInstance();

      /// Invoke la méthode hello de la classe dynamiquement créée

      Method thisMethod = thisClass.getDeclaredMethod("hello", params);

      thisMethod.invoke(iClass, paramsObj);

      }

    catch (Exception e) {

      e.printStackTrace();

      }

    }
 
  

 }