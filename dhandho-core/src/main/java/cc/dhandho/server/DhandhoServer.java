package cc.dhandho.server;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.AppContext;
import cc.dhandho.AppContextImpl;
import cc.dhandho.RtException;
import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.rest.JsonHandlers;
import cc.dhandho.rest.LoadCorpInfoJsonHandler;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;

public class DhandhoServer {
	// private static final Logger LOG = LoggerFactory.getLogger();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServer.class);

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	JsonHandlers handlers;

	AppContext app;
	DbConfig dbConfig;

	public DhandhoServer dbConfig(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
		return this;
	}

	public void start() {
		if(LOG.isInfoEnabled()) {
			LOG.info("start...");
		}
		try {
			this.doStart();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
		if(LOG.isInfoEnabled()) {
			LOG.info("start done");
		}
		
	}

	private void doStart() throws IOException {
		// app = new AppContext()
		// .dbConfig(new
		// DbConfig().dbUrl(dbUrl).dbName(dbName).dbUser(dbUser).dbPassword(dbPassword)).create();
		app = new AppContextImpl()
				// .dbConfig(new
				// DbConfig().dbName("test").dbUrl("memory:test").dbUser("admin").dbPassword("admin"))
				.dbConfig(dbConfig).create();

		handlers = new JsonHandlers(app);
		DbInitUtil.initDb(app);
		loadCorpInfo();
		//

		// TODO
		// File dir = new
		// File("D:/git/daydayup/dhandho/dhandho-core/src/test/resources/cc/dhandho/test/washed2"
		// .replace('/', File.separatorChar));

		// new GDBWashedFileSchemaLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();
		// new GDBWashedFileValueLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();
	}

	public void loadCorpInfo() throws IOException {
		
		LOG.warn("loading corp info may take too much time and block the current thread for a while,please wait ...");
		
		String jsonS = ("{" //
				+ "}" // end of message
		).replaceAll("'", "\"");

		JsonReader reader = JsonUtil.toJsonReader(jsonS);
		StringWriter sWriter = new StringWriter();
		JsonWriter writer = JsonUtil.newJsonWriter(sWriter);
		LoadCorpInfoJsonHandler h = app.newInstance(LoadCorpInfoJsonHandler.class);
		h.execute(GSON, reader, writer);
		LOG.info("done of loading corp info.");
		LOG.info(sWriter.getBuffer().toString());

	}

	private Reader traceReader(Reader reader) throws IOException {
		StringWriter swriter = new StringWriter();
		char[] cbuf = new char[1024];
		while (true) {
			int len = reader.read(cbuf);
			if (len == -1) {
				break;
			}
			swriter.write(cbuf, 0, len);

		}
		LOG.trace(":start of request.reader:");
		LOG.trace(swriter.getBuffer().toString());
		LOG.trace(":end of request.reader:");

		return new StringReader(swriter.getBuffer().toString());
	}

	public void handle(final String handlerS, Reader reader, final Writer writer) throws ServletException, IOException {

		if (LOG.isTraceEnabled()) {
			reader = traceReader(reader);
		}

		Reader readerF = reader;

		StringWriter writerF = new StringWriter();
		handlers.handle(handlerS, readerF, writerF);
		if (LOG.isTraceEnabled()) {
			LOG.trace(":start of response.writer:");
			LOG.trace(writerF.getBuffer().toString());
			LOG.trace(":end of response.writer:");
		}

		writer.write(writerF.getBuffer().toString());

	}

}
