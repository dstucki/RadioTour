package ch.hsr.sa.radiotour.technicalservices.importer;

import ch.hsr.sa.radiotour.domain.Stage;

public class StageImport implements ImportIF<Stage> {
	/*
	 * The int values represent the column index in the csvthe indexes
	 * arezerobased
	 */
	private final int start = 0;
	private final int destination = 1;
	private final int distance = 2;

	@Override
	public Stage convertTo(String[] strings) {
		return new Stage(strings[start], strings[destination],
				Double.valueOf(strings[distance]));
	}
}
