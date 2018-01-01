package cc.dhandho.test.sina;

import java.io.File;

import org.apache.commons.vfs2.FileObject;

import cc.dhandho.AllQuotesInfos;
import cc.dhandho.input.sina.SinaAllQuotesPreprocessor;
import cc.dhandho.input.sina.SinaQuotesCollector;
import cc.dhandho.input.washed.MemoryAllQuotesWashedDataLoader;
import cc.dhandho.test.util.TestUtil;
import junit.framework.TestCase;

public class SinaTest extends TestCase {

	public void test() throws Exception {

		FileObject data = TestUtil.newTmpFolder();

		FileObject sinaData = data.resolveFile("sina");

		FileObject outputParentDir = data.resolveFile("raw");

		new SinaQuotesCollector()//
				.pauseInterval(2000)//
				.outputParentDir(outputParentDir)//
				.maxLoops(2)//
				.start();

		FileObject to = data.resolveFile("sina" + File.separator + "washed");

		new SinaAllQuotesPreprocessor(outputParentDir, to).process();
		
		AllQuotesInfos aqis = new AllQuotesInfos();
		MemoryAllQuotesWashedDataLoader loader = new MemoryAllQuotesWashedDataLoader(to, aqis);
		loader.start();
		
		
	}
}
