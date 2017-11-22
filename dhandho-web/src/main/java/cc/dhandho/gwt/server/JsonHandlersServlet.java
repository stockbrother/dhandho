package cc.dhandho.gwt.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cc.dhandho.graphdb.DbConfig;
import cc.dhandho.server.DhandhoServer;
/**
 * 
 * @author Wu
 *
 */
public class JsonHandlersServlet extends HttpServlet {

	// private static final Logger LOG = LoggerFactory.getLogger();

	DhandhoServer server;

	@Override
	public void init() throws ServletException {
		super.init();
		ServletConfig servletConfig = this.getServletConfig();
		String dbUrl = servletConfig.getInitParameter("dbUrl");
		String dbName = servletConfig.getInitParameter("dbName");
		String dbUser = servletConfig.getInitParameter("dbUser");
		String dbPassword = servletConfig.getInitParameter("dbPassword");
		server = new DhandhoServer()
				.dbConfig(new DbConfig().dbName(dbName).dbUrl(dbUrl).dbUser(dbUser).dbPassword(dbPassword));

		server.start();

	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json");
		response.setStatus(HttpServletResponse.SC_OK);
		final String handlerS = request.getHeader("_handler");

		Reader reader = request.getReader();
		Writer writer = response.getWriter();
		server.handle(handlerS, reader, writer);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
