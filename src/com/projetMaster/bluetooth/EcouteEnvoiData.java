package com.projetMaster.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

/**
 * Classe Thread permettant l'envoi et la reception de donnee par Bluetootj. 
 *
 */
public class EcouteEnvoiData extends Thread{
	
	    private final BluetoothSocket socket;
	    private final InputStream inStream;
	    private final OutputStream outStream;
	    private String resultatChaineRecu;
	    private Context context;
	 
	    /**
	     * Constructeur de classe EcouteEnvoiData.
	     * @param socket le socket du device.
	     */
	    public EcouteEnvoiData(BluetoothSocket socket,Context context) {
	        this.socket = socket;
	        this.context = context;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	        resultatChaineRecu = null;
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        inStream = tmpIn;
	        outStream = tmpOut;
	    }
	 
	    /**
	     * Methode Run, elle attend la reception de data.
	     */
	    public void run() {
	    	int bufferSize = 4096;
	        byte[] buffer = new byte[bufferSize];
	        @SuppressWarnings("unused")
			int bytesRead = -1;
  
	        while (true) {
	            try {
	            	Log.v("EcouteEnvoiData", "attente de recevoir des donnes");
	                bytesRead = inStream.read(buffer);
	                String resultat = "";
	                if((bytesRead == bufferSize) && (buffer[bufferSize-1] != 0)){
	                	resultat = resultat + new String(buffer,0, bytesRead - 1);
	                }
	                resultat = resultat + new String(buffer,0, bytesRead - 1);
	                this.setresultatChaineRecu(resultat);
	                Log.v("EcouteEnvoi", "Message recu : "+ resultat);
	                
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    public String getresultatChaineRecu() {
			return resultatChaineRecu;
		}

		public void setresultatChaineRecu(String chaine) {
			this.resultatChaineRecu = chaine;
		}

		/**
	     * Methode permettant de transferer des datas vers le device.
	     * @param bytes les datas a transmettre.
	     */
	    public void write(byte[] bytes) {
	    	Log.v("EcouteEnvoiData", "envoi de : "+bytes);
	        try {
	        	bytes[bytes.length-1] = 0;
	            outStream.write(bytes);
	            Log.v("EcouteEnvoiData", "message envoyé");
	        } catch (IOException e) {
	        	Log.v("EcouteEnvoiData", "echec de l'envoi");
	        }
	    }
	 
	    /**
	     * Methode permettant de fermer la connection.
	     */
	    public void cancel() {
	        try {
	            socket.close();
	        } catch (IOException e) { }
	    }
	}

