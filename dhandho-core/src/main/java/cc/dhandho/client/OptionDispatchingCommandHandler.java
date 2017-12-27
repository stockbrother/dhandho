package cc.dhandho.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

import cc.dhandho.commons.commandline.CommandAndLine;
import cc.dhandho.commons.commandline.CommandLineWriter;

public class OptionDispatchingCommandHandler extends DhandhoCommandHandler {

	private Map<String, CommandHandler> childCommandHandlerMap = new HashMap<>();

	public void add(String opt, CommandHandler handler) {
		this.childCommandHandlerMap.put(opt, handler);
	}

	@Override
	protected void execute(CommandAndLine line, DhandhoCliConsole console, CommandLineWriter writer) {

		Optional<Map.Entry<String, CommandHandler>> entry = this.childCommandHandlerMap.entrySet().stream()
				.filter(new Predicate<Map.Entry<String, CommandHandler>>() {

					@Override
					public boolean test(Entry<String, CommandHandler> t) {
						String opt = t.getKey();
						if (line.hasOption(opt)) {
							return true;
						}
						return false;
					}
				}).findFirst();
		if (entry.isPresent()) {
			entry.get().getValue().execute(line);
		} else {
			writer.writeLine("no sub handler found.");
		}
	}

}
