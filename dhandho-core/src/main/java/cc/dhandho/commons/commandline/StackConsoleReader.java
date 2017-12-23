package cc.dhandho.commons.commandline;

import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackConsoleReader {

	private static final Logger LOG = LoggerFactory.getLogger(StackConsoleReader.class);

	private Stack<ReaderWrapper> stack = new Stack<ReaderWrapper>();

	public static class LineRead {
		private String line;
		private CommandLineReader owner;

		public LineRead(CommandLineReader owner, String line) {
			this.owner = owner;
			this.line = line;
		}

		public String getLine() {
			return line;
		}

		public CommandLineReader getOwner() {
			return owner;
		}
	}

	private static class ReaderWrapper {

		private boolean popWhenClosed;

		private BlockingQueue<String> buffer = new LinkedBlockingQueue<String>();

		private ExecutorService executor = Executors.newSingleThreadExecutor();

		private Future<String> worker;

		private boolean produceFinished;

		private int producted;

		private int consumed;

		private CommandLineReader target;

		public ReaderWrapper(CommandLineReader t, boolean pop) {
			this.target = t;
			this.popWhenClosed = pop;
			this.worker = this.executor.submit(new Callable<String>() {

				@Override
				public String call() throws Exception {
					ReaderWrapper.this.run();
					return null;
				}
			});
		}

		protected void run() {

			while (true) {
				String line = this.target.readLine();
				if (line == null) {
					this.produceFinished = true;
					break;
				}

				try {
					this.buffer.put(line);
					this.producted++;
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

		}

		private boolean isFinished() {
			return this.produceFinished && this.consumed == this.producted;
		}

		private String tryReadLine(long time, TimeUnit tu) {
			String rt = null;
			if (!this.isFinished()) {
				try {
					rt = buffer.poll(time, tu);
					if (rt != null) {
						this.consumed++;
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			return rt;

		}

	}

	public StackConsoleReader push(CommandLineReader rd) {
		return this.push(rd, true);
	}

	public StackConsoleReader push(CommandLineReader rd, boolean popWhenClosed) {
		LOG.debug("push reader:" + rd);

		this.stack.push(new ReaderWrapper(rd, popWhenClosed));
		return this;
	}

	public LineRead readLine() {
		if (this.stack.isEmpty()) {
			return null;
		}
		LineRead rt = null;

		while (true) {
			ReaderWrapper top = this.stack.peek();
			if (top.isFinished()) {
				this.pop();
				continue;
			}

			String line = top.tryReadLine(1, TimeUnit.SECONDS);
			if (line == null) {
				continue;
			}

			rt = new LineRead(top.target, line);
			break;

		}

		return rt;
	}

	public void pop() {
		this.stack.pop();
	}

}
