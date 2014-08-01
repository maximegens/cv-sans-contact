package com.projetMaster.bluetooth;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe Bluetooth permettant de gerer toutes les fonctionnalites du Bluetooth.
 */
public class Bluetooth{

	protected Activity context;
	protected BluetoothAdapter blueAdapter;
	protected Set<BluetoothDevice> listDevicesBT;
	protected Set<BluetoothDevice> bondedDevices;
	protected BroadcastRechercheBluetooth recherchePeripheriqueBT;
	protected boolean rechercheBTAEtaitActive;
	
	/******** attention il ne pas y avoir d'interaction avec l'UI thread ailleurs quand dans l'UI thread ************/
	

	/**
	 * Constructeur de la classe Bluetooth.
	 * @param context le context de l'application.
	 */
	public Bluetooth(Activity context) {
		
			this.blueAdapter = BluetoothAdapter.getDefaultAdapter();
			this.listDevicesBT = new HashSet<BluetoothDevice>();
			this.bondedDevices = new HashSet<BluetoothDevice>();
			this.context = context;
			this.recherchePeripheriqueBT = new BroadcastRechercheBluetooth(this,context);
			this.rechercheBTAEtaitActive = false;
	}
	
	/**
	 * Fonction permettant d'activer le Bluetooth pour une durée de ConstantesBluetooth.TIME_VISIBLE_BT.
	 */
	public void activation() {
		
		if (blueAdapter == null) 
			Toast.makeText(context, "Votre appareil ne posséde pas le Bluetooth", Toast.LENGTH_SHORT).show();
		else{
			if (!blueAdapter.isEnabled()){
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,ConstantesBluetooth.TIME_VISIBLE_BT);
			    this.context.startActivityForResult(discoverableIntent, ConstantesBluetooth.REQUEST_DISCOVERABLE_AND_ENABLE_BT);
				Log.v("Bluetooth", "Bluetooth activer");
			}else
				Log.v("activation","Bluetooth deja activé");
		}
	}
	
	
	/**
	 * Fonction permettant de desactiver le Bluetooth.
	 */
	public void desactivation() {
		
		if (blueAdapter == null) 
			Toast.makeText(context, "Votre appareil ne posséde pas le Bluetooth", Toast.LENGTH_SHORT).show();
		else if(blueAdapter.isEnabled()){
			blueAdapter.disable();
			Log.v("Bluetooth", "Bluetooth desactiver");
		}else
				Log.v("Bluetooth", "Bluetooth deja desactivé");
		
	}


	/**
	 * Fonction recherchant, scannant, les peripheriques bluetooths aux alentours.
	 * La liste des devices recuperés est ajouter dans listNomDevicesBT pour juste les noms et dans listDevicesBT pour les objets Bluetooth.
	 */
	public void rechercheDevices() {
		
		if(blueAdapter.isEnabled() && !blueAdapter.isDiscovering()){
	    	blueAdapter.startDiscovery();
	    	this.context.registerReceiver(recherchePeripheriqueBT, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
	    	this.context.registerReceiver(recherchePeripheriqueBT, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	    	this.context.registerReceiver(recherchePeripheriqueBT, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	    	this.rechercheBTAEtaitActive = true;
	    }else{
			Toast.makeText(context, "Attention Bluetooth desactive ou recherche deja en cours", Toast.LENGTH_SHORT).show();
			Log.v("Bluetooth recherche ", "recherche bluetooth impossible Bluetooth desactiver ou recherche deja en cours");
	    }
	}

	/**
	 * Fonction permettant de se connecter au device Bluetooth passer en parametre.
	 * @param device le device auquel se sonnecter.
	 * @return la socket BT du device.
	 */
	public BluetoothSocket connexionVers(BluetoothDevice device) {
		
		BluetoothSocket socket = null;
		bondedDevices = blueAdapter.getBondedDevices();
		
		Log.v("Nom du device auquel se connecter", device.getName());
		
		if(blueAdapter.isEnabled()){
			if (bondedDevices.contains(device)){
				SeConnecterAuDeviceThread connexionAuDevice = new SeConnecterAuDeviceThread(this.context, device, this.blueAdapter);
				connexionAuDevice.start();
				try {
					connexionAuDevice.join();
					socket = connexionAuDevice.getClientSocket();
					Log.v("connexionVers", "connexion a "+ device.getName()+" reussi");
					return socket;
				} catch (InterruptedException e) {
					Log.v("connexionVers", "impossible d'attendre la fin du join()");				
				}
			}else
				Log.v("connexionVers", "Device non Appareillé");
		}else
			Log.v("connexionVers", "Bluetooth desactiver");
		
		return socket;
	}

	/**
	 * Permettant de lancer le serveur Bluetooth et d'attendre une connexion dessus.
	 */
	public void lancementServeur(){
		
		if(blueAdapter.isEnabled()){
			ServeurBluetooth serveurBluetooth = new ServeurBluetooth(blueAdapter,context.getApplicationContext());
			serveurBluetooth.start();
			Toast.makeText(context, "Serveur lancé", Toast.LENGTH_LONG).show();
			Log.v("Bluetooth lancementServeur", "Serveur lancé");
		}else{
			Toast.makeText(context, "Attention Bluetooth desactive", Toast.LENGTH_SHORT).show();
			Log.v("Bluetooth lancementServeur", "impossible bluetooth desactive");
		}
		
	}
	
	/**
	 * Permet un transfert de donnee avec le client en parametre.
	 */
	public void Envoi(BluetoothSocket socket,String message) {
		Log.v("Bluetooth", "** Envoi **");
		if(socket != null){
			Log.v("Envoi", "la socket pour l'envoi est  " + socket);
			EcouteEnvoiData envoi = new EcouteEnvoiData(socket,context.getApplicationContext());
			envoi.write((message+" ").getBytes());
		}else{
			Toast.makeText(context, "Pas de devices auquel envoyer des données", Toast.LENGTH_SHORT).show();
			Log.v("Envoi", "la socket est null");
		}
	}
	
	/**
	 * Permet de quitter l'application en verifiant si toutes les conditions sont remplies pour la fermeture.
	 */
	public void verificationTerminaisson(){
		if(this.blueAdapter.isDiscovering()){
			this.blueAdapter.cancelDiscovery();
	    }
		if(this.rechercheBTAEtaitActive)
			this.context.unregisterReceiver(this.recherchePeripheriqueBT);
	}
	
	/*** Les Getters et Setters ***/
	
	public Set<BluetoothDevice> getListDevicesBT() {
		return listDevicesBT;
	}

	public BluetoothAdapter getBlueAdapter() {
		return blueAdapter;
	}

	public BroadcastRechercheBluetooth getRecherchePeripheriqueBT() {
		return recherchePeripheriqueBT;
	}

	public void setListDevicesBT(Set<BluetoothDevice> listDevicesBT) {
		this.listDevicesBT = listDevicesBT;
	}
}
