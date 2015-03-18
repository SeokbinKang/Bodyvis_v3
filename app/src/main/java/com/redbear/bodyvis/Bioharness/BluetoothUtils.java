package com.redbear.bodyvis.Bioharness;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public abstract class BluetoothUtils {

	private BluetoothUtils(){}
	
	public static Boolean checkBluetoothAvailabilityOnDevice(){
		return checkBluetoothAvailabilityOnDevice(null);
	}
	
	public static Boolean checkBluetoothAvailabilityOnDevice(Context context){
		// Check for Bluetooth support and then check to make sure it is turned on
	    // Emulator doesn't support Bluetooth and will return null
		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(btAdapter==null) { 
	      Log.e("Fatal Error", "Bluetooth not support");
	    } else {
	      if (btAdapter.isEnabled()) {
	        Log.d("checkBluetoothAvailabilityOnDevice", "...Bluetooth ON...");
	        return true;
	      } else {
	        //Prompt user to turn on Bluetooth
	    	if(context != null){
	    		Log.d("checkBluetoothAvailabilityOnDevice", "User needs to turn on Bluetooth");
	    		Toast toast = Toast.makeText(context, "Turn on Bluetooth", Toast.LENGTH_LONG);
	    		toast.show();
	    	}
	      }
	    }
		return false;
	}
}