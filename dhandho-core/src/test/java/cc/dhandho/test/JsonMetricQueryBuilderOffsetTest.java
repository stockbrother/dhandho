package cc.dhandho.test;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.Quarter;
import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.container.ContainerImpl;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.importer.GDBWashedFileSchemaLoader;
import cc.dhandho.importer.GDBWashedFileValueLoader;
import cc.dhandho.report.JsonMetricSqlLinkQueryBuilder;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;
@Ignore
public class JsonMetricQueryBuilderOffsetTest {

	private Logger LOG = LoggerFactory.getLogger(JsonMetricQueryBuilderOffsetTest.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	static Container app;
	static DbReportMetaInfos aliasInfos;
	static ODatabaseSession db;

	@BeforeClass
	public static void setUp() {
		app = new ContainerImpl();

		//DbInitUtil.initDb(app);

		File dir = new File("src/test/resources/cc/dhandho/test/washed1".replace('/', File.separatorChar));

		//new GDBWashedFileSchemaLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();
		//new GDBWashedFileValueLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();

		aliasInfos = new DbReportMetaInfos();
		//db = app.openDB();
		//aliasInfos.initialize(db);

	}

	@AfterClass
	public static void tearDown() {
		db.close();
		app.destroy();
	}

	@Test
	@Ignore
	public void testSimple() throws IOException {

		

	}
	
	@Test
	public void testOffset() throws IOException {

		String jsonS = ("{" //
				+ "'corpId':'000002'"// end of corpId
				+ ",'dates':[2016,2015,2014,2013,2012]"// end of years
				+ ",'metrics':[" //
				+ "  { 'name':'总资产收益率'," //
				+ "    'offset':0," //
				+ "    'operator':'/'," //
				+ "    'metrics':[" //
				+ "      '净利润'," //
				+ "      { 'name':'平均资产总计',"//
				+ "        'offset':0," //
				+ "        'operator':'avg'," //
				+ "        'metrics':["
				+ "          '资产总计[0]',"//
				+ "          '资产总计[-1]'"//
				+ "        ]"
				+ "      }"//
				+ "    ]" //
				+ "  }," //
				+ "  '资产总计'," //
				+ "  '资产总计[-1]'" //
				+ "]" //
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);

		JsonMetricSqlLinkQueryBuilder r = JsonMetricSqlLinkQueryBuilder.newInstance(reader, aliasInfos);
		r.build().query(db,writer);
		
		System.out.println("data:");
		System.out.println(sWriter.getBuffer());

	}
}
