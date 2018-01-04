package cc.dhandho.rest.server;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import com.age5k.jcps.JcpsException;

import cc.dhandho.DhoDataHome;
import cc.dhandho.report.MetricDefines;

public class MetricDefinesLoader {

	public MetricDefines load(DhoDataHome home) {

		try {
			FileObject fo = home.getClientFile().resolveFile("metric-defines.xml");
			if (!fo.exists()) {
				throw new JcpsException("file not found:" + fo.getURL());
			}
			MetricDefines rt = MetricDefines.load(fo);
			return rt;
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}
}
