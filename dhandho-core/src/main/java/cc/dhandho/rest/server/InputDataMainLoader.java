package cc.dhandho.rest.server;

import com.age5k.jcps.framework.container.Container;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.InputDataLoader;
import cc.dhandho.input.loader.ArchivableCorpInfoInputDataLoader;
import cc.dhandho.input.loader.ArchivableWashedInputDataLoader;

public class InputDataMainLoader implements Container.Aware, InputDataLoader {

	Container app;
	DhoDataHome home;
	AllQuotesInfos allQuotesInfos;
	DbProvider dbProvider;

	@Override
	public void setContainer(Container app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhoDataHome.class, true);
		// this.metaInfos = app.findComponent(ReportMetaInfos.class, true);
		// this.metricDefines = app.findComponent(MetricDefines.class, true);
		// this.reportEngine = app.findComponent(ReportEngine.class, true);
		this.allQuotesInfos = app.findComponent(AllQuotesInfos.class, true);
	}

	@Override
	public void load() {
		// all quotesInfos should not be archived, for that it is in RAM.
		new AllQuotesLoader().load(this.home, allQuotesInfos);

		// load corp info to DB.
		ArchivableCorpInfoInputDataLoader dbu = app.newInstance(ArchivableCorpInfoInputDataLoader.class);
		this.dbProvider.executeWithDbSession(dbu);
		// load washed data to DB.
		ArchivableWashedInputDataLoader wdu = app.newInstance(ArchivableWashedInputDataLoader.class);
		this.dbProvider.executeWithDbSession(wdu);

	}
}
