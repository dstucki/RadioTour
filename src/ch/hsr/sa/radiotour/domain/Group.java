package ch.hsr.sa.radiotour.domain;

import java.util.TreeSet;

public class Group {
	private static int static_id = 0;
	private static int field_id = 0;
	private final TreeSet<Integer> driverNumbers;
	private final int id;

	public Group() {
		this.id = static_id++;
		this.driverNumbers = new TreeSet<Integer>();
	}

	public static int getStatic_id() {
		return static_id;
	}

	public boolean isField() {
		return field_id == this.id;
	}

	public void setAsField() {
		field_id = this.id;
	}

	public TreeSet<Integer> getDriverNumbers() {
		return driverNumbers;
	}

	public int getId() {
		return id;
	}

}
