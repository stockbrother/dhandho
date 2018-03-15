package cc.dhandho.test.rest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.handler.StockChartJsonHandler;
import cc.dhandho.rest.handler.StockChartsJsonHandler;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.JsonUtil;

public class StockChartRestTest {
	static Container app;
	static JsonHandlers handlers;

	@BeforeClass
	public static void setUp() {
		app = new ContainerImpl();
		TestUtil.populateDbWithDefaultDataHomeForTest(app);
		handlers = new JsonHandlers(app);

	}

	@AfterClass
	public static void tearDown() {

	}

	@Test
	public void testChartJsonHandler() {
		//

		JsonObject jo = new JsonObject();
		jo.add("corpId", new JsonPrimitive("000002"));
		jo.add("years", JsonUtil.newJsonArray(new int[] { 2016, 2015, 2014 }));
		jo.add("metrics", JsonUtil.newJsonArray(new String[] { "净资产收益率", "利润率", "总资产周转率", "权益乘数" }));

		JsonObject res = (JsonObject) handlers.handle(StockChartJsonHandler.class.getName(), jo);
		System.out.println(res);
	}

	@Test
	public void testChartsJsonHandler() {

		JsonObject jo = new JsonObject();
		jo.add("corpId", new JsonPrimitive("000002"));
		jo.add("years", JsonUtil.newJsonArray(new int[] { 2016, 2015, 2014 }));
		JsonArray metricsArray = new JsonArray();

		metricsArray.add(JsonUtil.newJsonArray(new String[] { "净资产收益率" }));
		metricsArray.add(JsonUtil.newJsonArray(new String[] { "利润率" }));
		//metricsArray.add(JsonUtil.newJsonArray(new String[] { "总资产周转率" }));
		//metricsArray.add(JsonUtil.newJsonArray(new String[] { "权益乘数" }));
		//metricsArray.add(JsonUtil.newJsonArray(new String[] { "利润率", "总资产周转率", "权益乘数" }));

		jo.add("metricsArray", metricsArray);

		JsonObject res = (JsonObject) handlers.handle(StockChartsJsonHandler.class.getName(), jo);
		System.out.println(res);
	}
}
