package cc.dhandho.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.client.MetricsDefine;
import cc.dhandho.rest.SvgChartMetricQueryBuilder;
import cc.dhandho.util.JsonUtil;
import junit.framework.TestCase;

public class MetricDefineTest extends TestCase {

	public void testLoadMetricDefine() throws IOException {
		InputStream is = TestUtil.getHome().resolveFile("client/metrics-define.xml").getContent().getInputStream();
		MetricsDefine metrics = MetricsDefine.load(is);

		String metric = "权益乘数";
		StringWriter sWriter = new StringWriter();
		try {

			JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
			metrics.buildMetricAsJson(metric, writer);
			System.out.println(sWriter.getBuffer());

		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

	public void testMetricDefine_GenereateRequest() throws IOException {
		InputStream is = TestUtil.getHome().resolveFile("client/metrics-define.xml").getContent().getInputStream();
		MetricsDefine metricsDefine = MetricsDefine.load(is);

		String corpId = "000001";
		String[] metrics = new String[] { "权益乘数", "净资产收益率", "毛利率" };
		int[] years = new int[] { 2016, 2015, 2014, 2013, 2012 };

		StringWriter sWriter = new StringWriter();
		try {

			JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
			metricsDefine.buildMetricRequestAsJson(corpId, years, metrics, writer);

			System.out.println(sWriter.getBuffer());

		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}
}
