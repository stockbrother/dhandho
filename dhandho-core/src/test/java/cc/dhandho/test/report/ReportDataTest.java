package cc.dhandho.test.report;

import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.CorpDatedMetricReportData;
import cc.dhandho.util.JsonUtil;
import junit.framework.TestCase;

public class ReportDataTest extends TestCase {

	@Override
	protected void setUp() throws Exception {

	}

	@Override
	protected void tearDown() throws Exception {

	}
	
	public void testSvg() throws Exception {
		String[] headerArray = new String[] { "c1", "c2" };
		CorpDatedMetricReportData r = new CorpDatedMetricReportData(headerArray);
		String corpId = "000001";
		Date reportDate1 = DateUtil.FORMAT_YEAR.parse("2016");
		Date reportDate2 = DateUtil.FORMAT_YEAR.parse("2015");
		CorpDatedMetricReportData.ReportRow row1 = r.addRow(corpId, reportDate1, new Double[] { 11D, 12D });
		CorpDatedMetricReportData.ReportRow row2 = r.addRow(corpId, reportDate2, new Double[] { 21D, 22D });
		StringBuilder sb = r.toSvg(new StringBuilder());
		String svg = sb.toString();
		TestCase.assertTrue(svg.startsWith("<svg"));
		TestCase.assertTrue(svg.endsWith("</svg>\r\n"));
	}
	
	public void testHtml() throws Exception {
		String[] headerArray = new String[] { "c1", "c2" };
		CorpDatedMetricReportData r = new CorpDatedMetricReportData(headerArray);
		String corpId = "000001";
		Date reportDate1 = DateUtil.FORMAT_YEAR.parse("2016");
		Date reportDate2 = DateUtil.FORMAT_YEAR.parse("2015");
		CorpDatedMetricReportData.ReportRow row1 = r.addRow(corpId, reportDate1, new Double[] { 11D, 12D });
		CorpDatedMetricReportData.ReportRow row2 = r.addRow(corpId, reportDate2, new Double[] { 21D, 22D });
		StringBuilder sb = r.toHtmlTable(new StringBuilder());
		String html = sb.toString();
		TestCase.assertTrue(html.startsWith("<table>"));
		TestCase.assertTrue(html.indexOf("<td>CorpID</td>") > 0);
		TestCase.assertTrue(html.indexOf("<td>ReportDate</td>") > 0);
		TestCase.assertTrue(html.indexOf("<td>c1</td>") > 0);
		TestCase.assertTrue(html.indexOf("<td>c2</td>") > 0);
		TestCase.assertTrue(html.indexOf("<td>" + corpId + "</td>") > 0);
		TestCase.assertTrue(html.indexOf("<td>11.0</td>")>0);
		TestCase.assertTrue(html.indexOf("<td>12.0</td>")>0);
		TestCase.assertTrue(html.indexOf("<td>21.0</td>")>0);
		TestCase.assertTrue(html.indexOf("<td>22.0</td>")>0);		
		TestCase.assertTrue(html.endsWith("</table>"));
	}

	public void testJson() throws Exception {
		String[] headerArray = new String[] { "c1", "c2" };
		CorpDatedMetricReportData r = new CorpDatedMetricReportData(headerArray);
		String corpId = "000001";
		Date reportDate1 = DateUtil.FORMAT_YEAR.parse("2016");
		Date reportDate2 = DateUtil.FORMAT_YEAR.parse("2015");
		CorpDatedMetricReportData.ReportRow row1 = r.addRow(corpId, reportDate1, new Double[] { 11D, 12D });

		CorpDatedMetricReportData.ReportRow row2 = r.addRow(corpId, reportDate2, new Double[] { 21D, 22D });

		StringBuilder sb = new StringBuilder();
		r.writeToJson(sb);

		JsonElement json = JsonUtil.parse(sb.toString());
		// TODO
		TestCase.assertNotNull(json);
		TestCase.assertTrue(json.isJsonObject());
		JsonObject obj = json.getAsJsonObject();
		JsonArray rowA = obj.get("rowArray").getAsJsonArray();

		TestCase.assertNotNull(rowA);
		TestCase.assertEquals(2, rowA.size());

		JsonArray rowO1 = rowA.get(0).getAsJsonArray();
		JsonArray rowO2 = rowA.get(1).getAsJsonArray();
		String coprId1 = rowO1.get(0).getAsString();
		String date1 = rowO1.get(1).getAsString();

		Double d11 = rowO1.get(2).getAsDouble();
		Double d12 = rowO1.get(3).getAsDouble();

		String coprId2 = rowO2.get(0).getAsString();
		String date2 = rowO2.get(1).getAsString();
		Double d21 = rowO2.get(2).getAsDouble();
		Double d22 = rowO2.get(3).getAsDouble();

		TestCase.assertEquals(corpId, coprId1);
		TestCase.assertEquals(corpId, coprId2);
		TestCase.assertEquals(DateUtil.format(reportDate1), date1);
		TestCase.assertEquals(DateUtil.format(reportDate2), date2);
		TestCase.assertEquals(11D, d11, 0.001D);
		TestCase.assertEquals(12D, d12, 0.001D);
		TestCase.assertEquals(21D, d21, 0.001D);
		TestCase.assertEquals(22D, d22, 0.001D);

		CorpDatedMetricReportData r2 = CorpDatedMetricReportData.parseJson(sb.toString());
		StringBuilder sb2 = new StringBuilder();
		r2.writeToJson(sb2);

		TestCase.assertEquals(sb.toString(), sb2.toString());
	}
}
