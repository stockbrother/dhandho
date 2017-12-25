package cc.dhandho.commons.jfx;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class ConsoleMouseListener implements MouseListener {

	private ConsolePane console;

	public ConsoleMouseListener(ConsolePane console) {
		this.console = console;
	}

	public void mouseClicked(MouseEvent event) {
	}

	public void mousePressed(MouseEvent event) {
		if (event.isPopupTrigger()) {
			//console.menu.show((Component) event.getSource(), event.getX(), event.getY());
		}
	}

	public void mouseReleased(MouseEvent event) {
		if (event.isPopupTrigger()) {
			//console.menu.show((Component) event.getSource(), event.getX(), event.getY());
		}
		//console.text.repaint();
	}

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}
}