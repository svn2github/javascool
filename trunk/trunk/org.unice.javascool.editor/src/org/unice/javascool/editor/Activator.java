package org.unice.javascool.editor;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.unice.javascool.traducteurJVS.Translator;
import org.unice.javascool.util.rmi.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	private static Process registry=null;
	private Runnable hook=new Runnable(){
		@Override public void run(){
			try{
				if(UtilServer.getMyProcess()!=null){
					UtilServer.closeRessources();
					String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
					IJavaScoolServer server=(IJavaScoolServer)Naming.lookup(string);
					server.stop();
					UtilServer.dispose();
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(registry!=null)
					registry.destroy();
			}
		}
	};

	// The plug-in ID
	public static final String PLUGIN_ID = "org.unice.javascool.editor";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Runtime.getRuntime().addShutdownHook(new Thread(hook));
		initStub();
		Translator.init(UtilServer.getPluginFolder(org.unice.javascool.editor.Activator.PLUGIN_ID)+"bin"+File.separator+"javascool");
		initRegistry();
		plugin = this;
	}




	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
			plugin = null;
			super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	private void initStub() {
		String s=File.separator;
		String rootDir=UtilServer.getPluginFolder(PLUGIN_ID);
		String binDir=rootDir+"bin"+s;
		String path="org"+s+"unice"+s+"javascool"+s+"util"+s+"rmi"+s;
		String stubName="JavaScoolServer_Stub.class";
		File stubClass=new File(binDir+path+stubName);
		if(!stubClass.exists()){
			String serverDir=rootDir+"ServerSide"+s;
			File serverStub=new File(serverDir+path+stubName);
			FileInputStream is=null;
			FileOutputStream os=null;
			try {
				is=new FileInputStream(serverStub.getAbsolutePath());
				os=new FileOutputStream(stubClass.getAbsolutePath());
				int read;
				while((read=is.read())!=-1) os.write(read);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				try {
					os.flush();
					os.close();
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public void initRegistry(){
		if(!testRegistry()){
			ProcessBuilder pb=new ProcessBuilder(new String[]{"rmiregistry",JavaScoolServerRegister.getPort()});
			pb.directory(new File(System.getProperty("user.dir")));
			pb.environment().remove("CLASSPATH");
			pb.redirectErrorStream(true);
			try {
				registry=pb.start();
			} catch (IOException e) {
				System.err.println("probleme au demarrage, execution de programmes impossible");
			}
		}
		UtilServer.waitForRegister(JavaScoolServerRegister.getProtocol());
		Thread attachServer=new Thread(new UtilServer());
		attachServer.start();
	}

	public boolean testServer(){
		String register=JavaScoolServerRegister.getProtocol();
		String server=register+JavaScoolServerRegister.getServer();
		UtilServer.waitForRegister(register);
		boolean res=UtilServer.testServer(server);
		System.out.println("server "+res);
		return res;
	}

	public boolean testRegistry() {
		String registry=JavaScoolServerRegister.getProtocol();
		return UtilServer.testRegistry(registry);
	}
}
