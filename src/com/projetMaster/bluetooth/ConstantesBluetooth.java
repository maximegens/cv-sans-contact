package com.projetMaster.bluetooth;

import java.io.File;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.os.Environment;

/**
 * Class regroupant toutes les constantes de l'application.
 *
 */
public class ConstantesBluetooth {

		/** Les constantes pour le Bluetooth **/
        public static int REQUEST_DISCOVERABLE_AND_ENABLE_BT = 1;
        public static int TIME_VISIBLE_BT = 3600;
        public static String CHANGE_STATUS_BT = BluetoothAdapter.ACTION_SCAN_MODE_CHANGED;
        public static final UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
        public static String domaine = "";
        
        
        
    	/** 
    	 * retrouve un fichier dans la racine de la carte sd.
    	 * @param name le nom du fichier a rechercher.
    	 * @return Le fichier trouvé.
    	 **/
    	public static File getFileByNameInExternalStorageDirectory(String name){
    		
    		int i = 0;
    		File fichier = null;
    		boolean trouve = false;
    		File mCurrentFile = Environment.getExternalStorageDirectory();

    		File[] fichiers = mCurrentFile.listFiles();
    		 
    		 while(trouve == false && i <fichiers.length){
    			 if(fichiers[i].getName().equals(name)){
    				 fichier = new File(fichiers[i].getPath());
    	    		 trouve = true;
    			 }else
    				 i++;
    		 }
    	     return fichier;
    	}
}