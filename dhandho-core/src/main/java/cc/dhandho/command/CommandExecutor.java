package cc.dhandho.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cc.dhandho.commons.commandline.CommandType;
import cc.dhandho.rest.server.DhoServer;

public class CommandExecutor {
	private static final Logger LOG = LoggerFactory.getLogger(CommandExecutor.class);

	private CommandLineParser parser = new BasicParser();

	private Usage usage = new Usage();

	DhoServer server;

	private Map<String, CommandType> commandMap = new HashMap<>();

	private Map<String, CommandHandler> handlerMap = new HashMap<>();

	public CommandExecutor(DhoServer server) {
		this.server = server;
	}

	public void addCommand(CommandType type, CommandHandler handler) {
		this.handlerMap.put(type.getName(), handler);
		this.commandMap.put(type.getName(), type);
	}

	public JsonElement execute(String line) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("processing line:" + line);
		}

		String[] args = line.split(" ");
		String cmd = args[0];
		cmd = cmd.trim();
		CommandType command = this.commandMap.get(cmd);
		CommandLine cl = null;
		String[] args2 = null;
		if (command == null) {
			command = this.getHelpCommand();
			args2 = new String[] {};
		} else {
			args2 = new String[args.length - 1];
			System.arraycopy(args, 1, args2, 0, args2.length);
			cl = this.parseCommandLine(command, args2, false);
		}
		if (cl == null) {// not found the command or format error.
			command = this.getHelpCommand();
			args2 = new String[] { cmd };
			cl = this.parseCommandLine(command, args2, true);
		}

		try {
			return this.processLine(command, cl);
		} catch (Throwable t) {
			return this.unexpectedThrowable(command, cl, t);
		}

	}

	public JsonElement processLine(CommandType type, CommandLine cl) {
		CommandHandler h = this.handlerMap.get(type.getName());
		if (h == null) {
			CommandType help = this.getHelpCommand();
			throw new JcpsException("please type:" + help.getName());
		} else {
			CommandContext cc = new CommandContext(type, cl);
			return h.execute(cc);
		}
	}

	public CommandType getHelpCommand() {

		CommandType type = this.commandMap.get("help");//
		if (type == null) {
			throw new JcpsException("no help command found.");
		}
		return type;
	}

	protected JsonElement unexpectedThrowable(CommandType type, CommandLine cl, Throwable t) {

		LOG.error("", t);
		JsonObject rt = new JsonObject();
		rt.addProperty("error", t.getMessage());
		return rt;

	}

	public CommandLine parseCommandLine(CommandType command, String[] args, boolean force) {

		CommandLine rt = null;
		try {

			Options options = command.getOptions();
			rt = this.parser.parse(options, args);

		} catch (ParseException e) {
			LOG.warn("parse error for cmd:" + command.getName(), e);
			if (force) {
				throw new RuntimeException(e);
			}
		}
		return rt;
	}

	public CommandType getCommand(String cname) { //
		return this.commandMap.get(cname);
	}

	public List<CommandType> getCommandList() {

		List<String> cmdL = new ArrayList<String>(this.commandMap.keySet());

		String[] names = cmdL.toArray(new String[cmdL.size()]);

		// sorted
		Arrays.sort(names);
		List<CommandType> rt = new ArrayList<CommandType>();
		for (int i = 0; i < names.length; i++) {
			CommandType cmd = this.commandMap.get(names[i]);
			rt.add(cmd);

		}

		return rt;
	}

	public Usage getUsage() {
		//
		return this.usage;
	}

}
