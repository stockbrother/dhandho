package cc.dhandho.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RestRequestContext {
	Gson gson;
	JsonReader reader;
	JsonWriter writer;

	public RestRequestContext(Gson gson, JsonReader reader, JsonWriter writer) {
		this.gson = gson;
		this.reader = reader;
		this.writer = writer;
	}

	public Gson getGson() {
		return gson;
	}

	public JsonReader getReader() {
		return reader;
	}

	public JsonWriter getWriter() {
		return writer;
	}
	
	public JsonElement parseReader() {
		return Streams.parse(reader);
	}
}
