package com.projetMaster.baseDeDonnees;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.projetMaster.recherche.Recherche;
import com.projetMaster.recherche.Reponse;
 
/**
 * Classe RechercheBDD - Géres la table TABLE_RECHERCHE de la base de données de l'application.
 *
 */
public class RechercheBDD {
 
	private static final int VERSION_BDD = 1;
	private static final String CVSC_BASE_NAME = "CVSC.db";
	public static final String TABLE_RECHERCHE = "table_Recherche";
	public static final String TABLE_REPONSE = "table_Reponse";
 
	/** table TABLE_RECHERCHE **/
	public static final String COLUMN_ID = "ID";
	public static final int NUM_COLUMN_ID = 0;
	public static final String COLUMN_TYPE = "TYPE";
	public static final int NUM_COLUMN_TYPE = 1;
	public static final String COLUMN_CATEGORIE = "CATEGORIE";
	public static final int NUM_COLUMN_CATEGORIE = 2;
	public static final String COLUMN_POSTE = "POSTE";
	public static final int NUM_COLUMN_POSTE = 3;
	public static final String COLUMN_RESUME = "RESUME";
	public static final int NUM_COLUMN_RESUME = 4;
	public static final String COLUMN_PDF = "PDF";
	public static final int NUM_COLUMN_PDF = 5;
	
	/** table TABLE_REPONSE **/
	public static final String COLUMN_ID_REPONSE = "ID";
	public static final int NUM_COLUMN_ID_REPONSE = 0;
	public static final String COLUMN_ID_DE_LA_RECHERCHE = "IDRECHERCHE";
	public static final int NUM_COLUMN_ID_DE_LA_RECHERCHE = 1;
	public static final String COLUMN_RESUME_REPONSE = "RESUME";
	public static final int NUM_COLUMN_RESUME_REPONSE = 2;
	public static final String COLUMN_PDF_REPONSE = "PDF";
	public static final int NUM_COLUMN_PDF_REPONSE = 3;
	
	private SQLiteDatabase bdd;
 
	private BaseDeDonneeCVSC maBaseSQLite;
 
	public RechercheBDD(Context context){
		//On créer la BDD et sa table
		maBaseSQLite = new BaseDeDonneeCVSC(context, CVSC_BASE_NAME, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertRecherche(Recherche recherche){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COLUMN_TYPE, recherche.getType());
		values.put(COLUMN_CATEGORIE, recherche.getCategories());
		values.put(COLUMN_POSTE, recherche.getPoste());
		values.put(COLUMN_RESUME, recherche.getResume());
		if(recherche.getPdf() == null)
			values.put(COLUMN_PDF, "pas_de_pdf");
		else
			values.put(COLUMN_PDF, recherche.getPdf().getAbsolutePath());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_RECHERCHE, null, values);
	}
	
	public long insertReponse(Reponse reponse){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(COLUMN_ID_DE_LA_RECHERCHE, reponse.getIdRecherche());
		values.put(COLUMN_RESUME_REPONSE, reponse.getResume());
		if(reponse.getPdf() == null)
			values.put(COLUMN_PDF_REPONSE, "pas_de_pdf");
		else
			values.put(COLUMN_PDF_REPONSE, reponse.getPdf().getAbsolutePath());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.insert(TABLE_REPONSE, null, values);
	}
 
	public int updateRecherche(int id, Recherche recherche){
		//La mise à jour d'une recherche dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(COLUMN_TYPE, recherche.getType());
		values.put(COLUMN_CATEGORIE, recherche.getCategories());
		values.put(COLUMN_POSTE, recherche.getPoste());
		values.put(COLUMN_RESUME, recherche.getResume());
		if(recherche.getPdf() == null)
			values.put(COLUMN_PDF, "pas_de_pdf");
		else
			values.put(COLUMN_PDF, recherche.getPdf().getAbsolutePath());
		return bdd.update(TABLE_RECHERCHE, values, COLUMN_ID + " = " +id, null);
	}
	
	public int updateReponse(int id, Reponse reponse){
		//La mise à jour d'une reponse dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(COLUMN_ID_REPONSE, reponse.getId());
		values.put(COLUMN_ID_DE_LA_RECHERCHE, reponse.getIdRecherche());
		values.put(COLUMN_RESUME_REPONSE, reponse.getResume());
		if(reponse.getPdf() == null)
			values.put(COLUMN_PDF_REPONSE, "pas_de_pdf");
		else
			values.put(COLUMN_PDF_REPONSE, reponse.getPdf().getAbsolutePath());
		//on insère l'objet dans la BDD via le ContentValues
		return bdd.update(TABLE_REPONSE, values, COLUMN_ID + " = " +id, null);
	}
 
	public int removeRechercheWithID(int id){
		//Suppression d'une recherche de la BDD grâce à l'ID
		return bdd.delete(TABLE_RECHERCHE, COLUMN_ID + " = " +id, null);
	}
 
	public Recherche getRechercheById(int id){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_RECHERCHE, new String[] {COLUMN_ID, COLUMN_TYPE, COLUMN_CATEGORIE,COLUMN_POSTE,COLUMN_RESUME,COLUMN_PDF}, COLUMN_ID + " LIKE \"" + id +"\"", null, null, null, null);
		return cursorToRecherche(c);
	}
	
	public Reponse getReponseeById(int id){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_REPONSE, new String[] {COLUMN_ID_REPONSE, COLUMN_ID_DE_LA_RECHERCHE, COLUMN_RESUME_REPONSE,COLUMN_PDF_REPONSE}, COLUMN_ID_REPONSE + " LIKE \"" + id +"\"", null, null, null, null);
		return cursorToReponse(c);
	}
	
	public Reponse getReponseeByIdRecherche(int idRecherche){
		Log.v("REchercheBDD", "idRecherche a trouve : "+idRecherche);
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(TABLE_REPONSE, new String[] {COLUMN_ID_REPONSE, COLUMN_ID_DE_LA_RECHERCHE, COLUMN_RESUME_REPONSE,COLUMN_PDF_REPONSE}, COLUMN_ID_DE_LA_RECHERCHE + " LIKE \"" + idRecherche +"\"", null, null, null, null);
		return cursorToReponse(c);
	}
	
	public int getCountRecherche(){
		Cursor dataCount = maBaseSQLite.getReadableDatabase().rawQuery("select * from " + TABLE_RECHERCHE, null);
		return dataCount.getCount();
	}
	
	public int getCountReponse(){
		Cursor dataCount = maBaseSQLite.getReadableDatabase().rawQuery("select * from " + TABLE_REPONSE, null);
		return dataCount.getCount();
	}
 
	//Cette méthode permet de convertir un cursor en une Recherche
	private Recherche cursorToRecherche(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		Recherche recherche = new Recherche();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		recherche.setId(c.getInt(NUM_COLUMN_ID));
		recherche.setType(c.getString(NUM_COLUMN_TYPE));
		recherche.setCategories(c.getString(NUM_COLUMN_CATEGORIE));
		recherche.setPoste(c.getString(NUM_COLUMN_POSTE));
		recherche.setResume(c.getString(NUM_COLUMN_RESUME));
		if(c.getString(NUM_COLUMN_PDF).equals("pas_de_pdf"))
			recherche.setPdf(null);
		else
			recherche.setPdf(new File(c.getString(NUM_COLUMN_PDF)));
		
		
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return recherche;
	}
	
	//Cette méthode permet de convertir un cursor en une Reponse
	private ArrayList<Reponse> cursorToReponseList(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		int i = 1;
		int nb = c.getCount();
		if (c.getCount() == 0)
			return null;
 
		ArrayList<Reponse> lesreponse = new ArrayList<Reponse>();
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		
		while(i<= nb){
		Reponse reponse = new Reponse();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		reponse.setId(c.getInt(NUM_COLUMN_ID_REPONSE));
		reponse.setIdRecherche(c.getInt(NUM_COLUMN_ID_DE_LA_RECHERCHE));
		reponse.setResume(c.getString(NUM_COLUMN_RESUME_REPONSE));
	
		if(c.getString(NUM_COLUMN_PDF_REPONSE).equals("pas_de_pdf"))
			reponse.setPdf(null);
		else
			reponse.setPdf(new File(c.getString(NUM_COLUMN_PDF_REPONSE)));
		
		lesreponse.add(reponse);
		
		c.moveToNext();
		i++;
		}
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return lesreponse;
	}
	
	//Cette méthode permet de convertir un cursor en une Reponse
		private Reponse cursorToReponse(Cursor c){
			//si aucun élément n'a été retourné dans la requête, on renvoie null
			if (c.getCount() == 0)
				return null;
	 
			//Sinon on se place sur le premier élément
			c.moveToFirst();
			//On créé un livre
			Reponse reponse = new Reponse();
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			reponse.setId(c.getInt(NUM_COLUMN_ID_REPONSE));
			reponse.setIdRecherche(c.getInt(NUM_COLUMN_ID_DE_LA_RECHERCHE));
			reponse.setResume(c.getString(NUM_COLUMN_RESUME_REPONSE));
		
			if(c.getString(NUM_COLUMN_PDF_REPONSE).equals("pas_de_pdf"))
				reponse.setPdf(null);
			else
				reponse.setPdf(new File(c.getString(NUM_COLUMN_PDF_REPONSE)));
			
			
			//On ferme le cursor
			c.close();
	 
			//On retourne le livre
			return reponse;
		}
}