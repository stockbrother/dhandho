package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class EchoJsonHandler implements JsonHandler{

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException{
		JsonElement json = Streams.parse(reader);		
		Streams.write(json, writer);
	}

}
