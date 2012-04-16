package ch.hsr.sa.radiotour.technicalservices.importer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class CSVReader {
	private final InputStreamReader in;
	private String CSV_SEPARATOR = ";";

	public CSVReader(InputStream in) {
		this.in = new InputStreamReader(in);
	}

	public ArrayList<String[]> readFile() {
		final ArrayList<String[]> returnList = new ArrayList<String[]>();
		Scanner scan = null;
		try {
			scan = new Scanner(in);
			if (scan.hasNext()) {
				scan.nextLine();// skip header
			}
			while (scan.hasNext()) {
				returnList.add(readLine(scan.nextLine()));
			}
		} finally {
			scan.close();
		}

		return returnList;
	}

	private String[] readLine(String line) {
		final StringTokenizer tokenizer = new StringTokenizer(line,
				CSV_SEPARATOR);
		final String[] array = new String[tokenizer.countTokens()];
		for (int i = 0; i < array.length; i++) {
			array[i] = tokenizer.nextToken();
		}

		return array;
	}

	public void setCSV_SEPARATOR(String csvSeparator) {
		this.CSV_SEPARATOR = csvSeparator;
	}
}
