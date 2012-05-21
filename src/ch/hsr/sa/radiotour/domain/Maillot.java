package ch.hsr.sa.radiotour.domain;

import com.j256.ormlite.field.DatabaseField;

public class Maillot {

	@DatabaseField(id = true)
	private int id;
	@DatabaseField
	private String maillot;
	@DatabaseField
	private int color;
	@DatabaseField
	private int points;
	@DatabaseField
	private long time;

	public Maillot(int id, String maillot, int color, int points, long time) {
		super();
		this.id = id;
		this.maillot = maillot;
		this.color = color;
		this.points = points;
		this.time = time;
	}

	public Maillot() {

	}

	public int getId() {
		return id;
	}

	public String getMaillot() {
		return maillot;
	}

	public int getPoints() {
		return points;
	}

	public long getTime() {
		return time;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMaillot(String maillot) {
		this.maillot = maillot;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
