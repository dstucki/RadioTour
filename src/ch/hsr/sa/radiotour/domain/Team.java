package ch.hsr.sa.radiotour.domain;

import java.util.ArrayList;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Team")
public class Team {

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private ArrayList<Integer> driverNumbers;
	@DatabaseField
	private String name;

	public Team(String name) {
		this.name = name;
		this.driverNumbers = new ArrayList<Integer>();
	}

	public Team() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Integer> getDriverNumbers() {
		return driverNumbers;
	}

	public void setDriverNumbers(ArrayList<Integer> driverNumbers) {
		this.driverNumbers = driverNumbers;
	}

}
