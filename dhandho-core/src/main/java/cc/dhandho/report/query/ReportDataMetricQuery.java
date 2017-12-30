package cc.dhandho.report.query;

import java.util.Date;
import java.util.function.Consumer;

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

		rst.stream().forEach(new Consumer<OResult>() {

			@Override
			public void accept(OResult row) {

				String corpId = row.getProperty(QueryJsonWrapper.CORP_ID);
				Date reportDate = row.getProperty(QueryJsonWrapper.REPORT_DATA);

				ReportRow rr = rt.addRow(corpId, reportDate);

				// TODO
				for (int i = 0; i < row.getPropertyNames().size() - 2; i++) {
					String key = "m" + (i + 1);
					Object value = row.getProperty(key);
					rr.set(key, (Double) value);
				}

			}
		});
		return rt;
	}

}
