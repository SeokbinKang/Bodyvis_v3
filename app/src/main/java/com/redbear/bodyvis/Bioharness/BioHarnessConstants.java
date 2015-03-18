package com.redbear.bodyvis.Bioharness;

public final class BioHarnessConstants {

	private BioHarnessConstants(){}

    public final static String HEART_RATE_NAME = "HeartRate";
    public final static String RESPIRATION_RATE_NAME = "RespirationRate";
    public final static String SKIN_TEMPERATURE_NAME = "SkinTemperature";
    public final static String POSTURE_NAME = "Posture";
    public final static String PEAK_ACCELERATION_NAME = "PeakAcceleration";
    
    // A general packet contains a summary of everything, including:
	//    private byte _SequenceNum;
	//    private int _TSYear;
	//    private byte _TSMonth;
	//    private byte _TSDay;
	//    private long _MsOfDay;
	//    private int _HeartRate;
	//    private double _RespirationRate;
	//    private double _SkinTemperature;
	//    private int _Posture;
	//    private double _VMU;
	//    private double _PeakAcceleration;
	//    private double _BatteryVoltage;
	//    private double _BreathingWaveAmpl;
	//    private double _ECGAmplitude;
	//    private double _ECGNoise;
	//    private double _XAxis_Accn_Min;
	//    private double _XAxis_Accn_Peak;
	//    private double _YAxis_Accn_Min;
	//    private double _YAxis_Accn_Peak;
	//    private double _ZAxis_Accn_Min;
	//    private double _ZAxis_Accn_Peak;
	//    private int _ZephyrSysChan;
	//    private int _GSR;
	//    private byte _ROGStatus;
	//    private byte _AlarmSts;
	//    private byte _WornStatus;
	//    private byte _UserIntfBtnStatus;
	//    private byte _BHSigLowStatus;
	//    private byte _BHSensConnStatus;
	//    private byte _BatteryStatus;
    public final static int GENERAL_PACKET_ID = 0x20; //32
    
    public final static int BREATHING_PACKET_ID = 0x21; //33
    
    public final static int ECG_PACKET_ID = 0x22; //34
    
    // R to R interval is the time between heart beats; used to calculate heart rate
    // see http://courses.kcumb.edu/physio/ecg%20primer/normecgcalcs.htm
    public final static int RTOR_PACKET_ID = 0x24; //35
    
    public final static int ACCELEROMETER_PACKET_ID = 0x2A; //42
    
    // A summary data packet seems similar to a general packet but contains
    // different data
	//    private byte _SequenceNum;
	//    private int _TSYear;
	//    private byte _TSMonth;
	//    private byte _TSDay;
	//    private long _MsOfDay;
	//    private byte _VersionNumber;
	//    private int _HeartRate;
	//    private double _RespirationRate;
	//    private double _SkinTemperature;
	//    private int _Posture;
	//    private double _Activity;
	//    private double _PeakAcceleration;
	//    private double _BatteryVoltage;
	//    private byte _BatteryStatus;
	//    private double _BreathingWaveAmpl;
	//    private double _BreathingWaveNoise;
	//    private byte _BreathingRateConfidence;
	//    private double _ECGAmplitude;
	//    private double _ECGNoise;
	//    private byte _HeartRateConfidence;
	//    private int _HRV;
	//    private byte _SystemConfidence;
	//    private int _GSR;
	//    private byte _ROGStatus;
	//    private short _ROGTime;
	//    private double _Vertical_AxisAccnMin;
	//    private double _Vertical_AxisAccnPeak;
	//    private double _Lateral_AxisAccnMin;
	//    private double _Lateral_AxisAccnPeak;
	//    private double _Sagittal_AxisAccnMin;
	//    private double _Sagittal_AxisAccnPeak;
	//    private double _Device_Internal_Temperature;
	//    private byte _Status_Worn_Det_Level;
	//    private byte _Status_Button_Press_Det_Flag;
	//    private byte _Status_Fitted_to_Garment_Flag;
	//    private byte _Status_Heart_Rate_Unreliable_Flag;
	//    private short _LinkQuality;
	//    private byte _RSSI;
	//    private short _TxPower;
	//    private short _Reserved;
    public final static int SUMMARY_PACKET_ID = 0x2B; //43
    public final static int EVENT_DATA_PACKET_ID = 0x2B; //43
	
}
