package cc.dhandho.graphdb;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import com.age5k.jcps.JcpsException;

public class DbSessionTL {

	private static ThreadLocal<ODatabaseSession> TL = new ThreadLocal<>();

	public static ODatabaseSession get() {
		return TL.get();
	}

	public static void set(ODatabaseSession db) {
		TL.set(db);
	}

	public static ODatabaseSession get(boolean force) {
		ODatabaseSession dbs = TL.get();
		if (force && dbs == null) {
			throw new JcpsException("no db session found for thread:" + Thread.currentThread());
		}
		return dbs;

	}

}
