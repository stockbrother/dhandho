package cc.dhandho.commons.commandline;

import java.util.Stack;

public class StackConsoleWriter implements CommandLineWriter {

	private Stack<CommandLineWriter> stack = new Stack<CommandLineWriter>();

	public StackConsoleWriter push(CommandLineWriter rd) {
		this.stack.push(rd);
		return this;
	}

	public CommandLineWriter peek() {
		return this.stack.peek();
	}

	public CommandLineWriter pop() {
		return this.stack.pop();
	}

	@Override
	public CommandLineWriter writeLine() {
		if (this.stack.isEmpty()) {
			return null;
		}

		CommandLineWriter top = this.stack.peek();

		top.writeLine();

		return this;
	}

	@Override
	public CommandLineWriter writeLine(String line) {
		if (this.stack.isEmpty()) {
			return null;
		}

		CommandLineWriter top = this.stack.peek();

		top.writeLine(line);

		return this;
	}

	@Override
	public CommandLineWriter write(String str) {
		if (this.stack.isEmpty()) {
			throw new RuntimeException("writer stack is empty!");
		}
		CommandLineWriter top = this.stack.peek();
		top.write(str);

		return this;
	}

}
