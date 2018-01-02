package cc.dhandho.input.loader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.RtException;
import cc.dhandho.input.sse.SseCorpInfo2Loader;
import cc.dhandho.input.sse.SseCorpInfoLoader;
import cc.dhandho.input.szse.SzseCorpInfoLoader;
import cc.dhandho.rest.server.DhandhoServer;

/**
 * Load CorpInfo data from file to DB.
 * 
 * @author wu TODO add virtual file system,for easiser testing.
 */
public class CorpInfoInputDataLoader extends InputDataLoader {
	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServer.class);

	@Override
	public void handle(ODatabaseSession db) {

		try {
			loadCorpInfo(db);
		} catch (IOException e) {
			throw new RtException(e);
		}

	}

	private void loadCorpInfo(ODatabaseSession db) throws IOException {
		SseCorpInfoLoader l1 = new SseCorpInfoLoader("sse.corplist.csv");
		l1.setDb(db);
		l1.execute(getFileReader("sse.corplist.csv"));

		SseCorpInfo2Loader l2 = new SseCorpInfo2Loader("sse.corplist2.csv");
		l2.setDb(db);
		l2.execute(getFileReader("sse.corplist2.csv"));

		SzseCorpInfoLoader l3 = new SzseCorpInfoLoader("szse.corplist.csv");
		l3.setDb(db);
		l3.execute(getFileReader("szse.corplist.csv"));

	}

	private Reader getFileReader(String file) throws IOException {
		FileObject fileO = this.home.getInputCorpsFolder().resolveFile(file);
		InputStream is = fileO.getContent().getInputStream();
		return new InputStreamReader(is, Charset.forName("UTF-8"));
	}

}
