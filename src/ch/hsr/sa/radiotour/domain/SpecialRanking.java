package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class SpecialRanking {
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField()
	private String name;

	public SpecialRanking(String name) {
		this.name = name;
	}

	public SpecialRanking() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
