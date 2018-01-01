package cc.dhandho.report.query;

import java.util.Date;
import java.util.function.Consumer;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;

import cc.dhandho.ReportMetaInfos;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.report.CorpDatedMetricReportData.ReportRow;

public class ReportDataMetricQuery extends MetricsQuery<CorpDatedMetricReportData> {

	public ReportDataMetricQuery(JsonObject json, ReportMetaInfos reportMetaInfos) {
		super(json, reportMetaInfos);
	}

	public ReportDataMetricQuery(JsonReader reader, ReportMetaInfos reportMetaInfos) {
		super(reader, reportMetaInfos);
	}

	@Override
	public CorpDatedMetricReportData handle(OResultSet rst) {

		CorpDatedMetricReportData rt = new CorpDatedMetricReportData(this.query.getMetricArray());

		rst.stream().forEach(new Consumer<OResult>() {

			@Override
			public void accept(OResult row) {

				String corpId = row.getProperty(QueryJsonWrapper.CORP_ID);
				Date reportDate = row.getProperty(QueryJsonWrapper.REPORT_DATA);

				Double[] valueArray = new Double[row.getPropertyNames().size() - 2];

				for (int i = 0; i < valueArray.length; i++) {
					String key = "m" + (i + 1);
					Object value = row.getProperty(key);
					valueArray[i] = (Double) value;
				}

				ReportRow rr = rt.addRow(corpId, reportDate, valueArray);

			}
		});
		return rt;
	}

}
