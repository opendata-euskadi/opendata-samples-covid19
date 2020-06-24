package euskadi.opendata.covid19.util;

import java.time.Year;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.Dates;
import r01f.util.types.Strings;
import r01f.util.types.Strings.StringIsContainedWrapper;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class COVID19DateUtils {
	/**
	 * Parses a date in dd-MMM. format (ie: 12-abr.)
	 * @param date
	 * @return
	 */
	public static Date parseDate(final String date) {
		Date outDate = null;
		Pattern p = Pattern.compile("([0-9]+)-([a-zA-Z]+)\\.");
		Matcher m = p.matcher(date);
		if (m.find()) {
			int dayOfMonth = Integer.parseInt(m.group(1));
			int monthOfYear = _monthOfYear(m.group(2));
			
			String dateStr = Strings.customized("{}/{}/{} 23:00",
												Year.now().getValue(),
												Strings.leftPad(Integer.toString(monthOfYear),2,'0'),
												Strings.leftPad(Integer.toString(dayOfMonth),2,'0'));
			outDate = Dates.fromFormatedString(dateStr,"yyyy/MM/dd HH:mm"); 
		} else {
			throw new IllegalArgumentException(date + " is NOT a legal date!");
		}
		return outDate;
	}
	private static int _monthOfYear(final String month) {
		int out = -1;
		StringIsContainedWrapper w = Strings.isContainedWrapper(month);
		if (w.containsAnyIgnoringCase("ene")) {
			out = 1;
		} else if (w.containsAny("feb")) {
			out = 2;
		} else if (w.containsAny("mar")) {
			out = 3;
		} else if (w.containsAny("abr")) {
			out = 4;
		} else if (w.containsAny("may")) {
			out = 5;
		} else if (w.containsAny("jun")) {
			out = 6;
		} else if (w.containsAny("jul")) {
			out = 7;
		} else if (w.containsAny("ago")) {
			out = 8;
		} else if (w.containsAny("sep")) {
			out = 9;
		} else if (w.containsAny("oct")) {
			out = 10;
		} else if (w.containsAny("nov")) {
			out = 11;
		} else if (w.containsAny("dic")) {
			out = 12;
		}
		if (out == -1) throw new IllegalArgumentException(month + " is NOT a valid month!!!");
		return out;
	}
	public static void main(final String[] args) {
		System.out.println(COVID19DateUtils.parseDate("13-abr."));
	}
}
