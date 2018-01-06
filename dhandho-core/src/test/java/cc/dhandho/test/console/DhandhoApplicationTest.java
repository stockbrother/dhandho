package cc.dhandho.test.console;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.client.jfx.DhandhoJfxApplication;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class DhandhoApplicationTest extends TestCase {

	public void test() {
		DhoServer server = TestUtil.newInMemoryTestDhandhoServer();
		server.start();
		DhandhoJfxApplication.setServer(server);
		FileObject consoleHome = TestUtil.newConsoleHome();
		DhandhoJfxApplication.launch(server,consoleHome);
		
	}
	
}
