package cc.dhandho.client.handler;

import java.io.IOException;

import com.age5k.jcps.JcpsException;

import cc.dhandho.client.CommandContext;
import cc.dhandho.rest.handler.SinaAllQuotesDataLoadRRHandler;

public class SinaDataLoadCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {

		cc.getServer().handle(SinaAllQuotesDataLoadRRHandler.class.getName());

	}

}
