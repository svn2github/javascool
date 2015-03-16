package org.javascool;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.UIManager;

public class Launcher {

	private static String jarLocation="http://javascool.gforge.inria.fr/javascool-proglets.jar";

	private JFrame mainFrame=new JFrame("Java's Cool Launcher");
	private JRootPane rootPane=mainFrame.getRootPane();
	private JProgressBar progressBar=new JProgressBar();
	private JLabel status=new JLabel();
	private JLabel description=new JLabel();
	private JButton cancelButton=new JButton(new AbstractAction() {
		private static final long serialVersionUID = 2538808580079356993L;

		@Override
		public void actionPerformed(ActionEvent e) {
			mainFrame.dispose();
			System.exit(-1);
		}
	});

	private Launcher(){
		cancelButton.setText("Annuler");
		description.setText("Téléchargement et lancement de Java's Cool");
		progressBar.setStringPainted(true);
		status.setText("Initialisation...");
		rootPane.setLayout(new BorderLayout(10,10));
		rootPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rootPane.add(progressBar,BorderLayout.CENTER);
		rootPane.add(status, BorderLayout.SOUTH);
		rootPane.add(description, BorderLayout.NORTH);
		rootPane.add(cancelButton, BorderLayout.EAST);
		this.mainFrame.setSize(400, 200);
		this.mainFrame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-mainFrame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-mainFrame.getHeight()/2);
		this.mainFrame.setAlwaysOnTop(true);
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setVisible(true);
		setProgress("Prepare Launching", 0);
		if(!isJavascoolCoreInstalled()){
			downloadPackage(jarLocation,10,95);
		}
		launchJVS();
		this.mainFrame.setVisible(false);
		System.exit(0);
	}

	private void launchJVS() {
		String classPath=".";
		for(File f:new File(getJavaRunPath()).listFiles()){
			if(f.getName().endsWith(".jar"))
				classPath=classPath+File.pathSeparator+f.getAbsolutePath();
		}
		if(OsUtils.isMac()){
			if(!new File(getJavaRunPath()+"Javascool.png").exists()){
				try {
					InputStream is=Launcher.class.getResourceAsStream("Javascool.png");
					FileOutputStream fos=new FileOutputStream(new File(getJavaRunPath()+"Javascool.png"));
					java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
					byte data[] = new byte[1024];
					int count;
					while((count = is.read(data,0,1024))!=-1)
						bout.write(data,0,count);
				} catch (Exception e) {
				}
			}
		}
		try {
			if(OsUtils.isWindows())
				Runtime.getRuntime().exec(System.getProperty("java.home")+File.separator+"bin"+File.separator+"java -cp '"+classPath+"' org.javascool.Core");
			else if(OsUtils.isUnix()){
				String[] cmd = {"/bin/sh", "-c", System.getProperty("java.home")+File.separator+"bin"+File.separator+"java "+(OsUtils.isMac()?"-Xdock:name=\"Java's Cool 4\" -Xdock:icon=\""+new File(getJavaRunPath()+"Javascool.png").getAbsolutePath()+"\"":"")+" -cp '"+classPath+"' org.javascool.Core"};
				Runtime.getRuntime().exec(cmd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isJavascoolCoreInstalled(){
		return new File(getJavaRunPath()+"javascool-proglets.jar").exists();
	}

	private void setProgress(String status,int progress){
		if(!status.equals(""))
			this.status.setText(status);
		if(progress!=0){
			progressBar.setIndeterminate(false);
			this.progressBar.setValue(progress);
		}else
			progressBar.setIndeterminate(true);
		this.progressBar.repaint();
	}

	private void downloadPackage(String jarUrl,final int progressStart,final int progressEnd){
		try {
			String[] jarPath=new java.net.URL(jarUrl).getPath().split("/");
			String jarName=(jarPath[jarPath.length-1]);
			String status="Téléchargement de "+jarName;
			setProgress(status, 0);
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(new 
					java.net.URL(jarUrl).openStream());
			float contentLen=-1;
			try {
				contentLen=(new java.net.URL(jarUrl))
						.openConnection().getContentLength();
			} catch (Exception e) {}
			java.io.FileOutputStream fos = new 
					java.io.FileOutputStream(this.getJavaRunPath()+jarName);
			java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
			byte data[] = new byte[12000];
			int count; float total=0;
			while((count = in.read(data,0,12000))!=-1)
			{
				bout.write(data,0,count);
				total=total+count;
				if(contentLen!=-1)
					setProgress(status, (int)(((total/contentLen)*(progressEnd-progressStart)+progressStart)));
			}
			bout.close();
			fos.close();
			in.close();
			setProgress(status, progressEnd);
		}catch(Exception e){} 
	}

	private String getApplicationFolder() {
		String applicationName="javascool";
		String location="";
		if (OsUtils.isWindows()) {
			location=System.getenv("APPDATA") + "\\" + applicationName + "\\";
		} else if (OsUtils.isMac()) {
			location=System.getProperty("user.home") + "/Library/Application Support/" + applicationName + "/";
		} else {
			location=System.getProperty("user.home") + "/." + applicationName + "/";
		}
		new File(location).mkdirs();
		return location;
	}

	private String getJavaRunPath(){
		new File(getApplicationFolder()+"Java"+File.separator).mkdirs();
		return getApplicationFolder()+"Java"+File.separator;
	}

	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){}
		new Launcher();
	}

	public static final class OsUtils
	{
		private static String OS = null;
		public static String getOsName()
		{
			if(OS == null) { OS = System.getProperty("os.name"); }
			return OS;
		}
		public static boolean isWindows()
		{
			return getOsName().startsWith("Windows");
		}

		public static boolean isUnix(){
			return !isWindows();
		}
		
		public static boolean isMac(){
			return OS.toUpperCase().contains("MAC");
		}
	}

}
