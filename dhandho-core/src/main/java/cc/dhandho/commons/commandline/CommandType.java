/**
 * Dec 19, 2013
 */
package cc.dhandho.commons.commandline;

import org.apache.commons.cli.Options;

/**
 * @author Wu
 * 
 */
public class CommandType {

	private String name;
		
	private Options options;

	private String description;

	public CommandType(String name,String desc) {
		this.name = name;
		this.options = new Options();
		this.description = desc;
	}

	public Options getOptions() {
		return options;
	}

	public CommandType addOption(String opt, String longOpt, boolean hasArg, String description) {
		this.options.addOption(opt, longOpt, hasArg, description);
		return this;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	public String getDescription() {
		return this.description;
	}

}
