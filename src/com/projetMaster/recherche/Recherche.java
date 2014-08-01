package com.projetMaster.recherche;

import java.io.File;


/**
 * Classe Recherche : contient les définitions et méthodes d'une recherche quel soit de type Demande ou Offre.
 *
 */
public class Recherche {
	
	private int id;
	private String type;
	private String categories;
	private String poste;
	private String resume;
	private File pdf;
	
	/** 
	 * Constructeur par défault.
	 */
	public Recherche(){
		
	}
	
	/**
	 * Constructeur de la classe Recherche.
	 * @param type le type de la recherche (Demande ou Offre).
	 * @param categories la categories de la recherche.
	 * @param poste le poste de la recherches.
	 * @param pdf l'offre ou le CV de la recherche.
	 */
	public Recherche(String type, String categories, String poste,String resume, File pdf) {
		super();
		this.type = type;
		this.categories = categories;
		this.poste = poste;
		this.pdf = pdf;
		if(resume.equals("Automatique"))
			this.resume = this.creationResume(type, categories, poste);
		else
			this.resume = resume;
	}
	
	/**
	 * Méthode pour créer automatiquement le résumé.
	 * @param type le type de la recherche (Demande ou Offre).
	 * @param categories la categories de la recherche.
	 * @param poste le poste de la recherches
	 * @return le résumé créé
	 */
	public String creationResume(String type, String categories, String poste){
		return Constante.DEBUT_RESUME+categories+" au poste "+poste+" ;";
	}
	
	
	/** les Getters et Setters **/
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getPoste() {
		return poste;
	}
	public void setPoste(String poste) {
		this.poste = poste;
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
	
	/**
	 * La méthode toString qui forunit une description de l'objet Recherche.
	 */
	@Override
	public String toString() {
		if(this.pdf == null)
			return "Recherche [categories=" + categories + ", poste=" + poste + ", resume=" + resume + ", pdf = pas de pdf]";
		else
			return "Recherche [categories=" + categories + ", poste=" + poste+ ", resume=" + resume + ", pdf= "+pdf.getName()+"]";
		}
	

}
