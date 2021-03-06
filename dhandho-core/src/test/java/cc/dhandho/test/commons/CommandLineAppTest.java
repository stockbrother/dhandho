package cc.dhandho.test.commons;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cc.dhandho.commons.commandline.AbstractComandLineApp;
import junit.framework.TestCase;

public class CommandLineAppTest extends TestCase {
	public static class MyApp extends AbstractComandLineApp {

	}

	public void test() throws Exception {
		MyApp app = new MyApp();
		Future<Object> fo = app.start();
		fo.get(10, TimeUnit.SECONDS);
		app.shutdown();
	}
}
