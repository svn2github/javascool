/********************************************************************************
      ______________________________________________
     | By Philippe Vienne <philoumailabo@gmail.com> |
     | Distrubuted on GNU General Public Licence    |
     | Revision 558 du SVN                          |
     | © 2010 INRIA, All rights reserved            |
     |______________________________________________|

 ********************************************************************************/

package org.javascool;

// Used to define the gui
import javax.swing.JApplet;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.io.File;

// Used to register elements
import java.util.HashMap;

public class FileManger extends Main {

	private String file=new String("");
	private String filepath=new String("");
	private File tmpfile = null;
	private JFileChooser fc = new JFileChooser();
	
	public void FileManager(){}
	
	public String getFile(){return this.file;}
	public void setFile(String textoffile){this.file=textoffile;}
	
	public String getFilepath(){return this.filepath;}
	public void setFilepath(String path){this.filepath=path;}
	
	public void openFile(){
		fc.setDialogTitle("Ouvrir un programme");
    	fc.setDialogType(JFileChooser.OPEN_DIALOG);
    	fc.setApproveButtonText("Ouvrir");
    	if (fc.showOpenDialog(Main.this) == 0) {
    		tmpfile=fc.getSelectedFile();
    		file=Utils.loadString(file.getPath());
    		filepath=file.getPath();
    		tmpfile=null;
    	}
	}
	
	public void saveFile(){
	    if(file == null) {
    		fc.setDialogTitle(fcTitle == null ? "Enregister un programme" : fcTitle);
    		fc.setDialogType(JFileChooser.SAVE_DIALOG);
    		fc.setApproveButtonText("Enregister");
    		if (fc.showSaveDialog(Main.this) == 0) {
				tmpfile = fc.getSelectedFile();
				Utils.saveString(file.getPath(), se.getText());
    		} 
    	} else {
      		Utils.saveString(file.getPath(), se.getText());
    	}
    }
}
