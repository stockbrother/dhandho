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
	public CommandLineWriter writeLn() {
		if (this.stack.isEmpty()) {
			return null;
		}

		CommandLineWriter top = this.stack.peek();

		top.writeLn();

		return this;
	}

	@Override
	public CommandLineWriter writeLn(String line) {

		peek(true).writeLn(line);

		return this;
	}

	@Override
	public CommandLineWriter write(String str) {
		peek(true).write(str);

		return this;
	}

	private CommandLineWriter peek(boolean force) {
		if (this.stack.isEmpty()) {
			if (force) {
				throw new RuntimeException("writer stack is empty!");
			}
			return null;
		}
		CommandLineWriter top = this.stack.peek();
		return top;
	}

	@Override
	public CommandLineWriter write(int value) {
		//
		peek(true).write(value);
		return this;
	}

}
