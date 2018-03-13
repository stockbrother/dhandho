package cc.dhandho.rest.server;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;

import cc.dhandho.DhoDataHome;
import cc.dhandho.report.MetricDefines;

public class MetricDefinesLoader {

	private static final Logger LOG = LoggerFactory.getLogger(MetricDefinesLoader.class);

	public MetricDefines load(DhoDataHome home) {

		try {
			FileObject fo = home.getClientFile().resolveFile("metric-defines.xml");
			if (!fo.exists()) {
				throw new JcpsException("file not found:" + fo.getURL());
			}
			LOG.info("loading metric defines from:" + fo.getURL());
			MetricDefines rt = MetricDefines.load(fo);
			LOG.info("done of loading metric defines");
			return rt;
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}
	}
}
