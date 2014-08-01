package com.projetMaster.baseDeDonnees;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe BaseDeDonneeCVSC - Géres la base de donnée de l'application.
 *
 */
public class BaseDeDonneeCVSC extends SQLiteOpenHelper {

		
		// Nom des tables
		public static final String TABLE_RECHERCHE = "table_Recherche";
		public static final String TABLE_REPONSE = "table_Reponse";

		// Description des colonnes de la table TABLE_RECHERCHE
		public static final String COLUMN_ID = "ID";
		public static final String COLUMN_TYPE = "TYPE";
		public static final String COLUMN_CATEGORIE = "CATEGORIE";
		public static final String COLUMN_POSTE = "POSTE";
		public static final String COLUMN_RESUME = "RESUME";
		public static final String COLUMN_PDF = "PDF";

		// Description des colonnes TABLE_REPONSE
		public static final String COLUMN_ID_REPONSE = "ID";
		public static final String COLUMN_ID_DE_LA_RECHERCHE = "IDRECHERCHE";
		public static final String COLUMN_RESUME_REPONSE = "RESUME";
		public static final String COLUMN_PDF_REPONSE = "PDF";

		

		private static final String CREATE_TABLE_RECHERCHE = "CREATE TABLE "
				+ TABLE_RECHERCHE + " (" 
				+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ COLUMN_TYPE + " TEXT NOT NULL, " 
				+ COLUMN_CATEGORIE + " TEXT NOT NULL, "
				+ COLUMN_POSTE + " TEXT NOT NULL, "
				+ COLUMN_RESUME +" TEXT NOT NULL, "
				+ COLUMN_PDF +" TEXT NOT NULL"
				+");";
		
		private static final String CREATE_TABLE_REPONSE = "CREATE TABLE "
				+ TABLE_REPONSE + " (" 
				+ COLUMN_ID_REPONSE + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
				+ COLUMN_ID_DE_LA_RECHERCHE + " TEXT NOT NULL, " 
				+ COLUMN_RESUME_REPONSE + " TEXT NOT NULL, "
				+ COLUMN_PDF_REPONSE + " TEXT NOT NULL "
				+");";

		public BaseDeDonneeCVSC(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
	 
		@Override
		public void onCreate(SQLiteDatabase db) {
			//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
			db.execSQL(CREATE_TABLE_RECHERCHE);
			db.execSQL(CREATE_TABLE_REPONSE);
		}
	 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
			//comme ça lorsque je change la version les id repartent de 0
			db.execSQL("DROP TABLE " + TABLE_RECHERCHE + ";");
			db.execSQL("DROP TABLE " + TABLE_REPONSE + ";");
			onCreate(db);
		}

}
