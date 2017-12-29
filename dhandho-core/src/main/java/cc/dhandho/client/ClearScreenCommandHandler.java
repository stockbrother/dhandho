package cc.dhandho.client;

public class ClearScreenCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandContext cc) {
		
		cc.getConsole().clear();
		
	}

}
