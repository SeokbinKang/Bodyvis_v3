//package edu.umd.cs.hcil.bioharness;
//
//import java.util.Set;
//
//import zephyr.android.BioHarnessBT.BTClient;
//
//import edu.umd.cs.hcil.utils.BluetoothUtils;
//import edu.umd.cs.hcil.utils.StringUtils;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.util.Log;
//
//public class BioHarnessManager {
//
//	private String _strBioHarnessMACAddress = null;
//	BluetoothAdapter _btAdapter = null;
//	BTClient _bioHarnessBTClient = null;
//	BioHarnessBTConnectedListener _bioHarnessBTConnectedListener;
//	
//	public BioHarnessManager(String strBioHarnessMACAddress){
//		_strBioHarnessMACAddress = strBioHarnessMACAddress;
//	}
//	
//	public Boolean Connect(){
//		if(!StringUtils.isNullOrEmpty(_strBioHarnessMACAddress)){
//			BluetoothDevice Device = _btAdapter.getRemoteDevice(_strBioHarnessMACAddress);
//			String strBTDeviceName = Device.getName();
//			_bioHarnessBTClient = new BTClient(_btAdapter, _strBioHarnessMACAddress);
//			_bioHarnessBTConnectedListener = new BioHarnessBTConnectedListener(Newhandler, Newhandler);
//			_bioHarnessBTClient.addConnectedEventListener(_bioHarnessBTConnectedListener);
//		
//			
//			if(_bioHarnessBTClient.IsConnected())
//			{
//				_bioHarnessBTClient.start();
//				
//				Log.d("_bioHarnessBTClient", "Connected to BioHarness "+ strBTDeviceName);
//				 
//				 //Reset all the values to 0s
//
//			}
//			else
//			{
//				Log.d("_bioHarnessBTClient", "Unable to connect!");
//			}
//		}
//		return _bioHarnessBTClient.IsConnected();
//	}
//	
//	public void Disconnect(){
//		if(_bioHarnessBTClient != null && _bioHarnessBTClient.IsConnected()){
//			_bioHarnessBTClient.removeConnectedEventListener(_bioHarnessBTConnectedListener);
//			_bioHarnessBTClient.stop();
//			_bioHarnessBTClient.Close();
//			_bioHarnessBTConnectedListener = null;
//			_bioHarnessBTClient = null;
//		}
//	}
//	
//	public static String findBioHarnessMACAddress(){
//		if(BluetoothUtils.checkBluetoothAvailabilityOnDevice()){
//			return null;
//		}
//		
//		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
//		
//		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
//		String strBioharnessMACAddress = null;
//
//        for (BluetoothDevice device : pairedDevices) 
//        {
//        	if (device.getName().startsWith("BH")) 
//        	{
//        		BluetoothDevice btDevice = device;
//        		strBioharnessMACAddress = btDevice.getAddress();
//                break;
//        	}
//        }         
//		return strBioharnessMACAddress;
//	}
//}
