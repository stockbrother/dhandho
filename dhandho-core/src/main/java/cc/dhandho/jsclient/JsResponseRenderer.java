package cc.dhandho.jsclient;

public class JsResponseRenderer extends JsDivWrapper {

	public JsResponseRenderer() {

	}

	public void showResponse(String message) {
		this.element.innerText = message;
	}

}
