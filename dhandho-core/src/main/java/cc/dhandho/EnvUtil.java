package cc.dhandho;

import java.io.File;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

import cc.dhandho.input.xueqiu.DateUtil;

public class EnvUtil {

	static TimeZone timeZone = TimeZone.getTimeZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC));
	static TimeZone dataTimeZone = TimeZone.getTimeZone(ZoneOffset.ofHours(8));//
	public static final String KEY_OPENSTOCK_DATA_DIR = "cc.dhandho.data.dir";

	static {
		System.setProperty(KEY_OPENSTOCK_DATA_DIR, "d:\\data");
	}

	public static TimeZone getTimeZone() {
		return dataTimeZone;
	}

	public static Date[] newDateArrayOfYearLastDay(int[] years) {
		Date[] rt = new Date[years.length];
		for (int i = 0; i < years.length; i++) {
			rt[i] = newDateOfYearLastDay(years[i]);
		}
		return rt;
	}

	public static Date newDateOfYearLastDay(int year) {
		return DateUtil.newDateOfYearLastDay(year, dataTimeZone);
	}

	public static int getYear(Date xValue) {
		//
		return DateUtil.getYear(xValue, dataTimeZone);
	}

	public static File getDataBaseDir() {
		return new File(getDataDir(), "db");
	}

	public static File getDataDir() {
		String dataDirS = System.getProperty(KEY_OPENSTOCK_DATA_DIR);
		if (dataDirS == null) {
			dataDirS = System.getenv(KEY_OPENSTOCK_DATA_DIR.replace('.', '_'));

			throw new RuntimeException("system property:" + KEY_OPENSTOCK_DATA_DIR + " or environment variable "
					+ KEY_OPENSTOCK_DATA_DIR.replace('.', '_') + " not found.");
		}

		File d = new File(dataDirS);
		if (!d.exists()) {
			throw new RuntimeException("data dir:" + d.getAbsolutePath() + " not exist.");
		}
		return d;
	}

	/**
	 * Return the first dir, which contains the corp list data.
	 * 
	 * @return
	 */
	public static File getDir1() {

		return new File(getDataDir(), "1");

	}

	public static boolean isProxyEnabled() {
		return true;
	}

	public static String getProxyHome() {
		return "proxy.houston.hpecorp.net";
	}

	public static int getProxyPort() {
		return 8080;
	}

	public static String getHttpHost163() {
		//
		return "quotes.money.163.com";
	}

	public static String getDbName() {
		return "openstock";
	}

	public static Date floorHour(Date date) {
		
		return DateUtil.floorHour(date, dataTimeZone);
	}

}
