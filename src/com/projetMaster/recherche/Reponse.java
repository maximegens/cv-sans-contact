package com.projetMaster.recherche;

import java.io.File;

/**
 * Classe Reponse - contient les caractéristiques d'une réponse reçu par Bluetooth ou WIFI.
 *
 */
public class Reponse {
	private int id;
	private int idRecherche;
	private String resume;
	private File pdf;

	public Reponse(int id) {
		this.id = id;
	}
	
	public Reponse() {
		
	}
	public Reponse(int idRecherche,String resume,File fichier) {
		
		this.idRecherche =idRecherche;
		this.resume = resume;
		this.pdf = fichier;
	}
	
	public int getIdRecherche() {
		return idRecherche;
	}

	public void setIdRecherche(int idRecherche) {
		this.idRecherche = idRecherche;
	}
	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public File getPdf() {
		return pdf;
	}

	public void setPdf(File pdf) {
		this.pdf = pdf;
	}


	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
