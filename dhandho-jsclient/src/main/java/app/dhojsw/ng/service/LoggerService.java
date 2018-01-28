package app.dhojsw.ng.service;

import static def.dom.Globals.console;

import def.angular.core.Injectable;

@Injectable()
public class LoggerService {

	public void info(Object msg, Object... args) {
		console.info(msg, args);
	}

	public void debug(Object msg, Object... args) {
		console.debug(String.valueOf(msg), args);
	}

	public void trace(Object msg, Object... args) {
		console.info(msg, args);
	}

	public void warn(Object msg, Object... args) {
		console.warn(msg, args);
	}

	public void error(Object msg, Object... args) {
		console.info(msg, args);
	}

}
