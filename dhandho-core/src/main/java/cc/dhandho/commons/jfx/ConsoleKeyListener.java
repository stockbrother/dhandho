package cc.dhandho.commons.jfx;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

class ConsoleKeyListener implements EventHandler<KeyEvent> {
	ConsolePane console;

	public ConsoleKeyListener(ConsolePane console) {
		this.console = console;

	}

	@Override
	public void handle(KeyEvent event) {

		if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
			type(event, true, false);
			console.gotUp = false;
		} else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
			console.gotUp = true;
			type(event, false, true);
		} else if (KeyEvent.KEY_TYPED.equals(event.getEventType())) {
			type(event, false, false);
		}
	}

	private synchronized void type(KeyEvent e, boolean pressed, boolean release) {
		KeyCode code = e.getCode();

		switch (e.getCode()) {
		case ENTER:
			if (pressed) {
				if (console.gotUp) {
					console.enter();
				}
			}
			e.consume();
			break;

		case UP:
			if (pressed) {
				console.history.historyUp();
			}
			e.consume();
			break;

		case DOWN:
			if (pressed) {
				console.history.historyDown();
			}
			e.consume();
			break;

		case LEFT:
		case BACK_SPACE:
		case DELETE:
			if (console.text.getCaretPosition() <= console.positionOfCmdStart) {
				// This doesn't work for backspace.
				// See default case for workaround
				e.consume();
			}
			break;

		case RIGHT:
			console.forceCaretMoveToStart();
			break;

		case HOME:
			e.consume();
			break;

		case U: // clear line
			if (e.isControlDown()) {
				console.replaceRange("", console.positionOfCmdStart, console.textLength());
				console.history.histLine = 0;
				e.consume();
			}
			break;

		case ALT:
		case CONTROL:
		case META:
		case SHIFT:
		case PRINTSCREEN:
		case SCROLL_LOCK:
		case PAUSE:
		case INSERT:
		case F1:
		case F2:
		case F3:
		case F4:
		case F5:
		case F6:
		case F7:
		case F8:
		case F9:
		case F10:
		case F11:
		case F12:
		case ESCAPE:

			// only modifier pressed
			break;

		// Control-C
		case C:
			if (console.text.getSelectedText() == null) {
				if ((e.isControlDown()) && (pressed)) {
					console.append("^C");
				}
				e.consume();
			}
			break;

		case TAB:
			if (pressed) {
				String part = console.text.getText().substring(console.positionOfCmdStart);
				console.nameComplete.doCommandComplete(part);
			}
			e.consume();
			break;

		default:
			if (e.isAltDown() | e.isControlDown() | e.isMetaDown()) {
				// plain character
				console.forceCaretMoveToEnd();
			}

			break;
		}
	}

}