package cc.dhandho.commons.console.jfx;

import javafx.scene.control.TextArea;

public class ConsoleTexPane extends TextArea {
	
	private JfxConsolePane console;

	public ConsoleTexPane(JfxConsolePane console) {
	
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
