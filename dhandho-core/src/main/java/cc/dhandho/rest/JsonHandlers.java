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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;
import cc.dhandho.commons.container.Container;
import cc.dhandho.util.JsonUtil;

public class JsonHandlers {
	private static final Logger LOG = LoggerFactory.getLogger(JsonHandlers.class);

	public static Gson gson = new GsonBuilder().create();

	private Map<String, RestRequestHandler> handlerMap = new HashMap<>();

	private Container app;

	public JsonHandlers(Container app) {
		this.app = app;
	}

	public void handle(Class<? extends RestRequestHandler> handlerS, JsonReader reader, JsonWriter writer)
			throws IOException {
		handle(handlerS.getName(), reader, writer);
	}

	public void handle(String handlerS) throws IOException {
		handle(handlerS, JsonNull.INSTANCE);
	}

	public JsonElement handle(String handlerS, JsonElement request) throws IOException {
		Reader reader = new StringReader(request.toString());
		StringWriter writer = new StringWriter();
		this.handle(handlerS, reader, writer);
		return JsonUtil.parse(writer.getBuffer().toString());
	}

	public void handle(String handlerS, Reader reader, Writer writer) throws IOException {
		this.handle(handlerS, gson.newJsonReader(reader), gson.newJsonWriter(writer));
	}

	public void handle(String handlerS, JsonReader reader, JsonWriter writer) throws IOException {

		RestRequestHandler handler = this.resolveHandler(handlerS);

		handler.execute(new RestRequestContext(gson, reader, writer));

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
		if (handler != null) {
			return handler;
		}

		try {
			// TODO cache handler instance.
			Class handlerClass = Class.forName(handlerS);
			handler = (RestRequestHandler) app.newInstance(handlerClass);
		} catch (ClassNotFoundException e) {
			throw RtException.toRtException(e);
		}
		return handler;
	}

}
