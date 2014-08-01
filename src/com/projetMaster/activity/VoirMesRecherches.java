package com.projetMaster.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.projetMaster.adapter.RechercheAdapter;
import com.projetMaster.baseDeDonnees.RechercheBDD;
import com.projetMaster.recherche.Constante;
import com.projetMaster.recherche.Recherche;
import com.projetMaster.recherche.Reponse;

/**
 * Classe VoirMesRecherches - Activity qui permet de visualiser la liste des recherches en cours dans la base de données.
 *
 */
public class VoirMesRecherches extends Activity {
	
	TextView nbRecherche;
	Spinner spinnerChoixType;
	ListView listeViewDesRecherches;
	
	String[] listeType = {Constante.DEMANDE,Constante.OFFRE};
	List<Recherche> listeRecherches = new ArrayList<Recherche>();
	
	ArrayAdapter<String> adapterSpinnerType;
	ArrayAdapter<String> adapterListViewRecherche;
	
	RechercheBDD rechercheBdd = new RechercheBDD(this);
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recherches);

		nbRecherche = (TextView)findViewById(R.id.nbRecherche);
		spinnerChoixType = (Spinner)findViewById(R.id.spinnerChoixRecherche);
		listeViewDesRecherches = (ListView)findViewById(R.id.listViewRecherche);
		
		/** initialisation du spinner **/
		adapterSpinnerType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listeType);
		adapterSpinnerType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerChoixType.setAdapter(adapterSpinnerType);
		
		remplirListBdd();
		
		/** initialisation de la listeview **/
		RechercheAdapter adapterListViewRecherche = new RechercheAdapter(this, listeRecherches);
		listeViewDesRecherches.setAdapter(adapterListViewRecherche);
		nbRecherche.setText("Vous avez "+adapterListViewRecherche.getCount()+" recherches");
		
		/** lorsqu'on maintient le clic appuyé **/
		listeViewDesRecherches.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				Intent intentModif = new Intent(VoirMesRecherches.this, ModifierRecherche.class);
				intentModif.putExtra("id", position+1);/*car la base de donnees commence a 1 et pas 0 */
				startActivity(intentModif);
				return true;
			}
		});
		
		/** lorsqu'on clic **/
		listeViewDesRecherches.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long id) {
				Intent intent = new Intent(VoirMesRecherches.this, VoirMesReponses.class);
				intent.putExtra("idRecherche", position);
				startActivity(intent);
			}
		});
		
	}
	
	
	/** Gestion du menu **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_voir_mes_recherche, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.menu_ajouter_recherche:
        	  Intent intent = new Intent(VoirMesRecherches.this, AjouterRecherche.class);
        	  startActivity(intent);
        	  return true;
 
       }
       return false;}
	
	
	/**
	 * Remplissage de la List avec toutes les recherches contenu dans la base de données.
	 */
	private void remplirListBdd(){
		
		int i;
		listeRecherches.clear();
		rechercheBdd.open();
		int countRecherche = rechercheBdd.getCountRecherche();
		int countReponse = rechercheBdd.getCountReponse();
		for(i = 1; i <= countRecherche; i++){
			Recherche r = rechercheBdd.getRechercheById(i);
			this.listeRecherches.add(r);
		}
		rechercheBdd.close();
	}
}
