package cc.dhandho.jsclient.ng;

import def.angular.core.Injectable;
import def.dom.Globals;

@Injectable
public class LoggerService {

	public void debug(Object msg) {
		Globals.console.debug(String.valueOf(msg));
	}

	public void log(Object msg) {
		Globals.console.log(String.valueOf(msg));
	}

	public void warn(Object msg) {
		Globals.console.warn(String.valueOf(msg));
	}

	public void error(Object msg) {
		Globals.console.error(String.valueOf(msg));
	}

}
