package cc.dhandho.test.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.commons.http.HttpClientUtil;
import cc.dhandho.commons.http.JsonElementHttpResponseHandler;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.rest.web.RestJettyHandler;
import cc.dhandho.rest.web.JettyWebServer;
import junit.framework.TestCase;

public class JettyWebServerTest {

	@Test
	public void testWebServer() throws IOException {

		String handlerS = "my.JsonHandler";
		DhoServer dserver = mock(DhoServer.class);

		doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {

				Object[] arguments = invocation.getArguments();
				if (arguments.length == 3) {
					String handlerName = (String) arguments[0];
					Reader r = (Reader) arguments[1];
					Writer w = (Writer) arguments[2];
					TestCase.assertEquals(handlerS, handlerName);

					mockHandle(handlerName, r, w);
				} else {
					throw new Exception("not supported.");
				}
				return null;
			}
		}).when(dserver).handle(any(String.class), any(Reader.class), any(Writer.class));

		JettyWebServer web = new JettyWebServer(dserver);

		web.start();
		try {

			String host = "localhost";
			int port = 8080;
			String uri = RestJettyHandler.contextPath + handlerS;

			JsonElement json = HttpClientUtil.doHttpGet(host, port, uri, new JsonElementHttpResponseHandler());
			TestCase.assertTrue(json.isJsonObject());
			JsonObject jsonO = (JsonObject) json;
			TestCase.assertEquals("1", jsonO.get("first").getAsString());
			TestCase.assertEquals("2", jsonO.get("second").getAsString());
		} finally {
			web.shutdown();
		}
	}

	@Test
	public void test() {

	}

	private void mockHandle(String handlerS, Reader r, Writer w) throws IOException {
		w.write("{'first':'1','second':'2'}".replaceAll("'", "\""));

	}
}
