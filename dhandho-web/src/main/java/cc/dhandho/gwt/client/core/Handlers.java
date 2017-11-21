package cc.dhandho.gwt.client.core;

import java.util.HashMap;
import java.util.Map;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONObject;

import cc.dhandho.gwt.client.app.Dhandho;

public class Handlers {
	Resource resource;

	public Handlers() {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json; charset=UTF-8");
		resource = new Resource(Dhandho.REST_URI, headers);
	}

	public void handle(String handler, JSONObject json, RequestCallback rcb) throws RequestException {
		Method m = resource.post().text(json.toString());
		m.header("_handler", handler);
		m.send(rcb);
	}

	public void handle(String handler, JSONObject json, JsonCallback jcb) {
		Method m = resource.post().text(json.toString());
		m.header("_handler", handler);
		m.send(jcb);
	}

}
