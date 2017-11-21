package cc.dhandho.gwt.client.core;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import cc.dhandho.gwt.client.common.ClientObject;
import cc.dhandho.gwt.client.common.ClientUiObject;

public class ConsolePanel extends ClientUiObject<VerticalPanel> {

	public ConsolePanel(ClientObject co) {
		super(new VerticalPanel(), co);
	}

	public void print(Object line) {
		print(line, null);
	}

	public void print(Object line, Throwable t) {
		if (t != null) {
			line = line + ",Error:" + t.getMessage();// TODO stack trace.
		}
		Label l = new Label(String.valueOf(line));

		this.widget.add(l);
	}
}
