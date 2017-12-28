package cc.dhandho.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import cc.dhandho.xueqiu.XueqiuDataWasher;

public class XueqiuDataWashJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void execute(RestRequestContext rrc) throws IOException {
		File source = new File("C:\\dhandho\\data\\xueqiu\\raw");
		File target = new File("C:\\dhandho\\data\\xueqiu\\washed");

		XueqiuDataWasher washer = new XueqiuDataWasher(source, Charset.forName("UTF-8"), target);
		// washer.setLimitOfFiles(1);
		washer.types("balsheet", "cfstatement", "incstatement");

		washer.execute();

	}

}
