package com.euler.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class GetStrings {
	final static DateTimeFormatter _TimeFormatter = DateTimeFormatter
			.ofPattern("MMM-dd hh:mm:ss");

	private static int _MaxWidthForString = 80;
	public static String getString(final long[] longVector) {
		final int vecLen = longVector.length;
		String s = "[";
		for (int k = 0, sLen = s.length(); k < vecLen; ++k) {
			final String ss = String.format("%d%c", longVector[k],
					(k == vecLen - 1 ? ']' : ','));
			final int ssLen = ss.length();
			if (sLen + ssLen > _MaxWidthForString) {
				s += "\n  ";
				sLen = 2;
			}
			s += ss;
			sLen += ssLen;
		}
		return s;
	}

	public static String getStringFromMillis(final long millis) {
		final Instant instant = Instant.ofEpochMilli(millis);
		final ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant,
				ZoneId.systemDefault());
		return zonedDateTime.format(_TimeFormatter);
	}

	public static String getCurrentTimeString() {
		return getStringFromMillis(System.currentTimeMillis());
	}

}
