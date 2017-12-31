package cc.dhandho.input.szse;

import java.util.Date;
import java.util.Map;

import com.orientechnologies.orient.core.record.OVertex;

import cc.dhandho.input.AbstractCorpInfoCsvLoader;
import cc.dhandho.input.CsvUtil;
/**
 * Shenzhen Secure E
 * 
 * @author wu
 *
 */
public class SzseCorpInfoLoader extends AbstractCorpInfoCsvLoader {

	public SzseCorpInfoLoader(String name) {
		super(name);
	}

	@Override
	public void handleRowInternal(String[] next, Map<String, Integer> colIndexMap) {

		String x0 = CsvUtil.getValueByColumn(next, colIndexMap, "A股代码");
		String x1 = CsvUtil.getValueByColumn(next, colIndexMap, "公司简称");
		String x2 = CsvUtil.getValueByColumn(next, colIndexMap, "公司全称");
		String x3 = CsvUtil.getValueByColumn(next, colIndexMap, "所属行业");
		Date x4 = CsvUtil.getDateValueByColumn(next, colIndexMap, "A股上市日期", DF);
		String x5 = CsvUtil.getValueByColumn(next, colIndexMap, "省份");
		String x6 = CsvUtil.getValueByColumn(next, colIndexMap, "城市");
		String x7 = CsvUtil.getValueByColumn(next, colIndexMap, "公司网址");
		String x8 = CsvUtil.getValueByColumn(next, colIndexMap, "注册地址");

		OVertex v = getCorpInfoVertex(x0);
		v.setProperty("corpName", x1);
		v.setProperty("fullName", x2);
		v.setProperty("category", x3);
		v.setProperty("ipoDate", x4);
		v.setProperty("province", x5);
		v.setProperty("city", x6);
		v.setProperty("webSite", x7);
		v.setProperty("address", x8);
		v.save();

	}


}
