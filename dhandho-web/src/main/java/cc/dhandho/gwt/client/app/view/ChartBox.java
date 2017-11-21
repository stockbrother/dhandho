package cc.dhandho.gwt.client.app.view;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.VerticalPanel;

import cc.dhandho.gwt.client.app.control.RequestEditor;

public class ChartBox extends VerticalPanel {
	RequestEditor requestEditor;
	SvgChart chart;
	MainPanel mainPanel;
	boolean isJinRon;

	ChartBox(boolean isJinRon, String group, MainPanel mainPanel) {
		this.isJinRon = isJinRon;
		this.mainPanel = mainPanel;
		this.requestEditor = new RequestEditor(mainPanel);
		this.requestEditor.setGroup(group);
		chart = new SvgChart();
		this.add(chart);

	}

	public void update(boolean isJinRon) {
		chart.unsetSvg("Loading");

		if (this.isJinRon != isJinRon) {
			chart.unsetSvg(isJinRon ? "N/A for JR" : "N/A for Non-JR");
			return;
		}

		JSONObject rt = this.requestEditor.buildRequest();
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
			mainPanel.handlers.handle("cc.dhandho.rest.CorpChartJsonHandler", rt, jcb);
		} catch (RequestException e) {
			onFailure(e);
		}

	}

	private void onResponse(Request request, Response response) {
		String jsonString = response.getText();
		JSONObject json = (JSONObject) JSONParser.parseStrict(jsonString);

		String svg = ((JSONString) json.get("svg")).stringValue();
		int width = (int) json.get("width").isNumber().doubleValue();
		int height = (int) json.get("height").isNumber().doubleValue();

		chart.setSvg(width, height, svg);
	}

	public void onFailure(Throwable exception) {
		mainPanel.console.print(exception);
	}
}