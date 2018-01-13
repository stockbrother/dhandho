package cc.dhandho.test.jsweet;

import java.io.File;

import org.jsweet.transpiler.ModuleKind;
import org.junit.Test;

import cc.dhandho.client.jsweet.DhoMain;
import cc.dhandho.test.jsweet.util.TestJswHelper;

public class DhoMainJswTest {
	@Test
	public void testEval() {
		TestJswHelper helper = JswTestUtil.newDefaultTestJswHelper();

		helper//
				//.addJsLib(new File("C:/cygwin64/home/wu/node_modules/jquery/dist/jquery.js"))//
				//.addJsLib(new File("src/html/jquery-1.11.3.min.js"))//
				.addJsLib(new File("src/html/tmp.js"))//
				.moduleKind(ModuleKind.none)//
				.sourceFiles(new Class<?>[] { DhoMain.class, })//
				.consumer((logHandler, r) -> {
					logHandler.assertNoProblems();
					System.out.println("show:" + (Object) r.get("show"));
					System.out.println("showX:" + (Object) r.get("showX"));
				}).execute();

	}

}
