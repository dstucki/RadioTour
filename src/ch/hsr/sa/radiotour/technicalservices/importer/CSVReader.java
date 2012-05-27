package ch.hsr.sa.radiotour.technicalservices.importer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.util.Log;

public class CSVReader {
	private InputStream in = null;
	private final String CSV_SEPARATOR = ";";

	public CSVReader(InputStream in) {
		this.in = in;
	}

	public void setInputStream(InputStream in) {
		this.in = in;
	}

	public ArrayList<String[]> readFile() {
		final ArrayList<String[]> returnList = new ArrayList<String[]>();
		Scanner scan = null;
		try {

			scan = new Scanner(in, "UTF8");
			if (scan.hasNext()) {
				scan.nextLine();// skip header
			}
			while (scan.hasNext()) {
				returnList.add(readLine(scan.nextLine()));
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), e.getMessage());
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

}
