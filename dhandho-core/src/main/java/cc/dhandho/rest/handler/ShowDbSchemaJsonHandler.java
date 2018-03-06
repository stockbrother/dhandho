package cc.dhandho.rest.handler;

import java.util.Collection;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.handler.Handler3;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.metadata.schema.OClass;

import cc.dhandho.rest.AbstractRestRequestHandler;
import cc.dhandho.rest.RestRequestContext;
import cc.dhandho.rest.server.DbProvider;

/**
 * Load Washed Data to DB.
 * 
 * @author wu
 *
 */
public class ShowDbSchemaJsonHandler extends AbstractRestRequestHandler {

	DbProvider dbProvider;

	@Override
	public void setContainer(Container app) {
		super.setContainer(app);
		this.dbProvider = app.findComponent(DbProvider.class, true);
	}

	@Override
	public void handle(RestRequestContext rrc) {
		// JsonObject req = (JsonObject) rrc.parseReader();

		JsonObject json = this.dbProvider.executeWithDbSession(new Handler3<ODatabaseSession, JsonObject>() {

			@Override
			public JsonObject handle(ODatabaseSession arg0) {
				Collection<OClass> clsL = arg0.getMetadata().getSchema().getClasses();
				JsonArray arr = new JsonArray();
				for (OClass cls : clsL) {
					JsonObject jsonI = new JsonObject();
					jsonI.addProperty("name", cls.getName());
					jsonI.addProperty("count", cls.count());
					jsonI.addProperty("superClassesNames", cls.getSuperClassesNames().toString());//
					arr.add(jsonI);//
				}
				JsonObject rt = new JsonObject();
				rt.add("classArray", arr);//
				return rt;
			}
		});
		rrc.write(json);
	}

}
