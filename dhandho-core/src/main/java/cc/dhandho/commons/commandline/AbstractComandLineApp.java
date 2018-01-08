package cc.dhandho.commons.commandline;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.server.ExecutorUtil;

import cc.dhandho.commons.commandline.StackConsoleReader.LineRead;
import cc.dhandho.util.SemaphoreFuture;

/**
 * Sample code of usage: <br>
 * <code>
 * public class MyCommandLineApp extends AbstractComandLineApplication{
 *    
 *   public void processLine(CommandAndLine cl) {
 *		
 *		line.getConsole().peekWriter().writeLine("this is a command.");
 *		 
 *   }
 * }
 * 
 * MyCommandLineApp app = new MyCommandLineApp();
 * app.start();
 * 
 * </code> <br>
 * To read from other source instead of standard input. <br>
 * <code>
 * app.pushReader(reader);
 * </code><br>
 * to write to other writer instead of standard output. <br>
 * <code>  
 * app.pushWriter(writer);
 * </code>
 * 
 * @author Wu
 * 
 */
public abstract class AbstractComandLineApp implements CommandLineApp {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractComandLineApp.class);

	private StackConsoleReader reader;

	private ExecutorService executor;

	private CommandLineParser parser = new BasicParser();

	private Map<String, CommandType> commandMap = new HashMap<String, CommandType>();

	private StackConsoleWriter writer;

	private String prompt = "$";

	private int commandCounter;

	private boolean echo = true;

	private boolean printLine;

	private boolean isRunning;

	private boolean isAlive = true;

	private Map<String, String> attributeMap = new HashMap<String, String>();

	Future<String> fs;
	SemaphoreFuture<Object> startFuture;

	public AbstractComandLineApp() {

		this.reader = new StackConsoleReader();
		this.writer = new StackConsoleWriter();
		this.executor = ExecutorUtil.newSingleThreadExecutor(AbstractComandLineApp.class.getName());//
	}

	@Override
	public String getAttribute(String key) {
		return this.attributeMap.get(key);
	}

	@Override
	public CommandLineApp prompt(String p) {
		this.prompt = p;
		this.prompt();
		return this;
	}

	protected void run() {
		try {
			LOG.info("running of console ...!");

			while (this.isRunning) {

				this.writer.write(this.prompt);

				if (!this.startFuture.isDone()) {
					this.startFuture.done("ready to input.");
				}

				LineRead line = this.reader.readLine();
				if (line == null) {
					if (this.isRunning) {
						throw new JcpsException("reader is closed while app is running.");
					} else {
						continue;
					}
				}
				if (this.echo) {
					this.writer.writeLn(line.getLine());
				}

				this.processLine(this.commandCounter, line);

				this.commandCounter++;
			}
			LOG.warn("exiting graph console");
		} catch (Throwable t) {
			LOG.error("todo exit", t);
		} finally {
			this.isAlive = false;
		}
	}

	public void echo(boolean echo) {
		this.echo = echo;
	}

	public void prompt() {
		this.writer.write(this.prompt);
	}

	public void processLine(int idx, LineRead lr) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("processing line:" + lr.getLine() + ",idx:" + idx);
		}

		try {
			String line = lr.getLine();
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
			CommandAndLine cnl = new CommandAndLine(this, idx, command, cl);
			try {
				this.processLine(cnl);
			} catch (Throwable t) {
				this.unexpectedThrowable(cnl, t);
			}

		} finally {
			if (this.printLine) {
				this.writer.writeLn();
			}
		}

	}

	protected void unexpectedThrowable(CommandAndLine cnl, Throwable t) {
		StringWriter sWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(sWriter));
		this.writer.write(sWriter.getBuffer().toString());
	}

	public abstract void processLine(CommandAndLine cl);

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

	public CommandType getHelpCommand() {

		CommandType type = this.commandMap.get("help");//
		if (type == null) {
			throw new JcpsException("no help command found.");
		}
		return type;
	}

	public void printHelp(Options options) {

	}

	@Override
	public Future<Object> start() {

		this.isRunning = true;
		this.startFuture = new SemaphoreFuture<>();

		fs = this.executor.submit(new Callable<String>() {

			@Override
			public String call() throws Exception {
				AbstractComandLineApp.this.run();
				return null;

			}
		});
		return this.startFuture;
	}

	@Override
	public void shutdownAsync() {
		this.isRunning = false;
		this.reader.close();
		this.executor.shutdown();
	}

	@Override
	public void shutdown() {
		this.shutdownAsync();
		try {
			this.fs.get(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw JcpsException.toRtException(e);
		} catch (ExecutionException e) {
			throw JcpsException.toRtException(e);
		} catch (TimeoutException e) {
			throw JcpsException.toRtException(e);
		}
	}

	@Override
	public CommandLineApp pushReader(CommandLineReader cr) {
		return this.pushReader(cr, false);
	}

	@Override
	public CommandLineApp pushReader(CommandLineReader cr, boolean popWhenClosed) {

		this.reader.push(cr, popWhenClosed);

		return this;
	}

	@Override
	public CommandLineApp pushWriter(CommandLineWriter cw) {
		this.writer.push(cw);
		return this;

	}

	@Override
	public CommandLineWriter peekWriter() {
		return this.writer.peek();
	}

	@Override
	public CommandLineWriter popWriter() {

		return this.writer.pop();
	}

	@Override
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

	@Override
	public CommandType getCommand(String name) {
		return this.commandMap.get(name);
	}

	@Override
	public void printLine(boolean printLine) {
		this.printLine = printLine;
	}

	@Override
	public void addCommand(String name, CommandType type) {
		this.commandMap.put(name, type);
	}

	@Override
	public boolean isAlive() {
		return this.isAlive;
	}

}
