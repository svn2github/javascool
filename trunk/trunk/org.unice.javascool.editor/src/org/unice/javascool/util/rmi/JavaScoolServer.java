package org.unice.javascool.util.rmi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaScoolServer extends UnicastRemoteObject implements IJavaScoolServer{

	private static final long serialVersionUID = 1L;
	private URLClassLoader cl=null;
	private Class<?> myClass=null;
	private boolean isRunning=false;


	protected JavaScoolServer() throws RemoteException {
		super();
	}

	/**
	 * To get the static fields of the program represented by the pg String
	 * with the cp classpath
	 * @param cp the classpath to execute the program
	 * @param pg the prefixed name of the main's program class
	 * @return a tab filled with the static fields of the program
	 */
	public HashMap<String,Object> getFields(String cp,String pg) throws RemoteException {
		HashMap<String,Object> res=new HashMap<String,Object>();
		if(myClass!=null){
			Field[] myFields=myClass.getDeclaredFields();
			for(int i=0;i<myFields.length;i++){
				Field myField=myFields[i];
				int mod=myField.getModifiers();
				if(Modifier.isPublic(mod) && Modifier.isStatic(mod)){
					try {
						res.put(myField.getName(),myField.get(null));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return res;
	}

	public void run(String cp,final String pg,final String[] args) throws RemoteException {
		try{
			URL[] url =buildPath(cp);
			cl = new URLClassLoader(url,null) ;
			myClass=Class.forName(pg,false, cl);
			final Method myMethod=myClass.getMethod("main",String[].class);
			final Object[] args0=new String[1];
			Thread myThread=new Thread(){
				@Override
				public void run(){
					try {
						isRunning=true;
						setContextClassLoader(cl);
						myMethod.invoke(null,(Object[])args0);
					}catch (Exception e) {
						System.err.println("Une erreur s'est produite pendant l'execution du programme");
					}finally{
						try {
							isRunning=false;
							myClass=null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			};
			myThread.start();
		}catch(ClassNotFoundException e){ 
			System.err.println("Impossible de trouver le fichier "+pg+".class\ndans le repertoire "+cp);
			System.err.println("L'editeur courant a-t-il ete compile?");
			myClass=null;
		} catch (SecurityException e) {
			e.printStackTrace();
			myClass=null;
		} catch (NoSuchMethodException e) {
			System.err.println("Impossible de trouver la methode main dans le fichier "+pg);
			myClass=null;
		}
	}

	private URL[] buildPath(String cp) {
		try {
			URL first_url=new File(cp).toURI().toURL();
			ArrayList<URL> arrayListRes=new ArrayList<URL>();
			arrayListRes.add(first_url);
			String jvm_path=System.getProperty("java.class.path");
			for(String s:jvm_path.split(File.pathSeparator)) arrayListRes.add(new File(s).toURI().toURL());
			URL[] res=new URL[arrayListRes.size()];
			for(int i=0;i<arrayListRes.size();i++) res[i]=arrayListRes.get(i);
			return res;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new URL[0];
	}

	public void stop() throws RemoteException {
		final String string=JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer();
		try {
			System.out.flush();
			System.out.close();
			System.err.flush();
			System.err.close();
			try {
				System.in.close();
			} catch (IOException e) {
			}
			Naming.unbind(string);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			System.exit(0);
		} catch (NotBoundException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		System.exit(0);
	}

	public boolean isRunning() throws RemoteException {
		return isRunning;
	}

}
