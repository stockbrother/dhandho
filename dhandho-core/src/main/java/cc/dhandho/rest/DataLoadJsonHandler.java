package cc.dhandho.rest;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cc.dhandho.Quarter;
import cc.dhandho.importer.GDBWashedFileSchemaLoader;
import cc.dhandho.importer.GDBWashedFileValueLoader;

/**
 * Load Washed Data to DB.
 * @author wu
 *
 */
public class DataLoadJsonHandler extends AppContextAwareJsonHandler{

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {
		
		File dir = new File("C:\\dhandho\\data\\xueqiu\\washed");
		
		//DbInitUtil.initDb(app);
		
		//new GDBWashedFileSchemaLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();

		//new GDBWashedFileValueLoader(app, dir, Quarter.Q4)/* .limit(10) */.start();
		
	}

}
