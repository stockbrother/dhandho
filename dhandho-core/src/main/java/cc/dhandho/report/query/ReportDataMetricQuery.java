package cc.dhandho.report.query;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.report.ReportData;
import cc.dhandho.report.ReportData.ReportRow;

public class ReportDataMetricQuery extends MetricsQuery<ReportData> {

	public ReportDataMetricQuery(JsonObject json, ReportMetaInfos reportMetaInfos) {
		super(json, reportMetaInfos);
	}

	public ReportDataMetricQuery(JsonReader reader, ReportMetaInfos reportMetaInfos) {
		super(reader, reportMetaInfos);
	}

	@Override
	public ReportData handle(OResultSet rst) {
		ReportData rt = new ReportData();

		while (rst.hasNext()) {
			OResult row = rst.next();
			
			ReportRow rr = rt.addRow();
			//TODO
			for (String key : row.getPropertyNames()) {
				Object value = row.getProperty(key);
				
				rr.set(key, (Double) value);
			}
		}

		return rt;
	}

}
