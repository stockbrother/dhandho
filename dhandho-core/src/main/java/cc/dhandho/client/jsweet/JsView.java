package cc.dhandho.client.jsweet;

import def.dom.HTMLElement;

public class JsView<T extends HTMLElement> {
	
	protected T element;
	
	public JsView(T element) {
		this.element = element;
	}
	
}
