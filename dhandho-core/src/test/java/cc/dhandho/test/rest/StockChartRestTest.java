package cc.dhandho.test.rest;

import org.junit.Test;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.handler.StockChartJsonHandler;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.JsonUtil;

public class StockChartRestTest {
	@Test
	public void test() {

		Container app = new ContainerImpl();
		TestUtil.populateDbWithDefaultDataHomeForTest(app);
		//
		JsonHandlers handlers = new JsonHandlers(app);

		JsonObject jo = new JsonObject();
		jo.add("corpId", new JsonPrimitive("000002"));
		jo.add("years", JsonUtil.newJsonArray(new int[] { 2016, 2015, 2014 }));
		jo.add("metrics", JsonUtil.newJsonArray(new String[] { "净资产收益率", "利润率", "总资产周转率", "权益乘数" }));

		JsonObject res = (JsonObject) handlers.handle(StockChartJsonHandler.class.getName(), jo);

	}
}
