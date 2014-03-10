package uk.co.alt236.btlescan.containers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.util.SparseArray;

public class AdRecordUtils {
	/* Helper functions to parse out common data payloads from an AD structure */
	static final String HEXES = "0123456789ABCDEF";

	public static String byteArrayToHexString(final byte[] array){
		final StringBuffer sb = new StringBuffer();
		boolean firstEntry = true;
		sb.append('[');

		for ( final byte b : array ) {
			if(!firstEntry){
				sb.append(", ");
			}
			sb.append(HEXES.charAt((b & 0xF0) >> 4));
			sb.append(HEXES.charAt((b & 0x0F)));
			firstEntry = false;
		}

		sb.append(']');
		return sb.toString();
	}

	public static String getRecordDataAsString(AdRecord nameRecord) {
		if(nameRecord == null){return new String();}
		return new String(nameRecord.getData());
	}

	public static byte[] getServiceData(AdRecord serviceData) {
		if (serviceData == null) {return null;}
		if (serviceData.getType() != AdRecord.TYPE_SERVICE_DATA) return null;

		final byte[] raw = serviceData.getData();
		//Chop out the uuid
		return Arrays.copyOfRange(raw, 2, raw.length);
	}

	public static int getServiceDataUuid(AdRecord serviceData) {
		if (serviceData == null) {return -1;}
		if (serviceData.getType() != AdRecord.TYPE_SERVICE_DATA) return -1;

		final byte[] raw = serviceData.getData();
		//Find UUID data in byte array
		int uuid = (raw[1] & 0xFF) << 8;
		uuid += (raw[0] & 0xFF);

		return uuid;
	}

	/*
	 * Read out all the AD structures from the raw scan record
	 */
	public static List<AdRecord> parseScanRecordAsList(byte[] scanRecord) {
		final List<AdRecord> records = new ArrayList<AdRecord>();

		int index = 0;
		while (index < scanRecord.length) {
			final int length = scanRecord[index++];
			//Done once we run out of records
			if (length == 0) break;

			int type = scanRecord[index];
			//Done if our record isn't a valid type
			if (type == 0) break;

			final byte[] data = Arrays.copyOfRange(scanRecord, index+1, index+length);

			records.add(new AdRecord(length, type, data));

			//Advance
			index += length;
		}

		return Collections.unmodifiableList(records);
	}

	@SuppressLint("UseSparseArrays")
	public static Map<Integer, AdRecord> parseScanRecordAsMap(byte[] scanRecord) {
		final Map<Integer, AdRecord> records = new HashMap<Integer, AdRecord>();

		int index = 0;
		while (index < scanRecord.length) {
			final int length = scanRecord[index++];
			//Done once we run out of records
			if (length == 0) break;

			int type = scanRecord[index];
			//Done if our record isn't a valid type
			if (type == 0) break;

			final byte[] data = Arrays.copyOfRange(scanRecord, index+1, index+length);

			records.put(type, new AdRecord(length, type, data));

			//Advance
			index += length;
		}

		return Collections.unmodifiableMap(records);
	}


	public static SparseArray<AdRecord> parseScanRecordAsSparseArray(byte[] scanRecord) {
		final SparseArray<AdRecord> records = new SparseArray<AdRecord>();

		int index = 0;
		while (index < scanRecord.length) {
			final int length = scanRecord[index++];
			//Done once we run out of records
			if (length == 0) break;

			int type = scanRecord[index];
			//Done if our record isn't a valid type
			if (type == 0) break;

			final byte[] data = Arrays.copyOfRange(scanRecord, index+1, index+length);

			records.put(type, new AdRecord(length, type, data));

			//Advance
			index += length;
		}

		return records;
	}
}