package cc.dhandho.rest.handler;

import java.io.IOException;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.rest.RestRequestContext;

/**
 * Import corp info from class path to DB.
 * 
 * @author Wu
 *
 */
public class LoadCorpInfoJsonHandler extends DbSessionJsonHandler {

	@Override
	public void execute(RestRequestContext rrc, ODatabaseSession db) throws IOException {

		
		//TODO move file to other folder for not load again.
	}

}
