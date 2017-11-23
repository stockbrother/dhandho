package cc.dhandho.importer;

import java.util.Map;

import com.orientechnologies.orient.core.record.OVertex;

public class SseCorpInfo2Loader extends AbstractCorpInfoCsvLoader {

	public SseCorpInfo2Loader(String name) {
		super(name);
	}

	@Override
	protected void handleRow(String[] next, Map<String, Integer> colIndexMap) {
		String x0 = CsvUtil.getValueByColumn(next, colIndexMap, "A股代码");
		if ("-".equals(x0)) {
			return;//ignore
		}
		String x1 = CsvUtil.getValueByColumn(next, colIndexMap, "公司全称");
		String x2 = CsvUtil.getValueByColumn(next, colIndexMap, "所属行业");
		OVertex v = getCorpInfoVertex(x0);		
		v.setProperty("fullName", x1);
		v.setProperty("category", x2);		
		v.save();
	}

}
