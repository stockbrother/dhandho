package cc.dhandho.test;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.DbAliasInfos;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.LoadCorpInfoJsonHandler;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;

public class CorpInfoLoadJsonHandlerTest {

	private Logger LOG = LoggerFactory.getLogger(CorpInfoLoadJsonHandlerTest.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	static AppContext app;
	static DbAliasInfos aliasInfos;
	
	@BeforeClass
	public static void setUp() {
		app = new AppContextImpl()
				.dbConfig(new DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin"))
				.create();

		DbInitUtil.initDb(app);
		
	}

	@AfterClass
	public static void tearDown() {
		
		app.destroy();
	}
	
	@Test
	public void test2() throws IOException {
		
		String jsonS = ("{" //
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
		LoadCorpInfoJsonHandler h  = new LoadCorpInfoJsonHandler();		
		h.execute(GSON, reader, writer);

		System.out.println(sWriter.getBuffer());

	}
}
