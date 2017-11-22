package cc.dhandho.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.RtException;
import cc.dhandho.importer.SseCorpInfo2Loader;
import cc.dhandho.importer.SseCorpInfoLoader;
import cc.dhandho.importer.SzseCorpInfoLoader;

/**
 * Import corp info from class path to DB.
 * 
 * @author Wu
 *
 */
public class LoadCorpInfoJsonHandler extends AppContextAwareJsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {

		ODatabaseSession db = DbSessionTL.get(true);
		
		SseCorpInfoLoader l1 = new SseCorpInfoLoader();
		l1.setDb(db);
		l1.execute(getResource("cc/dhandho/load/sse.corplist.csv"));

		SseCorpInfo2Loader l2 = new SseCorpInfo2Loader();
		l2.setDb(db);
		l2.execute(getResource("cc/dhandho/load/sse.corplist2.csv"));

		SzseCorpInfoLoader l3 = new SzseCorpInfoLoader();
		l3.setDb(db);
		l3.execute(getResource("cc/dhandho/load/szse.corplist.csv"));

	}

	private Reader getResource(String resource) {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(resource);
		if (is == null) {
			throw new RtException("resource not found:" + resource);
		}
		return new InputStreamReader(is, Charset.forName("UTF-8"));
	}

}
