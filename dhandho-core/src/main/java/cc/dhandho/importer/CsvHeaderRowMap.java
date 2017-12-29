package cc.dhandho.importer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CsvHeaderRowMap extends CsvRowMap {
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd");

	private SimpleDateFormat dateFormat;

	public SimpleDateFormat getDateFormat() {
		if (dateFormat != null) {
			return this.dateFormat;
		}
		CsvRow r = this.get("日期格式", false);

		if (r == null) {
			this.dateFormat = FORMAT;
		} else {
			String df = r.getString(1, true);
			this.dateFormat = new SimpleDateFormat(df);
		}
		return this.dateFormat;
	}
	
	public Date getAsDate(String key) {
		return this.get(key, true).getAsDate(1, this.getDateFormat());
	}

	public Map<String,Integer> getAsIndexMap(String key) {
		Map<String,Integer> rt = new HashMap<>();
		CsvRow row = this.get(key, true);
		for (int i = 0; i < row.size(); i++) {
			String key2 = row.getString(i, true);
			rt.put(key2, i);			
		}
		return rt;
	}

	public Date[] getAsDateArray(String key) {
		CsvRow row = this.get(key, true);
		//
		SimpleDateFormat df = this.getDateFormat();
		Date[] rt = row.getAsDateArray(df);
		return rt;
	}

}
