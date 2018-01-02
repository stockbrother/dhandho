package cc.dhandho.input.loader;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.Quarter;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.RtException;
import cc.dhandho.commons.container.Container;
import cc.dhandho.input.washed.GDBWashedFileSchemaLoader;
import cc.dhandho.input.washed.GDBWashedFileValueLoader;
import cc.dhandho.rest.server.DbProvider;

/**
 * Load CorpInfo data from file to DB.
 * 
 * @author wu TODO add virtual file system,for easiser testing.
 */
public class WashedInputDataLoader extends InputDataLoader {
	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOG = LoggerFactory.getLogger(WashedInputDataLoader.class);
	// public static String DATA_VERSION_CORP_INFO = "dataVersion_corpInfo";

	private DhandhoHome home;
	DbProvider dbp;
	ReportMetaInfos reportMetaInfos;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.dbp = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhandhoHome.class, true);
		this.reportMetaInfos = app.findComponent(ReportMetaInfos.class, true);
	}

	@Override
	public void handle(ODatabaseSession db) {
		try {

			FileObject dir = this.home.getInportWashedDataFolder();

			new GDBWashedFileSchemaLoader(this.dbp, dir, Quarter.Q4, (DbReportMetaInfos) this.reportMetaInfos)
					/* .limit(10) */.start();

			new GDBWashedFileValueLoader(this.dbp, dir, Quarter.Q4)/* .limit(10) */.start();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
