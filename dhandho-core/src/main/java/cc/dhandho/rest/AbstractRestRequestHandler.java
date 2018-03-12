package cc.dhandho.rest;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.container.Container;

/**
 * AppContext.newInstance();
 * 
 * @author Wu
 *
 */
public abstract class AbstractRestRequestHandler implements RestRequestHandler, Container.Aware {

	protected Container app;

	@Override
	public void setContainer(Container app) {
		this.app = app;
	}

	@Override
	public void handle(RestRequestContext rrc) {
		try {
			this.handleInternal(rrc);
		} catch (Exception e) {
			throw JcpsException.toRtException(e);
		}
	}

	public abstract void handleInternal(RestRequestContext rrc) throws Exception;

}
