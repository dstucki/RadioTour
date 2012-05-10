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

	public static String getCountryFlag(String olympicCountryCode) {
		String isoCode = null;

		if ("SUI".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ch";
		}
		if ("GER".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "de";
		}
		if ("ERI".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "er";
		}
		if ("KOR".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "kr";
		}
		if ("HKG".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "hk";
		}
		if ("ETH".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "et";
		}
		if ("IRI".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ir";
		}
		if ("THA".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "th";
		}
		if ("ITA".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "it";
		}
		if ("LUX".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "lu";
		}
		if ("GRE".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "gr";
		}
		if ("BEL".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "be";
		}

		return "country_" + isoCode;
	}
}