package cc.dhandho.test.jsweet;

import org.junit.Assert;
import org.junit.Test;

public class JsViewTest2 extends AbstractTest {

	
	public void testTranspile() {
		transpile(logHandler -> {

			Assert.assertEquals("There should be 0 problems", 0, logHandler.reportedProblems.size());
		}, getSourceFile(JsView.class), getSourceFile(MyTestMain.class));
	}

	@Test
	public void testEval() {
		eval((logHandler, rst) -> {

			Assert.assertEquals("There should be 0 problems", 0, logHandler.reportedProblems.size());
		}, getSourceFile(JsView.class), getSourceFile(MyTestMain.class));
	}

}
