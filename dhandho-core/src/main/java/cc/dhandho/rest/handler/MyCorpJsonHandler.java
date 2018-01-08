package cc.dhandho.rest.handler;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.provider.Provider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.mycorp.MyCorps;
import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.util.JsonUtil;

public class MyCorpJsonHandler extends AbstractRestRequestHandler {

	Provider<MyCorps> myCorpsProvider;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		myCorpsProvider = app.findComponentLater(MyCorps.class, true);
	}

	@Override
	public void handle(RestRequestContext arg0) {

		JsonObject json = (JsonObject) arg0.parseReader();
		String command = json.get("command").getAsString();
		if ("add".equals(command)) {
			String corpId = json.get("corpId").getAsString();
			this.myCorpsProvider.get().add(corpId, true);
		} else if ("show".equals(command)) {
			JsonWriter w = arg0.getWriter();
			JsonObject rt = new JsonObject();
			JsonArray a = JsonUtil.toJsonArray(this.myCorpsProvider.get().getCorpIdList());
			rt.add("corpIdsBody", a);
			JsonUtil.write(rt, w);

		} else if ("remove".equals(command)) {
			String corpId = json.get("corpId").getAsString();
			this.myCorpsProvider.get().remove(corpId, true);
		} else {
			throw new JcpsException("not supported.");
		}

	}

}
