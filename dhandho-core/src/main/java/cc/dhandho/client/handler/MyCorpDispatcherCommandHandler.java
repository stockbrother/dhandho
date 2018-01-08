package cc.dhandho.client.handler;

public class MyCorpDispatcherCommandHandler extends OptionDispatcherCommandHandler {
	public static final String OPT_a = "a";// add corp with id

	public MyCorpDispatcherCommandHandler() {
		this.add(OPT_a, new MyCorpAddCommandHandler());

	}

}
