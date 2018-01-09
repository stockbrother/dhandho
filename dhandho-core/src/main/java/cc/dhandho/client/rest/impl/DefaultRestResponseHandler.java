package cc.dhandho.client.rest.impl;

import cc.dhandho.client.HtmlRenderer;
import cc.dhandho.client.rest.RestResponseContext;
import cc.dhandho.client.rest.RestResponseHandler;
import cc.dhandho.util.JsonUtil;

public class DefaultRestResponseHandler implements RestResponseHandler {
	HtmlRenderer htmlRenderer;
	
	
	public DefaultRestResponseHandler(HtmlRenderer htmlRenderer){
		this.htmlRenderer = htmlRenderer;
	}
	
	@Override
	public void handle(RestResponseContext arg0) {
		
		htmlRenderer.showHtml(JsonUtil.toString(arg0.response, true));
		
	}

}
