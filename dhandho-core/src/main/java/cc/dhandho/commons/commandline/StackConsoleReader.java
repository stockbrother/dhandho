package cc.dhandho.commons.commandline;

import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.framework.server.ExecutorUtil;

public class StackConsoleReader {

	private static final Logger LOG = LoggerFactory.getLogger(StackConsoleReader.class);

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

		private ExecutorService executor = ExecutorUtil.newSingleThreadExecutor(ReaderWrapper.class.getName());

		private Future<String> worker;

		private boolean producerClosed;

		private int producted;

		private int consumed;

		private CommandLineReader target;

		private boolean closed;

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

			while (!this.closed) {
				String line = this.target.readLine();
				if (line == null) {
					this.producerClosed = true;
					break;
				}

				try {
					this.buffer.put(line);
					this.producted++;
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			LOG.warn("closed or producer is closed.");
		}

		private boolean isProducerClosedAndAllConsumed() {
			return this.producerClosed && this.consumed == this.producted;
		}

		private void close() {
			this.closed = true;
			this.target.close();
			this.executor.shutdown();
		}

		private String tryReadLine(long time, TimeUnit tu) {
			String rt = null;
			if (!this.isProducerClosedAndAllConsumed()) {
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

	private Stack<ReaderWrapper> stack = new Stack<ReaderWrapper>();

	private boolean closed;

	public StackConsoleReader push(CommandLineReader rd) {
		return this.push(rd, true);
	}

	public StackConsoleReader push(CommandLineReader rd, boolean popWhenClosed) {
		LOG.debug("push reader:" + rd);

		this.stack.push(new ReaderWrapper(rd, popWhenClosed));
		return this;
	}

	public void close() {
		this.closed = true;
		for (ReaderWrapper rw : this.stack) {
			rw.close();
		}
	}

	public LineRead readLine() {
		if (this.stack.isEmpty()) {
			return null;
		}
		LineRead rt = null;

		while (!this.closed) {
			ReaderWrapper top = this.stack.peek();
			if (top.isProducerClosedAndAllConsumed()) {
				break;
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
