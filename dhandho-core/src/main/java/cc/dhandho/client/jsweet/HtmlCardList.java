package cc.dhandho.client.jsweet;

import static def.dom.Globals.document;

import def.dom.HTMLElement;

public class HtmlCardList extends JsView<HTMLElement> {

	public HtmlCardList() {
		super(document.createElement(jsweet.util.StringTypes.div));
	}

}
