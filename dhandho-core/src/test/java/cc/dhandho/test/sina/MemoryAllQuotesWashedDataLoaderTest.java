package cc.dhandho.test.sina;

import java.io.IOException;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.DhandhoHome;
import cc.dhandho.RtException;
import cc.dhandho.input.washed.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class MemoryAllQuotesWashedDataLoaderTest extends TestCase{

	public void test() {
		DhandhoHome home = TestUtil.getHome();
		
		AllQuotesInfos allQuotesInfos = new AllQuotesInfos();		
		try {

			FileObject to = home.getInputFolder().resolveFile("sina");
			if (to.exists()) {
				//LOG.warn("loading all-quotes from folder:" + to.getURL());
				MemoryAllQuotesWashedDataLoader loader = new MemoryAllQuotesWashedDataLoader(to, allQuotesInfos);
				loader.start();
			} else {
				//LOG.warn("skip loadin all-quotes since folder not exist:" + to.getURL());
			}
		} catch (IOException e) {
			throw RtException.toRtException(e);
		}
	}
}
