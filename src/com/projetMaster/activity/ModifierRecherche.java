package com.projetMaster.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
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
 * Classe ModifierRecherche - Activity qui permet de modifier une recherche existante et de la mettre à jour dans la base de donnée.
 * @author christine
 *
 */
public class ModifierRecherche extends Activity {
	
	RadioGroup radioChoixRechercheModif;
	RadioButton radioRechercheOffreModif;
	RadioButton radioRechercheDemandeModif;
	RadioGroup radioChoixResumeModif;
	RadioButton radioResumeManuelModif;
	RadioButton radioResumeAutomatiqueModif;
	
	Button voirPDF;
	
	Spinner spinnerCategorieModif;
	Spinner spinnerPosteModif;
	
	Button validerModif;
	Button suppression;
	EditText editTextResumeModif;
	
	ArrayAdapter<String> adapterSpinnerCategorieModif;
	ArrayAdapter<String> adapterSpinnerPosteModif;
	
	ArrayList<String> itemsCategories = new ArrayList<String>();
	ArrayList<String> itemsPostes = new ArrayList<String>();
	ArrayList<String> itemsPosteInformatique = new ArrayList<String>();
	ArrayList<String> itemsPosteRestauration = new ArrayList<String>();
	ArrayList<String> itemsPosteAgriculture = new ArrayList<String>();
	ArrayList<String> itemsPosteHotellerie = new ArrayList<String>();
	
	final RechercheBDD rechercheBdd = new RechercheBDD(this);
	Recherche r;
	int idRecherche;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifier_recherche);
		
		idRecherche = (Integer)getIntent().getExtras().get("id");
		
		radioChoixRechercheModif = (RadioGroup) findViewById(R.id.radioTypeRechercheModif);
		radioRechercheOffreModif = (RadioButton)findViewById(R.id.radioOffreModif);
		radioRechercheDemandeModif = (RadioButton)findViewById(R.id.radioDemandeModif);
		radioChoixResumeModif = (RadioGroup) findViewById(R.id.radioResumeModif);
		radioResumeManuelModif = (RadioButton)findViewById(R.id.radioManuelModif);
		radioResumeAutomatiqueModif = (RadioButton)findViewById(R.id.radioAutomatiqueModif);
		spinnerCategorieModif = (Spinner)findViewById(R.id.spinnerCategorieModif);
		spinnerPosteModif = (Spinner)findViewById(R.id.spinnerPosteModif);
		editTextResumeModif = (EditText)findViewById(R.id.editTextResumeModif);
		validerModif = (Button)findViewById(R.id.validerRechercheModif);
		suppression = (Button)findViewById(R.id.suppressionModif);
		voirPDF = (Button)findViewById(R.id.modifVoirPdfModif);
		
		/** initialisation du nom des bouton radio **/
		radioRechercheOffreModif.setText(Constante.OFFRE);
		radioRechercheDemandeModif.setText(Constante.DEMANDE);
		radioResumeManuelModif.setText(Constante.RESUME_MANUEL);
		radioResumeAutomatiqueModif.setText(Constante.RESUME_AUTOMATIQUE);
		
		/** remplissage des listes **/
		remplirSpinner(itemsCategories,itemsPostes,itemsPosteInformatique,itemsPosteRestauration,itemsPosteAgriculture,itemsPosteHotellerie);
		
		
		/** Initialisation des spinners **/
		adapterSpinnerCategorieModif = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,itemsCategories);
		adapterSpinnerCategorieModif.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCategorieModif.setAdapter(adapterSpinnerCategorieModif);
		adapterSpinnerPosteModif = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item);
		adapterSpinnerPosteModif.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPosteModif.setAdapter(adapterSpinnerPosteModif);
 
		
		/** remplissage des champs depuis la base de donné **/
		rechercheBdd.open();
		
		r = rechercheBdd.getRechercheById(idRecherche);
		//Toast.makeText(getApplicationContext(), r.toString(),Toast.LENGTH_SHORT ).show();
		
		if(r.getType().equals(Constante.OFFRE)){
			radioRechercheOffreModif.setChecked(true);
			radioRechercheDemandeModif.setChecked(false);
		}else if(r.getType().equals(Constante.DEMANDE)){
			radioRechercheOffreModif.setChecked(false);
			radioRechercheDemandeModif.setChecked(true);
		}else{
			/* offre par defaut */
			radioRechercheOffreModif.setChecked(true);
			radioRechercheDemandeModif.setChecked(false);
		}
		
		spinnerCategorieModif.setSelection(itemsCategories.indexOf(r.getCategories()));
			
		if(itemsPosteInformatique.contains(r.getPoste())){
			Log.v("itemsPosteInformatique", r.getPoste());
			Log.v("itemsPosteInformatique", ""+itemsPosteAgriculture.indexOf(r.getPoste()));
			spinnerPosteModif.setSelection(itemsPostes.indexOf(r.getPoste()));
		}else if(itemsPosteRestauration.contains(r.getPoste())){
			Log.v("itemsPosteRestauration", r.getPoste());
			Log.v("itemsPosteRestauration", ""+itemsPosteRestauration.indexOf(r.getPoste()));
			spinnerPosteModif.setSelection(itemsPostes.indexOf(r.getPoste()));
		}else if(itemsPosteAgriculture.contains(r.getPoste())){
			Log.v("itemsPosteAgriculture", r.getPoste());
			Log.v("itemPosteAgriculture", ""+itemsPosteAgriculture.indexOf(r.getPoste()));
			spinnerPosteModif.setSelection(itemsPosteAgriculture.indexOf(r.getPoste()));
		}else if(itemsPosteHotellerie.contains(r.getPoste())){
			Log.v("itemsPosteHotellerie", r.getPoste());
			Log.v("itemsPosteHotellerie", ""+itemsPosteHotellerie.indexOf(r.getPoste()));
			spinnerPosteModif.setSelection(itemsPostes.indexOf(r.getPoste()));
		}else
			Toast.makeText(getApplicationContext(), "Erreur lors de la récupération du postye", Toast.LENGTH_SHORT).show();
			
		final File fichier = rechercheBdd.getRechercheById(idRecherche).getPdf();
		
		/** bouton voir PDF **/
		voirPDF.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(fichier != null)
					VisualiserFichier(fichier);
				else
					Toast.makeText(getApplicationContext(), "Il n'y a pas de PDF pour cette recherche", Toast.LENGTH_SHORT).show();
			}
		});
		
		if(r.getResume().startsWith(Constante.DEBUT_RESUME)){
			radioResumeAutomatiqueModif.setChecked(true);
			radioResumeManuelModif.setChecked(false);
		}else{
			editTextResumeModif.setVisibility(0);
			radioResumeAutomatiqueModif.setChecked(false);
			radioResumeManuelModif.setChecked(true);
			editTextResumeModif.setText(r.getResume());
		}
			
		rechercheBdd.close();
		
		
		
		/** remplissage du spinner adapterSpinnerPosteModif en fonction du choix du spinner spinnerCategorieModif **/
        spinnerCategorieModif.setOnItemSelectedListener(new OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long id) {
				adapterSpinnerPosteModif.clear();
				if(position == 0){
					for(String poste : itemsPosteInformatique)
						adapterSpinnerPosteModif.add(poste);
					adapterSpinnerPosteModif.notifyDataSetChanged();
				}
				if(position == 1){
					for(String poste : itemsPosteRestauration)
						adapterSpinnerPosteModif.add(poste);
					adapterSpinnerPosteModif.notifyDataSetChanged();
				}
				if(position == 2){
					for(String poste : itemsPosteAgriculture)
						adapterSpinnerPosteModif.add(poste);
					adapterSpinnerPosteModif.notifyDataSetChanged();
				}
				if(position == 3){
					for(String poste : itemsPosteHotellerie)
						adapterSpinnerPosteModif.add(poste);
					adapterSpinnerPosteModif.notifyDataSetChanged();
				}
					
			}

			public void onNothingSelected(AdapterView<?> arg0) {				
			
			}
		});
         
        /** modification lors du changement de choix pour le résumé **/
        radioChoixResumeModif.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {	
				
					RadioButton rEnCours=(RadioButton)findViewById(checkedId);
		    		if (rEnCours.getId()==R.id.radioManuelModif){
		    			editTextResumeModif.setVisibility(0);
		    			editTextResumeModif.setEnabled(true);
		    			editTextResumeModif.setText(r.getResume());
		    		}
		    		if (rEnCours.getId()==R.id.radioAutomatiqueModif){
		    			editTextResumeModif.setText(null);
		    			editTextResumeModif.setEnabled(false);
		    		}
			}
		});
        
        /** bouton suppression **/
        suppression.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				rechercheBdd.open();
				Toast.makeText(getApplicationContext(), "Fonction désactivé car provoque une corruption de la base de donnée", Toast.LENGTH_SHORT).show();
//				if(rechercheBdd.removeRechercheWithID(idRecherche) == 1){
//					Toast.makeText(getApplicationContext(), "Recherche correctement supprimée", Toast.LENGTH_SHORT).show();
//					/** retour a la liste des recherches **/
//					Intent redirection = new Intent(ModifierRecherche.this, VoirMesRecherches.class);
//					startActivity(redirection);
//				}else
//					Toast.makeText(getApplicationContext(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
				rechercheBdd.close();
			}
		});
        
        
        /** Bouton validez **/
        validerModif.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				final RadioButton choixRechercheChecked = (RadioButton)findViewById(radioChoixRechercheModif.getCheckedRadioButtonId());
				final RadioButton choixResumeChecked = (RadioButton)findViewById(radioChoixResumeModif.getCheckedRadioButtonId());
				String type = choixRechercheChecked.getText().toString();
				String categorie = spinnerCategorieModif.getSelectedItem().toString();
				String poste = spinnerPosteModif.getSelectedItem().toString();
				
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
					resume = editTextResumeModif.getText().toString();
				}else
					resume = choixResumeChecked.getText().toString();
				
				/** creation de l'objet recherche **/
				Recherche recherche = new Recherche(type, categorie, poste,resume,pdf);
				
				/** insertion dans la base de donnees **/
				rechercheBdd.open();
				if(rechercheBdd.updateRecherche(idRecherche, recherche) == 1)
					Toast.makeText(getApplicationContext(), "Mise à jour de la recherche effectuée", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(), "Erreur lors de la mise ) jour", Toast.LENGTH_SHORT).show();
				rechercheBdd.close();
				
				/** retour a la liste des recherches **/
				Intent redirection = new Intent(ModifierRecherche.this, VoirMesRecherches.class);
				startActivity(redirection);
				
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
	
	
    /**
     * Utilisé pour visualiser un fichier
     * @param pFile le fichier à visualiser
     */
    private void VisualiserFichier(File pFile) {
    	Intent i = new Intent(Intent.ACTION_VIEW);
    	MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = pFile.getName().substring(pFile.getName().indexOf(".") + 1).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext);
    	i.setDataAndType(Uri.fromFile(pFile), type);
    	try {
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "Oups, vous n'avez pas d'application qui puisse lancer ce fichier", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
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
        	  Intent intent = new Intent(ModifierRecherche.this, VoirMesRecherches.class);
        	  startActivity(intent);
        	  return true;
 
       }
       return false;}
	
}
