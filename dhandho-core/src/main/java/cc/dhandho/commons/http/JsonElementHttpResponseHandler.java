package cc.dhandho.commons.http;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler3;
import com.google.gson.JsonElement;

import cc.dhandho.util.JsonUtil;
//import junit.framework.TestCase;

public class JsonElementHttpResponseHandler implements Handler3<CloseableHttpResponse, JsonElement> {
	@Override
	public JsonElement handle(CloseableHttpResponse arg0) {
		StatusLine sl = arg0.getStatusLine();
	//	TestCase.assertEquals(sl.toString(), 200, sl.getStatusCode());
		try {
			InputStream is = arg0.getEntity().getContent();
			JsonElement json = JsonUtil.parse(is, Charset.forName("utf8"));
			return json;
		} catch (UnsupportedOperationException e) {
			throw JcpsException.toRtException(e);
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}
}
