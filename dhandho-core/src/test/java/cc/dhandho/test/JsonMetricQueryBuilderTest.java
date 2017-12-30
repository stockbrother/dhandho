package cc.dhandho.test;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.MemoryAliasInfos;
import cc.dhandho.report.query.JsonMetricSqlJoinQueryBuilder;
import cc.dhandho.report.query.JsonArrayMetricsQuery;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;
import junit.framework.Assert;
@Ignore
public class JsonMetricQueryBuilderTest {

	private Logger LOG = LoggerFactory.getLogger(JsonMetricQueryBuilderTest.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void test2() throws IOException {

		String jsonS = ("{" //
				+ "'corpId':'a'"// end of corpId
				+ ",'years':[2016,2015,2014,2013,2012]"// end of years
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

		MemoryAliasInfos aliasInfos = new MemoryAliasInfos();
		Integer idxA1 = aliasInfos.addColumnIndex("A", "净利润");
		Integer idxB1 = aliasInfos.addColumnIndex("B", "资产总计");
		Integer idxB2 = aliasInfos.addColumnIndex("B", "所有者权益_或股东权益_合计");

		String cA1 = "a.d_" + idxA1;
		String cB1 = "b.d_" + idxB1;
		String cB2 = "b.d_" + idxB2;

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		
		JsonArrayMetricsQuery r = JsonArrayMetricsQuery.newInstance(reader, aliasInfos);
		//r.build();

		String sql = null;//r.getSql();

		Assert.assertEquals(
				"select ((" + cA1 + "/" + cB1 + ")*(" + cB1 + "/" + cB2 + "))"
						+ " from "+DbInitUtil.V_MASTER_REPORT+" where corpId=?",
				sql);

		System.out.println(sql);

		StringWriter sWriter = new StringWriter();
		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);

		System.out.println(sWriter.getBuffer());

	}
}
