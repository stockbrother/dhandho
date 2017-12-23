package cc.dhandho.test;

import cc.dhandho.client.DhandhoConsole;
import cc.dhandho.server.DhandhoServer;
import junit.framework.TestCase;

public class DhandhoConsoleTest extends TestCase {

	@Override
	protected void setUp() throws Exception {

	}

	@Override
	protected void tearDown() throws Exception {

	}

	public void test() throws Exception {

		DhandhoConsole console = TestUtil.newInMemoryTestDhandhoConsole();
		console.start();
		console.prompt();
		while (true) {
			Thread.sleep(1000);
		}

		// console.shutdown();

	}

}
