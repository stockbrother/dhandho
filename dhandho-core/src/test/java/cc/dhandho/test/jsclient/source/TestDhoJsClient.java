package cc.dhandho.test.jsclient.source;

import static def.dom.Globals.document;
import static def.dom.Globals.window;
import static def.jquery.Globals.$;

import cc.dhandho.jsclient.JsDhoClient;
import def.dom.HTMLDivElement;

public class TestDhoJsClient {

	public static void main(String[] args) {
		$(window.document).ready(() -> {
			
			JsDhoClient client = new JsDhoClient().rootDivId("mainDiv");
			
			HTMLDivElement mainDiv = (HTMLDivElement)document.getElementById("mainDiv");
			mainDiv.appendChild(client.element);
			client.start();
			
/*			
			window.$set("dho", client);

			String html = "<div></div>";
			Object rt = client.showHtml(html);
			$export("showHtmlResult", rt);
*/			return null;

		});
	}

}
