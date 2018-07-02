package com.transform;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.CharSequenceReader;

import com.transform.exception.InvalidDataException;

public class CsvTransform {

	private static final String INPUT_FILE = "src/test/resources/sample-with-broken-utf8.csv";
	private static final String OUTPUT_FILE = "src/test/resources/output.csv";

	public static void main(String[] args) throws IOException {
		CsvTransform transform = new CsvTransform();
		transform.transformCsv();
	}
		
	private void transformCsv() throws IOException {
		List<Record> records = createRecords();
		FileWriter out = new FileWriter(OUTPUT_FILE);
		if (!records.isEmpty()) {
			List<String> headers = records.get(0).headers();
			try (CSVPrinter printer = new CSVPrinter(out, 
					CSVFormat.RFC4180.withHeader(headers.stream().toArray(String[]::new)))) {
				for (Record record : records) {
					printer.printRecord(record.values());
				}
			}
		}
	}
	
	private String sanitize() throws IOException {
		CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
		utf8Decoder.onMalformedInput(CodingErrorAction.REPLACE);
		utf8Decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		utf8Decoder.replaceWith("?");
		byte[] data = Files.readAllBytes(Paths.get(INPUT_FILE));
		ByteBuffer input = ByteBuffer.wrap(data);
		CharBuffer output = utf8Decoder.decode(input);
		return output.toString();
	}

	
	private List<Record> createRecords() throws IOException {
		String cleanUtf8 = sanitize();
		CharSequenceReader reader = new CharSequenceReader(cleanUtf8);
		Iterable<CSVRecord> inRecords = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
		List<Record> records = new ArrayList<>();
		for (CSVRecord csvRecord : inRecords) {
			try {
				RecordRaw rawRecord = new RecordRaw(csvRecord);
				records.add(new Record(rawRecord));
			} catch (InvalidDataException e) {
				e.printStackTrace();
			}
		}
		return records;
	}
}
