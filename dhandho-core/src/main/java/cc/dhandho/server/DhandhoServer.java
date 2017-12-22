package cc.dhandho.server;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * 
 * @author wuzhen
 *
 */
public interface DhandhoServer extends DbProvider {

	public void start();

	public void shutdown();

	public void handle(final String handlerS, Reader reader, final Writer writer) throws IOException;

}
