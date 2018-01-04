package cc.dhandho.input.loader;

import com.age5k.jcps.framework.container.Container;
import com.age5k.jcps.framework.handler.Handler2;
import com.orientechnologies.orient.core.db.ODatabaseSession;

import cc.dhandho.DhoDataHome;

/**
 * TODO avoid loading twice, move input folder to archive folder.
 * 
 * @author Wu
 *
 */
public abstract class InputDataLoader implements Container.Aware, Handler2<ODatabaseSession> {
	protected Container app;
	protected DhoDataHome home;

	@Override
	public void setContainer(Container app) {
		this.app = app;
		this.home = app.findComponent(DhoDataHome.class, true);
	}

}
