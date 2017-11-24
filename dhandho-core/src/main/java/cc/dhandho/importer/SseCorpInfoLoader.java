package cc.dhandho.importer;

import java.util.Date;
import java.util.Map;

import com.orientechnologies.orient.core.record.OVertex;

public class SseCorpInfoLoader extends AbstractCorpInfoCsvLoader {

	public SseCorpInfoLoader(String name) {
		super(name);
	}

	@Override
	protected void handleRowInternal(String[] next, Map<String, Integer> colIndexMap) {
		String x0 = CsvUtil.getValueByColumn(next, colIndexMap, "A股代码");
		String x1 = CsvUtil.getValueByColumn(next, colIndexMap, "A股简称");
		Date x2 = CsvUtil.getDateValueByColumn(next, colIndexMap, "A股上市日期", DF, "-");
		OVertex v = getCorpInfoVertex(x0);
		v.setProperty("corpName", x1);
		v.setProperty("ipoDate", x2);
		v.save();
	}
	
}
