package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public interface JsonHandler {

	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException;

}
