package TraitementSansFil;

import com.projetMaster.bluetooth.Bluetooth;

/** classe qui s'occupe de la recherche en arri�re plan de l'application **/
public class TraitementBluetooth extends Thread{

	Bluetooth bluetooth;
	
	public TraitementBluetooth(Bluetooth bluetooth) {
		this.bluetooth = bluetooth;
	}
	
	 public void run() {
		 long start = System.currentTimeMillis();
		// boucle tant que la dur�e de vie du thread est < � 5 secondes
		//while( System.currentTimeMillis() < ( start + (1000 * 5))) {
			 if(bluetooth.getBlueAdapter().isEnabled())
				bluetooth.rechercheDevices();
			 else{
				 bluetooth.activation();
				 
			 }
		// }
		 
	}	
	
}
