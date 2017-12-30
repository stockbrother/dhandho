package cc.dhandho.rest.handler;

import java.io.IOException;

import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;

import cc.dhandho.RtException;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.RestRequestHandler;

public class EchoJsonHandler implements RestRequestHandler {

	@Override
	public void handle(RestRequestContext rrc) {
		JsonElement json = Streams.parse(rrc.getReader());

		try {
			Streams.write(json, rrc.getWriter());
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
