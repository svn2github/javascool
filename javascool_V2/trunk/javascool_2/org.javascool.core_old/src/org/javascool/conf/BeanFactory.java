package org.javascool.conf;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.tools.JavaFileManager.Location;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;

/**
 * To distribute singleton arraylist of abstractbean to get information from conf files
 * @author Unice
 *
 */
public class BeanFactory {

	private static String confFile=null;
	public static String repConfFile=null;
	public static String errConfFile=null;
	public static String macConfFile=null;
	public static String fonConfFile=null;

	/** arraylist's beanfonctions' getter */
	public static ArrayList<BeanFonctions> getBeanFonctions(String file) throws IOException, ConfException{
		ArrayList<BeanFonctions> bts=BeanFonctions.getBeanFonctions(file);
		return bts;
	}

	/** arraylist's beanmacros' getter */
	public static ArrayList<BeanMacros> getBeanMacros(String file) throws IOException, ConfException{
		ArrayList<BeanMacros> bts=BeanMacros.getBeanMacros(file);
		return bts;
	}

	/** arraylist's beanerrors' getter */
	public static ArrayList<BeanErrors> getBeanErrors(String file) throws IOException, ConfException{
		ArrayList<BeanErrors> bes=BeanErrors.getBeanErrors(file);
		return bes;
	}

	/** arraylist's beandirectories' getter */
	public static ArrayList<BeanDirectories> getBeanDirectories(String file) throws IOException, ConfException{
		ArrayList<BeanDirectories> bds=BeanDirectories.getBeanDirectories(file);
		return bds;
	}

	/** to save all config */
	static void saveAll(){
		BeanDirectories.saveAll();
		BeanErrors.saveAll();
		BeanFonctions.saveAll();
		BeanMacros.saveAll();
	}

	public static void init(){
		String jvsConfRoot=initSystem();
		File confRep=new File(jvsConfRoot);
		if(confRep.mkdir()){
			URL url=Platform.getBundle(Activator.PLUGIN_ID).getEntry("/lib");
			try {
				url=FileLocator.resolve(url);
			} catch (IOException e) {
				System.err.println("impossible d'initialiser la configuration");
			}
			Path res=new Path(url.getPath());
			String pluginDir=res.toOSString();
			File libDir=new File(pluginDir);
			String[] confFiles=libDir.list();
			for(String f:confFiles){
				if(f.endsWith(".bml"))
					Util.copyFile(libDir.getPath(),confRep.getPath(),f);
			}
		}
		String[] files=confRep.list();
		for(String f:files) initConfFiles(confRep.getPath(),f);
		if(repConfFile==null)repConfFile=confFile;
		if(macConfFile==null)macConfFile=confFile;
		if(fonConfFile==null)fonConfFile=confFile;
		if(errConfFile==null)errConfFile=confFile;
	}

	private static void initConfFiles(String path, String f) {
		String file=path+File.separator+f;
		if(f.endsWith(".bml"))
			if(f.contains("erreur")) errConfFile=file;
			else if(f.contains("macro")) macConfFile=file;
			else if(f.contains("repertoire")) repConfFile=file;
			else if(f.contains("fonction")) fonConfFile=file;
			else if(f.equals("conf.bml")) confFile=file;
	}

	private static String initSystem() {
		String system=System.getProperty("os.name").toLowerCase();
		if(system.contains("windows")) return initWindows();
		else return initOther();
	}

	private static String initOther() {
		String root=System.getProperty("user.home");
		String res=root+File.separator+".JavasCool";
		return res;
	}

	private static String initWindows() {
		String root=System.getProperty("user.home");
		String res=root+File.separator+"JavasCoolConf";
		return res;
	}
}
