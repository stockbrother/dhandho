package cc.dhandho.input.loader;

import com.orientechnologies.orient.core.db.ODatabaseSession;

public class ArchivableCorpInfoInputDataLoader extends CorpInfoInputDataLoader {

	@Override
	public void handle(ODatabaseSession db) {
		super.handle(db);

		home.archive(this.home.getInputCorpsFolder(), true);

	}

}
