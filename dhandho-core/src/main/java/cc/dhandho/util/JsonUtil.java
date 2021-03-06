package cc.dhandho.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import com.age5k.jcps.JcpsException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class JsonUtil {
	protected static Gson GSON4LOG = new GsonBuilder().setPrettyPrinting().create();

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	public static void write(JsonElement json, JsonWriter writer) {
		GSON.toJson(json, writer);
	}

	public static void write(JsonElement json, Appendable writer) {
		GSON.toJson(json, writer);
	}

	public static JsonReader toJsonReader(JsonElement json) {
		return toJsonReader(JsonUtil.toString(json, true));
	}

	public static JsonReader toJsonReader(String jsonString) {
		return GSON.newJsonReader(new StringReader(jsonString));
	}

	public static JsonWriter newJsonWriter(Appendable app) {
		return newJsonWriter(Streams.writerForAppendable(app));
	}

	public static JsonWriter newJsonWriter(Writer writer) {
		try {
			return GSON.newJsonWriter(writer);
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static JsonReader newJsonReader(String string) {
		return newJsonReader(new StringReader(string));
	}

	public static JsonReader newJsonReader(Reader reader) {
		return GSON.newJsonReader(reader);
	}

	public static JsonElement parse(JsonReader reader) {
		return new JsonParser().parse(reader);
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

	public static <T> JsonArray toJsonArray(Collection<T> values) {
		return toJsonArray(values.toArray());
	}

	public static JsonArray toJsonArray(String[] values) {
		//
		JsonArray rt = new JsonArray();
		for (String s : values) {
			rt.add(s);
		}
		return rt;
	}

	public static String toString(JsonElement json) {
		return toString(json, true);
	}

	public static String toString(JsonElement json, boolean pretty) {
		//
		StringBuilder sb = new StringBuilder();
		JsonUtil.write(json, sb);
		return sb.toString();
	}

	public static void value(String value, JsonWriter w) {
		try {
			w.value(value);
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static void beginArray(JsonWriter w) {
		try {
			w.beginArray();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static void endArray(JsonWriter w) {
		try {
			w.endArray();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public static <T> JsonArray toJsonArray(T[] array) {
		JsonArray rt = new JsonArray();
		for (T s : array) {
			if (s instanceof String) {
				rt.add((String) s);
			} else if (s instanceof Number) {
				rt.add((Number) s);
			} else if (s instanceof Boolean) {
				rt.add((Boolean) s);
			} else if (s instanceof Character) {
				rt.add((Character) s);
			} else if (s instanceof JsonElement) {
				rt.add((JsonElement) s);
			} else {
				throw new JcpsException("no supported.");
			}
		}

		return rt;
	}

	public static void array(String[] types, JsonWriter w) {
		beginArray(w);
		for (String v : types) {

			JsonUtil.value(v, w);
		}
		endArray(w);
	}

	public static JsonObject toJsonObject(Map<String, Object> result) {
		JsonObject rt = new JsonObject();
		for (Map.Entry<String, Object> en : result.entrySet()) {
			Object value = en.getValue();
			String key = en.getKey();
			if (value == null) {
				rt.add(key, JsonNull.INSTANCE);
			} else if (value instanceof Number) {
				rt.addProperty(key, (Number) value);
			} else if (value instanceof String) {
				rt.addProperty(key, (String) value);
			} else if (value instanceof Boolean) {
				rt.addProperty(key, (Boolean) value);
			} else if (value instanceof JsonElement) {
				rt.add(key, (JsonElement) value);
			} else {
				throw new JcpsException("type not supported:" + value.getClass().getName());
			}

		}
		return rt;
	}

	public static JsonElement parse(InputStream is, Charset forName) {
		return Streams.parse(new JsonReader(new InputStreamReader(is, forName)));
	}

	public static int[] getAsIntArray(JsonArray array) {

		//
		int[] rt = new int[array.size()];
		for (int i = 0; i < rt.length; i++) {
			rt[i] = array.get(i).getAsInt();
		}
		return rt;

	}

	public static JsonArray newJsonArray(int[] is) {
		JsonArray rt = new JsonArray();
		for (int vI : is) {
			rt.add(vI);
		}
		return rt;
	}

	public static JsonArray newJsonArray(String[] is) {
		JsonArray rt = new JsonArray();
		for (String vI : is) {
			rt.add(vI);
		}
		return rt;
	}

}
