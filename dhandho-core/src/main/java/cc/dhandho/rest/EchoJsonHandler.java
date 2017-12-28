package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;

public class EchoJsonHandler implements RestRequestHandler {

	@Override
	public void execute(RestRequestContext rrc) throws IOException {
		JsonElement json = Streams.parse(rrc.getReader());
		Streams.write(json, rrc.getWriter());
	}

}
