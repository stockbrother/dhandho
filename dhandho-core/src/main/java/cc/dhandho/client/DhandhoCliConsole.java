package cc.dhandho.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.vfs2.FileObject;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.handler.Handler;

import cc.dhandho.DhoDataHome;
import cc.dhandho.client.handler.CatCommandHandler;
import cc.dhandho.client.handler.ClearScreenCommandHandler;
import cc.dhandho.client.handler.CommandHandler;
import cc.dhandho.client.handler.CorpChartCommandHandler;
import cc.dhandho.client.handler.DupontAnalysisCommandHandler;
import cc.dhandho.client.handler.ExitCommandHandler;
import cc.dhandho.client.handler.HelpCommandHandler;
import cc.dhandho.client.handler.InputDataLoadCommandHandler;
import cc.dhandho.client.handler.ShowCommandHandler;
import cc.dhandho.client.handler.SinaDataLoadCommandHandler;
import cc.dhandho.client.handler.SqlQueryCommandHandler;
import cc.dhandho.commons.commandline.AbstractComandLineApp;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.rest.server.DhoServer;

public class DhandhoCliConsole extends AbstractComandLineApp {
	protected DhoServer server;

	protected List<Handler> beforeShutdownHandlerList = new ArrayList<>();

	protected Map<String, CommandHandler> handlerMap = new HashMap<>();

	@Deprecated // move to server side.
	protected MetricDefines metrics;

	public HtmlRenderer htmlRenderer;

	public DhoServer getServer() {
		return server;
	}

	public DhandhoCliConsole server(DhoServer server) {
		this.server = server;
		return this;
	}

	@Override
	public Future<Object> start() {
		Future<Object> rt = super.start();

		this.echo(false);
		this.addCommand(new CommandType("help", "Print this message!").addDesc("\n"), new HelpCommandHandler());

		this.addCommand(new CommandType("cls", "Clear screen!"), new ClearScreenCommandHandler());
		this.addCommand(new CommandType("exit", "Exit!"), new ExitCommandHandler());

		this.addCommand(new CommandType("cat", "Print file content!")//
				.addOption(CatCommandHandler.OPT_f, "file", true, "Show file content.", true) //
				, new CatCommandHandler());

		this.addCommand(new CommandType("chart", "Show metric value as SVG chart for corpId, years and metrics!"),
				new CorpChartCommandHandler());

		this.addCommand(new CommandType("dupont", "Dupont Analysis or show the result as SVG chart!")//
				.addDesc("\nFor instance,")//
				.addDesc("\nCommand below perform analysis on year 2016 and save the data to DB.")//
				.addDesc("\n  dupont -a -y 2016")//
				.addDesc("\nCommand below will show the analysis as SVG with a corpId high-lighted.")//
				.addDesc("\n  dupont -s -c 000002 -y 2016")//
				.addDesc("\nCommand below with a filter 0.5 that show 50% points around the corpId high-lighted.")//
				.addDesc("\n  dupont -s -c 000002 -y 2016 -f 0.5")//
				.addOption(DupontAnalysisCommandHandler.OPT_a, "analysis", false,
						"Execute analysis and store the result to DB.")//
				.addOption(DupontAnalysisCommandHandler.OPT_s, "svg", false, "Show svg through html renderer.")//
				.addOption(DupontAnalysisCommandHandler.OPT_y, "year", true, "year when analysis or showing svg.")//
				.addOption(DupontAnalysisCommandHandler.OPT_c, "corpId", true, "Corp id when showing svg.")//
				.addOption(DupontAnalysisCommandHandler.OPT_f, "filter", true, "Show svg with a filter on the points.")//
				
				, new DupontAnalysisCommandHandler());

		this.addCommand(new CommandType("load", "Load input data from folder!"), new InputDataLoadCommandHandler());
		this.addCommand(new CommandType("query", "Execute query with sql!"), new SqlQueryCommandHandler());

		this.addCommand(new CommandType("show", "Show some thing.").addDesc("\nFor instance:")//
				.addDesc("\nshow -M")//
				.addDesc("\nshow -r -c 000002 -m 资产总计/净利润")//
				.addOption(ShowCommandHandler.OPT_M, "metric-defines", false, "Show all metrics define.") //
				.addOption(ShowCommandHandler.OPT_D, "db-meta", false, "Show DB meta info.") //
				.addOption(ShowCommandHandler.OPT_v, "vars", false, "Show all varibles.") //
				.addOption(ShowCommandHandler.OPT_r, "report", false, "Show report.") //
				.addOption(ShowCommandHandler.OPT_c, "code", true, "For the show command that require a corp code.") //
				.addOption(ShowCommandHandler.OPT_m, "metrics", true,
						"For the show command that requires a metric list.") //
				, new ShowCommandHandler()//
		);

		this.addCommand(new CommandType("sina", "Collect/wash/load all-quotes data from sina."),
				new SinaDataLoadCommandHandler());

		this.metrics = load(server.getDataHome());
		if (this.htmlRenderer == null) {
			this.htmlRenderer = new EmptyHtmlRenderer();
		}

		return rt;
	}

	public MetricDefines getMetricsDefine() {
		return this.metrics;
	}

	private MetricDefines load(DhoDataHome home) {
		FileObject file;
		try {
			file = home.resolveFile(home.getClientFile(), "metric-defines.xml");
			return MetricDefines.load(file.getContent().getInputStream());
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}

	public void addCommand(CommandType type, CommandHandler handler) {
		this.handlerMap.put(type.getName(), handler);
		this.addCommand(type.getName(), type);
	}

	@Override
	public void shutdown() {

		for (Handler h : this.beforeShutdownHandlerList) {
			h.handle();
		}

		super.shutdown();
	}

	public void addBeforeShutdownHandler(Handler h) {
		this.beforeShutdownHandlerList.add(h);
	}

	@Override
	public void processLine(CommandAndLine cl) {
		CommandContext cc = new CommandContext(cl);
		CommandHandler h = this.handlerMap.get(cl.getCommand().getName());
		if (h == null) {
			cl.getConsole().peekWriter().writeLine("not found handler for command:" + cl.getCommand().getName());
			return;
		}

		h.execute(cc);
	}

	public void htmlRenderer(HtmlRenderer htmlRenderer) {
		this.htmlRenderer = htmlRenderer;
	}

	public void clear() {

	}

}
