package cc.dhandho.commons.jfx;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ConsolePane extends StackPane {

	private static Logger LOG = LoggerFactory.getLogger(ConsolePane.class);

	private static String ZEROS = "000";

	private PipedOutputStream outPipe;
	private InputStream in;

	int cmdStart = 0;
	ConsoleHistory history;
	String startedLine;

	// PopupMenu menu;
	protected TextArea text;
	ConsoleNameComplete nameComplete;

	final int SHOW_AMBIG_MAX = 10;

	// hack to prevent key repeat for some reason?
	boolean gotUp = true;

	public ConsolePane() {

		this.history = new ConsoleHistory(this);

		ConsoleKeyListener kl = new ConsoleKeyListener(this);
		this.nameComplete = new ConsoleNameComplete(this);
		text = new TextArea();

		// Background background = new Background(new BackgroundFill(Color.BLACK,
		// CornerRadii.EMPTY, Insets.EMPTY));
		// Fill the background color on the matte
		// text.setBackground(background);

		// text.setStyle(
		// "-fx-control-inner-background:#000000; -fx-font-family: Consolas;
		// -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill:
		// #00ff00;");

		text.setStyle("" //
				+ "-fx-font-family: Consolas;"//
				+ "-fx-font-size: 12;"//
				+ "-fx-control-inner-background:#000000;"//
				+ "-fx-focus-color: -fx-control-inner-background;"//
				+ "-fx-faint-focus-color: -fx-focus-color;"//
				+ "-fx-text-fill: #00ff00;"//
		);
		text.setText("");
		// text.setFont(font);
		// text.setMargin(new Insets(7, 5, 7, 5));
		text.addEventHandler(KeyEvent.ANY, kl);
		// text.addKeyListener(kl);
		// setViewportView(text);
		// this.setContent(text);
		this.getChildren().add(text);
		// create popup menu
		// menu = new ConsoleMenu(this);

		// ConsoleMouseListener ml = new ConsoleMouseListener(this);
		// text.addMouseListener(ml);

		// make sure popup menu follows Look & Feel
		// UIManager.addPropertyChangeListener(this);

		outPipe = new PipedOutputStream();
		try {
			in = new PipedInputStream(outPipe);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private String format(Color c) {
		int r = (int) (255 * c.getRed());
		int g = (int) (255 * c.getGreen());
		int b = (int) (255 * c.getBlue());
		return String.format("#%02x%02x%02x", r, g, b);
	}

	public InputStream getInputStream() {
		return in;
	}

	void resetCommandStart() {
		cmdStart = textLength();
	}

	void append(String string) {
		int slen = textLength();
		text.selectRange(slen, slen);
		text.replaceSelection(string);
	}

	String replaceRange(Object s, int start, int end) {
		String st = s.toString();
		text.selectRange(start, end);
		text.replaceSelection(st);
		// text.repaint();
		return st;
	}

	void forceCaretMoveToEnd() {
		if (text.getCaretPosition() < cmdStart) {
			// move caret first!
			text.positionCaret(textLength());
		}
		// text.repaint();
	}

	void forceCaretMoveToStart() {
		if (text.getCaretPosition() < cmdStart) {
			// move caret first!
		}
		// text.repaint();
	}

	void enter() {
		String s = getCmd();

		history.addElement(s);
		s = s + "\n";
		append("\n");

		history.histLine = 0;
		acceptLine(s);
		// text.repaint();
	}

	String getCmd() {
		String s = "";
		System.out.println("cmdStart:" + this.cmdStart);
		System.out.println("textLength:" + textLength());

		s = text.getText(cmdStart, textLength());
		print(s);
		return s;
	}

	public void inputLine(String line) {
		this.acceptLine(line + "\r\n");//
	}

	private void acceptLine(String line) {
		// Patch to handle Unicode characters
		// Submitted by Daniel Leuck
		StringBuffer buf = new StringBuffer();
		int lineLength = line.length();
		for (int i = 0; i < lineLength; i++) {
			char c = line.charAt(i);
			if (c > 127) {
				String val = Integer.toString(c, 16);
				val = ZEROS.substring(0, 4 - val.length()) + val;
				buf.append("\\u" + val);
			} else {
				buf.append(c);
			}
		}
		line = buf.toString();
		// End unicode patch

		try {
			outPipe.write(line.getBytes());
			outPipe.flush();
		} catch (IOException e) {
			throw new RuntimeException("Console pipe broken...");
		}
		// text.repaint();
	}

	public void println(Object o) {
		print(String.valueOf(o) + "\n");
		// text.repaint();
	}

	public void print(final Object o) {
		print(o, null, null);
	}

	/**
	 * Prints "\\n" (i.e. newline)
	 */
	public void println() {
		print("\n");
		// text.repaint();
	}

	public void error(Object o) {
		print(o, Color.RED);
	}

	public void print(Object s, Font font) {
		print(s, font, null);
	}

	public void print(Object s, Color color) {
		print(s, null, color);
	}

	public void print(final Object o, final Font font, final Color color) {
		JfxUtil.runSafe(new Runnable() {
			public void run() {
				// AttributeSet old = getStyle();
				// setStyle(font, color);
				String old = text.getStyle();

				append(String.valueOf(o));
				resetCommandStart();
				text.positionCaret(cmdStart);
				// text.setStyle(old);
			}
		});
	}

	public void print(Object s, String fontFamilyName, int size, Color color) {

		print(s, fontFamilyName, size, color, false, false, false);
	}

	public void print(final Object o, final String fontFamilyName, final int size, final Color color,
			final boolean bold, final boolean italic, final boolean underline) {
		JfxUtil.runSafe(new Runnable() {
			public void run() {
				// AttributeSet old = getStyle();
				// setStyle(fontFamilyName, size, color, bold, italic, underline);

				append(String.valueOf(o));
				resetCommandStart();
				text.positionCaret(cmdStart);
				// setStyle(old, true);
			}
		});
	}

	protected int textLength() {
		return text.getLength();
	}

}
