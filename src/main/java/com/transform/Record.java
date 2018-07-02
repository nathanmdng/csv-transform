package com.transform;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.transform.exception.InvalidDataException;

public class Record {

	private final String address;
	private final String fullName;
	private final String zip;
	private final String notes;
	private final ZonedDateTime timestamp;
	private final Duration fooDuration;
	private final Duration barDuration;

	public Record(RecordRaw raw) throws InvalidDataException {
		this.address = raw.getAddress();
		this.fullName = raw.getFullName().toUpperCase(Locale.ENGLISH);
		this.zip = StringUtils.leftPad(raw.getZip(), 5, "0");
		this.notes = raw.getNotes();
		this.timestamp = convertTimestamp(raw.getTimestamp());
		this.fooDuration = parseDuration(raw.getFooDuration());
		this.barDuration = parseDuration(raw.getBarDuration());
	}

	public List<String> headers() {
		return new ArrayList<String>(toMap().keySet());
	}

	public List<String> values() {
		return new ArrayList<String>(toMap().values());
	}

	private Map<String, String> toMap() {
		Map<String, String> out = new LinkedHashMap<>();
		out.put("Timestamp", timestamp.format(DateTimeFormatter.ISO_DATE_TIME));
		out.put("Address", address);
		out.put("FullName", fullName);
		out.put("ZIP", zip);
		out.put("Notes", notes);
		out.put("FooDuration", formatMillis(fooDuration.toMillis()));
		out.put("BarDuration", formatMillis(barDuration.toMillis()));
		out.put("TotalDuration", formatMillis(fooDuration.plus(barDuration).toMillis()));
		return out;
	}

	private String formatMillis(long millis) {
		double seconds = millis / 1000.0;
		return String.valueOf(seconds);
	}

	static ZonedDateTime convertTimestamp(String value) throws InvalidDataException {
		try {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yy h:mm:ss a");
			LocalDateTime inputDate = LocalDateTime.parse(value, dateFormatter);
			ZonedDateTime pacificTime = ZonedDateTime.of(inputDate, ZoneId.of("America/Los_Angeles"));
			ZonedDateTime easternTime = pacificTime.withZoneSameInstant(ZoneId.of("-05:00"));
			return easternTime;
		} catch (DateTimeParseException e) {
			throw new InvalidDataException("unable to parse timestamp " + value);			
		}
	}

	static Duration parseDuration(String value) throws InvalidDataException {
		Pattern pattern = Pattern.compile("(\\d+):(\\d+):(\\d+).(\\d+)");
		Matcher matcher = pattern.matcher(value);
		if (matcher.find()) {
			int hours = Integer.parseInt(matcher.group(1));
			int minutes = Integer.parseInt(matcher.group(2));
			int seconds = Integer.parseInt(matcher.group(3));
			int millis = Integer.parseInt(matcher.group(4));
			return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds).plusMillis(millis);
		}
		throw new InvalidDataException("unable to parse duration " + value);
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

	public ZonedDateTime getTimestamp() {
		return timestamp;
	}

	public Duration getFooDuration() {
		return fooDuration;
	}

	public Duration getBarDuration() {
		return barDuration;
	}

}
