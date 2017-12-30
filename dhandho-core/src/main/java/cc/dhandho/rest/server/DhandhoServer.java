package cc.dhandho.rest.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonElement;

import cc.dhandho.DhandhoHome;
import cc.dhandho.graphdb.DbConfig;

/**
 * 
 * @author Wu
 *
 */
public interface DhandhoServer {

	/**
	 * 
	 * @param home
	 *            the Home folder in the VFS file system.
	 * @return
	 */
	public DhandhoServer home(DhandhoHome home);

	public DhandhoHome getHome();

	public void start();

	public void shutdown();

	public JsonElement handle(final String handlerS, JsonElement request) throws IOException;

	public void handle(final String handlerS) throws IOException;

	public void handle(final String handlerS, Reader reader, final Writer writer) throws IOException;

	public DhandhoServer dbConfig(DbConfig newInMemoryTestDbConfig);
	

}
