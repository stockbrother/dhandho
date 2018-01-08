package cc.dhandho.rest;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.provider.Provider;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.rest.server.DbProvider;

/**
 * AppContext.newInstance();
 * 
 * @author Wu
 *
 */
public abstract class AbstractRestRequestHandler implements RestRequestHandler, Container.Aware {

	protected Container app;

	protected DbProvider dbProvider;

	protected DhoDataHome dataHome;

	protected ReportMetaInfos metaInfos;

	protected MetricDefines metricDefines;

	protected ReportEngine reportEngine;

	protected AllQuotesInfos allQuotesInfos;

	protected Provider<MyCorps> myCorpsProvider;

	@Override
	public void setContainer(Container app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.dataHome = app.findComponent(DhoDataHome.class, true);
		this.metaInfos = app.findComponent(ReportMetaInfos.class, true);
		this.metricDefines = app.findComponent(MetricDefines.class, true);
		this.reportEngine = app.findComponent(ReportEngine.class, true);
		this.allQuotesInfos = app.findComponent(AllQuotesInfos.class, true);
		this.myCorpsProvider = app.findComponentLater(MyCorps.class, true);
	}

}
