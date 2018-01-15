package cc.dhandho.jsclient;

import static def.dom.Globals.document;
import def.dom.HTMLDivElement;

public class JsDivWrapper extends JsElementWrapper<HTMLDivElement>{

	public JsDivWrapper() {
		super(document.createElement(jsweet.util.StringTypes.div));		
	}
	
}
