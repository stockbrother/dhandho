package cc.dhandho.gwt.client.common;

import com.google.gwt.user.client.ui.Widget;

import cc.dhandho.gwt.client.app.view.MainPanel;
import cc.dhandho.gwt.client.core.Handlers;

public class ClientUiObject<W extends Widget> extends ClientObject {
	protected W widget;

	public ClientUiObject(W w, ClientObject co) {
		this(w, co.handlers, co.mainPanel);
	}

	public ClientUiObject(W w, Handlers handlers, MainPanel mainPanel) {
		super(handlers, mainPanel);
		this.widget = w;
	}

	public W getWidget() {
		return this.widget;
	}
	

}
