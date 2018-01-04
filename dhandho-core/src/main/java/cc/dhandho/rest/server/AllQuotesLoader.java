package cc.dhandho.rest.server;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.age5k.jcps.JcpsException;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhoDataHome;
import cc.dhandho.input.washed.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.util.VfsUtil;

/**
 * Load data into Memory.
 * 
 * @author wu
 *
 */
public class AllQuotesLoader {

	private static final Logger LOG = LoggerFactory.getLogger(AllQuotesLoader.class);

	public void load(DhoDataHome dataHome, AllQuotesInfos allQuotesInfos) {

		try {

			FileObject washed = dataHome.getInputFolder().resolveFile("sina/washed");

			if (washed.exists()) {
				FileObject fo = VfsUtil.getTheLastModifiedChild(washed);
				if (fo == null) {
					LOG.warn("no file under folder:" + washed.getURL());
				} else {

					long lastModified = fo.getContent().getLastModifiedTime();
					long lastLoaded = allQuotesInfos.getLastLoaded();

					if (lastLoaded > lastModified) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("since last loaded at:" + lastLoaded + ",files no modified in folder:"
									+ washed.getURL());
						}
					} else {
						LOG.warn("loading all-quotes from folder:" + washed.getURL());
						MemoryAllQuotesWashedDataLoader loader = new MemoryAllQuotesWashedDataLoader(washed,
								allQuotesInfos);
						loader.start();
						allQuotesInfos.setLastLoaded(System.currentTimeMillis());
					}
				}

			} else {
				LOG.warn("skip loadin all-quotes since folder not exist:" + washed.getURL());
			}
		} catch (IOException e) {
			throw JcpsException.toRtException(e);
		}

	}
}
