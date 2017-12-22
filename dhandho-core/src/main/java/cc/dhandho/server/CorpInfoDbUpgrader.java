package cc.dhandho.server;

import java.io.IOException;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;

import cc.dhandho.RtException;
import cc.dhandho.rest.LoadCorpInfoJsonHandler;
import cc.dhandho.util.DbInitUtil;
import cc.dhandho.util.JsonUtil;

/**
 * Load CorpInfo data from file to DB.
 * 
 * @author wu TODO add virtual file system,for easiser testing.
 */
public class CorpInfoDbUpgrader extends DbUpgrader {
	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServer.class);
	public static String DATA_VERSION_CORP_INFO = "dataVersion_corpInfo";

	@Override
	public void process(ODatabaseSession db) {

		OVertex meta = DbInitUtil.getMetaInfo(db);
		Integer dv = meta.getProperty(DATA_VERSION_CORP_INFO);
		if (dv == null) {
			meta.setProperty(DATA_VERSION_CORP_INFO, 0);
			meta.save();
			dv = meta.getProperty(DATA_VERSION_CORP_INFO);
		}
	
		if (dv == 0) {
			LOG.info(DATA_VERSION_CORP_INFO + ":" + 0);
			try {
				loadCorpInfo();
			} catch (IOException e) {
				throw new RtException(e);
			}
			
			meta = DbInitUtil.getMetaInfo(db);
			meta.setProperty(DATA_VERSION_CORP_INFO, 1);
			meta.save();
			
			LOG.info(DATA_VERSION_CORP_INFO + ":" + 1);
		} else if (dv == 1) {
			LOG.info(DATA_VERSION_CORP_INFO + ":" + 1);
		} else {
			throw new RtException("bug");
		}

	}

	private void loadCorpInfo() throws IOException {

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

}
