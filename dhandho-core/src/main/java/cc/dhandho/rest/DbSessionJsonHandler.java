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
public abstract class DbSessionJsonHandler extends AbstractRestRequestHandler {

	@Override
	public void execute(RestRequestContext rrc) throws IOException {

		this.dbProvider.executeWithDbSession(new Processor<ODatabaseSession>() {

			@Override
			public void process(ODatabaseSession db) {
				try {
					execute(rrc, db);
				} catch (IOException e) {
					throw RtException.toRtException(e);
				}
			}
		});

	}

	protected abstract void execute(RestRequestContext rrc, ODatabaseSession db)
			throws IOException;

}
