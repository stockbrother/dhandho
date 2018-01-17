package cc.dhandho.jsclient.ng;

import def.js.Array;
import def.js.JSON;

public class JsonResponse extends JsonWrapper {

	public JsonResponse(Object json) {
		super(json);
	}

	public static JsonResponse valueOf(Object json) {
		//
		Array<def.js.String> names = JSON.getOwnPropertyNames(json);
		
		return new JsonResponse(json);
	}

}
