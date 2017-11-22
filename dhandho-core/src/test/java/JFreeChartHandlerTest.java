import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.rest.JFreeChartHandler;
import cc.dhandho.test.BaseTest;
@Ignore
public class JFreeChartHandlerTest {

	private Logger LOG = LoggerFactory.getLogger(JFreeChartHandlerTest.class);

	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	@Before
	public void setUp() {

	}

	@After
	public void tearDown() {
	}

	@Test
	public void test1() throws IOException {

		String jsonS = ("{" //
				+ "'corpId':'a'"//
				+ "}"//
		).replace('\'', '\"');

		System.out.println(jsonS);

		JsonReader reader = GSON.newJsonReader(new StringReader(jsonS));
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = GSON.newJsonWriter(sWriter);

		new JFreeChartHandler().execute(GSON, reader, writer);

		System.out.println(sWriter.getBuffer());

		JsonReader reader2 = GSON.newJsonReader(new StringReader(sWriter.getBuffer().toString()));

		JsonElement element = Streams.parse(reader2);
		JsonObject object = element.getAsJsonObject();
		String svgString = object.get("svgString").getAsString();
		System.out.println(svgString);

	}

}
