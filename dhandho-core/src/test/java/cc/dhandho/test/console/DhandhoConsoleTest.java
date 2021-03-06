package cc.dhandho.test.console;

import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DhandhoConsoleTest extends TestCase {

	@Override
	protected void setUp() throws Exception {

	}

	@Override
	protected void tearDown() throws Exception {

	}

	public void test() throws Exception {

		DhandhoCliConsole console = TestUtil.newInMemoryTestDhandhoServerConsole();
		console.getServer().start();
		console.start();
		console.prompt();		
		console.shutdown();
		
	}

}
