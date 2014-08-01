package com.projetMaster.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projetMaster.adapter.ReponseAdapter;
import com.projetMaster.baseDeDonnees.RechercheBDD;
import com.projetMaster.bluetooth.ConstantesBluetooth;
import com.projetMaster.recherche.Constante;
import com.projetMaster.recherche.Reponse;

/**
 * Classe VoirMesReponses - Activity qui permet de visualiser la liste des réponses.
 *
 */
public class VoirMesReponses extends Activity {
	
	TextView nbReponse;
	ListView listeViewDesReponses;
	List<Reponse> listeReponse = new ArrayList<Reponse>();
	RechercheBDD rechercheBdd = new RechercheBDD(this);
	int idRecherche;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultats_recherches);
		
		idRecherche = (Integer)getIntent().getExtras().get("idRecherche");
		
		nbReponse = (TextView)findViewById(R.id.nbRechercheReponse);
		listeViewDesReponses = (ListView)findViewById(R.id.listViewRechercheReponse);
		
		
		
		
		/** Code de test **/
		Log.v("VoirMesReponses", "idRecherche a recherche : "+idRecherche);
		
		/** récupére les réponses de la recherche **/
//		Reponse rep = rechercheBdd.getReponseeByIdRecherche(idRecherche);
//		listeReponse.add(rep);
//		ArrayList<Reponse> lesReponses = rechercheBdd.getReponseeByIdRecherche(idRecherche);
//		for(Reponse laReponse : lesReponses){
//			listeReponse.add(laReponse);
//		}
		/* simpel affichage pas de trie en fct de la recherche */
		
		File pdf = ConstantesBluetooth.getFileByNameInExternalStorageDirectory("Exemples_de_CV.pdf");
		Reponse rep1 = new Reponse(1, "Jeune dynamique recherche emploi informatique", pdf);
		Reponse rep2 = new Reponse(2, "BTS agriculture motivé", pdf);
		Reponse rep3 = new Reponse(3, "Recherche petit boulot", pdf);
		Reponse rep4 = new Reponse(2, "Diplomé de master", pdf);
		Reponse rep5 = new Reponse(1, "Jeune dynamique", pdf);
		Reponse rep6 = new Reponse(4, "Serveur recherche job d'été", pdf);
		Reponse rep7 = new Reponse(5, "Jeune très motivé", pdf);
		listeReponse.add(rep1);
		listeReponse.add(rep2);
		listeReponse.add(rep3);
		listeReponse.add(rep4);
		listeReponse.add(rep5);
		listeReponse.add(rep6);
		listeReponse.add(rep7);
		/** fin code de test **/	
		
		
		
		
		/** initialisation de la listeview **/
		ReponseAdapter adapterListViewReponse = new ReponseAdapter(getApplicationContext(), listeReponse);
		listeViewDesReponses.setAdapter(adapterListViewReponse);
		
		nbReponse.setText("Vous avez "+listeViewDesReponses.getCount()+" reponses");
		
		/** lorsqu'on maintient le clic appuyé **/
		listeViewDesReponses.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				
				File pdf = listeReponse.get(position).getPdf();
				if(pdf == null)
					Toast.makeText(getApplicationContext(), "Il n'y a pas de PDF pour cette réponse", Toast.LENGTH_SHORT).show();
				else{
					Toast.makeText(getApplicationContext(), "Ouverture du PDF", Toast.LENGTH_SHORT).show();
					VisualiserFichier(pdf);
				}
			}
		});
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
}
