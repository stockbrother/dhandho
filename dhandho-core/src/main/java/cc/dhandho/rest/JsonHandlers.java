package cc.dhandho.rest;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.rest.handler.StockChartsJsonHandler;
import cc.dhandho.rest.handler.StockDetailJsonHandler;
import cc.dhandho.rest.handler.StockListJsonHandler;
import cc.dhandho.util.FileUtil;
import cc.dhandho.util.JsonUtil;

public class JsonHandlers {
	private static final Logger LOG = LoggerFactory.getLogger(JsonHandlers.class);

	public static Gson gson = new GsonBuilder().create();

	private Map<String, String> aliasHandlerMap = new HashMap<>();

	private Map<String, RestRequestHandler> handlerMap = new HashMap<>();

	private Container app;

	private boolean trace = true;

	public JsonHandlers(Container app) {
		this.app = app;
		aliasHandlerMap.put("stock-list", StockListJsonHandler.class.getName());
		aliasHandlerMap.put("stock-detail", StockDetailJsonHandler.class.getName());
		aliasHandlerMap.put("stock-charts", StockChartsJsonHandler.class.getName());

	}

	public void handle(Class<? extends RestRequestHandler> handlerS, JsonReader reader, JsonWriter writer)
			throws IOException {
		handle(handlerS.getName(), reader, writer);
	}

	public void handle(String handlerS) {
		handle(handlerS, JsonNull.INSTANCE);
	}

	public JsonElement handle(String handlerS, JsonElement request) {
		Reader reader = new StringReader(request.toString());
		StringWriter writer = new StringWriter();
		this.handle(handlerS, reader, writer);
		return JsonUtil.parse(writer.getBuffer().toString());
	}

	public void handle(String handlerS, Reader reader, Writer writer) {

		if (this.trace) {
			String string = FileUtil.readAsString(reader);
			LOG.info(string);
			reader = new StringReader(string);

			StringWriter swriter = new StringWriter();
			this.handle(handlerS, gson.newJsonReader(reader), JsonUtil.newJsonWriter(swriter));
			String response = swriter.getBuffer().toString();
			LOG.info("response:" + response);
			try {
				writer.write(response);
			} catch (IOException e) {
				throw JcpsException.toRtException(e);
			}
		} else {
			this.handle(handlerS, gson.newJsonReader(reader), JsonUtil.newJsonWriter(writer));
		}

	}

	public void handle(String handlerS, JsonReader reader, JsonWriter writer) {

		RestRequestHandler handler = this.resolveHandler(handlerS);

		handler.handle(new RestRequestContext(gson, reader, writer));

	}
	// TODO add data version for corpInfo and do not load twice for the same version
	// of data.

	private Reader traceReader(Reader reader) throws IOException {
		StringWriter swriter = new StringWriter();
		char[] cbuf = new char[1024];
		while (true) {
			int len = reader.read(cbuf);
			if (len == -1) {
				break;
			}
			swriter.write(cbuf, 0, len);

		}
		LOG.trace(":start of request.reader:");
		LOG.trace(swriter.getBuffer().toString());
		LOG.trace(":end of request.reader:");

		return new StringReader(swriter.getBuffer().toString());
	}

	private RestRequestHandler resolveHandler(String handlerS) {

		RestRequestHandler handler = handlerMap.get(handlerS);
		if (handler != null) {// got in cache
			return handler;
		}

		String className = this.aliasHandlerMap.get(handlerS);
		if (className != null) {
			handler = handlerMap.get(className);
			if (handler != null) {
				return handler;
			}
		} else {
			className = handlerS;
		}

		try {
			// TODO cache handler instance.
			Class handlerClass = Class.forName(className);
			handler = (RestRequestHandler) app.newInstance(handlerClass);
		} catch (ClassNotFoundException e) {
			throw JcpsException.toRtException(e);
		}
		return handler;
	}

}
