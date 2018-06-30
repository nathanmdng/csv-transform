package com.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

public class CsvTransformTest {

	@Test
	public void transformCsv() throws IOException {
		Reader in = new FileReader("src/test/resources/sample-with-broken-utf8.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord csvRecord : records) {
			RecordRaw rawRecord = new RecordRaw(csvRecord);
			// System.out.println(new Record(rawRecord).toMap());
		}
	}
	
	@Test
	public void testDuration() {
		String test = "1:23:32.123";
		Duration duration = Record.parseDuration(test);
		assertEquals("PT1H23M32.123S", duration.toString());
	}
	
	@Test
	public void testConvertTimestamp() {
		// String test = "5/12/10 4:48:12 PM";
		String test = "12/31/16 11:59:59 PM";
		Record.convertTimestamp(test);
	}
	
}
