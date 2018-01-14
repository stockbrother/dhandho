package cc.dhandho.test.jsweet;

import java.io.File;

import org.jsweet.transpiler.ModuleKind;
import org.junit.Test;

import cc.dhandho.client.jsweet.DhoJsClient;
import cc.dhandho.test.jsweet.source.TestDhoJsClient;
import cc.dhandho.test.jsweet.util.TestJswHelper;
import junit.framework.TestCase;

public class DhoJsClientTest {
	@Test
	public void testEval() {
		TestJswHelper helper = JswTestUtil.newDefaultTestJswHelper();

		helper//
				// .addJsLib(new
				// File("C:/cygwin64/home/wu/node_modules/jquery/dist/jquery.js"))//
				// .addJsLib(new File("src/html/jquery-1.11.3.min.js"))//
				.addJsLib(new File("src/html/tmp.js"))//
				.moduleKind(ModuleKind.none)//
				.sourceFiles(new Class<?>[] { DhoJsClient.class, TestDhoJsClient.class, })//
				.consumer((logHandler, r) -> {
					logHandler.assertNoProblems();
					TestCase.assertEquals(r.getExecutionTrace(),"OK", (Object) r.get("showHtmlResult"));

				}).execute();

	}

}
