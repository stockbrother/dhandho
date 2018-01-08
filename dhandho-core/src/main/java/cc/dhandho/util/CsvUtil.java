package cc.dhandho.util;

import java.io.Closeable;
import java.io.IOException;

import com.age5k.jcps.JcpsException;

import au.com.bytecode.opencsv.CSVReader;

public class CsvUtil {

	public static String[] readNext(CSVReader r) {
		//
		try {
			return r.readNext();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static void close(Closeable c) {
		try {
			c.close();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

}
