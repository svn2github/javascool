import java.io.File;

import org.javascool.compilation.Compile;
//import org.javascool.translation.Translator;

public class Test {
  public static void main(String[] args) {
    // Juste pour verifier que on est ok cote classpath
    proglet.Proglet.sleep(1);

    //TODO ici le chemin du fichier qu'on veut compiler
    String filePath = "/home/vthierry/Work/culsci/javascool/javascool_web_V1/trunk/org.javascool.proglet/work/Ctest.java";

    //TODO remplacer ici par le chemin vers les classpath
    String pathClass = "/home/vthierry/Work/culsci/javascool/javascool_web_V1/trunk/org.javascool.proglet";

    /*
    try { 
      //initialisation des macros par rapport au fichier de configuration	
      Translator.init(pathClassConf+File.separator+"work");
    } catch(Exception ex){
      System.out.println("erreur initTranslator");
      ex.printStackTrace();
    }
		
    if(filePath.endsWith(".jvs"))
      Translator.translate(filePath);
    */
    
    Compile.run(filePath, pathClass+File.separator+"work");
  }
}
