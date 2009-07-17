import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.javascool.compilation.Compile;
import org.javascool.translation.Translator;
import org.osgi.framework.Bundle;





public class Main {


	public static void main(String[] args) {

		//TODO ici le chemin du fichier qu'on veut compiler
		String filePath = "C:\\Users\\seb\\Desktop\\TestSeb.java";
		
		
		//TODO remplacer ici par le chemin vers le projet org.javascool.conf
		String pathClassConf = "E:\\workspaceJavascool_fusion\\org.javascool.core";
		
		
		
		try{
			//initialisation des macros par rapport au fichier de configuration	
			Translator.init(pathClassConf+File.separator+"bin");
		}catch(Exception ex){
			System.out.println("erreur initTranslator");
			ex.printStackTrace();
		}
		
		if(filePath.endsWith(".jvs"))
			Translator.translate(filePath);

		Compile.run(filePath, pathClassConf+File.separator+"bin");
		
	}
}