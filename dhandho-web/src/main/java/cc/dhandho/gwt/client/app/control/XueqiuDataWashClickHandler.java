package cc.dhandho.gwt.client.app.control;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import cc.dhandho.gwt.client.app.view.MainPanel;

public class XueqiuDataWashClickHandler implements ClickHandler {

	private static Logger LOG = Logger.getLogger(XueqiuDataWashClickHandler.class.getName());

	MainPanel mainPanel;

	public XueqiuDataWashClickHandler(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * Fired when the user clicks on the sendButton.
	 */
	public void onClick(ClickEvent event) {
		doRequest();
	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void doRequest() {

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
			mainPanel.handlers.handle("cc.dhandho.rest.XueqiuDataWashJsonHandler", rt, jcb);
		} catch (RequestException e) {
			onFailure(e);
		}
	}

	private void onResponse(Request request, Response response) {
		String jsonString = response.getText();
		JSONObject json = (JSONObject) JSONParser.parseStrict(jsonString);
		
	}

	public void onFailure(Throwable exception) {
		mainPanel.console.print(exception);
	}

}