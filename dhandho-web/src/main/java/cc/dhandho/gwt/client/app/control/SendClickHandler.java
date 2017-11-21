package cc.dhandho.gwt.client.app.control;

import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import cc.dhandho.gwt.client.app.view.MainPanel;

public class SendClickHandler implements ClickHandler, KeyUpHandler {

	private static Logger LOG = Logger.getLogger(SendClickHandler.class.getName());

	MainPanel mainPanel;

	public SendClickHandler(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/**
	 * Fired when the user clicks on the sendButton.
	 */
	public void onClick(ClickEvent event) {
		doRequest();
	}

	/**
	 * Fired when the user types in the nameField.
	 */
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			doRequest();
		}
	}

	/**
	 * Send the name from the nameField to the server and wait for a response.
	 */
	private void doRequest() {
		String corpId = mainPanel.corpId.getText();

		JSONObject rt = new JSONObject();
		rt.put("corpId", new JSONString(corpId));

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
			mainPanel.handlers.handle("cc.dhandho.rest.CorpInfoJsonHandler", rt, jcb);
		} catch (RequestException e) {
			onFailure(e);
		}
	}

	private void onResponse(Request request, Response response) {
		String jsonString = response.getText();
		JSONObject json = (JSONObject) JSONParser.parseStrict(jsonString);
		String category = json.get("category").isString().stringValue();
		boolean isJinRon = category.startsWith("J");
		mainPanel.console.print("isJinRon:" + isJinRon);
		
		mainPanel.chartList.updateAll(isJinRon);
	}

	public void onFailure(Throwable exception) {
		mainPanel.console.print(exception);
	}

}