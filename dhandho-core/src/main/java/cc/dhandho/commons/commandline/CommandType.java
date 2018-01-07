/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * @author Wu
 * 
 */
public class CommandType {

	private String name;

	private Options options;

	private StringBuilder description = new StringBuilder();

	public CommandType(String name, String desc) {
		this.name = name;
		this.options = new Options();
		this.addDesc(desc);
	}

	public CommandType addDesc(String desc) {
		this.description.append(desc);
		return this;
	}

	public Options getOptions() {
		return options;
	}

	public CommandType addOption(String opt, String longOpt, boolean hasArg, String description) {
		return this.addOption(opt, longOpt, hasArg, description, false);
	}

	public CommandType addOption(String opt, String longOpt, boolean hasArg, String description, boolean required) {
		Option O = new Option(opt, longOpt, hasArg, description);
		O.setRequired(required);
		this.options.addOption(O);
		return this;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getDescription() {
		return this.description.toString();
	}

}
