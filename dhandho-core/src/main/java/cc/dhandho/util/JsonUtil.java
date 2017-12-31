package cc.dhandho.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.RtException;

public class JsonUtil {
	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void write(JsonElement json, JsonWriter writer) {
		GSON.toJson(json, writer);
	}

	public static void write(JsonElement json, Writer writer) {
		GSON.toJson(json, writer);
	}

	public static JsonReader toJsonReader(String jsonString) {
		return GSON.newJsonReader(new StringReader(jsonString));
	}

	public static JsonWriter newJsonWriter(Writer writer) {
		try {
			return GSON.newJsonWriter(writer);
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}

	public static JsonReader newJsonReader(String string) {
		return newJsonReader(new StringReader(string));
	}

	public static JsonReader newJsonReader(Reader reader) {
		return GSON.newJsonReader(reader);
	}

	public static JsonElement parse(String jsonS) {
		return new JsonParser().parse(jsonS);
	}

	public static JsonElement parse(Reader r) {
		return new JsonParser().parse(r);
	}

	public static JsonReader toJsonReader(JsonArray json) {
		StringWriter sWriter = new StringWriter();
		write(json, sWriter);
		return new JsonReader(new StringReader(sWriter.getBuffer().toString()));
	}

	public static String[] getAsStringArray(JsonArray array) {
		//
		String[] rt = new String[array.size()];
		for (int i = 0; i < rt.length; i++) {
			rt[i] = array.get(i).getAsString();
		}
		return rt;
	}

	public static JsonElement toJsonArray(String[] values) {
		//
		JsonArray rt = new JsonArray();
		for (String s : values) {
			rt.add(s);
		}
		return rt;
	}

}
