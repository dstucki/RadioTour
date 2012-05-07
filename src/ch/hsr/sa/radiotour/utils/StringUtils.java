package ch.hsr.sa.radiotour.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class StringUtils {

	public static String getTimeAsString(Date date) {
		final NumberFormat formatter = new DecimalFormat("00");
		if (date == null) {
			return "00:00:00";
		}
		final StringBuilder tempString = new StringBuilder();
		tempString.append(formatter.format(date.getHours()));
		tempString.append(":");
		tempString.append(formatter.format(date.getMinutes()));
		tempString.append(":");
		tempString.append(formatter.format(date.getSeconds()));
		return tempString.toString();
	}
}
