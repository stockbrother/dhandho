import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.CorpChartJsonHandler2;

public class CorpChartHandler2Test {

	private Logger LOG = LoggerFactory.getLogger(CorpChartHandler2Test.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	AppContext app;

	@Before
	public void setUp() {
		String dbUrl = "remote:localhost";
		String dbName = "test";
		String dbUser = "admin";
		String dbPassword = "admin";

		app = new AppContextImpl()
				.dbConfig(new DbConfig().dbUrl(dbUrl).dbName(dbName).dbUser(dbUser).dbPassword(dbPassword)).create();
		
	}

	@After
	public void tearDown() {
		
		app.destroy();
	}

	@Test
	public void test1() throws IOException {

		String jsonS = ("{" //
				+ "'corpId':'a'"//
				+ ",'years':[2016,2015,2014,2013,2012]"//
				+ ",'components':['资产总计','负债合计']"//
				+ "}"//
		).replace('\'', '\"');
		System.out.println(jsonS);

		JsonReader reader = GSON.newJsonReader(new StringReader(jsonS));
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = GSON.newJsonWriter(sWriter);

		new CorpChartJsonHandler2().execute(GSON, reader, writer);

		System.out.println(sWriter.getBuffer());

	}

	@Test
	public void test2() throws IOException {

		String jsonS = ("{" //
				+ "'corpId':'a'"// end of corpId
				+ ",'years':[2016,2015,2014,2013,2012]"// end of years
				+ ",'components':" //
				+ "  [" //
				+ "    { 'name':'净资产收益率'," //
				+ "      'offset':0," //
				+ "      'operator':'*'," //
				+ "      'components':[" //
				+ "        { 'name':'总资产收益率'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "            '净利润'," //
				+ "            '资产总计'," //
				+ "          ]" //
				+ "        }," //
				+ "        { 'name':'权益乘数'," //
				+ "          'offset':0," //
				+ "          'operator':'/'," //
				+ "          'components':[" //
				+ "          '资产总计'," //
				+ "          '所有者权益_或股东权益_合计'," //
				+ "          ]" //
				+ "        }" //
				+ "      ]" //
				+ "    }" //
				+ "  ]" // end of formulas
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = GSON.newJsonReader(new StringReader(jsonS));
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = GSON.newJsonWriter(sWriter);

		new CorpChartJsonHandler2().execute(GSON, reader, writer);

		System.out.println(sWriter.getBuffer());

	}
}
