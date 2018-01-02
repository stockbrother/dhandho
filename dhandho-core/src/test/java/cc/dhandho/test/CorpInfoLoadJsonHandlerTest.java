package cc.dhandho.test;

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

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.commons.container.Container;
import cc.dhandho.commons.container.ContainerImpl;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.handler.LoadCorpInfoJsonHandler;
import cc.dhandho.util.JsonUtil;

@Ignore
public class CorpInfoLoadJsonHandlerTest {

	private Logger LOG = LoggerFactory.getLogger(CorpInfoLoadJsonHandlerTest.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	static Container app;
	static DbReportMetaInfos aliasInfos;
	
	@BeforeClass
	public static void setUp() {
		app = new ContainerImpl();

		//DbInitUtil.initDb(app);
		
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
		h.handle(new RestRequestContext(GSON, reader, writer));

		System.out.println(sWriter.getBuffer());

	}
}
