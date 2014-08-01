package com.projetMaster.activity;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import TraitementSansFil.TraitementBluetooth;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.projetMaster.baseDeDonnees.RechercheBDD;
import com.projetMaster.bluetooth.Bluetooth;
import com.projetMaster.bluetooth.ConstantesBluetooth;
import com.projetMaster.recherche.Constante;
import com.projetMaster.recherche.Recherche;
import com.projetMaster.recherche.Reponse;

/**
 * Classe Accueil - Activity d'accueil lorsque l'utilisateur lance l'application.
 *
 */
public class Accueil extends Activity {
	
	private Button ajouterRecherche;
	private Button voirMesRecherche;
	private Button actualiserBT;
	private TextView testListeBluetooth;
	RechercheBDD rechercheBdd = new RechercheBDD(this);
	Bluetooth bluetooth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil);
		
		ajouterRecherche = (Button)findViewById(R.id.ajoutRecherche);
		voirMesRecherche = (Button)findViewById(R.id.voirRecherche);
		testListeBluetooth = (TextView)findViewById(R.id.textViewTextListBluetooth);
		actualiserBT = (Button)findViewById(R.id.actualiserListeBluetooth);
		bluetooth = new Bluetooth(this);
		bluetooth.rechercheDevices();


		rechercheBdd.open();	
		File pdfOffre = ConstantesBluetooth.getFileByNameInExternalStorageDirectory(Constante.NOM_PDF_OFFRE);
		File pdfDemande = ConstantesBluetooth.getFileByNameInExternalStorageDirectory(Constante.NOM_PDF_CV);
		Recherche r1 = new Recherche(Constante.DEMANDE, "Informatique", "Développeur Web", "voici un resume",pdfDemande );
		Recherche r2 = new Recherche(Constante.OFFRE, "Informatique", "Chef de projet", "voici un resume",pdfOffre);
		Recherche r3 = new Recherche(Constante.DEMANDE, "Restauration", "Serveur", "voici un resume",pdfDemande);
		Recherche r4 = new Recherche(Constante.DEMANDE, "Agriculture", "Conducteur de tracteur", "voici un resume",pdfDemande);
		Recherche r5 = new Recherche(Constante.OFFRE, "Agriculture", "Fermier", "voici un resume",pdfOffre);	
		rechercheBdd.insertRecherche(r1);
		rechercheBdd.insertRecherche(r2);
		rechercheBdd.insertRecherche(r3);
		rechercheBdd.insertRecherche(r4);
		rechercheBdd.insertRecherche(r5);	
		Toast.makeText(this, "nb recherche :"+rechercheBdd.getCountRecherche()+"", Toast.LENGTH_SHORT).show();
//		Reponse rep1 = new Reponse(2, "test resume2", null);
//		Reponse rep2 = new Reponse(2, "test resume2", null);
//		Reponse rep3 = new Reponse(1, "test resume1", null);
//		Reponse rep4 = new Reponse(3, "test resume3", null);
//		rechercheBdd.insertReponse(rep1);
//		rechercheBdd.insertReponse(rep2);
//		rechercheBdd.insertReponse(rep3);
//		rechercheBdd.insertReponse(rep4);
//		Toast.makeText(this, "nb reponse :"+rechercheBdd.getCountReponse()+"", Toast.LENGTH_SHORT).show();
		rechercheBdd.close();
		
		
		actualiserBT.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String list = "";
				
				boolean stop = false;
				Iterator<BluetoothDevice> itBTDevice = bluetooth.getListDevicesBT().iterator();
				while(itBTDevice.hasNext() && stop == false){
					BluetoothDevice device = (BluetoothDevice) itBTDevice.next();
					if(device.getName().equals("Galaxy Nexus 2") || device.getName().equals("GT-I9300") ||device.getName().equals("lapin cretin")||device.getName().equals("Max SCL")){ 
						Log.v("ConnecterUnDeviceBTThread", "Tentative de connection a : " +device.getName() +" "+device.getAddress());
						BluetoothSocket clientSocket;
						clientSocket = null;
						clientSocket = bluetooth.connexionVers(device);
						
			        	
						stop = true;
					}
				} 
					
			}
		});
		
		ajouterRecherche.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Accueil.this, AjouterRecherche.class);
				startActivity(intent);
			}
		});
		
		voirMesRecherche.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Accueil.this, VoirMesRecherches.class);
				startActivity(intent);
			}
		});
		
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}

	/** Gestion du menu **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_accueil, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
          case R.id.menu_stopper:
             Toast.makeText(Accueil.this, "Arrêt des communication sans fils", Toast.LENGTH_SHORT).show();
             if(bluetooth.getBlueAdapter().isEnabled())
            	 bluetooth.desactivation();
             
             return true;
         case R.id.menu_quitter:
             finish();
             return true;
       }
       return false;}
	
}
