package cc.dhandho.gwt.client.app.control;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import cc.dhandho.gwt.client.app.InitDataJson;
import cc.dhandho.gwt.client.app.view.MainPanel;
import cc.dhandho.gwt.client.core.Handlers;

public class InitHandler {
	private static Logger LOG = Logger.getLogger(InitHandler.class.getName());
	Handlers handlers;
	MainPanel mainPanel;

	public InitHandler(Handlers handlers, MainPanel mainPanel) {
		this.handlers = handlers;
		this.mainPanel = mainPanel;
	}

	public void init() {
		JSONObject rt = new JSONObject();
		RequestCallback jcb = new RequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				onResponse(request, response);
				mainPanel.console.print("init complete");
			}

			@Override
			public void onError(Request request, Throwable exception) {
				mainPanel.console.print("init failure");
				onFailure(exception);

			}

		};

		try {
			this.mainPanel.console.print("init request:" + rt);
			this.mainPanel.console.print("please wait for init complete ...");

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