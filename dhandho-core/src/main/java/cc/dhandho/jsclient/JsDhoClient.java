package cc.dhandho.jsclient;

import static def.dom.Globals.console;
import static def.dom.Globals.document;
import static def.jquery.Globals.$;

import def.dom.HTMLBodyElement;
import def.dom.HTMLDivElement;
import def.jquery.JQueryXHR;
import def.js.JSON;
import jsweet.util.StringTypes;
import jsweet.util.function.TriFunction;

public class JsDhoClient extends JsDivWrapper {

	String url = "/handle/cc.dhandho.rest.handler.EchoJsonHandler";

	JsCommandInput commandInput;
	JsResponseRenderer responseRender;
	private String rootDivId;

	public JsDhoClient() {

	}

	public JsDhoClient rootDivId(String rootDivId) {
		this.rootDivId = rootDivId;
		return this;
	}

	public void start() {
		console.info(">>start");
		if (this.rootDivId == null) {
			// if root div is defined, then add this to it.
			HTMLBodyElement root = document.getElementsByTagName(StringTypes.body).$get(0);
			root.appendChild(this.element);
		} else {
			// if no root defined, use body as the root.
			HTMLDivElement root = (HTMLDivElement) document.getElementById(this.rootDivId);
			root.appendChild(this.element);
		}
		commandInput = new JsCommandInput(this);
		this.element.appendChild(this.commandInput.element);
		responseRender = new JsResponseRenderer();
		this.element.appendChild(responseRender.element);

		console.info("<<start");
	}

	public Object showHtml(String html) {
		console.info(">>showHtml:html");
		// Document document = window.document;
		HTMLDivElement divEle = (HTMLDivElement) document.getElementById("mainDiv");

		HTMLDivElement child = document.createElement(jsweet.util.StringTypes.div);

		// divEle.innerText = "showHtml,InnerText," +
		// renderer+",className:"+renderer.getClass().getName();
		child.innerHTML = html;
		divEle.insertBefore(child, null);
		console.info(">>showHtml");
		return "OK";
	}

	public void runCommand(String cmd) {
		def.js.Object data = new def.js.Object();
		data.$set("command", cmd);
		String dataS = JSON.stringify(data);

		$.post(url, dataS, new TriFunction<Object, String, JQueryXHR, Object>() {

			@Override
			public Object apply(Object t, String u, JQueryXHR v) {
				String response = JSON.stringify(t);
				JsDhoClient.this.responseRender.showResponse(response);//
				return null;
			}
		}, "json");

	}

}
