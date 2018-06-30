package com.transform;

import org.apache.commons.csv.CSVRecord;

public class RecordRaw {

	private final String address;
	private final String fullName;
	private final String zip;
	private final String notes;
	private final String timestamp;
	private final String fooDuration;
	private final String barDuration;

	public RecordRaw(CSVRecord record) {
		this.address = record.get("Address");
		this.zip = record.get("ZIP");
		this.fullName = record.get("FullName");
		this.notes = record.get("Notes");
		this.timestamp = record.get("Timestamp");
		this.fooDuration = record.get("FooDuration");
		this.barDuration = record.get("BarDuration");
	}

	public String getAddress() {
		return address;
	}

	public String getFullName() {
		return fullName;
	}

	public String getZip() {
		return zip;
	}

	public String getNotes() {
		return notes;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getFooDuration() {
		return fooDuration;
	}

	public String getBarDuration() {
		return barDuration;
	}

	@Override
	public String toString() {
		return "RecordRaw [address=" + address + ", fullName=" + fullName + ", zip=" + zip + ", notes=" + notes
				+ ", timestamp=" + timestamp + ", fooDuration=" + fooDuration + ", barDuration=" + barDuration + "]";
	}

}
