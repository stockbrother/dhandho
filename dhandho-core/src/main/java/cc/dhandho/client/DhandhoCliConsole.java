package cc.dhandho.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.DhandhoHome;
import cc.dhandho.RtException;
import cc.dhandho.commons.commandline.AbstractComandLineApp;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.commons.handler.Handler;
import cc.dhandho.report.MetricDefines;
import cc.dhandho.rest.server.DhandhoServer;

public class DhandhoCliConsole extends AbstractComandLineApp {
	protected DhandhoServer server;

	protected List<Handler> beforeShutdownHandlerList = new ArrayList<>();

	protected Map<String, CommandHandler> handlerMap = new HashMap<>();

	@Deprecated // move to server side.
	protected MetricDefines metrics;

	protected HtmlRenderer htmlRenderer;

	public DhandhoServer getServer() {
		return server;
	}

	public DhandhoCliConsole server(DhandhoServer server) {
		this.server = server;
		return this;
	}

	@Override
	public void start() {
		super.start();

		this.echo(false);
		this.addCommand(new CommandType("help", "Print this message!"), new HelpCommandHandler());
		this.addCommand(new CommandType("cls", "Clear screen!"), new ClearScreenCommandHandler());
		this.addCommand(new CommandType("exit", "Exit!"), new ExitCommandHandler());
		this.addCommand(new CommandType("chart", "Show metric value as SVG chart for corpId, years and metrics!"),
				new CorpChartCommandHandler());
		this.addCommand(new CommandType("show", "Show some thing. 'help show' for detail!")//
				.addOption(ShowCommandHandler.OPT_M, "metric-defines", false, "Show all metrics define.") //
				.addOption(ShowCommandHandler.OPT_v, "vars", false, "Show all varibles.") //
				.addOption(ShowCommandHandler.OPT_r, "report", false, "Show report.") //
				.addOption(ShowCommandHandler.OPT_c, "code", true, "For the show command that require a corp code.") //
				.addOption(ShowCommandHandler.OPT_m, "metrics", true,
						"For the show command that requires a metric list."), //
				new ShowCommandHandler()//
		);

		this.addCommand(new CommandType("cat", "Print file content!")//
				.addOption(CatCommandHandler.OPT_f, "file", true, "Show file content.") //
				, new CatCommandHandler());

		this.addCommand(new CommandType("sina", "Collect/wash/load all-quotes data from sina."),
				new SinaDataLoadCommandHandler());

		this.metrics = load(server.getHome());
		if (this.htmlRenderer == null) {
			this.htmlRenderer = new EmptyHtmlRenderer();
		}

	}

	public MetricDefines getMetricsDefine() {
		return this.metrics;
	}

	private MetricDefines load(DhandhoHome home) {
		FileObject file;
		try {
			file = home.resolveFile(home.getClientFile(), "metric-defines.xml");
			return MetricDefines.load(file.getContent().getInputStream());
		} catch (IOException e) {
			throw RtException.toRtException(e);
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
