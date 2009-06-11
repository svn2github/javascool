package org.javascool.conf;

import org.w3c.dom.Document;

public abstract class BeanTools extends AbstractBean{

	private String nom=null;
	private String signature=null;
	
	public void save(Document root) {
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}

