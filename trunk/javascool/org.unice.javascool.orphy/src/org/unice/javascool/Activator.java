package org.unice.javascool;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.unice.javascool.actions.OrphyAction;
import org.unice.javascool.orphyRMI.IOrphy;
import org.unice.javascool.orphyRMI.OrphyRegister;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.util.rmi.UtilServer;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = Messages.getString("Activator.0"); //$NON-NLS-1$
	private static Process registry=null;
	// The shared instance
	private static Activator plugin;
	
	private Runnable hook=new Runnable(){
		@Override public void run(){
			try{
				final String string = JavaScoolServerRegister.getProtocol() + OrphyRegister.getServer();
				try {
					IOrphy orphy = (IOrphy)Naming.lookup(string);
					orphy.close("");
					Naming.unbind(string);
					OrphyRegister.getMyProcess().destroy();
				}catch(Exception e){
					System.out.println("Activez Orphy en lancant l'interace graphique Orphy \nau moin une fois s'il vous plait.");
				}
				OrphyAction.cleanClose();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(registry!=null)
					registry.destroy();
			}	
		}
	};
	
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
		
		
		//Creation of the files needed for the API RXTX if they don't exist yet
		File destDllFile = new File(System.getProperty(Messages.getString("Activator.1"))+
				File.separator+Messages.getString("Activator.2")+
				File.separator+Messages.getString("Activator.3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		File destJarFile = new File(System.getProperty(Messages.getString("Activator.4"))+
				File.separator+Messages.getString("Activator.5")+File.separator+Messages.getString("Activator.6")+
				File.separator+Messages.getString("Activator.7")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		
		
		copyStubFile();
		
		if(!destJarFile.exists()){
			System.out.println("il n'existe pas");

			URL url = Platform.getBundle(PLUGIN_ID).getEntry(Messages.getString("Activator.8")); //$NON-NLS-1$
			try {
				url = FileLocator.resolve(url);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
			Path res=new Path(url.getPath()+File.separator+Messages.getString("Activator.9")+File.separator+Messages.getString("Activator.10")); //$NON-NLS-1$ //$NON-NLS-2$

			File sourceJarFile = new File(res.toOSString());

			// Declaration des flux

			java.io.FileInputStream sourceJarStream = null;
			java.io.FileOutputStream destJarStream=null;

			try {
				// Création du fichier :
				destJarFile.createNewFile();

				// Ouverture des flux
				sourceJarStream = new java.io.FileInputStream(sourceJarFile);
				destJarStream = new java.io.FileOutputStream(destJarFile);

				// Lecture par segment de 0.5Mo 
				byte buffer[]=new byte[512*1024];
				int nbLecture;

				while( (nbLecture = sourceJarStream.read(buffer)) != -1 ) {
					destJarStream.write(buffer, 0, nbLecture);
				} 

			} catch( java.io.FileNotFoundException f ) {
				System.out.println(Messages.getString("Activator.11") + f); //$NON-NLS-1$

			} catch( java.io.IOException e ) {
				System.out.println(Messages.getString("Activator.12") + e); //$NON-NLS-1$
			} finally {
				// Quoi qu'il arrive, on ferme les flux
				try {
					sourceJarStream.close();
				} catch(Exception e) { }
				try {
					destJarStream.close();
				} catch(Exception e) { }
			} 

		}
		
		if(!destDllFile.exists()){
	        
			URL url = Platform.getBundle(PLUGIN_ID).getEntry(Messages.getString("Activator.13")); //$NON-NLS-1$
			try {
				url = FileLocator.resolve(url);
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
			Path res=new Path(url.getPath()+File.separator+Messages.getString("Activator.14")+File.separator+Messages.getString("Activator.15")); //$NON-NLS-1$ //$NON-NLS-2$

			File sourceDllFile = new File(res.toOSString());
			
	        java.io.FileInputStream sourceDllStream = null;
	        java.io.FileOutputStream destDllStream=null;
	        
	        try {
	                // Création du fichier :
	                destDllFile.createNewFile();
	                
	                // Ouverture des flux
	                sourceDllStream = new java.io.FileInputStream(sourceDllFile);
	                destDllStream = new java.io.FileOutputStream(destDllFile);
	                
	                // Lecture par segment de 0.5Mo 
	                byte buffer[]=new byte[512*1024];
	                int nbLecture;
	                
	                while( (nbLecture = sourceDllStream.read(buffer)) != -1 ) {
	                        destDllStream.write(buffer, 0, nbLecture);
	                } 
	                
	        } catch( java.io.FileNotFoundException f ) {
	                
	        } catch( java.io.IOException e ) {
	                
	        } finally {
	                // Quoi qu'il arrive, on ferme les flux
	                try {
	                        sourceDllStream.close();
	                } catch(Exception e) { }
	                try {
	                        destDllStream.close();
	                } catch(Exception e) { }
	        } 
	
		}
		
		initRegistry();
		
		plugin = this;
	}

	public void copyStubFile(){
		//Creation of the Stub file
		
		URL urlStub = Platform.getBundle("org.unice.javascool.editor").getEntry(Messages.getString("Activator.8")); //$NON-NLS-1$
		try {
			urlStub = FileLocator.resolve(urlStub);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		Path stubPath = new Path(urlStub.getPath()+File.separator+"ServerSide"+File.separator+"org"+File.separator+"unice"+File.separator
				+"javascool"+File.separator+"orphy"+File.separator+"Orphy_Stub.class"); //$NON-NLS-1$ //$NON-NLS-2$
		File destStub = new File(stubPath.toOSString());
		
		if(destStub.exists()){
			destStub.delete();
		}
		
		urlStub = Platform.getBundle(PLUGIN_ID).getEntry(Messages.getString("Activator.8")); //$NON-NLS-1$
		try {
			urlStub = FileLocator.resolve(urlStub);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		stubPath = new Path(urlStub.getPath()+File.separator+"bin"+File.separator+"org"+File.separator+"unice"+File.separator
				+"javascool"+File.separator+"orphy"+File.separator+"Orphy_Stub.class"); //$NON-NLS-1$ //$NON-NLS-2$
		File sourceStub = new File(stubPath.toOSString());
		
		// Declaration des flux

		java.io.FileInputStream sourceStubStream = null;
		java.io.FileOutputStream destStubStream=null;

		try {
			// Création du fichier :
			destStub.createNewFile();

			// Ouverture des flux
			sourceStubStream = new java.io.FileInputStream(sourceStub);
			destStubStream = new java.io.FileOutputStream(destStub);

			// Lecture par segment de 0.5Mo 
			byte buffer[]=new byte[512*1024];
			int nbLecture;

			while( (nbLecture = sourceStubStream.read(buffer)) != -1 ) {
				destStubStream.write(buffer, 0, nbLecture);
			} 

		} catch( java.io.FileNotFoundException f ) {
			System.out.println(Messages.getString("Activator.11") + f); //$NON-NLS-1$

		} catch( java.io.IOException e ) {
			System.out.println(Messages.getString("Activator.12") + e); //$NON-NLS-1$
		} finally {
			// Quoi qu'il arrive, on ferme les flux
			try {
				sourceStubStream.close();
			} catch(Exception e) { }
			try {
				destStubStream.close();
			} catch(Exception e) { }
		} 

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

	public void initRegistry(){
		if(!UtilServer.testRegistry(JavaScoolServerRegister.getProtocol())){
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
		OrphyRegister.attachOrphyServer();
		waitForBinding(JavaScoolServerRegister.getProtocol()+OrphyRegister.getServer());

	}

	public static void waitForBinding(String string){
		try {
			while(true){
				try{
					Naming.lookup(string);
					return;
				}catch(NotBoundException e){
					Thread.yield();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}
