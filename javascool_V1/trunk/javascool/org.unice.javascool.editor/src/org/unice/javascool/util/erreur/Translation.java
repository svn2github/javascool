package org.unice.javascool.util.erreur;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.unice.javascool.conf.BeanErrors;
import org.unice.javascool.conf.BeanFactory;
import org.unice.javascool.conf.ConfException;

public class Translation {
	
	private  HashMap<Integer,String> commentaire = new HashMap<Integer, String>();
	private  HashMap<String, String>tableDeTraduction = new HashMap<String, String>();
	private String file=initHome();
	
	
	public Translation(){
		
		try {
			ArrayList<BeanErrors> errors = BeanErrors.getBeanErrors(file);
			//ajout des commentaires en fr
			addCommentaire(0, "Erreur");
			addCommentaire(1, "Les erreurs sont liees aux appels des methodes suivantes");
			addCommentaire(2, "Fonction");
			addCommentaire(3, "ligne");
			
			for (BeanErrors error : errors){
				String nomError = error.getJava();
				String traduction = error.getTraduction();
				tableDeTraduction.put(nomError,traduction);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfException e) {
			e.printStackTrace();
		} 
		
		
	}

	
	private String initHome() {
		String dirHome=System.getProperty("user.home");
		String system=System.getProperty("os.name").toLowerCase();
		if(system.contains("windows")){
			dirHome+=File.separator+"JavasCoolConf"+File.separator+"erreurs_conf.bml";
		}else{
			dirHome+=File.separator+".JavasCool"+File.separator+"erreurs_conf.bml";
		}
		return dirHome;
	}


	public HashMap<Integer, String> getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(HashMap<Integer, String> commentaire) {
		this.commentaire = commentaire;
	}
	
	public  void addCommentaire(int numero, String phrase){
		commentaire.put(numero,phrase);
	}
	
	public  String getCommentaire(int numero){
		return commentaire.get(numero);
	}
	
	public  HashMap<String, String> getTableDeTraduction() {
		return tableDeTraduction;
	}
	
	public  void setTableDeTraduction(HashMap<String, String> tableDeTraduction) {
		this.tableDeTraduction = tableDeTraduction;
	}

	public  void addTraduction(String nom , String traduction){
		tableDeTraduction.put(nom,traduction);
	}
	
	public  String getTraduction(String nom){
		return tableDeTraduction.get(nom);
	}

}
