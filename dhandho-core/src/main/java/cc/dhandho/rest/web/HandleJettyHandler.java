package cc.dhandho.rest.web;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import cc.dhandho.rest.server.DhoServer;

public class HandleJettyHandler extends AbstractHandler {
	DhoServer dserver;

	public static final String contextPath = "/handle";

	public HandleJettyHandler(DhoServer dserver) {
		this.dserver = dserver;
	}

	@Override
	public void handle(String s, Request request, HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		String uri = req.getRequestURI();
		String handlerS = uri.substring(contextPath.length() + 1);
		Reader reader = req.getReader();
		Writer writer = res.getWriter();

		this.dserver.handle(handlerS, reader, writer);

		request.setHandled(true);

	}
}