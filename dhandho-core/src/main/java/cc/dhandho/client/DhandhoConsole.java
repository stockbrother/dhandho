package cc.dhandho.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.dhandho.commons.commandline.AbstractComandLineApplication;
import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.graphdb.Handler;
import cc.dhandho.server.DhandhoServer;

public class DhandhoConsole extends AbstractComandLineApplication {
	protected DhandhoServer server;

	protected List<Handler> beforeShutdownHandlerList = new ArrayList<>();

	protected Map<String, CommandHandler> handlerMap = new HashMap<>();

	public DhandhoServer getServer() {
		return server;
	}

	public void setServer(DhandhoServer server) {
		this.server = server;
	}

	@Override
	public void start() {
		super.start();
		this.addCommand(new CommandType("help", "Print this message!"), new HelpCommandHandler());
		this.addCommand(new CommandType("start", "Start server!"), new StartCommandHandler());

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
