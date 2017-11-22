package cc.dhandho.rest;

import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.RtException;

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
			throw new RtException("no db session found for thread:" + Thread.currentThread());
		}
		return dbs;

	}

}
