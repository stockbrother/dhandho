package cc.dhandho.client.handler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileUtil;

import com.age5k.jcps.JcpsException;

import cc.dhandho.client.CommandContext;
import cc.dhandho.report.query.JsonArrayMetricsQuery;

/**
 * @see JsonArrayMetricsQuery
 * @author wu
 *
 */
public class CatCommandHandler extends DhandhoCommandHandler {
	public static final String OPT_f = "f";
	@Override
	public void execute(CommandContext cc) {
		String file = cc.getCommandLine().getLine().getOptionValue(OPT_f);

		try {
			FileObject fo = cc.getServer().getDataHome().resolveFile(file);

			// TODO performance.
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			FileUtil.writeContent(fo, os);
			cc.getConsole().peekWriter().write(os.toString("utf-8"));
			cc.getConsole().peekWriter().writeLine();
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}

}
