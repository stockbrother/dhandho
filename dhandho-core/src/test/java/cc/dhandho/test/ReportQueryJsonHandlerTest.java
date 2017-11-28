package cc.dhandho.test;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import cc.dhandho.server.DhandhoServer;

public class ReportQueryJsonHandlerTest {

	@BeforeClass
	public static void setUp() {

	}

	@AfterClass
	public static void tearDown() {

	}

	@Test
	public void testNoTx() throws IOException {
		DhandhoServer ds = new DhandhoServer();
		ds.start();
		// ds.handle(handlerS, reader, writer);

		// ds.destroy();
	}
}
