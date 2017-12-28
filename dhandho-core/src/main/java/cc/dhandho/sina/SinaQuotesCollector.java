package cc.dhandho.sina;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.vfs2.FileObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.dhandho.EnvUtil;
import cc.dhandho.commons.http.HttpClientFactory;
import cc.dhandho.commons.http.HttpResponseCallback;

public class SinaQuotesCollector {
	private static final Logger LOG = LoggerFactory.getLogger(SinaQuotesCollector.class);

	private HttpClientFactory clients;

	private static SimpleDateFormat DF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private String host = "vip.stock.finance.sina.com.cn";

	private int nextPage = 1;

	private int responses = 0;

	private int pageSize = 500;

	private boolean stop = false;

	private FileObject outputParentDir;

	private FileObject output;

	private int loops;

	/**
	 * Stop after loops exceed this setting.
	 */
	private int maxLoops = Integer.MAX_VALUE;

	public SinaQuotesCollector() {
		this.clients = HttpClientFactory.newInstance();
		if (EnvUtil.isProxyEnabled()) {
			this.clients.setProxy(EnvUtil.getProxyHome(), EnvUtil.getProxyPort());
		}
		this.clients.setPauseInterval(5 * 1000);//

	}

	public SinaQuotesCollector maxLoops(int max) {
		this.maxLoops = max;
		return this;
	}

	/**
	 * Wait awhile between two loops in millisecond
	 * 
	 * @param pause
	 * @return
	 */
	public SinaQuotesCollector pauseInterval(long pause) {
		this.clients.setPauseInterval(pause);//
		return this;
	}

	public SinaQuotesCollector outputParentDir(FileObject output) {
		this.outputParentDir = output;
		return this;
	}

	public FileObject start() throws IOException {
		if (!this.outputParentDir.exists()) {
			this.outputParentDir.createFolder();
		}

		FileObject output = null;
		while (true) {
			String name = DF.format(new Date());
			output = this.outputParentDir.resolveFile(name);
			if (output.exists()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
				continue;
			}
			break;

		}
		output.createFolder();

		this.output = output;

		LOG.info("download all-quotes from sina to folder:" + this.output.getURL());

		HttpResponseCallback hep = new HttpResponseCallback() {

			@Override
			public void onResponse(CloseableHttpResponse response) throws IOException {
				SinaQuotesCollector.this.onResponse(response);
			}
		};

		Iterator<String> uriIt = new Iterator<String>() {

			@Override
			public boolean hasNext() {
				return SinaQuotesCollector.this.hasNext();
			}

			@Override
			public String next() {
				return SinaQuotesCollector.this.next();
			}
		};

		this.clients.get(host, uriIt, hep);

		return this.output;

	}

	protected void onResponse(CloseableHttpResponse response) throws IOException {
		this.responses++;
		StringBuffer sb = new StringBuffer();
		for (Header header : response.getAllHeaders()) {
			sb.append("(").append(header.getName() + "=" + header.getValue()).append("),");
		}
		LOG.info("onResponse,statusLine:" + response.getStatusLine() + sb.toString());

		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode != 200) {
			LOG.error("stop for statusCode:" + stateCode + " not expected.");
			this.stop = true;
			return;
		}

		Header headerContentType = response.getFirstHeader("Content-Type");
		String contentType = headerContentType.getValue();
		if (contentType.startsWith("text/html")) {
			LOG.error("stop contentType:" + contentType + " not expected.");
			this.stop = true;
			return;
		}

		FileObject outputFile = output.resolveFile("Market_Center.getHQNodeData.p" + this.responses + ".json");
		Header h = response.getEntity().getContentEncoding();
		FileObject workFile = null;
		int i = 0;

		while (true) {

			workFile = output.resolveFile(outputFile.getName().getBaseName() + ".work" + (i++));
			if (!workFile.exists()) {
				LOG.info("found the last work file idx:" + i);
				break;
			}
		}

		OutputStream os = workFile.getContent().getOutputStream();
		response.getEntity().writeTo(os);

		os.close();
		workFile.moveTo(outputFile);

		LOG.info("got the response and write it to file:" + outputFile.getURL());//

		Reader fr = new InputStreamReader(outputFile.getContent().getInputStream());
		char[] line = new char[4];
		int len = fr.read(line);
		if ("null".equals(new String(line, 0, len))) {
			// the last file content is 'null'.
			this.stop = true;
		}

	}

	protected String next() {
		StringBuffer sb = new StringBuffer();
		sb.append("/quotes_service/api/json_v2.php/Market_Center.getHQNodeData")//
				.append("?num=").append(this.pageSize)//
				.append("&sort=symbol")//
				.append("&asc=0")//
				.append("&node=hs_a")//
				.append("&symbol=")//
				.append("&_s_r_a=page")//
				.append("&page=").append(this.nextPage)//
		;
		this.nextPage++;
		return sb.toString();
	}

	protected boolean hasNext() {
		if (this.maxLoops <= this.loops) {
			LOG.warn("max loops exeed:" + this.maxLoops);
			this.stop = true;
		}
		this.loops++;
		return !this.stop;
	}
}
