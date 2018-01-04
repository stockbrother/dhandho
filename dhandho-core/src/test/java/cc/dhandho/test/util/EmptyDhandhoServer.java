package cc.dhandho.test.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonElement;

import cc.dhandho.DhoDataHome;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.server.DhoServer;

public class EmptyDhandhoServer implements DhoServer {

	@Override
	public DhoServer home(DhoDataHome home) {
		return null;
	}

	@Override
	public DhoDataHome getHome() {
		return null;
	}

	@Override
	public void start() {

	}

	@Override
	public void shutdown() {

	}

	@Override
	public JsonElement handle(String handlerS, JsonElement request) throws IOException {

		return null;
	}

	@Override
	public void handle(String handlerS) throws IOException {

	}

	@Override
	public void handle(String handlerS, Reader reader, Writer writer) throws IOException {

	}

}
