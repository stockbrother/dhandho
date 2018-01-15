package cc.dhandho.jsclient;

import def.dom.HTMLElement;

public class JsElementWrapper<T extends HTMLElement> {
	
	public T element;
	
	public JsElementWrapper(T element) {
		this.element = element;
	}
	
}
