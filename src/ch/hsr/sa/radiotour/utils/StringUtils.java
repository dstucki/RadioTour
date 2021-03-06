package ch.hsr.sa.radiotour.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

public class StringUtils {

	public static String getTimeAsString(Date date) {
		final NumberFormat formatter = new DecimalFormat("00");
		if (date == null) {
			return "00:00";
		}
		final StringBuilder tempString = new StringBuilder();
		int additionalHours = 0;
		additionalHours += date.getDay() * 24;

		if (date.getHours() != 0) {
			tempString.append(formatter.format(additionalHours
					+ date.getHours()));
			tempString.append(':');
		}
		tempString.append(formatter.format(date.getMinutes()));
		tempString.append(':');
		tempString.append(formatter.format(date.getSeconds()));
		return tempString.toString();
	}

	public static String getTimeWithoutSecondsAsString(Date date) {

		final NumberFormat formatter = new DecimalFormat("00");
		if (date == null) {
			return "00:00";
		}
		int additionalHours = 0;
		additionalHours += date.getDay() * 24;

		final StringBuilder tempString = new StringBuilder();
		if (date.getHours() != 0) {
			tempString.append(formatter.format(additionalHours
					+ date.getHours()));
			tempString.append(':');
		}
		tempString.append(formatter.format(date.getMinutes()));

		return tempString.toString();
	}

	public static String getCountryFlag(String olympicCountryCode) {
		String isoCode = null;

		if ("SUI".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ch";
		}

		if ("AUS".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "au";
		}
		if ("RUS".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ru";
		}
		if ("SLO".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "si";
		}
		if ("SWE".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "se";
		}
		if ("CRO".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "hr";
		}
		if ("KAZ".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "kz";
		}
		if ("EST".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ee";
		}
		if ("NED".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "nl";
		}
		if ("ESP".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "es";
		}
		if ("VEN".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ve";
		}
		if ("FIN".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "fi";
		}
		if ("BRA".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "br";
		}
		if ("USA".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "us";
		}
		if ("AUT".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "at";
		}
		if ("POL".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "pl";
		}
		if ("LTU".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "lt";
		}
		if ("LIE".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "li";
		}
		if ("UZB".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "uz";
		}
		if ("GER".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "de";
		}
		if ("FRA".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "fr";
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
		if ("IRL".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "ir";
		}
		if ("LET".equalsIgnoreCase(olympicCountryCode)) {
			isoCode = "lv";
		}

		return "country_" + isoCode;
	}
}
