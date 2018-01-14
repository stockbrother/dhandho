package cc.dhandho.rest.web;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.age5k.jcps.JcpsException;
import com.age5k.jcps.framework.lifecycle.Server;

import cc.dhandho.rest.server.DhoServer;

public class JettyWebServer implements Server {

	org.eclipse.jetty.server.Server jserver;
	DhoServer dserver;

	public JettyWebServer(DhoServer dserver) {
		this.dserver = dserver;
	}

	@Override
	public void start() {
		jserver = new org.eclipse.jetty.server.Server(8080);
		ContextHandler handleContext = new ContextHandler();
		handleContext.setContextPath(BridgeJettyHandler.contextPath);

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		contexts.setHandlers(new Handler[] { handleContext });

		handleContext.setHandler(new BridgeJettyHandler(this.dserver));

		jserver.setHandler(contexts);
		try {
			jserver.start();
		} catch (Exception e) {
			throw JcpsException.toRtException(e);
		}
	}

	@Override
	public void shutdown() {
		try {
			jserver.stop();
		} catch (Exception e) {
			throw JcpsException.toRtException(e);
		}
	}
}
