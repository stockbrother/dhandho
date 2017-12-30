package cc.dhandho.test;

import cc.dhandho.client.jfx.DhandhoJfxApplication;
import cc.dhandho.rest.server.DhandhoServer;
import javafx.application.Application;
import junit.framework.TestCase;

public class DhandhoApplicationTest extends TestCase {

	public void test() {
		DhandhoServer server = TestUtil.newInMemoryTestDhandhoServer();
		server.start();
		DhandhoJfxApplication.setServer(server);
		
		Application.launch(DhandhoJfxApplication.class);
		
	}
	
}
