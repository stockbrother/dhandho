package cc.dhandho.test.jsweet;
import static jsweet.util.Lang.$export;

import def.dom.HTMLElement;
public class JsView<T extends HTMLElement> {
	T element;
	public JsView() {
		
	}
	
	public void show() {
		$export("JsView_show_Called", "yes");
		$export("showX", "JsView");
	}
	
}
