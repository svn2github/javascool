package compiler;

/**
 * <p>Titre : JMyFileChooser</p>
 * <p>Description : Classe permettant d'afficher un explorateur que pour des
 * iamges .gif, .jpg, .png</p>
 * <p>Copyright : Copyright (c) 2003</p>
 * <p>Société : Perso</p>
 * @author Indiana_jules
 * @version 1.0
 */

//Importation des packages nécessaires
import java.io.File;

import javax.swing.JFileChooser;

public class JMyFileChooser extends JFileChooser{
  /**Constructeur par défaut*/
  public JMyFileChooser() {
	 
    super(new File(System.getProperty("user.dir" )+File.separator+"work"));

    //Initialisation
    this.setFileSelectionMode(JFileChooser.FILES_ONLY);
    this.setDialogType(JFileChooser.OPEN_DIALOG);
   
    this.setMultiSelectionEnabled(false);
   


    //Définition des extensions
    JFileFilter filtre = new JFileFilter();
    filtre.addType("txt");
    filtre.addType("java");
    filtre.setDescription("Fichiers Java: .txt, .java");

    //On va faire un preview
    this.addChoosableFileFilter(filtre);
    this.setAccessory(new FilePreviewer(this));
    
  }
}
