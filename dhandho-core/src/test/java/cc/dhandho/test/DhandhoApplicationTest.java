package cc.dhandho.test;

import cc.dhandho.client.jfx.DhandhoJfxApplication;
import cc.dhandho.rest.server.DhoServer;
import cc.dhandho.test.util.TestUtil;
import javafx.application.Application;
import junit.framework.TestCase;

public class DhandhoApplicationTest extends TestCase {

	public void test() {
		DhoServer server = TestUtil.newInMemoryTestDhandhoServer();
		server.start();
		DhandhoJfxApplication.setServer(server);
		
		Application.launch(DhandhoJfxApplication.class);
		
	}
	
}
