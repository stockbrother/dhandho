package cc.dhandho.rest.web;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;

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
		ContextHandlerCollection contexts = new ContextHandlerCollection();

		{
			// rest service, bridge to dho server for json handling.
			ContextHandler context = new ContextHandler();
			context.setContextPath(BridgeJettyHandler.contextPath);
			context.setHandler(new BridgeJettyHandler(this.dserver));
			contexts.addHandler(context);
		}
		{
			// static web resources.
			ContextHandler context = new ContextHandler();
			context.setContextPath("/web");
			ResourceHandler handler = new ResourceHandler();
			handler.setResourceBase("src/web");
			context.setHandler(handler);
			contexts.addHandler(context);
		}
		{
			// generated js files by jsweet.
			ContextHandler context = new ContextHandler();
			context.setContextPath("/js");
			ResourceHandler handler = new ResourceHandler();
			handler.setResourceBase("target/jsweet/js");
			context.setHandler(handler);
			contexts.addHandler(context);
		}

		{
			// for debugging with source map.
			ContextHandler context = new ContextHandler();

			context.setContextPath("/src");
			ResourceHandler handler = new ResourceHandler();
			handler.setResourceBase("src");
			context.setHandler(handler);
			contexts.addHandler(context);
		}

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
