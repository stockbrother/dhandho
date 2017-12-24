package cc.dhandho.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.DhandhoHome;
import cc.dhandho.RtException;
import cc.dhandho.commons.commandline.AbstractComandLineApplication;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.graphdb.Handler;
import cc.dhandho.server.DhandhoServer;

public class DhandhoConsole extends AbstractComandLineApplication {
	protected DhandhoServer server;

	protected List<Handler> beforeShutdownHandlerList = new ArrayList<>();

	protected Map<String, CommandHandler> handlerMap = new HashMap<>();

	protected MetricsDefine metrics;

	public DhandhoServer getServer() {
		return server;
	}

	public DhandhoConsole server(DhandhoServer server) {
		this.server = server;
		return this;
	}

	@Override
	public void start() {
		super.start();
		this.echo(false);
		this.addCommand(new CommandType("help", "Print this message!"), new HelpCommandHandler());
		this.addCommand(new CommandType("exit", "Exit!"), new ExitCommandHandler());
		this.addCommand(new CommandType("chart", "Show metric value as SVG chart for corpId, years and metrics!"),
				new CorpChartCommandHandler());

		this.metrics = load(server.getHome());
	}

	public MetricsDefine getMetricsDefine() {
		return this.metrics;
	}

	private MetricsDefine load(DhandhoHome home) {
		FileObject file;
		try {
			file = home.resolveFile(home.getClientFile(), "metrics-define.xml");
			return MetricsDefine.load(file.getContent().getInputStream());
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
			h.execute();
		}

		super.shutdown();
	}

	public void addBeforeShutdownHandler(Handler h) {
		this.beforeShutdownHandlerList.add(h);
	}

	@Override
	public void processLine(CommandAndLine cl) {
		CommandHandler h = this.handlerMap.get(cl.getCommand().getName());
		if (h == null) {
			cl.getConsole().peekWriter().writeLine("not found handler for command:" + cl.getCommand().getName());
			return;
		}
		h.execute(cl);
	}

}
