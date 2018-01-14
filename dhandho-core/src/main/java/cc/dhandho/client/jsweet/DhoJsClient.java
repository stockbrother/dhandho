package cc.dhandho.client.jsweet;

import static def.dom.Globals.console;
import static def.dom.Globals.document;

import def.dom.HTMLDivElement;

public class DhoJsClient {

	public void start() {
		console.info(">>start");
		console.info("<<start");
	}

	public Object showHtml(String html) {
		console.info(">>showHtml:html");
		//Document document = window.document;
		HTMLDivElement divEle = (HTMLDivElement)document.getElementById("mainDiv");
		
		HTMLDivElement child = document.createElement(jsweet.util.StringTypes.div);
		
		// divEle.innerText = "showHtml,InnerText," +
		// renderer+",className:"+renderer.getClass().getName();
		child.innerHTML = html;
		divEle.insertBefore(child, null);
		console.info(">>showHtml");
		return "OK";
	}

}
