package cc.dhandho.test.util;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonElement;

import cc.dhandho.DhandhoHome;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.server.DhandhoServer;

public class EmptyDhandhoServer implements DhandhoServer {

	@Override
	public DhandhoServer home(DhandhoHome home) {
		return null;
	}

	@Override
	public DhandhoHome getHome() {
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

	@Override
	public DhandhoServer dbConfig(DbConfig newInMemoryTestDbConfig) {
		return null;
	}

}
