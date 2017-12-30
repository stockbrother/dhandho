package cc.dhandho.rest.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.apache.commons.vfs2.FileObject;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.input.sse.SseCorpInfo2Loader;
import cc.dhandho.input.sse.SseCorpInfoLoader;
import cc.dhandho.input.szse.SzseCorpInfoLoader;
import cc.dhandho.rest.RestRequestContext;

/**
 * Import corp info from class path to DB.
 * 
 * @author Wu
 *
 */
public class LoadCorpInfoJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {

		SseCorpInfoLoader l1 = new SseCorpInfoLoader("sse.corplist.csv");
		l1.setDb(db);
		l1.execute(getFileReader("sse.corplist.csv"));

		SseCorpInfo2Loader l2 = new SseCorpInfo2Loader("sse.corplist2.csv");
		l2.setDb(db);
		l2.execute(getFileReader("sse.corplist2.csv"));

		SzseCorpInfoLoader l3 = new SzseCorpInfoLoader("szse.corplist.csv");
		l3.setDb(db);
		l3.execute(getFileReader("szse.corplist.csv"));
		//TODO move file to other folder for not load again.
	}

	private Reader getFileReader(String file) throws IOException {
		FileObject fileO = this.home.getInportCorpsFile().resolveFile(file);
		InputStream is = fileO.getContent().getInputStream();
		return new InputStreamReader(is, Charset.forName("UTF-8"));
	}

}
