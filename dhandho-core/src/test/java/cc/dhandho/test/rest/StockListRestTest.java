package cc.dhandho.test.rest;

import cc.dhandho.rest.handler.StockListJsonHandler;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class StockListRestTest extends TestCase {

	public void test() {
		TestUtil.mockDhoServerWithHandler(new StockListJsonHandler());
	}
}
