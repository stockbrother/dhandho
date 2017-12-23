package cc.dhandho.server;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.AppContext;
import cc.dhandho.DhandhoHome;
import cc.dhandho.Quarter;
import cc.dhandho.RtException;
import cc.dhandho.importer.GDBWashedFileSchemaLoader;
import cc.dhandho.importer.GDBWashedFileValueLoader;

/**
 * Load CorpInfo data from file to DB.
 * 
 * @author wu TODO add virtual file system,for easiser testing.
 */
public class WashedDataUpgrader extends DbUpgrader {
	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOG = LoggerFactory.getLogger(WashedDataUpgrader.class);
	// public static String DATA_VERSION_CORP_INFO = "dataVersion_corpInfo";

	private DhandhoHome home;
	DbProvider dbp;

	@Override
	public void setAppContext(AppContext app) {
		super.setAppContext(app);
		this.dbp = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhandhoHome.class, true);
	}

	@Override
	public void process(ODatabaseSession db) {
		try {

			FileObject dir = this.home.getInportWashedDataFolder();

			new GDBWashedFileSchemaLoader(this.dbp, dir, Quarter.Q4)/* .limit(10) */.start();

			new GDBWashedFileValueLoader(this.dbp, dir, Quarter.Q4)/* .limit(10) */.start();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
