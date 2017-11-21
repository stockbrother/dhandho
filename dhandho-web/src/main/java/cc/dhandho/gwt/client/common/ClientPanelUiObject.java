package cc.dhandho.gwt.client.common;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import cc.dhandho.gwt.client.app.view.MainPanel;
import cc.dhandho.gwt.client.core.Handlers;

public class ClientPanelUiObject<W extends Panel> extends ClientUiObject<W> {

	public ClientPanelUiObject(W w, ClientObject co) {
		this(w, co.handlers, co.mainPanel);
	}

	public ClientPanelUiObject(W w, Handlers handlers, MainPanel mainPanel) {
		super(w, handlers, mainPanel);
	}

	public void add(Widget child) {
		this.widget.add(child);
	}
	
	public <T extends Widget> void add(ClientUiObject<T> obj) {
		this.add(obj.getWidget());
	}
	
}
