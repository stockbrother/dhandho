package cc.dhandho.commons.http;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.age5k.jcps.framework.handler.Handler3;

public class HttpClientUtil {

	public static <T> T doHttpGet(String host, int port, String uri, Handler3<CloseableHttpResponse, T> responseHandler)
			throws IOException {

		CloseableHttpClient httpclient = HttpClients.custom().build();
		try {

			HttpHost target = new HttpHost(host, port, "http");

			RequestConfig.Builder cb = RequestConfig.custom();

			RequestConfig config = cb.build();

			HttpGet httpget = new HttpGet(uri);
			httpget.setConfig(config);

			CloseableHttpResponse response = httpclient.execute(target, httpget);
			try {
				return responseHandler.handle(response);//
			} finally {
				response.close();
			}

		} finally {
			httpclient.close();
		}

	}
}
