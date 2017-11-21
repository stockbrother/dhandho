package cc.dhandho.gwt.client.common;

import cc.dhandho.gwt.client.app.view.MainPanel;
import cc.dhandho.gwt.client.core.Handlers;

public class ClientObject {
	
	public MainPanel mainPanel;
	
	public Handlers handlers;
	
	public ClientObject(Handlers handlers, MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		this.handlers = handlers;
	}
	
}
