/**************************************************************************
 *
 * Filename:    PS3000ACLibrary.java
 *
 * Author:      HSM
 *
 * Description: 
 *  This interface defines functions and enumerations from the ps3000aApi.h
 * C header file for PicoScope 3000 series oscilloscopes using the ps3000a 
 * library API functions.
 * 
 * Copyright © 2015-2017 Pico Technology Ltd. See LICENSE file for terms.
 * 
 ***************************************************************************/

package com.picotech.picoscope.ps3000a.jna;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.*;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.ShortByReference;

public interface PS3000ACLibrary extends Library {

	PS3000ACLibrary INSTANCE = (PS3000ACLibrary) Native.loadLibrary("ps3000a", PS3000ACLibrary.class);

	// C prototype definition :
	// uint32_t ps3000aEnumerateUnits(int16_t * count, int8_t * serials, int16_t
	// serialLth)
	int ps3000aEnumerateUnits(ShortByReference count, byte[] serials, ShortByReference serialLth);

	// C prototype definition :
	int ps3000aOpenUnit(ShortByReference handle, String serial);

	// C prototype definition
	// uint32_t ps3000aCloseUnit(int16_t handle);
	int ps3000aCloseUnit(short handle);

	// uint32_t ps3000aGetUnitInfo(int16_t handle, int8_t *string, int16_t
	// stringLength,
	// int16_t *requiredSize, PICO_INFO info)
	int ps3000aGetUnitInfo(short handle, byte[] string, short stringLength, ShortByReference requiredSize, int info);

	// C prototype definition
	// uint32_t ps3000aChangePowerSource(int16_t handle, PICO_STATUS powerstate)
	int ps3000aChangePowerSource(short handle, int powerstate);

	// uint32_t ps3000aSetChannel(int16_t handle, PS3000A_CHANNEL channel, short
	// enabled, PS3000A_COUPLING type, PS3000A_RANGE range, float analogueOffset)
	int ps3000aSetChannel(short handle, int channel, short enabled, int type, int range, float analogueOffset);

	// uint32_t ps3000aGetTimebase2(int16_t handle, uint32_t timebase, int32_t
	// noSamples, float *timeIntervalNanoseconds, int16_t oversample, int32_t
	// *maxSamples, uint32_t segmentIndex)
	int ps3000aGetTimebase2(short handle, int timebase, int noSamples, FloatByReference timeIntervalNanoseconds,
			short oversample, IntByReference maxSamples, int segmentIndex);

	// uint32_t ps3000aSetDataBuffer(int16_t handle, PS3000A_CHANNEL channelOrPort,
	// int16_t *buffer, int32_t bufferLth, uint32_t segmentIndex, PS3000A_RATIO_MODE
	// mode)
	int ps3000aSetDataBuffer(short handle, int channelOrPort, Pointer buffer, int bufferLength, int segmentIndex,
			int ratioMode);

	/*
	 * ========================================================= Functions missing
	 * from the original (minimalist) example
	 * =========================================================
	 */

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetDigitalPort) ( int16_t handle,
	 * PS3000A_DIGITAL_PORT port, int16_t enabled, int16_t logicLevel );
	 * 
	 * 
	 */

	int ps3000aSetDigitalPort(short handle, int port, short enabled, short logicLevel);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aIsReady) ( int16_t handle,
	 * int16_t *ready );
	 * 
	 * 
	 */

	int ps3000aIsReady(short handle, ShortByReference ready);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aRunBlock) ( int16_t handle,
	 * int32_t noOfPreTriggerSamples, int32_t noOfPostTriggerSamples, uint32_t
	 * timebase, int16_t oversample, int32_t *timeIndisposedMs, uint32_t
	 * segmentIndex, ps3000aBlockReady lpReady, void *pParameter );
	 * 
	 * 
	 */

	int ps3000aRunBlock(short handle, int noOfPreTriggerSamples, int noOfPostTriggerSamples, int timebase,
			short oversample, IntByReference timeIndisposedMs, int segmentIndex, Pointer lpReady, Pointer pParameter);

	/*
	 * 
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetTriggerChannelProperties) (
	 * int16_t handle, PS3000A_TRIGGER_CHANNEL_PROPERTIES *channelProperties,
	 * int16_t nChannelProperties, int16_t auxOutputEnable, int32_t
	 * autoTriggerMilliseconds );
	 * 
	 */

	int ps3000aSetTriggerChannelProperties(short handle, PS3000A_TRIGGER_CHANNEL_PROPERTIES channelProperties,
			short nChannelProperties, short auxOutputEnable, int autoTriggerMilliseconds);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetTriggerChannelConditionsV2) (
	 * int16_t handle, PS3000A_TRIGGER_CONDITIONS_V2 *conditions, int16_t
	 * nConditions );
	 * 
	 */

	int ps3000aSetTriggerChannelConditionsV2(short handle, PS3000A_TRIGGER_CONDITIONS_V2 triggerConditions,
			short nConditions);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetTriggerChannelDirections) (
	 * int16_t handle, PS3000A_THRESHOLD_DIRECTION channelA,
	 * PS3000A_THRESHOLD_DIRECTION channelB, PS3000A_THRESHOLD_DIRECTION channelC,
	 * PS3000A_THRESHOLD_DIRECTION channelD, PS3000A_THRESHOLD_DIRECTION ext,
	 * PS3000A_THRESHOLD_DIRECTION aux );
	 * 
	 */

	int ps3000aSetTriggerChannelDirections(short handle, int channelA, int channelB, int channelC, int channelD,
			int ext, int aux);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetTriggerDelay) ( int16_t
	 * handle, uint32_t delay );
	 * 
	 * delay in percentage of buffer (?)
	 * 
	 */

	int ps3000aSetTriggerDelay(short handle, int delay);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetPulseWidthQualifierV2) (
	 * int16_t handle, PS3000A_PWQ_CONDITIONS_V2 *conditions, int16_t nConditions,
	 * PS3000A_THRESHOLD_DIRECTION direction, uint32_t lower, uint32_t upper,
	 * PS3000A_PULSE_WIDTH_TYPE type );
	 * 
	 */

	int ps3000aSetPulseWidthQualifierV2(short handle, PS3000A_PWQ_CONDITIONS_V2 pwqConditions, short nConditions,
			int direction, int lower, int upper, int type);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aSetSimpleTrigger) ( int16_t
	 * handle, int16_t enable, PS3000A_CHANNEL source, int16_t threshold,
	 * PS3000A_THRESHOLD_DIRECTION direction, uint32_t delay, int16_t autoTrigger_ms
	 * );
	 * 
	 * 
	 */

	int ps3000aSetSimpleTrigger(short handle, short enable, int source, short threshold, int direction, int delay,
			short autoTrigger_ms);

	/*
	 * PREF0 PREF1 PICO_STATUS PREF2 PREF3 (ps3000aGetValues) ( int16_t handle,
	 * uint32_t startIndex, uint32_t *noOfSamples, uint32_t downSampleRatio,
	 * PS3000A_RATIO_MODE downSampleRatioMode, uint32_t segmentIndex, int16_t
	 * *overflow );
	 * 
	 * 
	 */

	int ps3000aGetValues(short handle, int startIndex, IntByReference noOfSamples, int downSampleRatio,
			int downSampleRatioMode, int segmentIndex, ShortByReference overflow);

	// Enumerations
	// ============

	public enum PS3000AChannel {
		PS3000A_CHANNEL_A, PS3000A_CHANNEL_B, PS3000A_CHANNEL_C, PS3000A_CHANNEL_D, PS3000A_EXTERNAL
	}

	public enum PS3000ACoupling {
		PS3000A_AC, PS3000A_DC
	}

	public enum PS3000ARange {
		PS3000A_10MV, PS3000A_20MV, PS3000A_50MV, PS3000A_100MV, PS3000A_200MV, PS3000A_500MV, PS3000A_1V, PS3000A_2V,
		PS3000A_5V, PS3000A_10V, PS3000A_20V, PS3000A_50V, PS3000A_MAX_RANGES
	}

	public enum PS3000ARatioMode {
		PS3000A_RATIO_MODE_NONE(0), PS3000A_RATIO_MODE_AGGREGATE(1), PS3000A_RATIO_MODE_DECIMATE(2),
		PS3000A_RATIO_MODE_AVERAGE(4);

		private final int ratio;

		PS3000ARatioMode(int ratio) {
			this.ratio = ratio;
		}
	}

	public enum PS3000A_CHANNEL {
		PS3000A_CHANNEL_A, PS3000A_CHANNEL_B, PS3000A_CHANNEL_C, PS3000A_CHANNEL_D, PS3000A_EXTERNAL,
		PS3000A_TRIGGER_AUX, PS3000A_MAX_TRIGGER_SOURCES
	}

	public enum PS3000A_THRESHOLD_MODE {
		PS3000A_LEVEL, PS3000A_WINDOW
	}

	public enum PS3000A_TRIGGER_STATE {
		PS3000A_CONDITION_DONT_CARE, PS3000A_CONDITION_TRUE, PS3000A_CONDITION_FALSE, PS3000A_CONDITION_MAX
	};

	public enum PS3000A_THRESHOLD_DIRECTION {
		PS3000A_ABOVE, // using upper threshold
		PS3000A_BELOW, // using upper threshold
		PS3000A_RISING, // using upper threshold
		PS3000A_FALLING, // using upper threshold
		PS3000A_RISING_OR_FALLING, // using both threshold
		PS3000A_ABOVE_LOWER, // using lower threshold
		PS3000A_BELOW_LOWER, // using lower threshold
		PS3000A_RISING_LOWER, // using lower threshold
		PS3000A_FALLING_LOWER, // using lower threshold
		PS3000A_POSITIVE_RUNT, PS3000A_NEGATIVE_RUNT,
	};

	public enum PS3000A_PULSE_WIDTH_TYPE {
		PS3000A_PW_TYPE_NONE, PS3000A_PW_TYPE_LESS_THAN, PS3000A_PW_TYPE_GREATER_THAN, PS3000A_PW_TYPE_IN_RANGE,
		PS3000A_PW_TYPE_OUT_OF_RANGE
	};

	// Structures
	// ==========

	/*
	 * typedef struct tPS3000ATriggerChannelProperties { int16_t thresholdUpper;
	 * uint16_t thresholdUpperHysteresis; int16_t thresholdLower; uint16_t
	 * thresholdLowerHysteresis; PS3000A_CHANNEL channel; PS3000A_THRESHOLD_MODE
	 * thresholdMode; } PS3000A_TRIGGER_CHANNEL_PROPERTIES;
	 */
	@FieldOrder({ "thresholdUpper", "thresholdUpperHysteresis", "thresholdLower", "thresholdLowerHysteresis", "channel",
			"thresholdMode" })
	public static class PS3000A_TRIGGER_CHANNEL_PROPERTIES extends Structure {
		public short thresholdUpper;
		public short thresholdUpperHysteresis;
		public short thresholdLower;
		public short thresholdLowerHysteresis;
		public int channel;
		public int thresholdMode;
	}

	/*
	 * #pragma pack(push,1) typedef struct tPS3000ATriggerConditionsV2 {
	 * PS3000A_TRIGGER_STATE channelA; PS3000A_TRIGGER_STATE channelB;
	 * PS3000A_TRIGGER_STATE channelC; PS3000A_TRIGGER_STATE channelD;
	 * PS3000A_TRIGGER_STATE external; PS3000A_TRIGGER_STATE aux;
	 * PS3000A_TRIGGER_STATE pulseWidthQualifier; PS3000A_TRIGGER_STATE digital; }
	 * PS3000A_TRIGGER_CONDITIONS_V2; #pragma pack(pop)
	 * 
	 */

	@FieldOrder({ "channelA", "channelB", "channelC", "channelD", "external", "aux", "pulseWidthQualifier", "digital" })
	public static class PS3000A_TRIGGER_CONDITIONS_V2 extends Structure {
		public int channelA;
		public int channelB;
		public int channelC;
		public int channelD;
		public int external;
		public int aux;
		public int pulseWidthQualifier;
		public int digital;
	}

	/*
	 * typedef struct tPS3000APwqConditionsV2 { PS3000A_TRIGGER_STATE channelA;
	 * PS3000A_TRIGGER_STATE channelB; PS3000A_TRIGGER_STATE channelC;
	 * PS3000A_TRIGGER_STATE channelD; PS3000A_TRIGGER_STATE external;
	 * PS3000A_TRIGGER_STATE aux; PS3000A_TRIGGER_STATE digital; }
	 * PS3000A_PWQ_CONDITIONS_V2;
	 * 
	 */

	@FieldOrder({ "channelA", "channelB", "channelC", "channelD", "external", "aux", "digital" })
	public static class PS3000A_PWQ_CONDITIONS_V2 extends Structure {
		public int channelA;
		public int channelB;
		public int channelC;
		public int channelD;
		public int external;
		public int aux;
		public int digital;
	}

}
