package cc.dhandho.rest;

import java.io.IOException;

public interface RestRequestHandler {

	public void execute(RestRequestContext rrc) throws IOException;

}
