package com.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import com.transform.exception.InvalidDataException;

public class CsvTransformTest {

	@Test
	public void transformCsv() throws IOException {
		Reader in = new FileReader("src/main/resources/sample-with-broken-utf8.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord csvRecord : records) {
			RecordRaw rawRecord = new RecordRaw(csvRecord);
			try {
				Record record = new Record(rawRecord);
				assertNotNull(record.getAddress());
				assertNotNull(record.getBarDuration());
				assertNotNull(record.getFooDuration());
				assertNotNull(record.getFullName());
				assertNotNull(record.getTimestamp());
			} catch (InvalidDataException e) {
				fail();
			}
		}
	}
	
	@Test
	public void testDuration() throws InvalidDataException {
		String test = "1:23:32.123";
		Duration duration = Record.parseDuration(test);
		assertEquals("PT1H23M32.123S", duration.toString());
	}
	
	@Test
	public void testConvertTimestamp() throws InvalidDataException {
		String test = "12/31/16 11:59:59 PM";
		ZonedDateTime convertTime = Record.convertTimestamp(test);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/d/yy h:mm:ss a");
		// should be 3 hours ahead
		assertEquals("1/1/17 2:59:59 AM", dateFormatter.format(convertTime));
	}
	
}
