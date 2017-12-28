package cc.dhandho.client;

public class ExitCommandHandler implements CommandHandler {

	@Override
	public void execute(CommandContext cc) {
		
		cc.getConsole().shutdownAsync();
		
	}

}
