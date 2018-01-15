package cc.dhandho.rest.server;

import java.io.Reader;
import java.io.Writer;

import com.google.gson.JsonElement;

import cc.dhandho.DhoDataHome;

/**
 * 
 * @author Wu
 *
 */
public interface DhoServer {

	/**
	 * 
	 * @param home
	 *            the Home folder in the VFS file system.
	 * @return
	 */
	public DhoServer dataHome(DhoDataHome home);

	public DhoDataHome getDataHome();

	public void start();

	public void shutdown();

	public JsonElement handle(final String handlerS, JsonElement request) ;
	
	public void handle(final String handlerS) ;

	public void handle(final String handlerS, Reader reader, final Writer writer) ;

}
