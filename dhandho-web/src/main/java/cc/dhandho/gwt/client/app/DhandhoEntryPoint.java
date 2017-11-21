package cc.dhandho.gwt.client.app;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.ui.RootPanel;

import cc.dhandho.gwt.client.app.control.InitHandler;
import cc.dhandho.gwt.client.app.view.MainPanel;
import cc.dhandho.gwt.client.core.Handlers;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DhandhoEntryPoint implements EntryPoint, UncaughtExceptionHandler {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	@Override
	public void onUncaughtException(Throwable e) {
		e.printStackTrace();// TODO print where
		if (mainPanel != null && mainPanel.console != null) {
			mainPanel.console.print("", e);
		}
	}

	Handlers handlers;

	MainPanel mainPanel;

	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(this);
		handlers = new Handlers();
		mainPanel = new MainPanel(handlers);

		getRootPanel("rootPanel").add(mainPanel.getWidget());

		try {

			new InitHandler(handlers, mainPanel).init();
		} catch (Throwable t) {
			mainPanel.console.print("exception:" + t);
		}
	}

	private RootPanel getRootPanel(String id) {
		RootPanel rt = RootPanel.get(id);
		if (rt == null) {
			throw new RuntimeException("no root panel with id:" + id);
		}
		return rt;

	}

}
