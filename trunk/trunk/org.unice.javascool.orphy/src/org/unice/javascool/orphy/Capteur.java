package org.unice.javascool.orphy;

public class Capteur {
	private String type;
	private String unite;
	private String abreviation;
	
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
