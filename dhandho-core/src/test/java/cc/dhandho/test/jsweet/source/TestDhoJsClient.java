package cc.dhandho.test.jsweet.source;

import static def.dom.Globals.window;
import static def.jquery.Globals.$;
import static jsweet.util.Lang.$export;

import cc.dhandho.client.jsweet.DhoJsClient;

public class TestDhoJsClient {

	public static void main(String[] args) {
		$(window.document).ready(() -> {
			//document = window.document;
			DhoJsClient client = new DhoJsClient();
			client.start();
			
			window.$set("dho", client);

			String html = "<div></div>";
			Object rt = client.showHtml(html);
			$export("showHtmlResult", rt);
			return null;

		});
	}

}
