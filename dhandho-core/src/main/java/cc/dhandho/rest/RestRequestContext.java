package cc.dhandho.rest;

import java.io.IOException;

import com.age5k.jcps.JcpsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class RestRequestContext {
	public static Gson GSON = new GsonBuilder().create();
	JsonReader reader;
	JsonWriter writer;
	Gson gson;

	public RestRequestContext(JsonReader reader, JsonWriter writer) {
		this(GSON, reader, writer);
	}

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

	public <T extends JsonElement> T parseReader() {
		return (T)Streams.parse(reader);
	}

	public void write(JsonElement res) {
		try {
			writer.jsonValue(res.toString());
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}
}
