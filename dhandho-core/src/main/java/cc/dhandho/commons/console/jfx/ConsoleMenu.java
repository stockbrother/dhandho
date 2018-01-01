package cc.dhandho.commons.console.jfx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ConsoleMenu extends JPopupMenu implements ActionListener {

	private final static String CUT = "Cut";
	private final static String COPY = "Copy";
	private final static String PASTE = "Paste";

	JfxConsolePane console;

	public ConsoleMenu(JfxConsolePane console) {
		add(new JMenuItem(CUT)).addActionListener(this);
		add(new JMenuItem(COPY)).addActionListener(this);
		add(new JMenuItem(PASTE)).addActionListener(this);
	}// handle cut, copy and paste

	@Override
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();
		if (cmd.equals(CUT)) {
			console.text.cut();
		} else if (cmd.equals(COPY)) {
			console.text.copy();
		} else if (cmd.equals(PASTE)) {
			console.text.paste();
		}
	}

}
