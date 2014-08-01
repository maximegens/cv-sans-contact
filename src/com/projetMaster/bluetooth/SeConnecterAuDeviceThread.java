package com.projetMaster.bluetooth;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

/**
 * Classe Thread permettant la connexion a un device. 
 *
 */
public class SeConnecterAuDeviceThread extends Thread {
	
	protected BluetoothSocket clientSocket;
    private final BluetoothDevice device;
    private BluetoothAdapter blueAdapter;
    private Context context;
 
    /**
     * Constructeur de la classe ConnecterUnDeviceBTThread.
     * @param context le context de l'application.
     * @param device le peripherique a relier.
     * @param blueAdapter lappareil Bluetooth du telephone.
     */
    public SeConnecterAuDeviceThread(Context context,BluetoothDevice device,BluetoothAdapter blueAdapter) {
    	this.context = context;
    	this.clientSocket = null;
        this.device = device;
        this.blueAdapter = blueAdapter;
    }
 
    /**
     * La methode Run permettant de lancer le Thread.
     */
    public void run() {
    	blueAdapter.cancelDiscovery();
    	
        try {
        	Log.v("ConnecterUnDeviceBTThread", "Tentative de connection a : " +device.getName() +" "+device.getAddress());
        	clientSocket = this.device.createRfcommSocketToServiceRecord(ConstantesBluetooth.uuid);
        	clientSocket.connect();
        	this.setClientSocket(clientSocket);
        	EcouteEnvoiData ecoute = new EcouteEnvoiData(clientSocket,context);
        	ecoute.start();
        } catch (IOException connectException) {
            try {
            	Log.v("ConnecterUnDeviceBTThread", "Impossible de se connecter, fermeture de la socket");
            	clientSocket.close();
            } catch (IOException closeException) {
            	Log.v("ConnecterUnDeviceBTThread", "Impossible de fermer la socket");
            }
            return;
        }
    }
 
    public void setClientSocket(BluetoothSocket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public BluetoothSocket getClientSocket() {
		return clientSocket;
	}

	/**
     * Stoppe lecoute des connexions et tue le Thread.
     */
    public void cancel() {
        try {
        	Log.v("ConnecterUnDeviceBTThread", "Fermeture du clientSocket");
        	clientSocket.close();
        } catch (IOException e) { }
    }
}
