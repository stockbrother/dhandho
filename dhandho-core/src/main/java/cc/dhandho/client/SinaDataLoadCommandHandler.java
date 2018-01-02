package cc.dhandho.client;

import java.io.IOException;

import com.age5k.jcps.JcpsException;

import cc.dhandho.rest.handler.SinaAllQuotesDataLoadRRHandler;

public class SinaDataLoadCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		try {
			cc.getServer().handle(SinaAllQuotesDataLoadRRHandler.class.getName());
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
