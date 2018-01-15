package cc.dhandho.jsclient;

import static def.dom.Globals.document;

import java.util.function.Function;

import def.dom.HTMLButtonElement;
import def.dom.HTMLInputElement;
import def.dom.MouseEvent;
import jsweet.util.StringTypes;

public class JsCommandInput extends JsDivWrapper {
	JsDhoClient client;
	HTMLInputElement input;
	HTMLButtonElement send;
	public JsCommandInput(JsDhoClient client) {
		this.client = client;
		input = document.createElement(jsweet.util.StringTypes.input);
		element.appendChild(input);
		send = document.createElement(StringTypes.button);
		send.textContent = "Run";
		send.addEventListener(StringTypes.click, new Function<MouseEvent, Object>() {

			@Override
			public Object apply(MouseEvent t) {
				doSend();
				return null;
			}
		});
		element.appendChild(send);

	}

	private void doSend() {
		String cmd = this.input.value;
		client.runCommand(cmd);
	}

}
