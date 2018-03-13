package cc.dhandho.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

import com.age5k.jcps.JcpsException;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.report.MetricDefines;
import cc.dhandho.util.JsonUtil;

public class MetricDefineTest {

	@Test
	public void testLoadMetricDefine() throws IOException {
		
		String res = "/cc/dhandho/home/data/client/metric-defines.xml";
		InputStream is = MetricDefineTest.class.getResourceAsStream(res);
		Assert.assertNotNull("res not found:" + res, is);
		MetricDefines metrics = MetricDefines.load(is);

		String metric = "权益乘数";
		StringWriter sWriter = new StringWriter();
		try {

			JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
			metrics.buildMetricAsJson(metric, writer);
			System.out.println(sWriter.getBuffer());

		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}
	
	@Test
	public void testMetricDefine_GenereateRequest() throws IOException {
		String res = "/cc/dhandho/home/data/client/metric-defines.xml";
		InputStream is = MetricDefineTest.class.getResourceAsStream(res);
		Assert.assertNotNull("res not found:" + res, is);
		MetricDefines metricsDefine = MetricDefines.load(is);

		String corpId = "000001";
		String[] metrics = new String[] { "净资产收益率", "利润率","总资产周转率", "权益乘数" };
		int[] years = new int[] { 2016, 2015, 2014, 2013, 2012 };

		StringWriter sWriter = new StringWriter();

		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
		metricsDefine.buildMetricRequestAsJson(corpId, years, metrics, writer);

		System.out.println(sWriter.getBuffer());

	}
}
