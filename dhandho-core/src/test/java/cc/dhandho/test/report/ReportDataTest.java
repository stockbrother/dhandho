package cc.dhandho.test.report;

import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.input.xueqiu.DateUtil;
import cc.dhandho.report.ReportData;
import cc.dhandho.util.JsonUtil;
import junit.framework.TestCase;

public class ReportDataTest extends TestCase {

	public void test() throws Exception {
		ReportData r = new ReportData();
		String corpId = "000001";
		Date reportDate1 = DateUtil.FORMAT_YEAR.parse("2016");
		Date reportDate2 = DateUtil.FORMAT_YEAR.parse("2015");
		ReportData.ReportRow row1 = r.addRow(corpId, reportDate1);
		row1.set("m1", 11D);
		row1.set("m2", 12D);

		ReportData.ReportRow row2 = r.addRow(corpId, reportDate2);
		row2.set("m1", 21D);
		row2.set("m2", 22D);
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

		JsonObject rowO1 = rowA.get(0).getAsJsonObject();
		JsonObject rowO2 = rowA.get(1).getAsJsonObject();
		Double d11 = rowO1.get("m1").getAsDouble();
		Double d12 = rowO1.get("m2").getAsDouble();

		Double d21 = rowO2.get("m1").getAsDouble();
		Double d22 = rowO2.get("m2").getAsDouble();

		TestCase.assertEquals(11D, d11, 0.001D);
		TestCase.assertEquals(12D, d12, 0.001D);
		TestCase.assertEquals(21D, d21, 0.001D);
		TestCase.assertEquals(22D, d22, 0.001D);

	}
}
