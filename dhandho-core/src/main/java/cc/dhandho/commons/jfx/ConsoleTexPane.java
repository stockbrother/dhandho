package cc.dhandho.commons.jfx;

import javafx.scene.control.TextArea;

public class ConsoleTexPane extends TextArea {
	
	private ConsolePane console;

	public ConsoleTexPane(ConsolePane console) {
	
	}

	public void cut() {
		if (this.getCaretPosition() < console.positionOfCmdStart) {
			super.copy();
		} else {
			super.cut();
		}
	}

	public void paste() {
		console.forceCaretMoveToEnd();
		super.paste();
	}
}
