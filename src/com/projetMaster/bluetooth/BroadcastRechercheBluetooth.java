package com.projetMaster.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * BroadcastReceiver permettant de rechercher des peripheriques Bluetooth aux alentours.
 */
public class BroadcastRechercheBluetooth extends BroadcastReceiver {

	public Bluetooth BT;
	
	/**
	 * Constructeur du BroadcastReceiver BroadcastRechercheBluetooth.
	 * @param BT Objet Bluetooth.
	 */
	public BroadcastRechercheBluetooth(Bluetooth BT,Activity context) {
		this.BT = BT;
	}
	
	/**
	 * Methode appeler lorsque le Broadcast est declarer.
	 * 	 */
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
	   if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
		   Log.v("BroadcastRechercheBT", " --- recherche commence ---");
		   Toast.makeText(context, "Découvert des peripheriques en cours", Toast.LENGTH_SHORT).show();
	       
       if (BluetoothDevice.ACTION_FOUND.equals(action)) {
           BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
           Log.v("BroadcastRechercheBT", "trouve : "+device.getName()+" "+device.getAddress());
           BT.getListDevicesBT().add(device);
       }
       if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
    	   Log.v("BroadcastRechercheBT", "--- recherche termine ---");
    	   Toast.makeText(context, "Découvert des peripheriques termine", Toast.LENGTH_SHORT).show();
       }
	}
}
