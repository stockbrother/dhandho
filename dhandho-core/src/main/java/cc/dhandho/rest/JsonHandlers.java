package cc.dhandho.rest;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AppContext;
import cc.dhandho.RtException;

public class JsonHandlers {

	public static Gson gson = new GsonBuilder().create();

	private Map<String, JsonHandler> handlerMap = new HashMap<>();

	private AppContext app;

	public JsonHandlers(AppContext app) {
		this.app = app;
	}

	public void handle(Class<? extends JsonHandler> handlerS, Reader reader, Writer writer) throws IOException {
		handle(handlerS.getName(), reader, writer);
	}

	public void handle(String handlerS, Reader reader, Writer writer) throws IOException {

		JsonReader jsonReader = gson.newJsonReader(reader);
		JsonWriter jsonWriter = gson.newJsonWriter(writer);

		JsonHandler handler = this.resolveHandler(handlerS);

		handler.execute(gson, jsonReader, jsonWriter);

	}

	private JsonHandler resolveHandler(String handlerS) {
		JsonHandler handler = handlerMap.get(handlerS);
		if (handler != null) {
			return handler;
		}

		try {
			// TODO cache handler instance.
			Class handlerClass = Class.forName(handlerS);
			handler = (JsonHandler) app.newInstance(handlerClass);
		} catch (ClassNotFoundException e) {
			throw RtException.toRtException(e);
		}
		return handler;
	}

}
