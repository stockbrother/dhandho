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

		
		//TODO move file to other folder for not load again.
	}

}
