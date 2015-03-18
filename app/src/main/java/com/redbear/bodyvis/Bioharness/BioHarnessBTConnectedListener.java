package com.redbear.bodyvis.Bioharness;

import zephyr.android.BioHarnessBT.BTClient;
import zephyr.android.BioHarnessBT.ConnectListenerImpl;
import zephyr.android.BioHarnessBT.ConnectedEvent;
import zephyr.android.BioHarnessBT.PacketTypeRequest;
import zephyr.android.BioHarnessBT.ZephyrPacketArgs;
import zephyr.android.BioHarnessBT.ZephyrPacketEvent;
import zephyr.android.BioHarnessBT.ZephyrPacketListener;
import zephyr.android.BioHarnessBT.ZephyrProtocol;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BioHarnessBTConnectedListener extends ConnectListenerImpl {
	private Handler _oldHandler;
	private Handler _aNewHandler; 

		
	private int GP_HANDLER_ID = 0x20;
	
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x101;
	private final int SKIN_TEMPERATURE = 0x102;
	private final int POSTURE = 0x103;
	private final int PEAK_ACCLERATION = 0x104;
	
	/*Creating the different Objects for different types of Packets*/
	private GeneralPacketInfo GPInfo = new GeneralPacketInfo();
	private ECGPacketInfo ECGInfoPacket = new ECGPacketInfo();
	private BreathingPacketInfo BreathingInfoPacket = new  BreathingPacketInfo();
	private RtoRPacketInfo RtoRInfoPacket = new RtoRPacketInfo();
	private AccelerometerPacketInfo AccInfoPacket = new AccelerometerPacketInfo();
	private SummaryPacketInfo SummaryInfoPacket = new SummaryPacketInfo();
	
	private PacketTypeRequest RqPacketType = new PacketTypeRequest();
	
	public BioHarnessBTConnectedListener(Handler handler, Handler _NewHandler) {
		super(handler, null);
		_oldHandler= handler;
		_aNewHandler = _NewHandler;

		//  Auto-generated constructor stub

	}
	
	public void Connected(ConnectedEvent<BTClient> eventArgs) {
		System.out.println(String.format("Connected to BioHarness %s.", eventArgs.getSource().getDevice().getName()));
		/*Use this object to enable or disable the different Packet types*/
		RqPacketType.GP_ENABLE = true;
		RqPacketType.BREATHING_ENABLE = true;
		RqPacketType.LOGGING_ENABLE = true;
		
		//Creates a new ZephyrProtocol object and passes it the BTComms object
		ZephyrProtocol _protocol = new ZephyrProtocol(eventArgs.getSource().getComms(), RqPacketType);
		
		_protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
			public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
				ZephyrPacketArgs zephyrPacketArgs = eventArgs.getPacket();
									
				byte CRCFailStatus = zephyrPacketArgs.getCRCStatus();
				byte numReceivedBytes = zephyrPacketArgs.getNumRvcdBytes() ;
				int packetMessageId = zephyrPacketArgs.getMsgID();
				byte [] DataArray = zephyrPacketArgs.getBytes();
				
				switch (packetMessageId)
				{
					case BioHarnessConstants.GENERAL_PACKET_ID:				
					
						//***************Displaying the Heart Rate********************************
						int HRate =  GPInfo.GetHeartRate(DataArray);
						Message text1 = _aNewHandler.obtainMessage(HEART_RATE);
						Bundle b1 = new Bundle();
						b1.putInt(BioHarnessConstants.HEART_RATE_NAME, HRate);
						text1.setData(b1);
						_aNewHandler.sendMessage(text1);
						Log.d("ZephyrPacketListener.ReceivedPacket()", "Heart Rate is "+ HRate);
	
						//***************Displaying the Respiration Rate********************************
						double RespRate = GPInfo.GetRespirationRate(DataArray);
						text1 = _aNewHandler.obtainMessage(RESPIRATION_RATE);
						b1.putDouble(BioHarnessConstants.RESPIRATION_RATE_NAME, RespRate);
						text1.setData(b1);
						_aNewHandler.sendMessage(text1);
						Log.d("ZephyrPacketListener.ReceivedPacket()", "Respiration Rate is "+ RespRate);
						
						//***************Displaying the Skin Temperature*******************************
						double SkinTempDbl = GPInfo.GetSkinTemperature(DataArray);
						text1 = _aNewHandler.obtainMessage(SKIN_TEMPERATURE);
						//Bundle b1 = new Bundle();
						b1.putDouble(BioHarnessConstants.SKIN_TEMPERATURE_NAME, SkinTempDbl);
						text1.setData(b1);
						_aNewHandler.sendMessage(text1);
						Log.d("ZephyrPacketListener.ReceivedPacket()", "Skin Temperature is "+ SkinTempDbl);
						
						//***************Displaying the Posture******************************************					
						int PostureInt = GPInfo.GetPosture(DataArray);
						text1 = _aNewHandler.obtainMessage(POSTURE);
						b1.putInt(BioHarnessConstants.POSTURE_NAME, PostureInt);
						text1.setData(b1);
						_aNewHandler.sendMessage(text1);
						Log.d("ZephyrPacketListener.ReceivedPacket()","Posture is "+ PostureInt);	
						
						//***************Displaying the Peak Acceleration******************************************
						double PeakAccDbl = GPInfo.GetPeakAcceleration(DataArray);
						text1 = _aNewHandler.obtainMessage(PEAK_ACCLERATION);
						b1.putDouble(BioHarnessConstants.PEAK_ACCELERATION_NAME, PeakAccDbl);
						text1.setData(b1);
						_aNewHandler.sendMessage(text1);
						Log.d("ZephyrPacketListener.ReceivedPacket()", "Peak Acceleration is "+ PeakAccDbl);	
						
						// The ROG status stands for "Red, Orange, Green" and is a measure of the wearer's
						// status based on analyzing heart rate, breathing rate and activity levels. If heart rate, for example,
						// is too high or too low, it will drop into orange or red status
						byte ROGStatus = GPInfo.GetROGStatus(DataArray);
						Log.d("ZephyrPacketListener.ReceivedPacket()", "ROG Status is "+ ROGStatus);
						break;
						
					case BioHarnessConstants.BREATHING_PACKET_ID:
						/*Do what you want. Printing Sequence Number for now*/
						System.out.println("Breathing Packet Sequence Number is "+BreathingInfoPacket.GetSeqNum(DataArray));
						break;
					case BioHarnessConstants.ECG_PACKET_ID:
						/*Do what you want. Printing Sequence Number for now*/
						System.out.println("ECG Packet Sequence Number is "+ECGInfoPacket.GetSeqNum(DataArray));
						break;
					case BioHarnessConstants.RTOR_PACKET_ID:
						/*Do what you want. Printing Sequence Number for now*/
						System.out.println("R to R Packet Sequence Number is "+RtoRInfoPacket.GetSeqNum(DataArray));
						break;
					case BioHarnessConstants.ACCELEROMETER_PACKET_ID:
						/*Do what you want. Printing Sequence Number for now*/
						System.out.println("Accelerometry Packet Sequence Number is "+AccInfoPacket.GetSeqNum(DataArray));
						break;
					case BioHarnessConstants.SUMMARY_PACKET_ID:
						/*Do what you want. Printing Sequence Number for now*/
						System.out.println("Summary Packet Sequence Number is "+SummaryInfoPacket.GetSeqNum(DataArray));
						break;
						
				}
			}
		});
	}
}
