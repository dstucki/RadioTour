package ch.hsr.sa.radiotour.domain;

import java.util.TreeSet;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Team")
public class Team {

	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private TreeSet<Integer> driverNumbers;
	@DatabaseField
	private String name;

	public Team(String name) {
		this.name = name;
		this.driverNumbers = new TreeSet<Integer>();
	}

	public Team() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeSet<Integer> getDriverNumbers() {
		return driverNumbers;
	}

	public void setDriverNumbers(TreeSet<Integer> driverNumbers) {
		this.driverNumbers = driverNumbers;
	}

}
