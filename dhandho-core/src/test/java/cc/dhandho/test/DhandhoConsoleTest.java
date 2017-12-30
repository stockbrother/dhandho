package cc.dhandho.test;

import cc.dhandho.client.DhandhoCliConsole;
import cc.dhandho.rest.server.DhandhoServer;
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
		
		//console.shutdownAsync();
		while (console.isAlive()) {
			Thread.sleep(1000);
		}

		// console.shutdown();

	}

}
