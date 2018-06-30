package com.transform;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class CsvTransform {

	public static void main(String[] args) throws IOException {
		CsvTransform transform = new CsvTransform();
		transform.transformCsv();
	}
	
	private void transformCsv() throws IOException {
		List<Record> records = createRecords();
		FileWriter out = new FileWriter("src/test/resources/sample2.csv");
		if (!records.isEmpty()) {
			List<String> headers = records.get(0).headers();
			try (CSVPrinter printer = new CSVPrinter(out, 
					CSVFormat.DEFAULT.withHeader(headers.stream().toArray(String[]::new)))) {
				for (Record record : records) {
					printer.printRecord(record.values());
				}
			}
		}
	}
	
	private List<Record> createRecords() throws IOException {
		Reader in = new FileReader("src/test/resources/sample-with-broken-utf8.csv");
		Iterable<CSVRecord> inRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		List<Record> records = new ArrayList<>();
		for (CSVRecord csvRecord : inRecords) {
			RecordRaw rawRecord = new RecordRaw(csvRecord);
			records.add(new Record(rawRecord));
		}
		return records;
	}
}
