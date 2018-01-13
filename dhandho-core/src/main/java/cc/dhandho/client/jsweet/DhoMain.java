package cc.dhandho.client.jsweet;

import static def.dom.Globals.console;
import static def.dom.Globals.document;
import static def.dom.Globals.window;
import static def.jquery.Globals.$;

import def.dom.HTMLDivElement;
import def.dom.HTMLElement;

public class DhoMain {

	public static void main(String[] args) {
		$(document).ready(() -> {
			DhoMain dhoMain = new DhoMain();
			dhoMain.start();
			window.$set("dhoMain", dhoMain);
			return null;

		});
	}

	public DhoMain() {
		
	}

	public void start() {
		console.info(">>start");
		console.info("<<start");
	}

	public Object showHtml(String html) {
		console.info(">>showHtml:html");
		
		HTMLDivElement divEle = (HTMLDivElement)document.getElementById("mainDiv");
		
		HTMLDivElement child = document.createElement(jsweet.util.StringTypes.div);
		
		// divEle.innerText = "showHtml,InnerText," +
		// renderer+",className:"+renderer.getClass().getName();
		child.innerHTML = html;
		divEle.insertBefore(child, null);
		console.info(">>showHtml");
		return "OK";
	}

	public void showHtml2() {
		console.info(">>showHtml2");
		// String s = String.valueOf($(window).$get("renderer"));
		HTMLElement ele = document.getElementById("block1");
		HTMLDivElement divEle = (HTMLDivElement) ele;
		HTMLDivElement child = document.createElement(jsweet.util.StringTypes.div);

		child.innerHTML = "Hello,InnerText";
		divEle.appendChild(child);

		// block1.$set("innerHTML", "::" );

		console.info("<<showHtml2");
	}
}
