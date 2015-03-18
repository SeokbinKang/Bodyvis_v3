package com.redbear.bodyvis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import zephyr.android.BioHarnessBT.BTClient;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import com.redbear.bodyvis.Bioharness.*;

public class Chat extends Activity {
	private final static String TAG = Chat.class.getSimpleName();

	public static final String EXTRAS_DEVICE = "EXTRAS_DEVICE";
	private TextView tv = null;
	private EditText et = null;
	private Button btn = null;
	private String mDeviceName;
	private String mDeviceAddress;
	private RBLService mBluetoothLeService;
	private Map<UUID, BluetoothGattCharacteristic> map = new HashMap<UUID, BluetoothGattCharacteristic>();


    //BIOHARNESS
    private final String BIOHARNESS_MAC_ADDRESS = "00:07:80:9D:8A:E8";
    private final String Arduino_MAC_ADDRESS = "F5:B7:52:74:92:2E";
    // Called when the activity is first created.
    BluetoothAdapter _btAdapter = null;
    public static Chat instance = null;
    BTClient _bioHarnessBTClient = null;
    BioHarnessBTConnectedListener _bioHarnessBTConnectedListener;

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName,
				IBinder service) {
			mBluetoothLeService = ((RBLService.LocalBinder) service)
					.getService();
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// Automatically connects to the device upon successful start-up
			// initialization.
			mBluetoothLeService.connect(mDeviceAddress);
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};

	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (RBLService.ACTION_GATT_DISCONNECTED.equals(action)) {
			} else if (RBLService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				getGattService(mBluetoothLeService.getSupportedGattService());
			} else if (RBLService.ACTION_DATA_AVAILABLE.equals(action)) {
				displayData(intent.getByteArrayExtra(RBLService.EXTRA_DATA));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		et = (EditText) findViewById(R.id.editText);
		btn = (Button) findViewById(R.id.send);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BluetoothGattCharacteristic characteristic = map
						.get(RBLService.UUID_BLE_SHIELD_TX);

				String str = et.getText().toString();
				byte b = 0x00;
				byte[] tmp = str.getBytes();
				byte[] tx = new byte[tmp.length + 1];
				tx[0] = b;
				for (int i = 1; i < tmp.length + 1; i++) {
					tx[i] = tmp[i - 1];
				}

				characteristic.setValue(tx);
				mBluetoothLeService.writeCharacteristic(characteristic);

				et.setText("");
			}
		});

		Intent intent = getIntent();

		mDeviceAddress = intent.getStringExtra(Device.EXTRA_DEVICE_ADDRESS);
		mDeviceName = intent.getStringExtra(Device.EXTRA_DEVICE_NAME);

//		getActionBar().setTitle(mDeviceName);
//		getActionBar().setDisplayHomeAsUpEnabled(true);

		Intent gattServiceIntent = new Intent(this, RBLService.class);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        initBioHarness();

        btn = (Button) findViewById(R.id.video);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, VideoPlayActivity.class);
                //intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
                //intent.putExtra(EXTRA_DEVICE_NAME, name);
                startActivity(intent);

            }
        });

        btn = (Button) findViewById(R.id.Run);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                View tvv = findViewById(R.id.tableLayout);
                tvv.setVisibility(View.INVISIBLE);
                tvv = findViewById(R.id.botLayout);
                tvv.setVisibility(View.INVISIBLE);
                tvv = findViewById(R.id.buttonConnect);
                tvv.setVisibility(View.INVISIBLE);
                tvv = findViewById(R.id.Run);
                tvv.setVisibility(View.INVISIBLE);

            }
        });
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			mBluetoothLeService.disconnect();
			mBluetoothLeService.close();

			System.exit(0);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		super.onStop();

		unregisterReceiver(mGattUpdateReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mBluetoothLeService.disconnect();
		mBluetoothLeService.close();

        if (_bioHarnessBTClient != null) {
            _bioHarnessBTClient.Close();
            _bioHarnessBTClient = null;

            TextView tv = (TextView) findViewById(R.id.textViewBioHarnessConnectionStatus);
            tv.setText("Off");
        }
		System.exit(0);
	}

	private void displayData(byte[] byteArray) {
		if (byteArray != null) {
			String data = new String(byteArray);
            Log.d("BodyVis",data);
            if(data.equals("V")){
                Intent intent = new Intent(Chat.this, VideoPlayActivity.class);
                //intent.putExtra(EXTRA_DEVICE_ADDRESS, addr);
                //intent.putExtra(EXTRA_DEVICE_NAME, name);
                startActivity(intent);
            }
			/*tv.append(data);
			// find the amount we need to scroll. This works by
			// asking the TextView's internal layout for the position
			// of the final line and then subtracting the TextView's height
			final int scrollAmount = tv.getLayout().getLineTop(
					tv.getLineCount())
					- tv.getHeight();
			// if there is no need to scroll, scrollAmount will be <=0
			if (scrollAmount > 0)
				tv.scrollTo(0, scrollAmount);
			else
				tv.scrollTo(0, 0);*/
		}
	}

	private void getGattService(BluetoothGattService gattService) {
		if (gattService == null)
			return;

		BluetoothGattCharacteristic characteristic = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_TX);
		map.put(characteristic.getUuid(), characteristic);

		BluetoothGattCharacteristic characteristicRx = gattService
				.getCharacteristic(RBLService.UUID_BLE_SHIELD_RX);
		mBluetoothLeService.setCharacteristicNotification(characteristicRx,
				true);
		mBluetoothLeService.readCharacteristic(characteristicRx);
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RBLService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(RBLService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(RBLService.ACTION_DATA_AVAILABLE);

		return intentFilter;
	}


    private void initBioHarness(){
        Button btnConnect = (Button) findViewById(R.id.buttonConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Connect to Arduino first
                // _arduinoBTComm.connect();


                TextView tv = (TextView) findViewById(R.id.textViewArduinoBodyVisConnectionStatus);
                // tv.setText(_arduinoBTComm.isConnected() ? "On" : "Off");

                //  _arduinoBTComm.sendData("1");
                // _arduinoBTComm.sendData("0");

                //Now connect to BioHarness
                String strBioharnessMACAddress = BIOHARNESS_MAC_ADDRESS;

                //String BhMacID = "00:07:80:88:F6:BF";
                _btAdapter = BluetoothAdapter.getDefaultAdapter();

                Set<BluetoothDevice> pairedDevices = _btAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        Log.d("_bioHarnessBTClient", "BH DEBUG : device name =" + device.getName());
                        if (device.getName().startsWith("BH")) {
                            BluetoothDevice btDevice = device;
                            strBioharnessMACAddress = btDevice.getAddress();
                            break;
                        }
                    }
                } else {
                    //TODO no paired devices
                }

                if (!StringUtils.isNullOrEmpty(strBioharnessMACAddress)) {
                    BluetoothDevice Device = _btAdapter.getRemoteDevice(strBioharnessMACAddress);
                    String strBTDeviceName = Device.getName();
                    _bioHarnessBTClient = new BTClient(_btAdapter, strBioharnessMACAddress);
                    _bioHarnessBTConnectedListener = new BioHarnessBTConnectedListener(Newhandler, Newhandler);
                    _bioHarnessBTClient.addConnectedEventListener(_bioHarnessBTConnectedListener);


                    if (_bioHarnessBTClient.IsConnected()) {
                        _bioHarnessBTClient.start();

                        Log.d("_bioHarnessBTClient", "Connected to BioHarness " + strBTDeviceName);
                        tv = (TextView) findViewById(R.id.textViewBioHarnessConnectionStatus);
                        tv.setText(_bioHarnessBTClient.IsConnected() ? "On" : "Off");
                        //Reset all the values to 0s

                    } else {
                        Log.d("_bioHarnessBTClient", "Unable to connect!");
                    }
                }

            }
        });


    }
    final Handler Newhandler = new Handler() {
        private final int HEART_RATE = 0x100;
        private final int RESPIRATION_RATE = 0x101;
        private final int SKIN_TEMPERATURE = 0x102;
        private final int POSTURE = 0x103;
        private final int PEAK_ACCELERATION = 0x104;
        TextView tv = null;


        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEART_RATE:
                    int heartRate = msg.getData().getInt(BioHarnessConstants.HEART_RATE_NAME);
                    //_arduinoBTComm.sendData("HR="+heartRate+"#");
                    tv = (TextView) findViewById(R.id.textViewHeartRateValue);
                    tv.setText(String.valueOf(heartRate));
                    //String HeartRatetext = msg.getData().getString("HeartRate");
                    Log.d("Newhandler.handleMessage", "HeartRate=" + heartRate);
                    SendMsgtoRBL("HR="+heartRate+"#");
                    break;

                case RESPIRATION_RATE:
                    double respirationRate = msg.getData().getDouble(BioHarnessConstants.RESPIRATION_RATE_NAME);
                    //     _arduinoBTComm.sendData("BR="+respirationRate+"#");
                    tv = (TextView) findViewById(R.id.textViewRespirationRateValue);
                    tv.setText(String.valueOf(respirationRate));
                    Log.d("Newhandler.handleMessage", "RespirationRate=" + respirationRate);
                    SendMsgtoRBL("BR="+respirationRate+"#");
                    break;

                case SKIN_TEMPERATURE:
                    double skinTemperature = msg.getData().getDouble(BioHarnessConstants.SKIN_TEMPERATURE_NAME);
                    tv = (TextView) findViewById(R.id.textViewSkinTemperatureValue);
                    tv.setText(String.valueOf(skinTemperature));
                    Log.d("Newhandler.handleMessage", "skinTemperature=" + skinTemperature);
                    break;

                case POSTURE:
                    int posture = msg.getData().getInt(BioHarnessConstants.POSTURE_NAME);
                    tv = (TextView) findViewById(R.id.textViewPostureValue);
                    tv.setText(String.valueOf(posture));
                    Log.d("Newhandler.handleMessage", "posture=" + posture);

                    break;

                case PEAK_ACCELERATION:
                    double peakAcceleration = msg.getData().getDouble(BioHarnessConstants.PEAK_ACCELERATION_NAME);
                    tv = (TextView) findViewById(R.id.textViewPeakAccelerationValue);
                    tv.setText(String.valueOf(peakAcceleration));
                    Log.d("Newhandler.handleMessage", "peakAcceleration=" + peakAcceleration);

                    break;


            }
        }

    };
    private void SendMsgtoRBL(String str) {
        if(mBluetoothLeService==null) {
            Log.d("RedBearLab","Service Not Established");
            return ;
        }
        BluetoothGattCharacteristic characteristic = map.get(RBLService.UUID_BLE_SHIELD_TX);


        byte b = 0x00;
        byte[] tmp = str.getBytes();
        byte[] tx = new byte[tmp.length + 1];
        //tx[0] = b;
        int i;
        for (i = 0; i < tmp.length ; i++) {
            tx[i] = tmp[i];
        }
        tx[i]=b;
        characteristic.setValue(tx);
        mBluetoothLeService.writeCharacteristic(characteristic);


    }

    private class BTBondReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            BluetoothDevice device = _btAdapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("Bond state", "BOND_STATED = " + device.getBondState());
        }
    }

    private class BTBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BTIntent", intent.getAction());
            Bundle b = intent.getExtras();
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
            Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
            try {
                BluetoothDevice device = _btAdapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
                Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[]{String.class});
                byte[] pin = (byte[]) m.invoke(device, "1234");
                m = device.getClass().getMethod("setPin", new Class[]{pin.getClass()});
                Object result = m.invoke(device, pin);
                Log.d("BTTest", result.toString());
            } catch (SecurityException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
