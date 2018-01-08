package cc.dhandho.report.dupont;

import com.age5k.jcps.JcpsException;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class CorpFilter {

	public float centerAroundPercentage;

	public String groupName;

	public static CorpFilter valueOf(float cap) {
		CorpFilter rt = new CorpFilter();
		rt.centerAroundPercentage = cap;
		return rt;
	}

	public static CorpFilter valueOf(JsonElement json) {

		if (json.isJsonPrimitive()) {
			JsonPrimitive jp = (JsonPrimitive) json;
			CorpFilter rt = new CorpFilter();
			if (jp.isNumber()) {
				rt.centerAroundPercentage = jp.getAsFloat();
			} else if (jp.isString()) {
				rt.groupName = jp.getAsString();
			} else {
				throw new JcpsException("todo");
			}
			return rt;
		} else {
			throw new JcpsException("todo");
		}
	}
}
