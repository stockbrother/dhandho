package cc.dhandho.test.rest;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.handler.StockListJsonHandler;
import cc.dhandho.test.util.TestUtil;
import cc.dhandho.util.JsonUtil;
import junit.framework.TestCase;

public class StockListRestTest extends TestCase {

	public void test() {
		
		Container app = TestUtil.populateDbWithDefaultDataHomeForTest(new ContainerImpl());
		JsonHandlers handlers = new JsonHandlers(app);
				
		JsonObject jo = new JsonObject();
		JsonObject res = (JsonObject) handlers.handle(StockListJsonHandler.class.getName(), jo);
		String expectedResponse = "{" +// 
				"				'type': 'table'," +// 
				"				'columnNames': [" + //
				"					'corpId'," + //
				"					'corpName'" + //
				"				]," + //
				"				'rowArray': [" +// 
				"					['000001', '平安银行']," +//
				"					['000002', '万  科Ａ']," +//
				"					['000004', '国农科技']," +//
				"					['000005', '世纪星源']," +//
				"					['000006', '深振业Ａ']," +//
				"					['000100', 'TCL 集团']," +//
				"					['600000', '浦发银行']," +//
				"					['600004', '白云机场']," +//
				"					['600006', '东风汽车']," +//
				"					['600007', '中国国贸']," +//
				"					['600008', '首创股份']," +//
				"					['600009', '上海机场']" +//				
				"				]" + 
				"			}";
		JsonElement je = JsonUtil.parse(expectedResponse);
		
		TestUtil.assertEqual(je, res);

	}
}
