package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;

public class DbUtil {
	
	public static interface DbCallback {		
		public void execute(ODatabaseSession db);
	}
	
	
}
