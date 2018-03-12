package cc.dhandho.test.rest;

import org.junit.Test;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.handler.StockChartsJsonHandler;
import cc.dhandho.test.util.TestUtil;

public class StockChartsRestTest {
	@Test
	public void test() {

		Container app = new ContainerImpl();
		TestUtil.populateDbWithDefaultDataHomeForTest(app);
		//
		JsonHandlers handlers = new JsonHandlers(app);

		JsonObject jo = new JsonObject();
		jo.add("corpId", new JsonPrimitive("000001"));

		JsonObject res = (JsonObject) handlers.handle(StockChartsJsonHandler.class.getName(), jo);
		

	}
}
