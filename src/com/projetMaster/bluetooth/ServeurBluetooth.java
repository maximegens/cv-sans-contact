package com.projetMaster.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

/**
 * Class serveur pour le bluetooth, permet detablir des connexions sur ce device.
 */
public class ServeurBluetooth extends Thread {
	
    private final BluetoothServerSocket socketServeur;
    protected BluetoothAdapter blueAdapter;
    private BluetoothSocket socketClient;
    private Context context;

    /**
     * Constructeur de la classe ServeurBluetooth.
     * @param context le contexte de l'application.
     * @param blueAdapter L'adaptateur Bluetooth.
     */
    public ServeurBluetooth(BluetoothAdapter blueAdapter,Context context) {
    	this.context = context;
    	this.blueAdapter = blueAdapter;
        BluetoothServerSocket tmp = null;
        this.socketClient = null;
        
        try {
            tmp = blueAdapter.listenUsingRfcommWithServiceRecord("UUID_BT", ConstantesBluetooth.uuid);
        } catch (IOException e) {
        	System.err.println(e.toString());
        }
        socketServeur = tmp;
    }

    public BluetoothServerSocket getSocketServeur() {
		return socketServeur;
	}
    
    public BluetoothSocket getSocketClient() {
		return socketClient;
	}

	/**
     * La methode Run permettant de lancer le Thread du serveur Bluetooth est d'attendre la connexion d'un device Bluetooth.
     */
	public void run() {
        
        while (true) {
            try {
            	Log.v("ServeurBluetooth", "En attente de connexion...");
            	this.socketClient = socketServeur.accept();
            	Log.v("ServeurBluetooth", "Client connecté");
            } catch (IOException e) {
            	System.err.println(e.toString());
                break;
            }
            
            if (this.socketClient != null) {
            	Log.v("ServeurBluetooth", "Lancement de l'écoute");
            	EcouteEnvoiData ecoute = new EcouteEnvoiData(this.socketClient,context);
            	ecoute.start();
                break;
            }
        }
    }

   /**
    * Ferme le serveur Bluetooth.
    */
    public void cancel() {
        try {
        	socketServeur.close();
        } catch (IOException e) { }
    }
}