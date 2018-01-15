package cc.dhandho.test.jsclient;

import java.io.File;

import org.jsweet.transpiler.ModuleKind;
import org.junit.Test;

import cc.dhandho.jsclient.JsCommandInput;
import cc.dhandho.jsclient.JsDhoClient;
import cc.dhandho.jsclient.JsDivWrapper;
import cc.dhandho.jsclient.JsElementWrapper;
import cc.dhandho.jsclient.JsResponseRenderer;
import cc.dhandho.rest.handler.EchoJsonHandler;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.rest.web.JettyWebServer;
import cc.dhandho.test.jsclient.source.TestDhoJsClient;
import cc.dhandho.test.jsclient.util.TestJswHelper;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class JsClientWithWebServerTest {
	@Test
	public void testEval() {

		DhoServer dserver = TestUtil.mockDhoServerWithHandler(new EchoJsonHandler());
		JettyWebServer web = new JettyWebServer(dserver);
		web.start();
		try {

			TestJswHelper helper = JswTestUtil.newDefaultTestJswHelper();

			helper.addJsLib(new File("src/html/tmp.js"))//
					.moduleKind(ModuleKind.none)//
					.sourceFiles(new Class<?>[] { JsElementWrapper.class, JsDivWrapper.class, JsResponseRenderer.class,
							JsCommandInput.class, JsDhoClient.class, TestDhoJsClient.class, })//
					.consumer((logHandler, r) -> {
						logHandler.assertNoProblems();
						TestCase.assertEquals(r.getExecutionTrace(), "OK", (Object) r.get("showHtmlResult"));

					}).execute();

		} finally {
			web.shutdown();
		}

	}

}
