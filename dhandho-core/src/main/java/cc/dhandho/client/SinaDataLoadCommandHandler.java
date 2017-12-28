package cc.dhandho.client;

import java.io.IOException;

import cc.dhandho.RtException;
import cc.dhandho.rest.SinaAllQuotesDataLoadRRHandler;

public class SinaDataLoadCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		try {
			cc.getServer().handle(SinaAllQuotesDataLoadRRHandler.class.getName());
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
