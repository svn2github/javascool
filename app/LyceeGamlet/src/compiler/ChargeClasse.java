package compiler;

//Cet exemple montre 3 méthodes pour charger une classe, suivant son
//emplacement:
// 1. classe située dans un répertoire du 'CLASSPATH'
//    chargée par Class.forName
// 2. classe située dans un répertoire absent de 'CLASSPATH'
//    chargée par la classe MonChargeur, figurant ci-après
// 3. classe accessible par le réseau (via une URL)
//    chargée par URLClassLoader
//Pour utiliser ces classes simplement, on suppose qu'elles implantent
//une interface connue (InterfaceClasse).
//Nous avons cinq fichiers, dont le texte suit, en commentaires:
// a) ./ChargeClasse.java  programme de démonstration
// b) ./InterfaceClasse    définissant une interface publique
// c) ./Classe0.java       classe implantant InterfaceClasse
// d) ajout1/Classe1.java  classe implantant InterfaceClasse, située
//                         dans un répertoire hors CLASSPATH
// e) ajout2/Classe2.java  implantant InterfaceClasse, suppossée
//                         accessible par le réseau (ressource URL)

//InterfaceClasse.java
//public interface InterfaceClasse {
// public String nom(); 
// }

//// Classe0.java
//public class Classe0 implements InterfaceClasse {
//static{
//   System.out.println("   chargement de Classe0 (chargeur standard)");
//   }
//public String nom() { return "   objet de Classe0";}
//}

//Classe1.java
//public class Classe1 implements InterfaceClasse {
//static{
//   System.out.println("   chargement de Classe1 (extension chargeur)");
//   }
//   public String nom() { return "   objet Classe1";}
//}

//Classe2.java
//public class Classe2 implements InterfaceClasse {
//static{
//   System.out.println("   chargement de Classe2 (chargeur par URL)");
//   }
//public String nom() { return "   objet Classe2";}
//}


//Sur le réseau
// deptinfo.unice.fr/~grin/mescours/minfo/java/tp/tpplugins/
// nicolas.delsaux.free.fr/web/Java/classes_et_classloaders.php
// www-igm.univ-mlv.fr/~roussel/JAVA/java.lang.ClassLoader.html
//netbeans et debug
// http://fr.netbeans.org/edi/36/utilisation/debug.html
//cours à Marseille
//http://www.dil.univ-mrs.fr/~gispert/enseignement/coursJava/sommaire.html

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import java.net.URLClassLoader;
import java.net.URL;

public class ChargeClasse {
ClassLoader cs; 
ChargeClasse() {
    cs = this.getClass().getClassLoader();
 }
public static void main(String []args) {
 ChargeClasse cc = new ChargeClasse();
 
 cc.desChargements();
 }
public void desChargements() {
  //1 Chargement par ClassLoader standard (classe située dans un répertoire
  //     définie dans CLASSPATH
  System.out.println("Chargement standard");
  try {    
    Class c0 = Class.forName("Classe0");
    System.out.println("   on achargé: "+c0.getClass().getName());
    InterfaceClasse obj0 = (InterfaceClasse) c0.newInstance(); 
    System.out.println("   "+obj0.nom());
    }
 catch(ClassNotFoundException e) {
    System.out.println("   échec 0 : ");
    }
catch(Exception e) {
    System.out.println("   échec 0 : ");
    }

 //2 Chargement par MonChargeur (extension ClassLoader standard)
 String unRep="D:\\projetLycée\\jars";
 MonChargeur mc = new MonChargeur(unRep,cs);
 System.out.println("Chargement personnel");
 try {    
    Class ca = mc.charge("Classe0",true);
    System.out.println("   a. chargé: "+ca.getClass().getName());
    Class cb = mc.charge("Classe1",true);
    System.out.println("   b. chargé: "+cb.getClass().getName());
    InterfaceClasse objb = (InterfaceClasse) cb.newInstance(); 
    System.out.println("   "+objb.nom());
     }
  catch(ClassNotFoundException e) {
     System.out.println("   échec 1 : "+e);
     }
  catch(Exception e) { System.out.println("   échec 1 : " +e); }

  //3 Chargement par extension URLClassLoader
 String repClasse="D:\\projetLycée\\jars";
 System.out.println("Chargement par URLLoader");
  try { 
     URLClassLoader cc = new URLClassLoader(
         new URL[] {new URL("file:"+repClasse+File.separator) },
         // null);
         cs);
    Class c = cc.loadClass("Classe2"); 
    System.out.println("   on a chargé "+c.getName());
    InterfaceClasse  obj2= (InterfaceClasse)c.newInstance();
    System.out.println("   "+obj2.nom());
    }
 catch(Exception e) { System.out.println("!!!chargement 2: "+e);}
}
}

//Charge classe d'un répertoire 
class MonChargeur extends ClassLoader{
String nomRep;  // nom répertoire terminé par /

MonChargeur(String r, ClassLoader c) {
  super(c);
  // cs = c;
  nomRep= r.endsWith(File.separator) ? r : r+File.separator;
  }

// Pourait être une surcharge de ClassLoader.loadClass
public synchronized Class charge(String nom, boolean resolve)
                                  throws ClassNotFoundException {
  Class c=null;
  // Pour classe située dans le 'CLASSPATH'
  try{ return findSystemClass(nom); }
  catch(Exception e) {
     byte to[] = chargeFic(nom);
     c = defineClass( nom, to, 0, to.length);
     resolveClass(c);
     }
  return c;
  }

// Du fichier .class vers tableau d'octats
byte[] chargeFic(String nom) throws ClassNotFoundException {
  byte to[] = null;  // tableau d'octets
  String nomFic = nomRep + nom + ".class";
  try {
     File fic = new File(nomFic);
     if (fic.length() > Integer.MAX_VALUE) 
                             throw new ClassNotFoundException();
     int lg = (int) fic.length();
     to = new byte[lg];
     FileInputStream  flot = new FileInputStream(fic);
     for( int nb=0, pos=0; lg>0 && nb != -1; ) {
        pos+=nb; lg-=nb;
        nb = flot.read(to,pos,lg); 
        }
     flot.close();
     }
  catch(Exception e) { throw new ClassNotFoundException(); }
  return to;
  }
} //fin class MonChargeur

//fin ChargeClasse.java