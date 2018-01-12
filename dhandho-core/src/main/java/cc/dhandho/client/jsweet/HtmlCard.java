package cc.dhandho.client.jsweet;

import static def.dom.Globals.document;
import def.dom.HTMLDivElement;

public class HtmlCard extends JsView<HTMLDivElement>{

	public HtmlCard() {
		super(document.createElement(jsweet.util.StringTypes.div));		
	}
	
}
