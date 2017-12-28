package cc.dhandho.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

public class OptionDispatchingCommandHandler extends DhandhoCommandHandler {

	private Map<String, CommandHandler> childCommandHandlerMap = new HashMap<>();

	public void add(String opt, CommandHandler handler) {
		this.childCommandHandlerMap.put(opt, handler);
	}

	@Override
	public void execute(CommandContext cc) {

		Optional<Map.Entry<String, CommandHandler>> entry = this.childCommandHandlerMap.entrySet().stream()
				.filter(new Predicate<Map.Entry<String, CommandHandler>>() {

					@Override
					public boolean test(Entry<String, CommandHandler> t) {
						String opt = t.getKey();
						if (cc.getCommandLine().hasOption(opt)) {
							return true;
						}
						return false;
					}
				}).findFirst();
		if (entry.isPresent()) {
			entry.get().getValue().execute(cc);
		} else {
			cc.getConsole().peekWriter().writeLine("no sub handler found.");
		}
	}

}
