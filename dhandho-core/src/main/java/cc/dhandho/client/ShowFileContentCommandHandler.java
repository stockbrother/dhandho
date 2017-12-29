package cc.dhandho.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileUtil;

import cc.dhandho.RtException;
import cc.dhandho.report.JsonMetricSqlLinkQueryBuilder;

/**
 * @see JsonMetricSqlLinkQueryBuilder
 * @author wu
 *
 */
public class ShowFileContentCommandHandler extends DhandhoCommandHandler {

	@Override
	public void execute(CommandContext cc) {
		String file = cc.getCommandLine().getLine().getOptionValue(ShowCommandHandler.OPT_f);

		try {
			FileObject fo = cc.getServer().getHome().resolveFile(file);

			// TODO performance.
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			FileUtil.writeContent(fo, os);
			cc.getConsole().peekWriter().write(os.toString("utf-8"));
			cc.getConsole().peekWriter().writeLine();
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}

	}

}
