package cc.dhandho.rest;

import com.age5k.jcps.framework.container.Container;

import cc.dhandho.DhandhoHome;
import cc.dhandho.ReportMetaInfos;
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

	protected DhandhoHome home;

	protected ReportMetaInfos metaInfos;

	protected MetricDefines metricDefines;
	
	protected ReportEngine reportEngine;

	@Override
	public void setContainer(Container app) {
		this.app = app;
		this.dbProvider = app.findComponent(DbProvider.class, true);
		this.home = app.findComponent(DhandhoHome.class, true);
		this.metaInfos = app.findComponent(ReportMetaInfos.class, true);
		this.metricDefines = app.findComponent(MetricDefines.class, true);
		this.reportEngine = app.findComponent(ReportEngine.class, true);
	}

}
