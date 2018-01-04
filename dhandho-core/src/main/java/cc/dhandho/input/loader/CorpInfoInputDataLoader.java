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

import com.age5k.jcps.JcpsException;
import cc.dhandho.input.sse.SseCorpInfo2Loader;
import cc.dhandho.input.sse.SseCorpInfoLoader;
import cc.dhandho.input.szse.SzseCorpInfoLoader;
import cc.dhandho.rest.server.DhoServer;

/**
 * Load CorpInfo data from file to DB.
 * 
 * @author wu TODO add virtual file system,for easiser testing.
 */
public class CorpInfoInputDataLoader extends InputDataLoader {
	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOG = LoggerFactory.getLogger(DhoServer.class);

	@Override
	public void handle(ODatabaseSession db) {

		try {
			loadCorpInfo(db);
		} catch (IOException e) {
			throw new JcpsException(e);
		}

	}

	private void loadCorpInfo(ODatabaseSession db) throws IOException {
		{
			String name = "sse.corplist.csv";
			Reader r = getFileReader(name);
			if (r != null) {

				SseCorpInfoLoader l1 = new SseCorpInfoLoader(name);
				l1.setDb(db);
				l1.execute(r);
			}
		}
		{
			String name = "sse.corplist2.csv";
			Reader r = getFileReader(name);
			if (r != null) {

				SseCorpInfo2Loader l2 = new SseCorpInfo2Loader(name);
				l2.setDb(db);
				l2.execute(r);
			}
		}
		{
			String name = "szse.corplist.csv";
			Reader r = getFileReader(name);
			if (r != null) {
				SzseCorpInfoLoader l3 = new SzseCorpInfoLoader(name);
				l3.setDb(db);
				l3.execute(r);
			}
		}

	}

	private Reader getFileReader(String file) throws IOException {
		FileObject fileO = this.home.getInputCorpsFolder().resolveFile(file);
		if (fileO.exists()) {

			InputStream is = fileO.getContent().getInputStream();
			return new InputStreamReader(is, Charset.forName("UTF-8"));
		} else {
			LOG.warn("file not exists:" + fileO.getURL());
			return null;
		}
		
	}

}
