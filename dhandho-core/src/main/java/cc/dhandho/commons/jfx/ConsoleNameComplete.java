package cc.dhandho.commons.jfx;

import java.awt.Color;

public class ConsoleNameComplete {

	ConsolePane console;

	public ConsoleNameComplete(ConsolePane console) {
		this.console = console;
	}

	void doCommandComplete(String part) {

		int i = part.length() - 1;

		// Character.isJavaIdentifierPart() How convenient for us!!
		while (i >= 0 && (Character.isJavaIdentifierPart(part.charAt(i)) || part.charAt(i) == '.')) {
			i--;
		}

		part = part.substring(i + 1);

		if (part.length() < 2) {
			// reasonable completion length
			return;
		}
		// no completion
		String[] complete = new String[] {};// TODO
		if (complete.length == 0) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			return;
		}

		// Found one completion (possibly what we already have)
		if (complete.length == 1 && !complete.equals(part)) {
			String append = complete[0].substring(part.length());
			console.append(append);
			return;
		}

		// Found ambiguous, show (some of) them

		String line = console.text.getText();
		String command = line.substring(console.cmdStart);
		// Find prompt
		for (i = console.cmdStart; line.charAt(i) != '\n' && i > 0; i--) {

		}

		String prompt = line.substring(i + 1, console.cmdStart);

		// Show ambiguous
		StringBuffer sb = new StringBuffer("\n");
		for (i = 0; i < complete.length && i < console.SHOW_AMBIG_MAX; i++) {
			sb.append(complete[i] + "\n");
		}
		if (i == console.SHOW_AMBIG_MAX) {
			sb.append("...\n");
		}

		console.print(sb, Color.gray);
		console.print(prompt); // print resets command start
		console.append(command); // append does not reset command start
	}

}
