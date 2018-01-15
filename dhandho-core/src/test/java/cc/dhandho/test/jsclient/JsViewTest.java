package cc.dhandho.test.jsclient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cc.dhandho.test.jsclient.util.TestJswHelper;

public class JsViewTest {

	@Before
	public void setUp() throws Exception {

	}

	public void tearDown() throws Exception {

	}

	public void testTranspile() {
		TestJswHelper helper = JswTestUtil.newDefaultTestJswHelper();

		helper.sourceFiles(new Class<?>[] { JsView.class, MyTestMain.class, })//
				.consumer(logHandler -> {

					Assert.assertEquals("There should be 0 problems", 0, logHandler.reportedProblems.size());
				}).execute();
	}

	@Test
	public void testEval() {
		TestJswHelper helper = JswTestUtil.newDefaultTestJswHelper();

		helper.sourceFiles(new Class<?>[] { JsView.class, MyTestMain.class, })//
				.consumer((logHandler, r) -> {
					logHandler.assertNoProblems();
					System.out.println("show:" + (Object) r.get("show"));
					System.out.println("showX:" + (Object) r.get("showX"));
				}).execute();

	}

}
