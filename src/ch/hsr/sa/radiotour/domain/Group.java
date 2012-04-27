package ch.hsr.sa.radiotour.domain;

import java.util.Date;
import java.util.Observable;
import java.util.TreeSet;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Group")
public class Group extends Observable implements Comparable<Group> {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private final TreeSet<Integer> driverNumbers;
	@DatabaseField(dataType = DataType.BOOLEAN)
	private boolean isField = false;
	@DatabaseField(dataType = DataType.DATE)
	private Date handicapTime;
	@DatabaseField
	private int orderNumber;

	public Group() {
		this.driverNumbers = new TreeSet<Integer>();
	}

	public TreeSet<Integer> getDriverNumbers() {
		return driverNumbers;
	}

	public void removeDriverNumber(Integer driverNumber) {
		driverNumbers.remove(driverNumber);
	}

	public boolean isField() {
		return isField;
	}

	public void setField(boolean flag) {
		isField = flag;
	}

	public Date getHandicapTime() {
		return handicapTime;
	}

	public void setHandicapTime(Date handicapTime) {
		this.handicapTime = handicapTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public int compareTo(Group another) {
		return orderNumber - another.orderNumber;
	}

}
