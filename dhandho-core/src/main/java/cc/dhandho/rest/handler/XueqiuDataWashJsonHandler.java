package cc.dhandho.rest.handler;

import java.io.File;
import java.nio.charset.Charset;

import cc.dhandho.input.xueqiu.XueqiuDataWasher;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;

public class XueqiuDataWashJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void handleInternal(RestRequestContext rrc) {

		File source = new File("C:\\dhandho\\data\\xueqiu\\raw");
		File target = new File("C:\\dhandho\\data\\xueqiu\\washed");

		XueqiuDataWasher washer = new XueqiuDataWasher(source, Charset.forName("UTF-8"), target);
		// washer.setLimitOfFiles(1);
		washer.types("balsheet", "cfstatement", "incstatement");

		washer.execute();

	}

}
