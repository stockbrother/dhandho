package cc.dhandho.test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import com.google.gson.stream.JsonReader;

import cc.dhandho.rest.handler.CorpChartJsonHandler;
import cc.dhandho.rest.server.DhandhoServer;
import cc.dhandho.util.JsonUtil;
import junit.framework.TestCase;

public class ChartQueryTest extends TestCase {

	@Override
	public void setUp() {

	}

	@Override
	public void tearDown() {

	}

	@Test
	public void testNoTx() throws IOException {
		DhandhoServer ds = TestUtil.newInMemoryTestDhandhoServer();
		ds.start();

		String jsonS = ("{" //
				+ "'corpId':'000001'"// end of corpId
				+ ",'dates':[2016,2015,2014,2013,2012]"// end of years
				+ ",'metrics':" //
				+ "  [" //
				+ "    { 'name':'净资产收益率'," //
				+ "      'offset':0," //
				+ "      'operator':'*'," //
				+ "      'metrics':[" //
				+ "        { 'name':'总资产收益率'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'metrics':[" //
				+ "            '净利润'," //
				+ "            '资产总计'" //
				+ "          ]" //
				+ "        }," //
				+ "        { 'name':'权益乘数'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'metrics':[" //
				+ "          '资产总计'," //
				+ "          '所有者权益_或股东权益_合计'" //
				+ "          ]" //
				+ "        }" //
				+ "      ]" //
				+ "    }" //
				+ "  ]" // end of formulas
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		StringWriter sWriter = new StringWriter();
		ds.handle(CorpChartJsonHandler.class.getName(), new StringReader(jsonS), sWriter);
		System.out.println(sWriter);//
		ds.shutdown();
	}
}
