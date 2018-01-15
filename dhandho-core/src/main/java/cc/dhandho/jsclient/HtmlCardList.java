package cc.dhandho.jsclient;

import static def.dom.Globals.document;

import def.dom.HTMLElement;

public class HtmlCardList extends JsElementWrapper<HTMLElement> {

	public HtmlCardList() {
		super(document.createElement(jsweet.util.StringTypes.div));
	}

}
