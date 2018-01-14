package cc.dhandho.client.jsweet;

import static def.dom.Globals.document;
import static def.dom.Globals.window;
import static def.jquery.Globals.$;

public class DhoMain {

	public static void main(String[] args) {
		$(document).ready(() -> {
			DhoJsClient client = new DhoJsClient();
			client.start();
			window.$set("dhoMain", client);
			return null;

		});
	}

	public DhoMain() {
		
	}

}
