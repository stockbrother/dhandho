package cc.dhandho.gwt.client.app;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.RootPanel;

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

	private static class InitHandler {
		private static Logger LOG = Logger.getLogger(InitHandler.class.getName());
		Handlers handlers;
		MainPanel mainPanel;

		InitHandler(Handlers handlers, MainPanel mainPanel) {
			this.handlers = handlers;
			this.mainPanel = mainPanel;
		}

		public void init() {
			JSONObject rt = new JSONObject();
			RequestCallback jcb = new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					onResponse(request, response);
				}

				@Override
				public void onError(Request request, Throwable exception) {
					onFailure(exception);

				}

			};

			try {
				this.mainPanel.console.print("init request:" + rt);

				handlers.handle("cc.dhandho.rest.InitDataJsonHandler", rt, jcb);
			} catch (RequestException e) {
				onFailure(e);
			}

		}

		protected void onResponse(Request request, Response response) {
			String jsonString = response.getText();
			mainPanel.console.print("-----");
			mainPanel.console.print(jsonString);//
			JSONObject json = (JSONObject) JSONParser.parseStrict(jsonString);

			this.onInitDataLoad(json);
		}

		private void onFailure(Throwable exception) {
			mainPanel.console.print(exception);
			LOG.log(Level.SEVERE, exception.toString());
		}

		public void onInitDataLoad(JSONObject json) {
			mainPanel.console.print("onInitDataLoad:" + json);

			InitDataJson mdTable = InitDataJson.valueOf(json);
			mainPanel.init(mdTable);

		}

	}

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
