package org.unice.javascool.orphy;

public class Capteur {
	private String type;
	private String unite;
	private String abreviation;
	
	
	/**
	 * Constructeur
	 * 
	 * @param type le type de du capteur
	 * @param unite l'unité de la mesure faite avec le capteur
	 * @param abreviation l'abreviation du capteur
	 */
	public Capteur(String type, String unite, String abreviation){
		this.type = type;
		this.unite = unite;
		this.abreviation = abreviation;
	}

	public String getType() {
		return type;
	}

	public String getUnite() {
		return unite;
	}

	public String getAbreviation() {
		return abreviation;
	}

}
