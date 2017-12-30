package cc.dhandho.input;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import cc.dhandho.RtException;

public class CsvUtil {

	private static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	
	public static Date getDateValueByColumn(String[] line, Map<String, Integer> colIndexMap, String col,
			DateFormat df) {
		return getDateValueByColumn(line, colIndexMap, col, DF, "");
	}

	public static Date getDateValueByColumn(String[] line, Map<String, Integer> colIndexMap, String col, DateFormat df,
			String asNull) {
		String str = getValueByColumn(line, colIndexMap, col, asNull);
		if (str == null || str.length() == 0) {
			return null;
		}
		try {
			return df.parse(str);
		} catch (ParseException e) {
			throw RtException.toRtException(e);
		}
	}

	public static String getValueByColumn(String[] line, Map<String, Integer> colIndexMap, String col) {
		return getValueByColumn(line, colIndexMap, col, "");
	}

	public static String getValueByColumn(String[] line, Map<String, Integer> colIndexMap, String col, String asNull) {
		Integer idx = colIndexMap.get(col);
		if (idx == null) {
			throw new RtException("no column found:" + col + ",all are:" + colIndexMap);
		}

		String rt = line[idx];
		if (rt == null) {
			return null;
		}

		rt = rt.trim();
		if (rt.equals(asNull)) {
			rt = null;
		}

		return rt;
	}
}
