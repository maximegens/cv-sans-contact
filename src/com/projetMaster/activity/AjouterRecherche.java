package com.projetMaster.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.projetMaster.baseDeDonnees.RechercheBDD;
import com.projetMaster.bluetooth.ConstantesBluetooth;
import com.projetMaster.recherche.Constante;
import com.projetMaster.recherche.Recherche;

/**
 * Classe AjouterRecherche - Activity qui permet d'ajouter une nouvelle recherche dans la base de donnée.
 *
 */
public class AjouterRecherche extends Activity {
	
	RadioGroup radioChoixRecherche;
	RadioButton radioRechercheOffre;
	RadioButton radioRechercheDemande;
	RadioGroup radioChoixResume;
	RadioButton radioResumeManuel;
	RadioButton radioResumeAutomatique;
	
	Spinner spinnerCategorie;
	Spinner spinnerPoste;
	
	Button validerRecherche;
	EditText editTextResume;
	
	ArrayAdapter<String> adapterSpinnerCategorie;
	ArrayAdapter<String> adapterSpinnerPoste;
	
	ArrayList<String> itemsCategories = new ArrayList<String>();
	ArrayList<String> itemsPostes = new ArrayList<String>();
	ArrayList<String> itemsPosteInformatique = new ArrayList<String>();
	ArrayList<String> itemsPosteRestauration = new ArrayList<String>();
	ArrayList<String> itemsPosteAgriculture = new ArrayList<String>();
	ArrayList<String> itemsPosteHotellerie = new ArrayList<String>();
	
	final RechercheBDD rechercheBdd = new RechercheBDD(this);
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ajout_recherche);

		radioChoixRecherche = (RadioGroup) findViewById(R.id.radioTypeRecherche);
		radioRechercheOffre = (RadioButton)findViewById(R.id.radioOffre);
		radioRechercheDemande = (RadioButton)findViewById(R.id.radioDemande);
		radioChoixResume = (RadioGroup) findViewById(R.id.radioResume);
		radioResumeManuel = (RadioButton)findViewById(R.id.radioManuel);
		radioResumeAutomatique = (RadioButton)findViewById(R.id.radioAutomatique);
		spinnerCategorie = (Spinner)findViewById(R.id.spinnerCategorie);
		spinnerPoste = (Spinner)findViewById(R.id.spinnerPoste);
		editTextResume = (EditText)findViewById(R.id.editTextResume);
		validerRecherche = (Button)findViewById(R.id.validerRecherche);
		
		/** initialisation du nom des bouton radio **/
		radioRechercheOffre.setText(Constante.OFFRE);
		radioRechercheDemande.setText(Constante.DEMANDE);
		radioResumeManuel.setText(Constante.RESUME_MANUEL);
		radioResumeAutomatique.setText(Constante.RESUME_AUTOMATIQUE);
		
		/** remplissage des listes **/
		remplirSpinner(itemsCategories,itemsPostes,itemsPosteInformatique,itemsPosteRestauration,itemsPosteAgriculture,itemsPosteHotellerie);
		
		/** Initialisation des spinners **/
		adapterSpinnerCategorie =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsCategories);
		adapterSpinnerCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategorie.setAdapter(adapterSpinnerCategorie);
		adapterSpinnerPoste =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		adapterSpinnerPoste.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPoste.setAdapter(adapterSpinnerPoste);
 
		/** remplissage du spinner adapterSpinnerPosteModif en fonction du choix du spinner spinnerCategorieModif **/
        spinnerCategorie.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
				adapterSpinnerPoste.clear();
				if(position == 0){
					for(String poste : itemsPosteInformatique)
						adapterSpinnerPoste.add(poste);
					adapterSpinnerPoste.notifyDataSetChanged();
				}
				if(position == 1){
					for(String poste : itemsPosteRestauration)
						adapterSpinnerPoste.add(poste);
					adapterSpinnerPoste.notifyDataSetChanged();
				}
				if(position == 2){
					for(String poste : itemsPosteAgriculture)
						adapterSpinnerPoste.add(poste);
					adapterSpinnerPoste.notifyDataSetChanged();
				}
				if(position == 3){
					for(String poste : itemsPosteHotellerie)
						adapterSpinnerPoste.add(poste);
					adapterSpinnerPoste.notifyDataSetChanged();
				}
					
			}

			public void onNothingSelected(AdapterView<?> arg0) {				
			
			}
		});
         
        /** modification lors du changement de choix pour le résumé **/
        radioChoixResume.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {	
				
					RadioButton rEnCours=(RadioButton)findViewById(checkedId);
		    		if (rEnCours.getId()==R.id.radioManuel){
		    			editTextResume.setVisibility(0);
		    			editTextResume.setEnabled(true);
		    		}
		    		if (rEnCours.getId()==R.id.radioAutomatique){
		    			editTextResume.setText(null);
		    			editTextResume.setEnabled(false);
		    		}
			}
		});
        
		
        /** Bouton validez **/
		validerRecherche.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				final RadioButton choixRechercheChecked = (RadioButton)findViewById(radioChoixRecherche.getCheckedRadioButtonId());
				final RadioButton choixResumeChecked = (RadioButton)findViewById(radioChoixResume.getCheckedRadioButtonId());
				String type = choixRechercheChecked.getText().toString();
				String categorie = spinnerCategorie.getSelectedItem().toString();
				String poste = spinnerPoste.getSelectedItem().toString();
				
				File pdf = null;
				/** verification si il s'agit d'une demande ou d'une offre pour la recherche du cv **/
				if(choixRechercheChecked.getText().toString().equals(Constante.OFFRE)){
					pdf = ConstantesBluetooth.getFileByNameInExternalStorageDirectory(Constante.NOM_PDF_OFFRE);
					
				}else if(choixRechercheChecked.getText().toString().equals(Constante.DEMANDE)){
					pdf = ConstantesBluetooth.getFileByNameInExternalStorageDirectory(Constante.NOM_PDF_CV);
				}else
					Toast.makeText(getApplicationContext(), "Veuillez indiquer si il s'agit d'une demande ou d'une offre", Toast.LENGTH_LONG).show();
				
				/** verification si il faut construire le résumé automatique ou non **/
				String resume;
				if(choixResumeChecked.getText().toString().equals(Constante.RESUME_MANUEL)){
					resume = editTextResume.getText().toString();
				}else
					resume = choixResumeChecked.getText().toString();
				
				/** creation de l'objet recherche **/
				Recherche recherche = new Recherche(type, categorie, poste,resume,pdf);
				
				/** insertion dans la base de donnees **/
				rechercheBdd.open();
				rechercheBdd.insertRecherche(recherche);
				Toast.makeText(getApplicationContext(), "Recherche correctement ajoutée", Toast.LENGTH_SHORT).show();
				rechercheBdd.close();
				
				
			}
		});
		
	}
	
	/**
	 * rempli les listes pour les spinners.
	 * 
	 **/
	public void remplirSpinner(ArrayList<String> itemsCategories,ArrayList<String> itemsPostes,
			ArrayList<String> itemsPosteInformatique,
			ArrayList<String> itemsPosteRestauration,ArrayList<String> itemsPosteAgriculture,
			ArrayList<String> itemsPosteHotellerie){
		 
		 	itemsCategories.add("Informatique");
		 	itemsCategories.add("Restauration");
		 	itemsCategories.add("Agriculture");
		 	itemsCategories.add("Hotellerie");
		 	itemsPosteInformatique.add("Chef de projet");
		 	itemsPosteInformatique.add("Développeur Web");
		 	itemsPosteInformatique.add("Analyse");
		 	itemsPosteInformatique.add("Développeur Java");
		 	itemsPosteRestauration.add("Serveur");
		 	itemsPosteRestauration.add("Cuisinier");
		 	itemsPosteRestauration.add("Comis");
		 	itemsPosteAgriculture.add("Fermier");
		 	itemsPosteAgriculture.add("Conducteur de tracteur");
		 	itemsPosteAgriculture.add("Fournisseur");
		 	itemsPosteHotellerie.add("Receptionniste");
		 	itemsPosteHotellerie.add("Portier");
		 	itemsPosteHotellerie.add("Femme de chambre");
		 	itemsPosteHotellerie.add("Gerant");
	}
	
	/** Gestion du menu **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_ajouter_recherche, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.menu_voir_mes_recherche:
        	  Intent intent = new Intent(AjouterRecherche.this, VoirMesRecherches.class);
        	  startActivity(intent);
        	  return true;
 
       }
       return false;}
	
}
