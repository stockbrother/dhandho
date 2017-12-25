package cc.dhandho.test;

import cc.dhandho.client.jfx.DhandhoApplication;
import cc.dhandho.server.DhandhoServer;
import javafx.application.Application;
import junit.framework.TestCase;

public class DhandhoApplicationTest extends TestCase {

	public void test() {
		DhandhoServer server = TestUtil.newInMemoryTestDhandhoServer();
		server.start();
		DhandhoApplication.setServer(server);
		
		Application.launch(DhandhoApplication.class);

	}
	
}
