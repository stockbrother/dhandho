package cc.dhandho.input.loader;

import com.orientechnologies.orient.core.db.ODatabaseSession;

public class ArchivableWashedInputDataLoader extends WashedInputDataLoader {

	@Override
	public void handle(ODatabaseSession db) {
		super.handle(db);
		this.home.archive(this.home.getInportWashedDataFolder(), true);
	}
}
