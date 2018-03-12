package cc.dhandho.test.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.handler.StockDetailJsonHandler;
import cc.dhandho.rest.server.DbProvider;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.JsonUtil;

public class StockDetailRestTest {
	@Test
	public void test() {

		Container app = new ContainerImpl();
		// TODO mock-able the DB provider.
		DbProvider dbProvider = mock(DbProvider.class); // TestUtil.newInMemoryTestDbProvider(true);
		app.addComponent(DbProvider.class, dbProvider);

		// mock the report engine.
		ReportEngine reportEngine = mock(ReportEngine.class);

		doAnswer(new Answer<Double>() {
			@Override
			public Double answer(InvocationOnMock invocation) throws Throwable {
				return 1000000000D;
			}
		}).when(reportEngine).getMetricValue(any(String.class), any(Integer.class), any(String.class));

		doAnswer(new Answer<String>() {
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				return "平安银行";
			}
		}).when(reportEngine).getCorpName(any(String.class));

		app.addComponent(ReportEngine.class, reportEngine);
		// mock the All quotes infos.
		AllQuotesInfos aqis = mock(AllQuotesInfos.class);
		doAnswer(new Answer<Double>() {

			@Override
			public Double answer(InvocationOnMock invocation) throws Throwable {
				return 13.29D;
			}
		}).when(aqis).getBuyPrice(any(String.class));
		doAnswer(new Answer<Long>() {

			@Override
			public Long answer(InvocationOnMock invocation) throws Throwable {
				return 100001L;
			}
		}).when(aqis).getLastLoaded();

		app.addComponent(AllQuotesInfos.class, aqis);

		//
		JsonHandlers handlers = new JsonHandlers(app);

		JsonObject jo = new JsonObject();
		jo.add("corpId", new JsonPrimitive("000001"));

		JsonObject res = (JsonObject) handlers.handle(StockDetailJsonHandler.class.getName(), jo);
		String expectedResponse = "{" + //
				"				'type': 'map'," + //
				"				'mapData': {" + //
				"                 'corpId': '000001'," + //
				"                 'corpName': '平安银行'," + //
				"                 'unitPrice': 13.29," + //
				"                 'priceDate': 100001," + //
				"                 'totalPrice': 1.329E10" + //
				"				}" + //
				"			}";
		JsonElement je = JsonUtil.parse(expectedResponse);

		TestUtil.assertEqual(je, res);

	}
}
