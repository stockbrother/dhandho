package cc.dhandho.rest;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.Processor;
import cc.dhandho.RtException;

/**
 * 
 * @author Wu
 *
 */
public abstract class DbSessionJsonHandler extends AppContextAwareJsonHandler {

	@Override
	public void execute(Gson gson, JsonReader reader, JsonWriter writer) throws IOException {

		this.dbProvider.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession db) {
				try {
					execute(gson, reader, writer, db);
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
			}
		});

	}

	protected abstract void execute(Gson gson, JsonReader reader, JsonWriter writer, ODatabaseSession db)
			throws IOException;

}
