package cc.dhandho.client.handler;

import cc.dhandho.client.handler.rest.MyCorpAddCommandHandler;
import cc.dhandho.client.handler.rest.MyCorpRemoveCommandHandler;
import cc.dhandho.client.handler.rest.MyCorpShowCommandHandler;

public class MyCorpDispatcherCommandHandler extends OptionDispatcherCommandHandler {
	public static final String OPT_a = "a";// add corp with id
	public static final String OPT_r = "r";// add corp with id
	public static final String OPT_s = "s";// add corp with id

	public MyCorpDispatcherCommandHandler() {
		this.add(OPT_a, new MyCorpAddCommandHandler());
		this.add(OPT_r, new MyCorpRemoveCommandHandler());
		this.add(OPT_s, new MyCorpShowCommandHandler());

	}

}
