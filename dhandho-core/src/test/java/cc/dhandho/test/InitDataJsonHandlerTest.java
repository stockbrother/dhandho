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

import cc.dhandho.rest.InitDataJsonHandler;
import cc.dhandho.rest.JsonMetricSqlLinkQueryBuilder;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;
import junit.framework.Assert;

@Ignore
public class InitDataJsonHandlerTest {

	private Logger LOG = LoggerFactory.getLogger(InitDataJsonHandlerTest.class);

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
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
		InitDataJsonHandler h = new InitDataJsonHandler();
		h.execute(new RestRequestContext(GSON, reader, writer));

		System.out.println(sWriter.getBuffer());

	}
}
