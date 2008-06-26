package org.unice.javascool.util.rmi;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.eclipse.core.runtime.Platform;

public class UtilServer implements Runnable{

	private static Process myProcess=null;
	private static Priority myRules=new Priority();

	public static String getPluginFolder(String id) {
		URL url = Platform.getBundle(id).getEntry("/");
		try {
			url = FileLocator.resolve(url);
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		Path res=new Path(url.getPath());
		return res.toOSString();
	}

	private static void processRedirect(Process exec) {
		waitForBinding(JavaScoolServerRegister.getProtocol()+JavaScoolServerRegister.getServer());
		final Thread inThread=new MyThread(exec){
			@Override
			public void run(){
				BufferedReader brIn=new BufferedReader(new InputStreamReader(exec.getInputStream()));
				int read;
				try {
					while((read=brIn.read()) != -1){
						try{
							exec.exitValue();
							break;
						}catch(IllegalThreadStateException e){
							System.out.print((char)read);
							System.out.flush();
						}
					}
				} catch (IOException ioException) {
					System.err.println("erreur inThread");
				}
				try {
					System.out.flush();
					brIn.close();
				} catch (IOException e) {
					System.err.println("erreur inThread");
				}
			}
		};
		final Thread errThread=new MyThread(exec){
			@Override
			public void run(){
				BufferedReader brErr=new BufferedReader(new InputStreamReader(exec.getErrorStream()));
				String read;
				try {
					while((read=brErr.readLine()) != null){
						try{
							exec.exitValue();
							break;
						}catch(IllegalThreadStateException e){
							System.err.println(read);
							System.err.flush();
						}
					}
				} catch (IOException ioExeption) {
					System.err.println("erreur errThread");
				}
				try {
					System.err.flush();
					brErr.close();
				} catch (IOException e) {
					System.err.println("erreur errThread");
				}
			}
		};
		final Thread outThread=new MyThread(exec){
			@Override
			public void run(){
				PrintStream os=new PrintStream(exec.getOutputStream());
				int read;
				while(true){
					try {
						if((read=myRules.demandeLecture(exec))!=-1){		
							os.print(Character.toChars(read));
							os.flush();
						}else{
							break;
						}
					} catch (IOException ioException){
						ioException.printStackTrace();
					}
				}
				os.flush();
				os.close();
			}
		};
		Thread waitThread=new MyThread(exec){
			@Override
			public void run(){
				try {
					exec.waitFor();
					return;
				} catch (InterruptedException e) {
					exec.destroy();
				}finally{
					try {
						errThread.join();
						inThread.join();
						outThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					exec.destroy();
				}

			}
		};
		outThread.start();
		inThread.start();
		errThread.start();
		waitThread.start();
	}


	public static Process getMyProcess() {
		return myProcess;
	}

	public static void setMyProcess(Process p) {
		myProcess = p;
	}
	public static void attachJavaSchoolServer(){
		try{
			String dirRootString=UtilServer.getPluginFolder(org.unice.javascool.editor.Activator.PLUGIN_ID);
			String dirString=dirRootString+"ServerSide";
			File dir=new File(dirString);
			String[] cmd=new String[]{"java","-Djava.rmi.server.codebase="+dir.toURI().toASCIIString(),"-Djava.security.policy="+dirRootString+".java.policy","org.unice.javascool.util.rmi.JavaScoolServerRegister"};
			ProcessBuilder pb=new ProcessBuilder(cmd);
			pb.directory(dir);
			pb.environment().put("CLASSPATH",".");
			myProcess=pb.start();
			UtilServer.processRedirect(myProcess);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
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

	public static boolean waitForBinding(String string,long time){
		long end=System.currentTimeMillis()+time;
		while((time==0) || (System.currentTimeMillis()<=end)){
			try{
				Object server= Naming.lookup(string);
				return server!=null;
			}catch(Exception e){
				Thread.yield();
			}
		}
		return false;
	}

	public static void waitForRegister(String register){
		try{
			while(true)
				try {
					Naming.list(register);
					return;
				} catch (RemoteException e) {
					Thread.yield();
				} 
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static boolean testServer(String server){
		try {
			IJavaScoolServer myServer=(IJavaScoolServer) Naming.lookup(server);
			if(myServer==null) return false;
			myServer.isRunning();
			return true;
		} catch (Exception e) {
			System.out.println("exception "+e);
			return false;
		}
	}

	public static boolean testRegistry(String registry){
		try {
			Naming.list(registry);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public void run() {
		UtilServer.attachJavaSchoolServer();
	}

	public static void dispose() {
		myRules.dispose();
	}

	public static void closeRessources() {
		try {
			//myProcess.getInputStream().close();
			//myProcess.getErrorStream().close();
			myProcess.getOutputStream().close();
		} catch (IOException e) {
		}
	}

}

class MyThread extends Thread{

	public Process exec;

	public MyThread(Process exec){
		this.exec=exec;
	}

}

class Priority{
	private static BufferedReader sysIn=new BufferedReader(new InputStreamReader(System.in),100);
	public synchronized int demandeLecture(Process exec) throws IOException{
		sysIn.mark(1);
		int res=sysIn.read();
		try{
			exec.exitValue();
			sysIn.reset();
			return -1;
		}catch(IllegalThreadStateException e){
			return res;
		}
	}

	public synchronized void demandeEcriture(PrintStream stream,String mess){
		stream.print(mess);
	}

	public void dispose(){
		try {
			sysIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}