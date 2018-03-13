package cc.dhandho.rest.server;

import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.container.impl.ContainerImpl;
import com.age5k.jcps.framework.lifecycle.ExecutorBasedServer;
import com.age5k.jcps.framework.lifecycle.ExecutorUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DbReportMetaInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.ReportMetaInfos;
import cc.dhandho.graphdb.MyDataUpgraders;
import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.report.ReportEngine;
import cc.dhandho.report.impl.ReportEngineImpl;
import cc.dhandho.rest.JsonHandlers;

/**
 * 
 * @author Wu 
 */
public class DhandhoServerImpl extends ExecutorBasedServer implements DhoServer {
	// private static final Logger LOG = LoggerFactory.getLogger();
	private static final Logger LOG = LoggerFactory.getLogger(DhandhoServerImpl.class);

	protected static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	JsonHandlers handlers;

	Container app;

	protected DhoDataHome dataHome;

	protected DbProvider dbProvider;

	public DhandhoServerImpl(DbProvider dbProvider) {
		this.dbProvider = dbProvider;
	}

	@Override
	public void start() {
		if (LOG.isInfoEnabled()) {
			LOG.info("start...");
		}
		super.start();

		new HomeFolderInitializer().initHomeFolder(dataHome);

		ReportMetaInfos metaInfos = new DbReportMetaInfos();
		MetricDefines metricDefines = new MetricDefinesLoader().load(dataHome);

		app = new ContainerImpl();
		app.addComponent(DhoDataHome.class, dataHome);
		app.addComponent(MetricDefines.class, metricDefines);
		app.addComponent(ReportMetaInfos.class, metaInfos);
		app.addComponent(DbProvider.class, this.dbProvider);

		ReportEngine reportEngine = app.newInstance(ReportEngineImpl.class);
		app.addComponent(ReportEngine.class, reportEngine);
		AllQuotesInfos allQuotesInfos = new AllQuotesInfos();
		app.addComponent(AllQuotesInfos.class, allQuotesInfos);
		InputDataMainLoader loader = app.addComponent(InputDataMainLoader.class, InputDataMainLoader.class);
		this.dbProvider.createDbIfNotExist();
		this.dbProvider.executeWithDbSession(new MyDataUpgraders());
		handlers = new JsonHandlers(app);
		
		// load it at server start.
		loader.load();
		this.dbProvider.executeWithDbSession(((DbReportMetaInfos) metaInfos).initializer());
		if (LOG.isInfoEnabled()) {
			LOG.info("done of start.");
		}
		
		// 
		app.addComponent(MyCorps.class).load();
		
	}

	/**
	 * If the home folder is not init before. And it is empty, then init it with the
	 * default content from res:// folder.
	 */

	@Override
	public void shutdown() {
		super.shutdown();		
		this.dbProvider.close();
	}

	@Override
	public void handle(final String handlerS, Reader reader, final Writer writer) {
		handlers.handle(handlerS, reader, writer);
	}

	@Override
	public void handle(final String handlerS) {
		handlers.handle(handlerS);
	}
//
//	public void handle(final String handlerS, JsonReader reader, final JsonWriter writer) {
//		handlers.handle(handlerS, reader, writer);
//	}

	@Override
	public JsonElement handle(String handlerS, JsonElement request) {
		return handlers.handle(handlerS, request);
	}

	@Override
	public DhoServer dataHome(DhoDataHome home) {
		this.dataHome = home;
		return this;
	}

	@Override
	public DhoDataHome getDataHome() {
		return dataHome;
	}

	@Override
	protected ExecutorService newExecutor() {
		//
		return ExecutorUtil.newScheduledThreadPool(DhandhoServerImpl.class.getName(), 1);
	}

}
